package nahama.ofalenmod.render;

import nahama.ofalenmod.entity.EntityLaserBase;
import nahama.ofalenmod.model.ModelLaser;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderLaser extends Render {
	private final ResourceLocation bulletTextures;
	protected ModelBase modelBullet;

	public RenderLaser(ModelBase model, String color) {
		this.modelBullet = model;
		this.shadowSize = 0.0F;
		this.bulletTextures = new ResourceLocation("ofalenmod:textures/entity/" + color + ".png");
	}

	public void renderLaser(EntityLaserBase entity, double x, double y, double z, float par5, float par6) {
		ModelLaser model = (ModelLaser) this.modelBullet;
		this.bindEntityTexture(entity);
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(2.0F, 2.0F, 2.0F, 1.0F);
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par6, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par6, -1.0F, 0.0F, 0.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	protected ResourceLocation getTexture(EntityLaserBase par1EntityArrow) {
		return bulletTextures;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getTexture((EntityLaserBase) entity);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float par5, float par6) {
		this.renderLaser((EntityLaserBase) entity, x, y, z, par5, par6);
	}
}
