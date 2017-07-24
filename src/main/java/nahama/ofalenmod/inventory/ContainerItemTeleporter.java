package nahama.ofalenmod.inventory;

import nahama.ofalenmod.handler.OfalenTeleportHandler;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemTeleporter extends Container {
	private EntityPlayer player;
	private boolean lastIsValid;

	public ContainerItemTeleporter(EntityPlayer player) {
		this.player = player;
		if (!player.worldObj.isRemote)
			player.getHeldItem().getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, OfalenTeleportHandler.isChannelValid(player.getHeldItem().getItemDamage()));
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
		return true;
	}

	@Override
	public void addCraftingToCrafters(ICrafting iCrafting) {
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, player.getHeldItem().getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID) ? 0 : 1);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		boolean isValid = player.getHeldItem().getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID);
		for (Object crafter : crafters) {
			ICrafting icrafting = (ICrafting) crafter;
			if (lastIsValid != isValid) {
				icrafting.sendProgressBarUpdate(this, 0, isValid ? 0 : 1);
			}
		}
		lastIsValid = isValid;
	}

	@Override
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			Minecraft.getMinecraft().thePlayer.getHeldItem().getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, (par2 == 0));
		}
	}

	@Override
	public ItemStack slotClick(int slotNumber, int par2, int par3, EntityPlayer player) {
		if (slotNumber == player.inventory.currentItem + 27) {
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
			if (slotNumber == player.inventory.currentItem + 27)
				return null;
			if (slotNumber < 27) {
				if (!this.mergeItemStack(itemStack1, 27, 36, false)) {
					return null;
				}
			} else if (slotNumber < 36 && !this.mergeItemStack(itemStack1, 0, 27, false)) {
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
