package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.util.BlockPos;
import nahama.ofalenmod.util.BlockRange;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityCollector extends TileEntityWorldEditorBase {
	/** 収集したアイテム。 */
	protected ItemStack[] itemStacks = new ItemStack[this.getSizeOwnInventory()];
	/** アイテムの収集が無効かどうか。 */
	private boolean isItemDisabled;
	/** 経験値の収集が無効かどうか。 */
	private boolean isExpDisabled;
	/** アイテムを消去するかどうか。 */
	private boolean canDeleteItem;
	/** 経験値を消去するかどうか。 */
	private boolean canDeleteExp;

	@Override
	protected BlockPos searchNextBlock(BlockRange range, BlockPos start) {
		// 無効化。
		return new BlockPos(xCoord, yCoord, zCoord);
	}

	@Override
	protected boolean canWork() {
		return true;
	}

	@Override
	protected boolean canWorkWithCoord(int x, int y, int z) {
		// 呼ばれない。
		return false;
	}

	@Override
	protected boolean work(int x, int y, int z) {
		// searchNextBlockを無効化してあるため、intervalがなくなるたびに呼ばれる。
		int amountEnergy = this.getAmountEnergy();
		int lastAmountEnergy = amountEnergy;
		// EntityItemとEntityXPOrbがあれば収集する。
		for (Object o : worldObj.loadedEntityList) {
			if (!isItemDisabled && o instanceof EntityItem) {
				// EntityItemにキャスト。
				EntityItem entityItem = (EntityItem) o;
				// 範囲外か、拾えない状態（ドロップされてすぐ）なら次のEntityへ。
				if (!range.isInRange(entityItem) || entityItem.delayBeforeCanPickup > 0)
					continue;
				ItemStack eItemStack = entityItem.getEntityItem();
				// アイテムフィルターで許可されていなかったら次のEntityへ。
				if (!OfalenNBTUtil.FilterUtil.canItemFilterThrough(tagItemFilter, eItemStack))
					continue;
				int remaining = amountEnergy;
				// 燃料が尽きていたら終了。
				if (remaining < 1)
					break;
				// インベントリの空きを考慮。
				remaining = Math.min(remaining, OfalenUtil.getRemainingItemAmountInInventory(itemStacks, eItemStack, this.getInventoryStackLimit()));
				// 一個も移動できないなら次のEntityへ。
				if (remaining < 1)
					continue;
				// スタック数が限界以下ならそのまま収集。
				if (remaining >= eItemStack.stackSize) {
					amountEnergy -= eItemStack.stackSize;
					if (!canDeleteItem)
						this.addInInventory(eItemStack.copy());
					entityItem.setDead();
					continue;
				}
				// 燃料か空きスロットが足りなかったら足りる分だけ収集して終了。
				ItemStack itemStack1 = eItemStack.copy();
				itemStack1.stackSize = remaining;
				if (!canDeleteItem)
					this.addInInventory(itemStack1);
				eItemStack.stackSize -= remaining;
				amountEnergy -= remaining;
			} else if (!isExpDisabled && (o instanceof EntityXPOrb)) {
				// EntityXPOrbにキャスト。
				EntityXPOrb e = (EntityXPOrb) o;
				if (!range.isInRange(e))
					continue;
				int remaining = amountEnergy;
				// 燃料が尽きていたら終了。
				if (remaining < 1)
					break;
				// インベントリの空きを考慮。
				remaining = Math.min(remaining, OfalenUtil.getRemainingItemAmountInInventory(itemStacks, new ItemStack(OfalenModItemCore.crystalExp), this.getInventoryStackLimit()));
				// 一個も移動できないなら次のEntityへ。
				if (remaining < 1)
					continue;
				// 燃料が足りていたらそのまま収集。
				if (remaining >= e.xpValue) {
					amountEnergy -= e.xpValue;
					if (!canDeleteExp)
						this.addExpCrystal(e.xpValue);
					e.setDead();
					continue;
				}
				// 燃料が足りなかったら足りる分だけ移動して終了。
				if (!canDeleteExp)
					this.addExpCrystal(remaining);
				e.xpValue -= remaining;
				amountEnergy -= remaining;
			}
		}
		this.consumeEnergy(lastAmountEnergy - amountEnergy);
		return false;
	}

	/** 経験値を結晶化し、インベントリに入れる。 */
	private void addExpCrystal(int xpValue) {
		int amount = xpValue;
		for (int i = 0; i < itemStacks.length; i++) {
			ItemStack itemStack = itemStacks[i];
			if (itemStack == null || itemStack.getItem() != OfalenModItemCore.crystalExp)
				continue;
			amount += itemStack.stackSize * OfalenUtil.power(64, itemStack.getItemDamage());
			itemStacks[i] = null;
		}
		for (int i = (int) (Math.log(amount) / Math.log(64)); i >= 0; i--) {
			this.addInInventory(new ItemStack(OfalenModItemCore.crystalExp, amount / OfalenUtil.power(64, i), i));
			amount %= OfalenUtil.power(64, i);
		}
	}

	/** インベントリにできるだけ入れて、余りの個数を返す。 */
	private void addInInventory(ItemStack itemStack) {
		int limit = Math.min(itemStack.getMaxStackSize(), this.getInventoryStackLimit());
		for (int i = 0; i < itemStacks.length; i++) {
			// 空のスロットがあったら、代入する。
			if (itemStacks[i] == null) {
				itemStacks[i] = itemStack;
				break;
			}
			// スタック限界まで入っているか、スタック不可なら次へ。
			if (itemStacks[i].stackSize >= limit || !OfalenUtil.canStack(itemStacks[i], itemStack))
				continue;
			// スロットの空きが足りるなら足して終了。
			if (itemStacks[i].stackSize + itemStack.stackSize <= limit) {
				itemStacks[i].stackSize += itemStack.stackSize;
				break;
			}
			// 足りないなら限界まで入れて次へ。
			itemStack.stackSize -= limit - itemStacks[i].stackSize;
			itemStacks[i].stackSize = limit;
		}
	}

	@Override
	public byte getAmountSettingID() {
		return (byte) (super.getAmountSettingID() + 2);
	}

	@Override
	public Object getWithID(int id) {
		switch (id - super.getAmountSettingID() + 2) {
		case 0:
			return isItemDisabled;
		case 1:
			return isExpDisabled;
		case 2:
			return canDeleteItem;
		case 3:
			return canDeleteExp;
		}
		return super.getWithID(id);
	}

	@Override
	public void setWithID(int id, byte changeType) {
		super.setWithID(id, changeType);
		switch (id - super.getAmountSettingID() + 2) {
		case 0:
			isItemDisabled = !isItemDisabled;
			break;
		case 1:
			isExpDisabled = !isExpDisabled;
			break;
		case 2:
			canDeleteItem = !canDeleteItem;
			break;
		case 3:
			canDeleteExp = !canDeleteExp;
			break;
		}
	}

	@Override
	public String getSettingNameWithID(int id) {
		switch (id - super.getAmountSettingID() + 2) {
		case 0:
			return "info.ofalen.setting.collector.isDisabled.item";
		case 1:
			return "info.ofalen.setting.collector.isDisabled.exp";
		case 2:
			return "info.ofalen.setting.collector.canDelete.item";
		case 3:
			return "info.ofalen.setting.collector.canDelete.exp";
		}
		return super.getSettingNameWithID(id);
	}

	@Override
	public byte getSettingTypeWithID(int id) {
		if (id >= super.getAmountSettingID() - 2)
			return 0;
		return super.getSettingTypeWithID(id);
	}

	@Override
	protected byte getColor() {
		return 7;
	}

	@Override
	protected void writeSettingToNBT(NBTTagCompound nbt) {
		super.writeSettingToNBT(nbt);
		nbt.setBoolean(OfalenNBTUtil.IS_ITEM_DISABLED, isItemDisabled);
		nbt.setBoolean(OfalenNBTUtil.IS_EXP_DISABLED, isExpDisabled);
		nbt.setBoolean(OfalenNBTUtil.CAN_DELETE_ITEM, canDeleteItem);
		nbt.setBoolean(OfalenNBTUtil.CAN_DELETE_EXP, canDeleteExp);
	}

	@Override
	protected void readSettingFromNBT(NBTTagCompound nbt) {
		super.readSettingFromNBT(nbt);
		isItemDisabled = nbt.getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED);
		isExpDisabled = nbt.getBoolean(OfalenNBTUtil.IS_EXP_DISABLED);
		canDeleteItem = nbt.getBoolean(OfalenNBTUtil.CAN_DELETE_ITEM);
		canDeleteExp = nbt.getBoolean(OfalenNBTUtil.CAN_DELETE_EXP);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setTag(OfalenNBTUtil.ITEMS, OfalenNBTUtil.writeItemStacksToNBTTagList(itemStacks));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		itemStacks = OfalenNBTUtil.loadItemStacksFromNBTTagList(nbt.getTagList(OfalenNBTUtil.ITEMS, 10), this.getSizeOwnInventory());
	}

	/** 燃料スロットを含まないスロット数を返す。 */
	public int getSizeOwnInventory() {
		return 27;
	}

	@Override
	public int getSizeInventory() {
		return super.getSizeInventory() + this.getSizeOwnInventory();
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < super.getSizeInventory())
			return super.getStackInSlot(slot);
		return itemStacks[slot - super.getSizeInventory()];
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot < super.getSizeInventory())
			return super.decrStackSize(slot, amount);
		slot -= super.getSizeInventory();
		if (itemStacks[slot] == null)
			return null;
		ItemStack itemstack;
		if (itemStacks[slot].stackSize <= amount) {
			itemstack = itemStacks[slot];
			itemStacks[slot] = null;
			return itemstack;
		}
		itemstack = itemStacks[slot].splitStack(amount);
		if (itemStacks[slot].stackSize < 1) {
			itemStacks[slot] = null;
		}
		return itemstack;
	}

	/** スロットの中身を設定する。 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot < super.getSizeInventory() || this.getSizeInventory() < slot) {
			super.setInventorySlotContents(slot, itemStack);
			return;
		}
		slot -= super.getSizeInventory();
		itemStacks[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
			itemStack.stackSize = this.getInventoryStackLimit();
	}

	/** インベントリの名前を返す。 */
	@Override
	public String getInventoryName() {
		return "container.ofalen.blockCollector";
	}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return super.getSizeInventory() <= slot || super.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] ret = new int[this.getSizeInventory()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = i;
		}
		return ret;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return slot < super.getSizeInventory() && super.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return super.getSizeInventory() <= slot;
	}
}
