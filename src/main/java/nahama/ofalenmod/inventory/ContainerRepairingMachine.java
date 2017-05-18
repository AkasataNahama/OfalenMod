package nahama.ofalenmod.inventory;

import nahama.ofalenmod.tileentity.TileEntityRepairingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRepairingMachine extends Container {
	private TileEntityRepairingMachine tileEntity;
	private short lastRepairingTime;
	private short lastBurningTime;
	private short lastItemBurningTime;

	public ContainerRepairingMachine(EntityPlayer player, TileEntityRepairingMachine tileEntity) {
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new Slot(tileEntity, 0, 56, 17));
		this.addSlotToContainer(new Slot(tileEntity, 1, 56, 53));
		this.addSlotToContainer(new SlotUnputtable(tileEntity, 2, 116, 35));
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
		iCrafting.sendProgressBarUpdate(this, 0, tileEntity.timeWorking);
		iCrafting.sendProgressBarUpdate(this, 1, tileEntity.timeBurning);
		iCrafting.sendProgressBarUpdate(this, 2, tileEntity.timeMaxBurning);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (Object crafter : crafters) {
			ICrafting icrafting = (ICrafting) crafter;
			if (lastRepairingTime != tileEntity.timeWorking) {
				icrafting.sendProgressBarUpdate(this, 0, tileEntity.timeWorking);
			}
			if (lastBurningTime != tileEntity.timeBurning) {
				icrafting.sendProgressBarUpdate(this, 1, tileEntity.timeBurning);
			}
			if (lastItemBurningTime != tileEntity.timeMaxBurning) {
				icrafting.sendProgressBarUpdate(this, 2, tileEntity.timeMaxBurning);
			}
		}
		lastRepairingTime = tileEntity.timeWorking;
		lastBurningTime = tileEntity.timeBurning;
		lastItemBurningTime = tileEntity.timeMaxBurning;
	}

	@Override
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			tileEntity.timeWorking = (short) par2;
		}
		if (par1 == 1) {
			tileEntity.timeBurning = (short) par2;
		}
		if (par1 == 2) {
			tileEntity.timeMaxBurning = (short) par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int number) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(number);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (number == 2) {
				if (!this.mergeItemStack(itemStack1, 3, 39, true)) {
					return null;
				}
				slot.onSlotChange(itemStack1, itemStack);
			} else if (number != 1 && number != 0) {
				if (itemStack1.isItemDamaged()) {
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
						return null;
					}
				} else if (tileEntity.isItemFuel(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 1, 2, false)) {
						return null;
					}
				} else if (number >= 3 && number < 30) {
					if (!this.mergeItemStack(itemStack1, 30, 39, false)) {
						return null;
					}
				} else if (number >= 30 && number < 39 && !this.mergeItemStack(itemStack1, 3, 30, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemStack1, 3, 39, false)) {
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
