package nahama.ofalenmod.inventory;

import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerItemCollector extends ContainerItemBase {
	public ContainerItemCollector(EntityPlayer player) {
		super(player);
	}

	@Override
	protected IInventory createInventory(EntityPlayer player) {
		return new InventoryItemFuture(player.inventory, (byte) 9, (short) 27, "collector");
	}

	@Override
	protected boolean isItemStackEnabled(ItemStack itemStack) {
		return itemStack.isItemEqual(new ItemStack(OfalenModItemCore.partsOfalen, 1, 9));
	}
}
