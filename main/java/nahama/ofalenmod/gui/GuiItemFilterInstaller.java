package nahama.ofalenmod.gui;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.inventory.ContainerItemFilterInstaller;
import nahama.ofalenmod.network.MFilterInstaller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiItemFilterInstaller extends GuiContainer {
	private ItemStack currentItem;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("ofalenmod:textures/gui/container/filter_installer.png");

	public GuiItemFilterInstaller(EntityPlayer player) {
		super(new ContainerItemFilterInstaller(player));
		currentItem = player.getHeldItem();
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new Button(-1, guiLeft + 120, guiTop + 16));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		OfalenModPacketCore.WRAPPER.sendToServer(new MFilterInstaller());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = StatCollector.translateToLocal("container.ofalen.installerFilter");
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
	}

	public static class Button extends GuiButton {
		public Button(int id, int x, int y) {
			super(id, x, y, 8, 8, "");
		}

		@Override
		public void drawButton(Minecraft minecraft, int cursorX, int cursorY) {
			if (!visible)
				return;
			minecraft.getTextureManager().bindTexture(GUITEXTURE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			// カーソルがあっているか。
			field_146123_n = cursorX >= xPosition && cursorY >= yPosition && cursorX < xPosition + width && cursorY < yPosition + height;
			int offY = field_146123_n ? 0 : height;
			this.drawTexturedModalRect(xPosition, yPosition, 176, offY, width, height);
		}
	}
}
