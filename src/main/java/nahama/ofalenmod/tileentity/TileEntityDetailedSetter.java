package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.setting.IItemOfalenSettable;
import nahama.ofalenmod.setting.OfalenSetting;
import nahama.ofalenmod.setting.OfalenSettingCategory;
import nahama.ofalenmod.setting.OfalenSettingContent;
import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenTimer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDetailedSetter extends TileEntity implements IInventory {
	protected ItemStack[] itemStacks = new ItemStack[this.getSizeInventory()];
	/** インベントリの状態。 */
	protected byte state = -1;

	/** 入れられたアイテムによって設定を適用する。 */
	public void apply() {
		OfalenTimer.start("TileEntityDetailedSetter.apply");
		if (itemStacks[0] == null || !(itemStacks[0].getItem() instanceof IItemOfalenSettable)) {
			OfalenLog.debuggingInfo("Failed to apply detailed setting!", "TileEntityDetailedSetter");
			return;
		}
		// 設定を取得していく。
		OfalenSetting setting = ((IItemOfalenSettable) itemStacks[0].getItem()).getSetting();
		int i;
		for (i = 1; i < this.getSizeInventory(); i++) {
			if (setting != null && setting instanceof OfalenSettingCategory) {
				setting = ((OfalenSettingCategory) setting).getChildSetting(itemStacks[i]);
			} else {
				break;
			}
		}
		if (setting == null || !(setting instanceof OfalenSettingContent<?>)) {
			// 正しく取得できなかったら終了。
			OfalenLog.error("Failed to apply detailed setting!", "TileEntityDetailedSetter");
			return;
		}
		// 設定を適用する。
		((OfalenSettingContent<?>) setting).changeTagByStack(itemStacks[0], itemStacks[i]);
		OfalenTimer.watchAndLog("TileEntityDetailedSetter.apply");
	}

	/** 更新時の処理。 */
	@Override
	public void updateEntity() {
		// 状態を更新させる。
		if (state != -1)
			state = -1;
	}

	/** インベントリの状態により数値を返す。 */
	protected byte getState() {
		// 状態が更新されていなければ更新する。
		if (state < 0)
			state = (byte) this.updateState();
		return state;
	}

	/** 状態を更新する。 */
	private int updateState() {
		// 最初の不正スロットの番号。最後まで正しかったら64追加。
		if (itemStacks[0] == null || !(itemStacks[0].getItem() instanceof IItemOfalenSettable))
			return 0;
		OfalenSetting setting = ((IItemOfalenSettable) itemStacks[0].getItem()).getSetting();
		for (int i = 1; i < itemStacks.length; i++) {
			// 空のスロットなら終了。
			if (itemStacks[i] == null)
				return i;
			// 設定の末端に達したら適用可能として終了。
			if (setting instanceof OfalenSettingContent<?>)
				return i + 64;
			// キャストエラー防止。
			if (!(setting instanceof OfalenSettingCategory))
				break;
			setting = ((OfalenSettingCategory) setting).getChildSetting(itemStacks[i]);
			// 不正なアイテムだったら終了。
			if (setting == null)
				return i;
		}
		return 0;
	}

	/** 設定を適用可能な状態か。 */
	public boolean isApplicableState() {
		return this.getState() > 63;
	}

	/** 最初の不正スロットの番号を返す。 */
	public int getFirstInvalidSlot() {
		return this.getState() % 32;
	}

	/** NBTにTileEntityの情報を記録する。 */
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < itemStacks.length; i++) {
			if (itemStacks[i] == null)
				continue;
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setByte(OfalenNBTUtil.SLOT, (byte) i);
			itemStacks[i].writeToNBT(nbt1);
			nbttaglist.appendTag(nbt1);
		}
		nbt.setTag(OfalenNBTUtil.ITEMS, nbttaglist);
	}

	/** NBTをTileEntityの情報に反映する。 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList(OfalenNBTUtil.ITEMS, 10);
		itemStacks = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbt1.getByte(OfalenNBTUtil.SLOT);
			if (0 <= b0 && b0 < itemStacks.length) {
				itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}
	}

	private boolean isExistingSlot(int slot) {
		return 0 <= slot && slot < this.getSizeInventory();
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 6;
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (!this.isExistingSlot(slot))
			return null;
		return itemStacks[slot];
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (!this.isExistingSlot(slot) || itemStacks[slot] == null)
			return null;
		ItemStack itemStack;
		if (itemStacks[slot].stackSize <= amount) {
			itemStack = itemStacks[slot];
			itemStacks[slot] = null;
			return itemStack;
		}
		itemStack = itemStacks[slot].splitStack(amount);
		if (itemStacks[slot].stackSize < 1)
			itemStacks[slot] = null;
		return itemStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack itemStack = this.getStackInSlot(slot);
		this.setInventorySlotContents(slot, null);
		return itemStack;
	}

	/** スロットの中身を設定する。 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (!this.isExistingSlot(slot))
			return;
		itemStacks[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
			itemStack.stackSize = this.getInventoryStackLimit();
	}

	/** 金床で設定された名前を持つかどうか。 */
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	/** インベントリの名前を返す。 */
	@Override
	public String getInventoryName() {
		return "container.ofalen.setterDetailed";
	}

	/** このインベントリの最大スタック数を返す。 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/** プレイヤーが使用できるかどうか。 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return this.isExistingSlot(slot);
	}
}
