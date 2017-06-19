package nahama.ofalenmod.gui;

import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.handler.OfalenFlightHandlerClient;
import nahama.ofalenmod.inventory.ContainerItemFloater;
import nahama.ofalenmod.network.MFloaterMode;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiItemFloater extends GuiContainer {
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("ofalenmod:textures/gui/container/floater.png");

	public GuiItemFloater(EntityPlayer player) {
		super(new ContainerItemFloater(player));
	}

	@Override
	public void initGui() {
		super.initGui();
		for (int i = 0; i < 6; i++) {
			OfalenUtil.add(buttonList, new GuiItemFloater.Button(-1 - i, guiLeft + 100 + (12 * i), guiTop + 25, i));
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		byte mode = (byte) Math.abs(button.id + 1);
		Minecraft.getMinecraft().thePlayer.getHeldItem().getTagCompound().setByte(OfalenNBTUtil.MODE, mode);
		OfalenFlightHandlerClient.checkPlayer();
		OfalenModPacketCore.WRAPPER.sendToServer(new MFloaterMode(mode, true));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = StatCollector.translateToLocal("container.ofalen.floater");
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		for (int i = 0; i < 6; i++) {
			s = String.valueOf(i);
			fontRendererObj.drawString(s, 100 + (12 * i) + 4 - (fontRendererObj.getStringWidth(s) / 2), 17, 4210752);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUITEXTURE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	public static class Button extends GuiButton {
		private byte mode;

		public Button(int id, int x, int y, int mode) {
			super(id, x, y, 8, 8, "");
			this.mode = (byte) mode;
		}

		@Override
		public void drawButton(Minecraft minecraft, int cursorX, int cursorY) {
			if (!visible)
				return;
			minecraft.getTextureManager().bindTexture(GUITEXTURE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			// カーソルがあっているか
			field_146123_n = cursorX >= xPosition && cursorY >= yPosition && cursorX < xPosition + width && cursorY < yPosition + height;
			int offY = (field_146123_n || minecraft.thePlayer.getHeldItem().getTagCompound().getByte(OfalenNBTUtil.MODE) == mode) ? 0 : height;
			this.drawTexturedModalRect(xPosition, yPosition, 176, offY, width, height);
		}
	}
}
