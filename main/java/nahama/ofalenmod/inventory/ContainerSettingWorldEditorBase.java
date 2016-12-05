package nahama.ofalenmod.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.tileentity.TileEntityWorldEditorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;

public class ContainerSettingWorldEditorBase extends Container {
	protected TileEntityWorldEditorBase tileEntity;
	protected short[] lastValues;

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
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int channel, int value) {
		tileEntity.setWithID(channel, value);
	}
}
