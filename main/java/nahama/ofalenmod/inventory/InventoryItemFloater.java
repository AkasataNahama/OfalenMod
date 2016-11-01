package nahama.ofalenmod.inventory;

import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryItemFloater implements IInventory {
	private InventoryPlayer inventoryPlayer;
	private ItemStack currentItem;
	private ItemStack[] itemStacks = new ItemStack[9];

	public InventoryItemFloater(InventoryPlayer inventory) {
		inventoryPlayer = inventory;
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 9;
	}

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
	public String getInventoryName() {
		return "container.ofalen.floater";
	}

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
	public void openInventory() {
		// アイテムを読み込む。
		currentItem = inventoryPlayer.getCurrentItem();
		int amount = currentItem.getMaxDamage() - currentItem.getItemDamage();
		itemStacks = new ItemStack[9];
		for (int i = 0; i < 9; i++) {
			if (amount < 1)
				break;
			if (amount < 64) {
				itemStacks[i] = new ItemStack(OfalenModItemCore.partsOfalen, amount, 8);
				break;
			}
			itemStacks[i] = new ItemStack(OfalenModItemCore.partsOfalen, 64, 8);
			amount -= 64;
		}
	}

	/** インベントリが閉じられた時の処理。 */
	@Override
	public void closeInventory() {
		currentItem = inventoryPlayer.getCurrentItem();
		int amount = 0;
		for (int i = 0; i < 9; i++) {
			if (itemStacks[i] == null)
				continue;
			amount += itemStacks[i].stackSize;
		}
		itemStacks = new ItemStack[9];
		inventoryPlayer.mainInventory[inventoryPlayer.currentItem].setItemDamage(currentItem.getMaxDamage() - amount);
		if (inventoryPlayer.getCurrentItem().getItemDamage() >= 64 * 9)
			inventoryPlayer.getCurrentItem().getTagCompound().setByte(OfalenNBTUtil.MODE, (byte) 0);
	}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}
}
