package nahama.ofalenmod.gui;

import org.lwjgl.opengl.GL11;

import nahama.ofalenmod.inventory.ContainerPresentBox;
import nahama.ofalenmod.tileentity.TileEntityPresentBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiPresentBox extends GuiContainer {

	private TileEntityPresentBox tileEntity;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

	public GuiPresentBox(EntityPlayer player, TileEntityPresentBox tileEnttiy) {
		super(new ContainerPresentBox(player, tileEnttiy));
		this.tileEntity = tileEnttiy;
		ySize = 222;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRendererObj.drawString(StatCollector.translateToLocal(tileEntity.getInventoryName()), 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUITEXTURE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

}
