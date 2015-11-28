package nahama.ofalenmod.inventory;

import nahama.ofalenmod.item.ItemTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemTeleporter extends Container {

	private InventoryItemTeleporter inventory;
	private int lastChannel;

	public ContainerItemTeleporter(EntityPlayer player) {
		inventory = new InventoryItemTeleporter(player.inventory);
		inventory.openInventory();
		this.addSlotToContainer(new Slot(inventory, 0, 134, 54));
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		int channel = player.getHeldItem().getItemDamage();
		inventory.closeInventory();
		player.getHeldItem().setItemDamage(channel);
	}

	@Override
	public ItemStack slotClick(int slotNumber, int par2, int par3, EntityPlayer player) {
		if (slotNumber - 1 == player.inventory.currentItem + 27) {
			return null;
		}
		return super.slotClick(slotNumber, par2, par3, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (slotNumber == 0) {
				if (!this.mergeItemStack(itemStack1, 1, 37, true)) {
					return null;
				}
			} else {
				if (slotNumber - 1 == player.inventory.currentItem + 27)
					return null;
				if (ItemTeleporter.isItemMaterial(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
						return null;
					}
				} else if (1 <= slotNumber && slotNumber < 28) {
					if (!this.mergeItemStack(itemStack1, 28, 37, false)) {
						return null;
					}
				} else if (28 <= slotNumber && slotNumber < 37 && !this.mergeItemStack(itemStack1, 1, 28, false)) {
					return null;
				}
			}

			if (itemStack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			if (itemStack1.stackSize == itemStack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, itemStack1);
		}
		return itemStack;
	}

}
