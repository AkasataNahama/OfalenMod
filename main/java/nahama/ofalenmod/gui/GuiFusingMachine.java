package nahama.ofalenmod.gui;

import nahama.ofalenmod.inventory.ContainerFusingMachine;
import nahama.ofalenmod.tileentity.TileEntityFusingMachine;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiFusingMachine extends GuiContainer {

	private TileEntityFusingMachine tileEntity;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("ofalenmod:textures/gui/container/fusing_machine.png");

	public GuiFusingMachine(EntityPlayer player, TileEntityFusingMachine tileEntity) {
		super(new ContainerFusingMachine(player, tileEntity));
		this.tileEntity = tileEntity;
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
			this.drawTexturedModalRect(k + 56 + 27, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		}
		// 融合作業の矢印を描く。
		i1 = tileEntity.getWorkProgressScaled(24);
		this.drawTexturedModalRect(k + 79, l + 34 - 17, 176, 14, i1 + 1, 16);
	}

}