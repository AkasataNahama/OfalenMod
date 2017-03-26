package nahama.ofalenmod.gui;

import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.inventory.ContainerItemTeleporter;
import nahama.ofalenmod.network.MTeleporterMeta;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiItemTeleporter extends GuiContainer {
	private ItemStack currentItem;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("ofalenmod:textures/gui/container/teleporter.png");

	public GuiItemTeleporter(EntityPlayer player) {
		super(new ContainerItemTeleporter(player));
		currentItem = player.getHeldItem();
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiTeleportingMarker.Button(-1, guiLeft + 62, guiTop + 17, true));
		buttonList.add(new GuiTeleportingMarker.Button(-2, guiLeft + 80, guiTop + 17, true));
		buttonList.add(new GuiTeleportingMarker.Button(-3, guiLeft + 98, guiTop + 17, true));
		buttonList.add(new GuiTeleportingMarker.Button(-4, guiLeft + 62, guiTop + 54, false));
		buttonList.add(new GuiTeleportingMarker.Button(-5, guiLeft + 80, guiTop + 54, false));
		buttonList.add(new GuiTeleportingMarker.Button(-6, guiLeft + 98, guiTop + 54, false));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		int diff = 0;
		switch (button.id) {
		case -1:
			diff = 100;
			break;
		case -2:
			diff = 10;
			break;
		case -3:
			diff = 1;
			break;
		case -4:
			diff = -100;
			break;
		case -5:
			diff = -10;
			break;
		case -6:
			diff = -1;
		}
		int channel = currentItem.getItemDamage() + diff;
		if (channel < 0) {
			channel = 0;
		} else if (channel > 999) {
			channel = 999;
		}
		currentItem.setItemDamage(channel);
		OfalenModPacketCore.WRAPPER.sendToServer(new MTeleporterMeta((short) channel));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = StatCollector.translateToLocal("container.ofalen.teleporter");
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
		currentItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
		int offY = currentItem.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID) ? 0 : 26;
		String s = OfalenUtil.getTripleFiguresNum(currentItem.getItemDamage());
		for (int i = 0; i < 3; i++) {
			char c = s.charAt(i);
			// チャンネルを表示する。
			this.drawTexturedModalRect(k + 62 + (i * 18), l + 27, 16 * Integer.parseInt(c + ""), 166 + offY, 16, 26);
		}
	}

	public static class Button extends GuiButton {
		private boolean isPlus;

		public Button(int id, int x, int y, boolean isPlus) {
			super(id, x, y, 16, 9, "");
			this.isPlus = isPlus;
		}

		@Override
		public void drawButton(Minecraft minecraft, int cursorX, int cursorY) {
			if (!visible)
				return;
			minecraft.getTextureManager().bindTexture(GUITEXTURE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			// カーソルがあっているか
			field_146123_n = cursorX >= xPosition && cursorY >= yPosition && cursorX < xPosition + width && cursorY < yPosition + height;
			int offX = isPlus ? 0 : width;
			int offY = field_146123_n ? 0 : height;
			this.drawTexturedModalRect(xPosition, yPosition, 176 + offX, offY, width, height);
		}
	}
}
