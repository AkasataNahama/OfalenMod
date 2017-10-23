package nahama.ofalenmod.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import nahama.ofalenmod.gui.*;
import nahama.ofalenmod.inventory.*;
import nahama.ofalenmod.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OfalenModGuiHandler implements IGuiHandler {
	private static String prefix = "container.ofalen.";

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
		case 0:
		case 1:
			break;
		case 3:
			return new ContainerItemTeleporter(player);
		case 5:
			return new ContainerItemFilter(player);
		case 6:
			return new ContainerItemFilterInstaller(player);
		}
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntitySmeltingMachine)
			return new ContainerSmeltingMachine(player, (TileEntitySmeltingMachine) tileEntity);
		if (tileEntity instanceof TileEntityRepairingMachine)
			return new ContainerRepairingMachine(player, (TileEntityRepairingMachine) tileEntity);
		if (tileEntity instanceof TileEntityConvertingMachine)
			return new ContainerConvertingMachine(player, (TileEntityConvertingMachine) tileEntity);
		if (tileEntity instanceof TileEntityFusingMachine)
			return new ContainerFusingMachine(player, (TileEntityFusingMachine) tileEntity);
		if (tileEntity instanceof TileEntityTeleportingMarker)
			return new ContainerTeleportingMarker(player, (TileEntityTeleportingMarker) tileEntity);
		if (tileEntity instanceof TileEntityPresentBox)
			return new ContainerPresentBox(player, (TileEntityPresentBox) tileEntity);
		if (tileEntity instanceof TileEntityDetailedSetter)
			return new ContainerDetailedSetter(player, (TileEntityDetailedSetter) tileEntity);
		if (id == 1) {
			if (tileEntity instanceof TileEntityPlacer)
				return new ContainerPlacer(player, (TileEntityPlacer) tileEntity);
			if (tileEntity instanceof TileEntityCollector)
				return new ContainerBlockCollector(player, (TileEntityCollector) tileEntity);
			if (tileEntity instanceof TileEntityWorldEditorBase)
				return new ContainerWorldEditorBase(player, (TileEntityWorldEditorBase) tileEntity);
		} else if (id == 0) {
			if (tileEntity instanceof TileEntityWorldEditorBase)
				return new ContainerSettingWorldEditorBase(player, (TileEntityWorldEditorBase) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
		case 0:
		case 1:
			break;
		case 3:
			return new GuiItemTeleporter(player);
		case 5:
			return new GuiContainerBasic(new ContainerItemFilter(player), prefix + "filterItem");
		case 6:
			return new GuiItemFilterInstaller(player);
		}
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntitySmeltingMachine)
			return new GuiSmeltingMachine(player, (TileEntitySmeltingMachine) tileEntity);
		if (tileEntity instanceof TileEntityRepairingMachine)
			return new GuiRepairingMachine(player, (TileEntityRepairingMachine) tileEntity);
		if (tileEntity instanceof TileEntityConvertingMachine)
			return new GuiConvertingMachine(player, (TileEntityConvertingMachine) tileEntity);
		if (tileEntity instanceof TileEntityFusingMachine)
			return new GuiFusingMachine(player, (TileEntityFusingMachine) tileEntity);
		if (tileEntity instanceof TileEntityTeleportingMarker)
			return new GuiTeleportingMarker(player, (TileEntityTeleportingMarker) tileEntity);
		if (tileEntity instanceof TileEntityPresentBox)
			return new GuiPresentBox(player, (TileEntityPresentBox) tileEntity);
		if (tileEntity instanceof TileEntityDetailedSetter)
			return new GuiDetailedSetter(player, (TileEntityDetailedSetter) tileEntity);
		if (id == 1) {
			if (tileEntity instanceof TileEntityPlacer)
				return new GuiWorldEditorBase.GuiPlacer(player, (TileEntityPlacer) tileEntity);
			if (tileEntity instanceof TileEntityCollector)
				return new GuiWorldEditorBase.GuiCollector(player, (TileEntityCollector) tileEntity);
			if (tileEntity instanceof TileEntityWorldEditorBase)
				return new GuiWorldEditorBase(player, (TileEntityWorldEditorBase) tileEntity);
		} else if (id == 0) {
			if (tileEntity instanceof TileEntityWorldEditorBase)
				return new GuiSettingWorldEditorBase(player, (TileEntityWorldEditorBase) tileEntity);
		}
		return null;
	}
}
