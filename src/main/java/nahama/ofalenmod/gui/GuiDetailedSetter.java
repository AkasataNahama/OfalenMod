package nahama.ofalenmod.gui;

import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.inventory.ContainerDetailedSetter;
import nahama.ofalenmod.network.MApplyDetailedSetter;
import nahama.ofalenmod.setting.IItemOfalenSettable;
import nahama.ofalenmod.setting.OfalenSetting;
import nahama.ofalenmod.setting.OfalenSettingCategory;
import nahama.ofalenmod.setting.OfalenSettingContent;
import nahama.ofalenmod.tileentity.TileEntityDetailedSetter;
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

import java.util.List;

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
		// 入っているアイテムを判定し、描画する。
		ItemStack stackOrigin = tileEntity.getStackInSlot(0);
		// 対応していないアイテムなら終了。
		if (stackOrigin == null || !(stackOrigin.getItem() instanceof IItemOfalenSettable))
			return;
		// 最初のアイテムが正しいなら、設定を取得する。
		OfalenSetting setting = ((IItemOfalenSettable) stackOrigin.getItem()).getSetting();
		// 設定名（アイテム名）を表示する。
		fontRendererObj.drawString(setting.getLocalizedName(), 29, 21, 0x404040);
		// 各スロットに対し、メッセージや候補アイテムを描画する。
		for (int slotNum = 1; slotNum < tileEntity.getSizeInventory(); slotNum++) {
			ItemStack stack = tileEntity.getStackInSlot(slotNum);
			OfalenSetting lastSetting = setting;
			if (setting != null) {
				if (setting instanceof OfalenSettingCategory) {
					setting = ((OfalenSettingCategory) setting).getChildSetting(stack);
				} else {
					setting = null;
				}
			}
			// このスロットの横に表示するメッセージ。
			String message = null;
			if (setting != null) {
				// settingが取得できたなら、メッセージを取得する。
				message = setting.getLocalizedName();
			} else {
				// settingが取得できなかった時。（スロットが空か不正、もしくは終端設定の次）
				if (lastSetting != null) {
					if (stack == null) {
						// アイテムが入っていないなら、候補のアイテムを表示する。
						this.drawSelectableItems(slotNum, lastSetting.getSelectableItemList());
					} else if (lastSetting instanceof OfalenSettingContent<?>) {
						// 終端設定の次ならメッセージを取得する。
						OfalenSettingContent<?> settingContent = (OfalenSettingContent<?>) lastSetting;
						message = settingContent.getSecondMessage(stackOrigin, stack);
					}
				}
			}
			if (message != null)
				this.drawMessage(slotNum, message);
		}
		OfalenTimer.watchAndLog("GuiDetailedSetter.drawGuiContainerForegroundLayer", 0.3);
	}

	private void drawSelectableItems(int slotNum, List<ItemStack> list) {
		int i = 0;
		for (ItemStack stack : list) {
			int posX = 26 + (i * 18);
			int posY = 18 + (slotNum * 18);
			GL11.glEnable(GL11.GL_LIGHTING);
			itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), stack, posX, posY);
			GL11.glDisable(GL11.GL_LIGHTING);
			mc.getTextureManager().bindTexture(GUITEXTURE);
			this.drawTexturedModalRect(posX - 1, posY - 1, 184, 0, 18, 18);
			i++;
		}
	}

	private void drawMessage(int slotNum, String message) {
		fontRendererObj.drawString(message, 29, 21 + (slotNum * 18), 0x404040);
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
