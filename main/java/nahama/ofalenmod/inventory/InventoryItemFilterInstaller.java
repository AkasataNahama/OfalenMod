package nahama.ofalenmod.inventory;

import nahama.ofalenmod.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import static nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;

public class InventoryItemFilterInstaller extends InventoryItemBase {
	public InventoryItemFilterInstaller(InventoryPlayer inventory) {
		super(inventory);
	}

	public void installFilter() {
		if (itemStacks[0] == null || itemStacks[1] == null || itemStacks[2] != null)
			return;
		if (!FilterUtil.isAvailableFilterTag(itemStacks[0]))
			return;
		itemStacks[2] = itemStacks[0].copy();
		itemStacks[0] = null;
		if (!FilterUtil.isAvailableFilterTag(itemStacks[1])) {
			FilterUtil.initFilterTag(itemStacks[2]);
			return;
		}
		FilterUtil.setFilterTag(FilterUtil.getFilterTag(itemStacks[2]), FilterUtil.getFilterTag(itemStacks[1]));
	}

	public void dropContents(EntityPlayer player) {
		for (ItemStack itemStack : itemStacks) {
			if (itemStack == null)
				continue;
			Util.dropItemStackCopyNearEntity(itemStack, player);
		}
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
