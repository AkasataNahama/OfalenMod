package nahama.ofalenmod.inventory;

import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemFilterInstaller extends ContainerItemBase {
	public ContainerItemFilterInstaller(EntityPlayer player) {
		super(player);
	}

	public void installFilter() {
		((InventoryItemFilterInstaller) inventory).installFilter();
	}

	@Override
	protected IInventory createInventory(EntityPlayer player) {
		return new InventoryItemFilterInstaller(player.inventory);
	}

	@Override
	protected void initIndexes() {
		index0 = 0;
		index1 = 3;
		index2 = (byte) (index1 + 27);
		index3 = (byte) (index2 + 9);
	}

	@Override
	protected void initItemSlot() {
		this.addSlotToContainer(new Slot(inventory, 0, 56, 35));
		this.addSlotToContainer(new Slot(inventory, 1, 83, 17));
		this.addSlotToContainer(new Slot(inventory, 2, 116, 35));
	}

	@Override
	protected void initPlayerSlot(EntityPlayer player) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	protected boolean isItemStackEnabled(ItemStack itemStack) {
		return OfalenNBTUtil.FilterUtil.isAvailableFilterTag(itemStack);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		if (player.worldObj.isRemote) {
			super.onContainerClosed(player);
			return;
		}
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlotOnClosing(i);
			if (itemStack != null)
				player.dropPlayerItemWithRandomChoice(itemStack, false);
		}
		super.onContainerClosed(player);
	}

	@Override
	protected boolean transferStackInItemSlot(ItemStack itemStack, int slotNumber) {
		return this.mergeItemStack(itemStack, index0, index0 + 2, true);
	}
}
