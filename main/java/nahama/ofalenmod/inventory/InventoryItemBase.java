package nahama.ofalenmod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class InventoryItemBase implements IInventory {
	protected InventoryPlayer inventoryPlayer;
	protected ItemStack[] itemStacks;

	public InventoryItemBase(InventoryPlayer inventory) {
		inventoryPlayer = inventory;
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public abstract int getSizeInventory();

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemStacks[slot];
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
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

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	/** スロットの中身を設定する。 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		itemStacks[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/** 金床で設定された名前を持つかどうか。 */
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public abstract String getInventoryName();

	/** このインベントリの最大スタック数を返す。 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	/** プレイヤーが使用できるかどうか。 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	/** インベントリが開かれた時の処理。 */
	@Override
	public abstract void openInventory();

	/** インベントリが閉じられた時の処理。 */
	@Override
	public abstract void closeInventory();

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}
}
