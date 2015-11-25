package nahama.ofalenmod.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLaserPistol extends ModelBase {

	ModelRenderer base;
	ModelRenderer grip;
	ModelRenderer lens;

	public ModelLaserPistol() {
		textureWidth = 1;
		textureHeight = 1;

		base = new ModelRenderer(this, 0, 0);
		base.addBox(-1.5F, -1.0F, -4.5F, 3, 3, 9);
		base.setRotationPoint(0F, 0F, 0F);
		base.setTextureSize(1, 1);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);

		grip = new ModelRenderer(this, 0, 0);
		grip.addBox(-1.0F, -3.0F, -1.0F, 2, 4, 2);
		grip.setRotationPoint(0F, -1F, -2F);
		grip.setTextureSize(1, 1);
		grip.mirror = true;
		setRotation(grip, (20F / 180F * (float)Math.PI), 0F, 0F);

		lens = new ModelRenderer(this, 0, 0);
		lens.addBox(-0.5F, -0.0F, 3.75F, 1, 1, 1);
		lens.setRotationPoint(0F, 0F, 0F);
		lens.setTextureSize(1, 1);
		lens.mirror = true;
		setRotation(lens, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		base.render(f5);
		grip.render(f5);
		lens.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
