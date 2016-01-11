package nahama.ofalenmod.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDicCore;
import nahama.ofalenmod.item.ItemOfalen;
import nahama.ofalenmod.tileentity.TileEntityConversionMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerConversionMachine extends Container {

	private TileEntityConversionMachine tileEntity;
	private int lastConversionTime;
	private int lastBurnTime;
	private int lastItemBurnTime;
	/** 変換機のインベントリの第一スロットの番号 */
	private static final int index0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号 */
	private static final int index1 = 4;
	/** クイックスロットの第一スロットの番号 */
	private static final int index2 = index1 + 27;
	/** このコンテナの全体のスロット数 */
	private static final int index3 = index2 + 9;

	public ContainerConversionMachine(EntityPlayer player, TileEntityConversionMachine tileEntity) {
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new Slot(tileEntity, 0, 56, 17));
		this.addSlotToContainer(new Slot(tileEntity, 1, 56, 53));
		this.addSlotToContainer(new SlotUnputable(tileEntity, 2, 116, 35));
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
		iCrafting.sendProgressBarUpdate(this, 0, tileEntity.workTime);
		iCrafting.sendProgressBarUpdate(this, 1, tileEntity.burnTime);
		iCrafting.sendProgressBarUpdate(this, 2, tileEntity.maxBurnTime);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) crafters.get(i);
			if (lastConversionTime != tileEntity.workTime) {
				icrafting.sendProgressBarUpdate(this, 0, tileEntity.workTime);
			}
			if (lastBurnTime != tileEntity.burnTime) {
				icrafting.sendProgressBarUpdate(this, 1, tileEntity.burnTime);
			}
			if (lastItemBurnTime != tileEntity.maxBurnTime) {
				icrafting.sendProgressBarUpdate(this, 2, tileEntity.maxBurnTime);
			}
		}
		lastConversionTime = tileEntity.workTime;
		lastBurnTime = tileEntity.burnTime;
		lastItemBurnTime = tileEntity.maxBurnTime;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			tileEntity.workTime = par2;
		}
		if (par1 == 1) {
			tileEntity.burnTime = par2;
		}
		if (par1 == 2) {
			tileEntity.maxBurnTime = par2;
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
				if (!this.mergeItemStack(itemStack1, index1, index3, true)) {
					return null;
				}
				slot.onSlotChange(itemStack1, itemStack);
			} else if (slotNumber > 3) {
				if (((itemStack1.getItem() instanceof ItemOfalen) && !itemStack1.isItemEqual(new ItemStack(OfalenModItemCore.coreOfalen, 1, 3))) || Block.getBlockFromItem(itemStack1.getItem()) == OfalenModBlockCore.blockOfalen) {
					if (!this.mergeItemStack(itemStack1, 3, 4, false)) {
						if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
							return null;
						}
					}
				} else if (OfalenModOreDicCore.isMuchedItemStack(OfalenModOreDicCore.listCreeperOfalenBlock, itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
						return null;
					}
				} else if (tileEntity.isItemFuel(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 1, 2, false)) {
						return null;
					}
				} else if (slotNumber >= index1 && slotNumber < index2) {
					if (!this.mergeItemStack(itemStack1, index2, index3, false)) {
						return null;
					}
				} else if (slotNumber >= index2 && slotNumber < index3 && !this.mergeItemStack(itemStack1, index1, index2, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemStack1, index1, index3, false)) {
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
