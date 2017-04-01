package nahama.ofalenmod.inventory;

import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class InventoryItemFilterInstaller extends InventoryItemBase {
	public InventoryItemFilterInstaller(InventoryPlayer inventory) {
		super(inventory);
	}

	public void installFilter() {
		// 適用対象スロットか見本スロットが空、もしくは適用後スロットが空でないなら終了。
		if (itemStacks[0] == null || itemStacks[1] == null || itemStacks[2] != null)
			return;
		// 適用対象が無効なら終了。
		if (!FilterUtil.isAvailableFilterTag(itemStacks[0]))
			return;
		itemStacks[2] = itemStacks[0].copy();
		itemStacks[0] = null;
		if (!FilterUtil.isAvailableFilterTag(itemStacks[1])) {
			// 見本が無効なら適用対象を初期化する。
			FilterUtil.initFilterTag(itemStacks[2]);
			return;
		}
		// 見本のフィルターを適用対象にコピーする。
		FilterUtil.copyFilterTag(FilterUtil.getFilterTag(itemStacks[2]), FilterUtil.getFilterTag(itemStacks[1]));
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (itemStacks[slot] == null)
			return null;
		ItemStack itemstack = itemStacks[slot];
		itemStacks[slot] = null;
		return itemstack;
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public String getInventoryName() {
		return "inventory.OfalenMod.InventoryItemFilterInstaller";
	}

	@Override
	public void openInventory() {
		itemStacks = new ItemStack[this.getSizeInventory()];
	}

	@Override
	public void closeInventory() {
		itemStacks = new ItemStack[this.getSizeInventory()];
	}
}
