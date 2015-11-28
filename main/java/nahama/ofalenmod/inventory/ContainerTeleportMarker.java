package nahama.ofalenmod.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.tileentity.TileEntityTeleportMarker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTeleportMarker extends Container {

	private TileEntityTeleportMarker tileEntity;
	private boolean lastIsValid;
	private int lastChannel;

	public ContainerTeleportMarker(EntityPlayer player, TileEntityTeleportMarker tileEntity) {
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
		for (int i = 0; i < crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) crafters.get(i);
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
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			tileEntity.isValid = (par2 == 0);
		} else if (par1 == 1) {
			tileEntity.setChannel(par2);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) != tileEntity ? false : player.getDistanceSq(tileEntity.xCoord + 0.5D, tileEntity.yCoord + 0.5D, tileEntity.zCoord + 0.5D) <= 64.0D;
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
