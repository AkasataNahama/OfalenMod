package nahama.ofalenmod.gui;

import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.inventory.ContainerSettingWorldEditorBase;
import nahama.ofalenmod.network.MWorldEditorSetting;
import nahama.ofalenmod.tileentity.TileEntityWorldEditorBase;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiSettingWorldEditorBase extends GuiContainer {
	protected static final ResourceLocation TEXTURE = new ResourceLocation("ofalenmod:textures/gui/container/world_editor_setting.png");
	protected TileEntityWorldEditorBase tileEntity;

	public GuiSettingWorldEditorBase(EntityPlayer player, TileEntityWorldEditorBase tileEntity) {
		super(new ContainerSettingWorldEditorBase(player, tileEntity));
		this.tileEntity = tileEntity;
		ySize = 222 + 18;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = StatCollector.translateToLocal(tileEntity.getInventoryName());
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
		this.drawSettingEntryNames();
		this.drawSettingValues();
	}

	private void drawSettingValue(String s, int y) {
		fontRendererObj.drawString(s, 146 - fontRendererObj.getStringWidth(s), y, 0x404040);
	}

	private void drawSettingEntryNames() {
		for (int i = 0; i < tileEntity.getAmountSettingID(); i++) {
			fontRendererObj.drawString(StatCollector.translateToLocal(tileEntity.getSettingNameWithID(i)), 8, 16 + (i * 10), 0x404040);
		}
	}

	private void drawSettingValues() {
		for (int i = 0; i < tileEntity.getAmountSettingID(); i++) {
			if (tileEntity.getSettingTypeWithID(i) == 0) {
				this.drawSettingValue(StatCollector.translateToLocal(tileEntity.getSettingNameWithID(i) + "." + tileEntity.getWithID(i)), 16 + (i * 10));
				continue;
			}
			this.drawSettingValue(String.valueOf(tileEntity.getWithID(i)), 16 + (i * 10));
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	public void initGui() {
		super.initGui();
		for (int i = 0; i < tileEntity.getAmountSettingID(); i++) {
			if (tileEntity.getSettingTypeWithID(i) == 0) {
				this.addButtonSetting(150, 15 + (i * 10), i, 0);
				continue;
			}
			// +ボタン
			this.addButtonSetting(150, 15 + (i * 10), i, 1);
			// -ボタン。+ボタンの幅が9、余白が2で11。
			this.addButtonSetting(150 + 11, 15 + (i * 10), i, -1);
		}
	}

	private void addButtonSetting(int x, int y, int idSetting, int type) {
		OfalenUtil.add(buttonList, new ButtonWorldEditorSetting(buttonList.size(), guiLeft + x, guiTop + y, idSetting, type));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		ButtonWorldEditorSetting buttonSetting = (ButtonWorldEditorSetting) button;
		int factor = buttonSetting.type;
		if (factor == 0) {
			// type == 0はBooleanなので反転。
			factor = tileEntity.getWithID(buttonSetting.idSetting) == 1 ? -1 : 1;
		} else {
			if (OfalenUtil.isKeyPressed(Minecraft.getMinecraft().gameSettings.keyBindSneak))
				factor *= 10;
			if (OfalenUtil.isKeyPressed(Minecraft.getMinecraft().gameSettings.keyBindSprint))
				factor *= 100;
		}
		OfalenModPacketCore.WRAPPER.sendToServer(new MWorldEditorSetting(tileEntity, buttonSetting.idSetting, tileEntity.getWithID(buttonSetting.idSetting) + factor));
	}

	protected static class ButtonWorldEditorSetting extends GuiButton {
		public byte idSetting;
		/** 設定に対応したボタンの種類。0ならBooleanの設定で反転、1か-1なら数値の設定で+か-。 */
		public byte type;

		public ButtonWorldEditorSetting(int id, int x, int y, int idSetting, int type) {
			super(id, x, y, 9, 9, "");
			this.type = (byte) type;
			this.idSetting = (byte) idSetting;
		}

		@Override
		public void drawButton(Minecraft minecraft, int cursorX, int cursorY) {
			if (!visible)
				return;
			minecraft.getTextureManager().bindTexture(TEXTURE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			// +ボタン、-ボタンなら、テクスチャをより右のものにする。
			int offX = (type == -1) ? 2 : type;
			// カーソルがボタン上にあるなら、テクスチャを下のものにする。
			field_146123_n = cursorX >= xPosition && cursorY >= yPosition && cursorX < xPosition + width && cursorY < yPosition + height;
			int offY = field_146123_n ? height : 0;
			this.drawTexturedModalRect(xPosition, yPosition, 176 + width * offX, offY, width, height);
		}
	}
}
