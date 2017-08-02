package nahama.ofalenmod.inventory;

import nahama.ofalenmod.tileentity.TileEntityWorldEditorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSettingWorldEditorBase extends Container {
	protected TileEntityWorldEditorBase tileEntity;
	private short[] lastValues;

	public ContainerSettingWorldEditorBase(TileEntityWorldEditorBase tileEntity) {
		this.tileEntity = tileEntity;
		lastValues = new short[tileEntity.getAmountSettingID()];
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	@Override
	public void addCraftingToCrafters(ICrafting iCrafting) {
		super.addCraftingToCrafters(iCrafting);
		for (int i = 0; i < tileEntity.getAmountSettingID(); i++) {
			iCrafting.sendProgressBarUpdate(this, i, tileEntity.getWithID(i));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (Object crafter : crafters) {
			ICrafting iCrafting = (ICrafting) crafter;
			for (int i = 0; i < lastValues.length; i++) {
				if (tileEntity.getWithID(i) != lastValues[i])
					iCrafting.sendProgressBarUpdate(this, i, tileEntity.getWithID(i));
			}
		}
		for (int i = 0; i < lastValues.length; i++) {
			if (tileEntity.getWithID(i) != lastValues[i])
				lastValues[i] = tileEntity.getWithID(i);
		}
	}

	@Override
	public void updateProgressBar(int channel, int value) {
		tileEntity.setWithID(channel, value);
	}

	@Override
	public Slot getSlotFromInventory(IInventory inventory, int num) {
		return new Slot(inventory, num, 0, 0);
	}

	@Override
	public void putStackInSlot(int p_75141_1_, ItemStack p_75141_2_) {
	}
}
