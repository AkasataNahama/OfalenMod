package nahama.ofalenmod.inventory;

import nahama.ofalenmod.tileentity.TileEntityTeleportingMarker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTeleportingMarker extends Container {
	private TileEntityTeleportingMarker tileEntity;
	private boolean lastIsValid;
	private short lastChannel;

	public ContainerTeleportingMarker(EntityPlayer player, TileEntityTeleportingMarker tileEntity) {
		this.tileEntity = tileEntity;
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
	public void addCraftingToCrafters(ICrafting iCrafting) {
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, tileEntity.isValid ? 0 : 1);
		iCrafting.sendProgressBarUpdate(this, 1, tileEntity.getChannel());
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (Object crafter : crafters) {
			ICrafting icrafting = (ICrafting) crafter;
			if (lastIsValid != tileEntity.isValid) {
				icrafting.sendProgressBarUpdate(this, 0, tileEntity.isValid ? 0 : 1);
			}
			if (lastChannel != tileEntity.getChannel()) {
				icrafting.sendProgressBarUpdate(this, 1, tileEntity.getChannel());
			}
		}
		lastIsValid = tileEntity.isValid;
		lastChannel = tileEntity.getChannel();
	}

	@Override
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			tileEntity.isValid = (par2 == 0);
		} else if (par1 == 1) {
			tileEntity.setChannel((short) par2);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) == tileEntity && player.getDistanceSq(tileEntity.xCoord + 0.5D, tileEntity.yCoord + 0.5D, tileEntity.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (0 <= slotNumber && slotNumber < 27) {
				if (!this.mergeItemStack(itemStack1, 27, 36, false)) {
					return null;
				}
			} else if (27 <= slotNumber && slotNumber < 36 && !this.mergeItemStack(itemStack1, 0, 27, false)) {
				return null;
			}
			if (itemStack1.stackSize == 0) {
				slot.putStack(null);
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
