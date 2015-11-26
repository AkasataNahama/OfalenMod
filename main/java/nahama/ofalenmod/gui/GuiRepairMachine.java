package nahama.ofalenmod.gui;

import org.lwjgl.opengl.GL11;

import nahama.ofalenmod.inventory.ContainerRepairMachine;
import nahama.ofalenmod.tileentity.TileEntityRepairMachine;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiRepairMachine extends GuiContainer {

	private TileEntityRepairMachine tileEntity;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("textures/gui/container/furnace.png");

	public GuiRepairMachine(EntityPlayer player, TileEntityRepairMachine tileEnttiy) {
		super(new ContainerRepairMachine(player, tileEnttiy));
		this.tileEntity = tileEnttiy;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = tileEntity.hasCustomInventoryName() ? tileEntity.getInventoryName() : StatCollector.translateToLocal(tileEntity.getInventoryName());
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUITEXTURE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		int i1;
		if (tileEntity.isBurning()) {
			// 燃焼中なら燃料の残り時間を描く。
			i1 = tileEntity.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		}
		// 修繕作業の矢印を描く。
		i1 = tileEntity.getWorkProgressScaled(24);
		this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}

}
