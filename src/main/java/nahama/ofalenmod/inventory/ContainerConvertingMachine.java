package nahama.ofalenmod.inventory;

import nahama.ofalenmod.core.OfalenModOreDictCore;
import nahama.ofalenmod.tileentity.TileEntityConvertingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerConvertingMachine extends Container {
	/** 変換機のインベントリの第一スロットの番号。 */
	private static final byte INDEX_0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号。 */
	private static final byte INDEX_1 = 4;
	/** クイックスロットの第一スロットの番号。 */
	private static final byte INDEX_2 = INDEX_1 + 27;
	/** このコンテナの全体のスロット数。 */
	private static final byte INDEX_3 = INDEX_2 + 9;
	private TileEntityConvertingMachine tileEntity;
	private short lastConvertingTime;
	private short lastBurningTime;
	private short lastItemBurningTime;

	public ContainerConvertingMachine(EntityPlayer player, TileEntityConvertingMachine tileEntity) {
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new Slot(tileEntity, 0, 56, 17));
		this.addSlotToContainer(new Slot(tileEntity, 1, 56, 53));
		this.addSlotToContainer(new SlotUnputtable(tileEntity, 2, 116, 35));
		this.addSlotToContainer(new Slot(tileEntity, 3, 83, 17));
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
			if (lastConvertingTime != tileEntity.timeWorking) {
				icrafting.sendProgressBarUpdate(this, 0, tileEntity.timeWorking);
			}
			if (lastBurningTime != tileEntity.timeBurning) {
				icrafting.sendProgressBarUpdate(this, 1, tileEntity.timeBurning);
			}
			if (lastItemBurningTime != tileEntity.timeMaxBurning) {
				icrafting.sendProgressBarUpdate(this, 2, tileEntity.timeMaxBurning);
			}
		}
		lastConvertingTime = tileEntity.timeWorking;
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
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (slotNumber == 2) {
				if (!this.mergeItemStack(itemStack1, INDEX_1, INDEX_3, true)) {
					return null;
				}
				slot.onSlotChange(itemStack1, itemStack);
			} else if (slotNumber > 3) {
				if (TileEntityConvertingMachine.isProperItemStack(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 3, 4, false)) {
						if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
							return null;
						}
					}
				} else if (OfalenModOreDictCore.isMatchedItemStack(OfalenModOreDictCore.listCreeperOfalenBlock, itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
						return null;
					}
				} else if (tileEntity.isItemFuel(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 1, 2, false)) {
						return null;
					}
				} else if (slotNumber >= INDEX_1 && slotNumber < INDEX_2) {
					if (!this.mergeItemStack(itemStack1, INDEX_2, INDEX_3, false)) {
						return null;
					}
				} else if (slotNumber >= INDEX_2 && slotNumber < INDEX_3 && !this.mergeItemStack(itemStack1, INDEX_1, INDEX_2, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemStack1, INDEX_1, INDEX_3, false)) {
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
