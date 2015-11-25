package nahama.ofalenmod.inventory;

import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDicCore;
import nahama.ofalenmod.item.Ofalen;
import nahama.ofalenmod.tileentity.TileEntityConversionMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerConversionMachine extends Container {

	private TileEntityConversionMachine tileEntity;

	private int lastConversionTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerConversionMachine(EntityPlayer player, TileEntityConversionMachine tileEntity) {
		this.tileEntity = tileEntity;

		//コンテナを設定
		this.addSlotToContainer(new Slot(this.tileEntity, 0, 56, 17));
		this.addSlotToContainer(new Slot(this.tileEntity, 1, 56, 53));
		this.addSlotToContainer(new SlotFurnace(player, this.tileEntity, 2, 116, 35));
		this.addSlotToContainer(new Slot(this.tileEntity, 3, 83, 17));
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
		iCrafting.sendProgressBarUpdate(this, 0, this.tileEntity.conversionTime);
		iCrafting.sendProgressBarUpdate(this, 1, this.tileEntity.burnTime);
		iCrafting.sendProgressBarUpdate(this, 2, this.tileEntity.currentItemBurnTime);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if (this.lastConversionTime != this.tileEntity.conversionTime) {
				icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.conversionTime);
			}

			if (this.lastBurnTime != this.tileEntity.burnTime) {
				icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.burnTime);
			}

			if (this.lastItemBurnTime != this.tileEntity.currentItemBurnTime) {
				icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.currentItemBurnTime);
			}
		}

		this.lastConversionTime = this.tileEntity.conversionTime;
		this.lastBurnTime = this.tileEntity.burnTime;
		this.lastItemBurnTime = this.tileEntity.currentItemBurnTime;
	}

	/**燃料・変換の残り時間表示の更新*/
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.tileEntity.conversionTime = par2;
		}

		if (par1 == 1) {
			this.tileEntity.burnTime = par2;
		}

		if (par1 == 2) {
			this.tileEntity.currentItemBurnTime = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileEntity.isUseableByPlayer(player);
	}

	/**Shift左クリックされた時の処理*/
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();

			//完成品のスロットか
			if (slotNumber == 2) {

				//インベントリに移動
				if (!this.mergeItemStack(itemStack1, 4, 40, true)) {
					return null;
				}
				slot.onSlotChange(itemStack1, itemStack);

			//インベントリのスロットか
			} else if (slotNumber != 1 && slotNumber != 0 && slotNumber != 3) {

				//変換可能アイテムなら、
				if (((itemStack1.getItem() instanceof Ofalen) && !itemStack1.isItemEqual(new ItemStack(OfalenModItemCore.coreOfalen, 1, 3))) || Block.getBlockFromItem(itemStack1.getItem()) == OfalenModBlockCore.blockOfalen) {
					//変換色として指定可能ならば移動
					if (!this.mergeItemStack(itemStack1, 3, 4, false)) {
						//変換可能ならば、変換前アイテムのスロットに移動
						if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
							return null;
						}
					}
				//匠Craftとのコラボアイテムなら、
				} else if (OfalenModOreDicCore.isTakumiCraftLoaded && itemStack1.isItemEqual(OfalenModOreDicCore.ofalenCreeper)) {
					//変換前アイテムのスロットに移動
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
						return null;
					}
				//燃料ならば、燃料のスロットに移動
				} else if (tileEntity.isItemFuel(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 1, 2, false)) {
						return null;
					}
				//変換可能でもなく、燃料でもないならホットバーに移動
				} else if (slotNumber >= 4 && slotNumber < 31) {
					if (!this.mergeItemStack(itemStack1, 31, 40, false)) {
						return null;
					}
				} else if (slotNumber >= 31 && slotNumber < 40 && !this.mergeItemStack(itemStack1, 4, 31, false)) {
					return null;
				}

			//それ以外(変換前アイテム・燃料)ならインベントリに移動
			} else if (!this.mergeItemStack(itemStack1, 4, 40, false)) {
				return null;
			}

			if (itemStack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
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