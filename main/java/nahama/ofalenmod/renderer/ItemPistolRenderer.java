package nahama.ofalenmod.renderer;

import nahama.ofalenmod.model.ModelLaserPistol;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class ItemPistolRenderer implements IItemRenderer{

	private static final ResourceLocation resource = new ResourceLocation("ofalenmod:textures/entity/white.png");
	private ModelLaserPistol model = new ModelLaserPistol();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return canRendering(item, type);
	}

	private boolean canRendering(ItemStack item, ItemRenderType type) {
		switch(type) {
		case ENTITY:
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		switch (helper) {
		case ENTITY_BOBBING:
		case ENTITY_ROTATION:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (canRendering(item, type)) {
			GL11.glPushMatrix();
			/*
			 * 描画する種類によって回転, 平行移動を行う.
			 */
			switch(type) {
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
				GL11.glRotatef(-80F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(10F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(92F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(0.07F, -0.5F, 0.7F);
				break;
			default:
				break;
			}
			/*
			 * リソースをTextureMangerにbindし, modelのrenderを呼んで描画する.
			 */
			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resource);
			this.model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GL11.glPopMatrix();
		}
	}

}
