package nahama.ofalenmod.inventory;

import nahama.ofalenmod.item.IItemList;
import nahama.ofalenmod.item.ItemListPaper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryItemList implements IInventory {

	private InventoryPlayer inventoryPlayer;
	private ItemStack currentItem;
	private ItemStack[] itemStacks = new ItemStack[27];

	public InventoryItemList(InventoryPlayer inventory) {
		inventoryPlayer = inventory;
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 27;
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
		return "container.OfalenMod.ItemList";
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
		itemStacks = new ItemStack[this.getSizeInventory()];
		NBTTagList nbtTagList = inventoryPlayer.getCurrentItem().getTagCompound().getTagList(ItemListPaper.TAG_NAME, 10);
		for (int i = 0; i < 27 && i < nbtTagList.tagCount(); i++) {
			NBTTagCompound nbt = nbtTagList.getCompoundTagAt(i);
			if (nbt == null)
				continue;
			itemStacks[i] = ItemStack.loadItemStackFromNBT(nbt);
		}
	}

	/** インベントリが閉じられた時の処理。 */
	@Override
	public void closeInventory() {
		ItemStack current = inventoryPlayer.getCurrentItem();
		if (!(current.getItem() instanceof IItemList))
			return;
		IItemList list = (IItemList) current.getItem();
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < 27; i++) {
			if (itemStacks[i] == null)
				continue;
			NBTTagCompound nbt = new NBTTagCompound();
			itemStacks[i].writeToNBT(nbt);
			nbtTagList.appendTag(nbt);
		}
		itemStacks = new ItemStack[this.getSizeInventory()];
		inventoryPlayer.mainInventory[inventoryPlayer.currentItem].getTagCompound().setTag(ItemListPaper.TAG_NAME, nbtTagList);
	}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}

}
