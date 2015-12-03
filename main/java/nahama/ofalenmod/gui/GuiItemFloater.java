package nahama.ofalenmod.gui;

import org.lwjgl.opengl.GL11;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.handler.OfalenFlightHandlerClient;
import nahama.ofalenmod.inventory.ContainerItemFloater;
import nahama.ofalenmod.network.MFloaterMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiItemFloater extends GuiContainer {

	private ItemStack currentItem;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("ofalenmod:textures/gui/container/floater.png");

	public GuiItemFloater(EntityPlayer player) {
		super(new ContainerItemFloater(player));
		currentItem = player.getHeldItem();
	}

	@Override
	public void initGui() {
		super.initGui();
		for (int i = 0; i < 5; i++) {
			buttonList.add(new GuiItemFloater.Button(-1 - i, guiLeft + 112 + (12 * i), guiTop + 15, i));
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		byte mode = (byte) Math.abs(button.id + 1);
		currentItem.getTagCompound().setByte("Mode", mode);
		OfalenFlightHandlerClient.allowPlayerToFloat(mode);
		OfalenModCore.wrapper.sendToServer(new MFloaterMode(mode, true));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = StatCollector.translateToLocal("container.OfalenMod.ItemFloater");
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		for (int i = 0; i < 5; i++) {
			s = String.valueOf(i);
			fontRendererObj.drawString(s, 112 + (12 * i) + 4 - (fontRendererObj.getStringWidth(s) / 2), 23, 4210752);
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
			int offY = (field_146123_n || minecraft.thePlayer.getHeldItem().getTagCompound().getByte("Mode") == mode) ? 0 : height;
			this.drawTexturedModalRect(xPosition, yPosition, 176, offY, width, height);
		}

	}

}
