package nahama.ofalenmod.inventory;

import nahama.ofalenmod.recipe.OfalenSmeltingRecipes;
import nahama.ofalenmod.tileentity.TileEntitySmeltingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerSmeltingMachine extends Container {

	private TileEntitySmeltingMachine tileEntity;

	private int lastSmeltTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerSmeltingMachine(EntityPlayer player, TileEntitySmeltingMachine tileEntity) {
		this.tileEntity = tileEntity;

		//コンテナを設定
		this.addSlotToContainer(new Slot(this.tileEntity, 0, 56, 17));
		this.addSlotToContainer(new Slot(this.tileEntity, 1, 56, 53));
		this.addSlotToContainer(new SlotFurnace(player, this.tileEntity, 2, 116, 35));
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
		iCrafting.sendProgressBarUpdate(this, 0, this.tileEntity.smeltTime);
		iCrafting.sendProgressBarUpdate(this, 1, this.tileEntity.burnTime);
		iCrafting.sendProgressBarUpdate(this, 2, this.tileEntity.currentItemBurnTime);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if (this.lastSmeltTime != this.tileEntity.smeltTime) {
				icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.smeltTime);
			}

			if (this.lastBurnTime != this.tileEntity.burnTime) {
				icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.burnTime);
			}

			if (this.lastItemBurnTime != this.tileEntity.currentItemBurnTime) {
				icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.currentItemBurnTime);
			}
		}

		this.lastSmeltTime = this.tileEntity.smeltTime;
		this.lastBurnTime = this.tileEntity.burnTime;
		this.lastItemBurnTime = this.tileEntity.currentItemBurnTime;
	}

	/**燃料・製錬の残り時間表示の更新*/
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.tileEntity.smeltTime = par2;
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
				if (!this.mergeItemStack(itemStack1, 3, 39, true)) {
					return null;
				}
				slot.onSlotChange(itemStack1, itemStack);

			//インベントリのスロットか
			} else if (slotNumber != 1 && slotNumber != 0) {

				//製錬可能ならば、製錬前アイテムのスロットに移動
				if (OfalenSmeltingRecipes.smelting().getSmeltingResult(itemStack1) != null) {
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
						return null;
					}
				//燃料ならば、燃料のスロットに移動
				} else if (tileEntity.isItemFuel(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 1, 2, false)) {
						return null;
					}
				//製錬可能でもなく、燃料でもないならホットバーに移動
				} else if (slotNumber >= 3 && slotNumber < 30) {
					if (!this.mergeItemStack(itemStack1, 30, 39, false)) {
						return null;
					}
				} else if (slotNumber >= 30 && slotNumber < 39 && !this.mergeItemStack(itemStack1, 3, 30, false)) {
					return null;
				}
			//それ(製錬前アイテム・燃料)以外ならインベントリに移動
			} else if (!this.mergeItemStack(itemStack1, 3, 39, false)) {
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