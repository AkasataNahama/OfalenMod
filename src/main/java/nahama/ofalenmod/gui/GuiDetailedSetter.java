package nahama.ofalenmod.gui;

import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.handler.OfalenDetailedSettingHandler;
import nahama.ofalenmod.inventory.ContainerDetailedSetter;
import nahama.ofalenmod.network.MApplyDetailedSetter;
import nahama.ofalenmod.tileentity.TileEntityDetailedSetter;
import nahama.ofalenmod.util.IItemOfalenSettable;
import nahama.ofalenmod.util.OfalenSetting;
import nahama.ofalenmod.util.OfalenTimer;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiDetailedSetter extends GuiContainer {
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("ofalenmod:textures/gui/container/detailed_setter.png");
	private TileEntityDetailedSetter tileEntity;

	public GuiDetailedSetter(EntityPlayer player, TileEntityDetailedSetter tileEntity) {
		super(new ContainerDetailedSetter(player, tileEntity));
		this.tileEntity = tileEntity;
		ySize = 222;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		OfalenTimer.start("GuiDetailedSetter.drawGuiContainerForegroundLayer");
		StringBuilder s = new StringBuilder(StatCollector.translateToLocal(tileEntity.getInventoryName()));
		fontRendererObj.drawString(s.toString(), xSize / 2 - fontRendererObj.getStringWidth(s.toString()) / 2, 6, 0x404040);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 0x404040);
		// 右側に情報を表示する。
		ItemStack itemStack = tileEntity.getStackInSlot(0);
		if (itemStack == null || !(itemStack.getItem() instanceof IItemOfalenSettable))
			return;
		int i = 0;
		// 一番上のスロットの右にはアイテム名を表示する。
		OfalenSetting setting = ((IItemOfalenSettable) itemStack.getItem()).getSetting();
		fontRendererObj.drawString(setting.getLocalizedSettingName(), 29, 21 + (i * 18), 0x404040);
		// 設定のパス。
		s = new StringBuilder();
		for (i = 1; i < tileEntity.getFirstInvalidSlot(); i++) {
			setting = setting.getChildSetting(tileEntity.getStackInSlot(i));
			fontRendererObj.drawString(setting.getLocalizedSettingName(), 29, 21 + (i * 18), 0x404040);
			s.append(setting.getSettingName()).append("/");
		}
		if (tileEntity.isApplicableState()) {
			// 適用可能な状態なら、現在の値と適用後の値を表示。
			Object currentValue = OfalenDetailedSettingHandler.getCurrentValueFromNBT(OfalenDetailedSettingHandler.getSettingTag(itemStack), s.toString(), setting);
			fontRendererObj.drawString(currentValue.toString() + " -> " + setting.getSettingValue(currentValue, tileEntity.getStackInSlot(i)), 29, 21 + (i * 18), 0x404040);
		} else {
			// 指定が完全でないなら、候補アイテムを表示。
			int j = 0;
			for (ItemStack stack : setting.getSelectableItems()) {
				int posX = 26 + (j * 18);
				int posY = 18 + (i * 18);
				GL11.glEnable(GL11.GL_LIGHTING);
				itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), stack, posX, posY);
				itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), stack, posX, posY, null);
				mc.getTextureManager().bindTexture(GUITEXTURE);
				this.drawTexturedModalRect(posX - 1, posY - 1, 184, 0, 18, 18);
				j++;
			}
		}
		OfalenTimer.watchAndLog("GuiDetailedSetter.drawGuiContainerForegroundLayer", 0.3);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		OfalenTimer.start("GuiDetailedSetter.drawGuiContainerBackgroundLayer");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUITEXTURE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		// 入れるべきではないスロットの背景を斜線入りにする。
		for (int i = tileEntity.getFirstInvalidSlot() + 1; i < tileEntity.getSizeInventory(); i++) {
			this.drawTexturedModalRect(k + 7, l + 17 + (i * 18), 184, 18, 18, 18);
		}
		OfalenTimer.watchAndLog("GuiDetailedSetter.drawGuiContainerBackgroundLayer", 0.1);
	}

	@Override
	public void initGui() {
		super.initGui();
		OfalenUtil.add(buttonList, new ButtonApplying(-1, guiLeft + 162, guiTop + 6));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (tileEntity.isApplicableState()) {
			OfalenModPacketCore.WRAPPER.sendToServer(new MApplyDetailedSetter(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
			tileEntity.apply();
		}
	}

	public class ButtonApplying extends GuiButton {
		public ButtonApplying(int id, int x, int y) {
			super(id, x, y, 8, 8, "");
		}

		@Override
		public void drawButton(Minecraft minecraft, int cursorX, int cursorY) {
			if (!visible)
				return;
			minecraft.getTextureManager().bindTexture(GUITEXTURE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int offY = 0;
			// 押せる状態なら一つ下へ。
			if (tileEntity.isApplicableState()) {
				offY += height;
				// カーソルが乗っているならもう一つ下へ。
				if (field_146123_n = cursorX >= xPosition && cursorY >= yPosition && cursorX < xPosition + width && cursorY < yPosition + height)
					offY += height;
			}
			this.drawTexturedModalRect(xPosition, yPosition, 176, offY, width, height);
		}
	}
}
