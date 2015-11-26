package nahama.ofalenmod.inventory;

import nahama.ofalenmod.tileentity.TileEntityTeleportMarker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerTeleportMarker extends Container {

	public ContainerTeleportMarker(EntityPlayer player, TileEntityTeleportMarker marker) {}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return false;
	}

}
