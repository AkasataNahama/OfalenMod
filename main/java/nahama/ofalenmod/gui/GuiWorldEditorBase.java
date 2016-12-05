package nahama.ofalenmod.gui;

import nahama.ofalenmod.inventory.ContainerBlockCollector;
import nahama.ofalenmod.inventory.ContainerPlacer;
import nahama.ofalenmod.inventory.ContainerWorldEditorBase;
import nahama.ofalenmod.tileentity.TileEntityCollector;
import nahama.ofalenmod.tileentity.TileEntityPlacer;
import nahama.ofalenmod.tileentity.TileEntityWorldEditorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiWorldEditorBase extends GuiContainerBasic {
	public GuiWorldEditorBase(Container container, String name) {
		super(container, name);
	}

	public GuiWorldEditorBase(EntityPlayer player, TileEntityWorldEditorBase tileEntity) {
		super(new ContainerWorldEditorBase(player, tileEntity), tileEntity.getInventoryName());
		texture = new ResourceLocation("ofalenmod:textures/gui/container/world_editor.png");
	}

	public static class GuiPlacer extends GuiWorldEditorBase {
		public GuiPlacer(EntityPlayer player, TileEntityPlacer tileEntity) {
			super(new ContainerPlacer(player, tileEntity), tileEntity.getInventoryName());
			texture = new ResourceLocation("ofalenmod:textures/gui/container/world_editor-1.png");
			ySize = 222;
		}
	}

	public static class GuiCollector extends GuiWorldEditorBase {
		public GuiCollector(EntityPlayer player, TileEntityCollector tileEntity) {
			super(new ContainerBlockCollector(player, tileEntity), tileEntity.getInventoryName());
			texture = new ResourceLocation("ofalenmod:textures/gui/container/world_editor-1.png");
			ySize = 222;
		}
	}
}
