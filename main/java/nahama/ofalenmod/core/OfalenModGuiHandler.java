package nahama.ofalenmod.core;

import cpw.mods.fml.common.network.IGuiHandler;
import nahama.ofalenmod.gui.GuiConversionMachine;
import nahama.ofalenmod.gui.GuiItemTeleporter;
import nahama.ofalenmod.gui.GuiRepairMachine;
import nahama.ofalenmod.gui.GuiSmeltingMachine;
import nahama.ofalenmod.gui.GuiTeleportMarker;
import nahama.ofalenmod.inventory.ContainerConversionMachine;
import nahama.ofalenmod.inventory.ContainerItemTeleporter;
import nahama.ofalenmod.inventory.ContainerRepairMachine;
import nahama.ofalenmod.inventory.ContainerSmeltingMachine;
import nahama.ofalenmod.inventory.ContainerTeleportMarker;
import nahama.ofalenmod.tileentity.TileEntityConversionMachine;
import nahama.ofalenmod.tileentity.TileEntityRepairMachine;
import nahama.ofalenmod.tileentity.TileEntitySmeltingMachine;
import nahama.ofalenmod.tileentity.TileEntityTeleportMarker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OfalenModGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == 2)
			return new ContainerItemTeleporter(player);
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntitySmeltingMachine) {
			return new ContainerSmeltingMachine(player, (TileEntitySmeltingMachine) tileentity);
		} else if (tileentity instanceof TileEntityRepairMachine) {
			return new ContainerRepairMachine(player, (TileEntityRepairMachine) tileentity);
		} else if (tileentity instanceof TileEntityConversionMachine) {
			return new ContainerConversionMachine(player, (TileEntityConversionMachine) tileentity);
		} else if (tileentity instanceof TileEntityTeleportMarker) {
			return new ContainerTeleportMarker(player, (TileEntityTeleportMarker) tileentity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == 2)
			return new GuiItemTeleporter(player);
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntitySmeltingMachine) {
			return new GuiSmeltingMachine(player, (TileEntitySmeltingMachine) tileentity);
		} else if (tileentity instanceof TileEntityRepairMachine) {
			return new GuiRepairMachine(player, (TileEntityRepairMachine) tileentity);
		} else if (tileentity instanceof TileEntityConversionMachine) {
			return new GuiConversionMachine(player, (TileEntityConversionMachine) tileentity);
		} else if (tileentity instanceof TileEntityTeleportMarker) {
			return new GuiTeleportMarker(player, (TileEntityTeleportMarker) tileentity);
		}
		return null;
	}

}