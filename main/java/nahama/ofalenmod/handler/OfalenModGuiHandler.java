package nahama.ofalenmod.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import nahama.ofalenmod.gui.GuiConversionMachine;
import nahama.ofalenmod.gui.GuiItemFloater;
import nahama.ofalenmod.gui.GuiItemShield;
import nahama.ofalenmod.gui.GuiItemTeleporter;
import nahama.ofalenmod.gui.GuiPresentBox;
import nahama.ofalenmod.gui.GuiRepairMachine;
import nahama.ofalenmod.gui.GuiSmeltingMachine;
import nahama.ofalenmod.gui.GuiTeleportMarker;
import nahama.ofalenmod.inventory.ContainerPresentBox;
import nahama.ofalenmod.inventory.ContainerConversionMachine;
import nahama.ofalenmod.inventory.ContainerItemFloater;
import nahama.ofalenmod.inventory.ContainerItemShield;
import nahama.ofalenmod.inventory.ContainerItemTeleporter;
import nahama.ofalenmod.inventory.ContainerRepairMachine;
import nahama.ofalenmod.inventory.ContainerSmeltingMachine;
import nahama.ofalenmod.inventory.ContainerTeleportMarker;
import nahama.ofalenmod.tileentity.TileEntityConversionMachine;
import nahama.ofalenmod.tileentity.TileEntityPresentBox;
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
			return new ContainerItemShield(player);
		if (id == 3)
			return new ContainerItemTeleporter(player);
		if (id == 4)
			return new ContainerItemFloater(player);
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntitySmeltingMachine) {
			return new ContainerSmeltingMachine(player, (TileEntitySmeltingMachine) tileentity);
		}
		if (tileentity instanceof TileEntityRepairMachine) {
			return new ContainerRepairMachine(player, (TileEntityRepairMachine) tileentity);
		}
		if (tileentity instanceof TileEntityConversionMachine) {
			return new ContainerConversionMachine(player, (TileEntityConversionMachine) tileentity);
		}
		if (tileentity instanceof TileEntityTeleportMarker) {
			return new ContainerTeleportMarker(player, (TileEntityTeleportMarker) tileentity);
		}
		if (tileentity instanceof TileEntityPresentBox) {
			return new ContainerPresentBox(player, (TileEntityPresentBox) tileentity);
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
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntitySmeltingMachine) {
			return new GuiSmeltingMachine(player, (TileEntitySmeltingMachine) tileentity);
		}
		if (tileentity instanceof TileEntityRepairMachine) {
			return new GuiRepairMachine(player, (TileEntityRepairMachine) tileentity);
		}
		if (tileentity instanceof TileEntityConversionMachine) {
			return new GuiConversionMachine(player, (TileEntityConversionMachine) tileentity);
		}
		if (tileentity instanceof TileEntityTeleportMarker) {
			return new GuiTeleportMarker(player, (TileEntityTeleportMarker) tileentity);
		}
		if (tileentity instanceof TileEntityPresentBox) {
			return new GuiPresentBox(player, (TileEntityPresentBox) tileentity);
		}
		return null;
	}

}
