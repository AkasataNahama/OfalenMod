package nahama.ofalenmod.gui;

import nahama.ofalenmod.inventory.ContainerItemList;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiItemList extends GuiContainer {

	private static final ResourceLocation GUITEXTURE = new ResourceLocation("ofalenmod:textures/gui/container/list.png");

	public GuiItemList(EntityPlayer player) {
		super(new ContainerItemList(player));
		ySize = 168;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = StatCollector.translateToLocal("container.OfalenMod.ItemList");
		fontRendererObj.drawString(s, 8, 6, 4210752);
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
