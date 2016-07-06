package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.block.BlockOfalen;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.item.ItemOfalen;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityFusingMachine extends TileEntityGradedMachineBase {
	/** 機械の中にあるアイテムの配列。 */
	protected ItemStack[] stacks = new ItemStack[5];

	/** アップデート時の処理。 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		// クライアントは終了。
		if (worldObj.isRemote)
			return;
		boolean isBurning = this.isBurning();
		// 燃焼中なら時間を減らす。
		if (isBurning)
			burnTime--;
		// 燃焼中でなく、燃料が空なら終了する。
		if (!this.isBurning() && stacks[3] == null) {
			workTime = 0;
			if (isBurning)
				this.updateIsBurning();
			return;
		}
		if (this.canWork()) {
			// 燃焼中でなく作業可能なら、新たに燃料を消費し、燃焼させる。
			if (!this.isBurning()) {
				maxBurnTime = burnTime = this.getItemBurnTime(stacks[3]);
				if (this.isBurning()) {
					if (stacks[3] != null) {
						--stacks[3].stackSize;
						if (stacks[3].stackSize == 0) {
							stacks[3] = stacks[3].getItem().getContainerItem(stacks[3]);
						}
					}
				}
			}
			// 燃焼中で作業可能なら、作業する。
			if (this.isBurning()) {
				++workTime;
				if (workTime >= this.getWorkTime()) {
					workTime = 0;
					this.work();
				}
			} else {
				// 燃焼中でないなら作業時間をリセットする。
				workTime = 0;
			}
		} else {
			// 作業不可能なら作業時間をリセットする。
			workTime = 0;
		}

		// 燃焼しているかが変わっていたら、更新する。
		if (isBurning != this.isBurning()) {
			this.updateIsBurning();
		}
	}

	/** 作業可能かどうか。 */
	@Override
	protected boolean canWork() {
		Item item = null;
		for (int i = 0; i < 3; i++) {
			if (stacks[i] == null)
				continue;
			if (item == null)
				item = stacks[i].getItem();
			else if (item != stacks[i].getItem())
				return false;
		}

		int color = this.getResultColor();
		if (color < 0) {
			return false;
		}

		int amount = this.getResultAmount(color, item);
		if (amount < 0) {
			return false;
		}

		ItemStack result = new ItemStack(item, amount, color);

		if (stacks[4] != null) {
			// 完成品スロットに空きがないなら不可。
			if (!stacks[4].isItemEqual(result))
				return false;
			int size = stacks[4].stackSize + result.stackSize;
			if (size > this.getInventoryStackLimit() || size > stacks[4].getMaxStackSize())
				return false;
		}

		return true;
	}

	/** 完成品の色を返す。 */
	protected int getResultColor() {
		int flag = 0;
		for (int i = 0; i < 3; i++) {
			if (stacks[i] == null) {
				continue;
			}
			if (this.isFusableOfalen(stacks[i].getItem())) {
				flag += 1 << stacks[i].getItemDamage();
			} else {
				return -1;
			}
		}
		switch (flag) {
		default:
			return -1;
		// 0000,0011 赤・緑→橙
		case 3:
			return 4;
		// 0000,0101 赤・青→紫
		case 5:
			return 6;
		// 0000,0110 緑・青→翠
		case 6:
			return 5;
		// 0000,0111 赤・緑・青→白
		case 7:
			return 3;
		// 0111,0000 橙・翠・紫→黒
		case 112:
			return 7;
		}
	}

	/** 完成品の量を返す。 */
	protected int getResultAmount(int color, Item item) {
		if (item == OfalenModItemCore.coreOfalen)
			return 1;
		if (3 < color && color < 7)
			return 2;
		if (color == 3 || color == 7)
			return 3;
		return -1;
	}

	/** 融合可能な種類かどうか。 */
	public boolean isFusableOfalen(Item item) {
		return item instanceof ItemOfalen || Block.getBlockFromItem(item) instanceof BlockOfalen;
	}

	/** 作業にかかる時間を返す。 */
	@Override
	protected int getWorkTime() {
		return OfalenModConfigCore.timeConversion * (4 - grade) / 4;
	}

	/** 作業時の処理。 */
	@Override
	public void work() {
		Item item = null;
		for (int i = 0; i < 3; i++) {
			if (stacks[i] == null)
				continue;
			item = stacks[i].getItem();
			break;
		}
		int color = this.getResultColor();
		int amount = this.getResultAmount(color, item);
		ItemStack result = new ItemStack(item, amount, color);
		// 完成品スロットに融合結果を代入/追加する。
		if (stacks[4] == null) {
			stacks[4] = result;
		} else if (stacks[4].isItemEqual(result)) {
			stacks[4].stackSize += amount;
		}
		// 変換前スロットのスタック数を減らす。
		for (int i = 0; i < 3; i++) {
			if (stacks[i] == null)
				continue;
			--stacks[i].stackSize;
			if (stacks[i].stackSize < 1) {
				stacks[i] = null;
			}
		}
		this.updateGrade();
	}

	/** 材料スロットから完成品スロットにアイテムを移動する。 */
	@Override
	protected void moveItemStack() {
	}

	/** NBTに機械の情報を記録する。 */
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i] == null)
				continue;
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setByte("Slot", (byte) i);
			stacks[i].writeToNBT(nbt1);
			nbttaglist.appendTag(nbt1);
		}
		nbt.setTag("Items", nbttaglist);
	}

	/** NBTから機械の情報を反映する。 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		stacks = new ItemStack[5];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbt1.getByte("Slot");
			if (0 <= b0 && b0 < stacks.length) {
				stacks[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}
		maxBurnTime = this.getItemBurnTime(stacks[3]);
	}

	@Override
	public int getSizeInventory() {
		return 5;
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return stacks[slot];
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (stacks[slot] == null)
			return null;
		ItemStack itemstack;
		if (stacks[slot].stackSize <= amount) {
			itemstack = stacks[slot];
			stacks[slot] = null;
			return itemstack;
		}
		itemstack = stacks[slot].splitStack(amount);
		if (stacks[slot].stackSize < 1) {
			stacks[slot] = null;
		}
		return itemstack;
	}

	/** スロットの中身を設定する。 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		stacks[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? customName : "container.OfalenMod.FusingMachine-" + grade;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		if (slot == 4)
			return false;
		if (slot == 3)
			return this.isItemFuel(itemStack);
		return this.isFusableOfalen(itemStack.getItem()) && slot == (itemStack.getItemDamage() & 3);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? new int[] { 4, 3 } : (side == 1 ? new int[] { 0, 1, 2 } : new int[] { 3 });
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		if (side == 0)
			if (slot == 3)
				return itemStack.getItem() == Items.bucket;
		return true;
	}
}
