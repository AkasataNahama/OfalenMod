package nahama.ofalenmod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryItemTeleporter implements IInventory {

	private InventoryPlayer inventoryPlayer;
	private ItemStack currentItem;
	private ItemStack material;

	public InventoryItemTeleporter(InventoryPlayer inventory) {
		inventoryPlayer = inventory;
		currentItem = inventoryPlayer.getCurrentItem();
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 1;
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return material;
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (material == null)
			return null;
		ItemStack itemstack;
		if (material.stackSize <= amount) {
			itemstack = material;
			material = null;
			return itemstack;
		}
		itemstack = material.splitStack(amount);
		if (material.stackSize < 1) {
			material = null;
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
		material = itemStack;
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
		return "container.OfalenMod.ItemTeleporter";
	}

	/** このインベントリの最大スタック数を返す。 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {}

	/** プレイヤーが使用できるかどうか。 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	/** インベントリが開かれた時の処理。 */
	@Override
	public void openInventory() {
		// アイテムを読み込む。
		if (!currentItem.hasTagCompound())
			currentItem.setTagCompound(new NBTTagCompound());
		if (currentItem.getTagCompound().hasKey("Material")) {
			material = ItemStack.loadItemStackFromNBT(currentItem.getTagCompound().getCompoundTag("Material"));
			return;
		}
		material = null;
		return;
	}

	/** インベントリが閉じられた時の処理。 */
	@Override
	public void closeInventory() {
		// アイテムを保存する。
		ItemStack result = currentItem.copy();
		if (!result.hasTagCompound())
			result.setTagCompound(new NBTTagCompound());
		if (material == null) {
			result.getTagCompound().removeTag("Material");
		} else {
			NBTTagCompound nbt = new NBTTagCompound();
			material.writeToNBT(nbt);
			result.getTagCompound().setTag("Material", nbt);
		}
		inventoryPlayer.mainInventory[inventoryPlayer.currentItem] = result;
	}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}

}
