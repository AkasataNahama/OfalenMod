package nahama.ofalenmod.gui;

import nahama.ofalenmod.inventory.ContainerTeleportMarker;
import nahama.ofalenmod.tileentity.TileEntityTeleportMarker;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

public class GuiTeleportMarker extends GuiContainer {

	public GuiTeleportMarker(EntityPlayer player, TileEntityTeleportMarker marker) {
		super(new ContainerTeleportMarker(player, marker));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {}

}
