package nahama.ofalenmod.inventory;

import nahama.ofalenmod.item.ItemShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerItemShield extends ContainerItemBase {
	public ContainerItemShield(EntityPlayer player) {
		super(player);
	}

	@Override
	protected IInventory createInventory(EntityPlayer player) {
		return new InventoryItemFuture(player.inventory, (byte) 6, (short) 27, "shield");
	}

	@Override
	protected boolean isItemStackEnabled(ItemStack itemStack) {
		return ItemShield.isItemMaterial(itemStack);
	}
}
