package nahama.ofalenmod.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import nahama.ofalenmod.gui.*;
import nahama.ofalenmod.inventory.*;
import nahama.ofalenmod.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OfalenModGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == 2)
			return new ContainerItemShield(player);
		if (id == 3)
			return new ContainerItemTeleporter(player);
		if (id == 4)
			return new ContainerItemFloater(player);
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntitySmeltingMachine) {
			return new ContainerSmeltingMachine(player, (TileEntitySmeltingMachine) tileEntity);
		}
		if (tileEntity instanceof TileEntityRepairMachine) {
			return new ContainerRepairMachine(player, (TileEntityRepairMachine) tileEntity);
		}
		if (tileEntity instanceof TileEntityConversionMachine) {
			return new ContainerConversionMachine(player, (TileEntityConversionMachine) tileEntity);
		}
		if (tileEntity instanceof TileEntityFusingMachine) {
			return new ContainerFusingMachine(player, (TileEntityFusingMachine) tileEntity);
		}
		if (tileEntity instanceof TileEntityTeleportMarker) {
			return new ContainerTeleportMarker(player, (TileEntityTeleportMarker) tileEntity);
		}
		if (tileEntity instanceof TileEntityPresentBox) {
			return new ContainerPresentBox(player, (TileEntityPresentBox) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == 2)
			return new GuiItemShield(player);
		if (id == 3)
			return new GuiItemTeleporter(player);
		if (id == 4)
			return new GuiItemFloater(player);
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntitySmeltingMachine) {
			return new GuiSmeltingMachine(player, (TileEntitySmeltingMachine) tileEntity);
		}
		if (tileEntity instanceof TileEntityRepairMachine) {
			return new GuiRepairMachine(player, (TileEntityRepairMachine) tileEntity);
		}
		if (tileEntity instanceof TileEntityConversionMachine) {
			return new GuiConversionMachine(player, (TileEntityConversionMachine) tileEntity);
		}
		if (tileEntity instanceof TileEntityFusingMachine) {
			return new GuiFusingMachine(player, (TileEntityFusingMachine) tileEntity);
		}
		if (tileEntity instanceof TileEntityTeleportMarker) {
			return new GuiTeleportMarker(player, (TileEntityTeleportMarker) tileEntity);
		}
		if (tileEntity instanceof TileEntityPresentBox) {
			return new GuiPresentBox(player, (TileEntityPresentBox) tileEntity);
		}
		return null;
	}

}
