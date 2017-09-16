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
	private TileEntityWorldEditorBase tileEntity;

	protected GuiWorldEditorBase(Container container, TileEntityWorldEditorBase tileEntity) {
		super(container, tileEntity.getInventoryName());
		this.tileEntity = tileEntity;
	}

	public GuiWorldEditorBase(EntityPlayer player, TileEntityWorldEditorBase tileEntity) {
		super(new ContainerWorldEditorBase(player, tileEntity), tileEntity.getInventoryName());
		texture = new ResourceLocation("ofalenmod:textures/gui/container/world_editor.png");
		this.tileEntity = tileEntity;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		int originX = (width - xSize) / 2;
		int originY = (height - ySize) / 2;
		int i = tileEntity.getRemainingEnergyScaled(162);
		this.drawTexturedModalRect(originX + 7 + 162 - i, originY + 15, 0, ySize, i, 1);
	}

	public static class GuiPlacer extends GuiWorldEditorBase {
		public GuiPlacer(EntityPlayer player, TileEntityPlacer tileEntity) {
			super(new ContainerPlacer(player, tileEntity), tileEntity);
			texture = new ResourceLocation("ofalenmod:textures/gui/container/world_editor-1.png");
			ySize = 222;
		}
	}

	public static class GuiCollector extends GuiWorldEditorBase {
		public GuiCollector(EntityPlayer player, TileEntityCollector tileEntity) {
			super(new ContainerBlockCollector(player, tileEntity), tileEntity);
			texture = new ResourceLocation("ofalenmod:textures/gui/container/world_editor-1.png");
			ySize = 222;
		}
	}
}
