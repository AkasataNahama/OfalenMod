package nahama.ofalenmod.inventory;

import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

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
		if (slot < 0 || this.getSizeInventory() < slot)
			return;
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
		return "container.ofalen.teleporter";
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
		if (currentItem.getTagCompound().getByte(OfalenNBTUtil.MATERIAL) > 0) {
			material = new ItemStack(OfalenModItemCore.partsOfalen, currentItem.getTagCompound().getByte(OfalenNBTUtil.MATERIAL), 7);
		} else {
			material = null;
		}
	}

	/** インベントリが閉じられた時の処理。 */
	@Override
	public void closeInventory() {
		// アイテムを保存する。
		ItemStack result = inventoryPlayer.getCurrentItem().copy();
		if (material == null) {
			result.getTagCompound().setByte(OfalenNBTUtil.MATERIAL, (byte) 0);
		} else {
			result.getTagCompound().setByte(OfalenNBTUtil.MATERIAL, (byte) material.stackSize);
		}
		inventoryPlayer.mainInventory[inventoryPlayer.currentItem] = result;
	}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}
}
