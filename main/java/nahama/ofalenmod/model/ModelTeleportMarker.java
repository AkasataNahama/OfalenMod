package nahama.ofalenmod.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTeleportMarker extends ModelBase {

	ModelRenderer base;
	ModelRenderer bld;
	ModelRenderer brd;
	ModelRenderer bru;
	ModelRenderer fru;
	ModelRenderer fld;
	ModelRenderer blu;
	ModelRenderer flu;
	ModelRenderer frd;

	public ModelTeleportMarker() {
		textureWidth = 64;
		textureHeight = 32;

		base = new ModelRenderer(this, 0, 0);
		base.addBox(0F, 0F, 0F, 8, 8, 8);
		base.setRotationPoint(-4F, -4F, -4F);
		base.setTextureSize(64, 32);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		bld = new ModelRenderer(this, 32, 0);
		bld.addBox(0F, 0F, 0F, 4, 4, 4);
		bld.setRotationPoint(4F, 4F, 4F);
		bld.setTextureSize(64, 32);
		bld.mirror = true;
		setRotation(bld, 0F, 0F, 0F);
		brd = new ModelRenderer(this, 32, 0);
		brd.addBox(0F, 0F, 0F, 4, 4, 4);
		brd.setRotationPoint(-8F, 4F, 4F);
		brd.setTextureSize(64, 32);
		brd.mirror = true;
		setRotation(brd, 0F, 0F, 0F);
		bru = new ModelRenderer(this, 32, 0);
		bru.addBox(0F, 0F, 0F, 4, 4, 4);
		bru.setRotationPoint(-8F, -8F, 4F);
		bru.setTextureSize(64, 32);
		bru.mirror = true;
		setRotation(bru, 0F, 0F, 0F);
		fru = new ModelRenderer(this, 32, 0);
		fru.addBox(0F, 0F, 0F, 4, 4, 4);
		fru.setRotationPoint(-8F, -8F, -8F);
		fru.setTextureSize(64, 32);
		fru.mirror = true;
		setRotation(fru, 0F, 0F, 0F);
		fld = new ModelRenderer(this, 32, 0);
		fld.addBox(0F, 0F, 0F, 4, 4, 4);
		fld.setRotationPoint(4F, 4F, -8F);
		fld.setTextureSize(64, 32);
		fld.mirror = true;
		setRotation(fld, 0F, 0F, 0F);
		blu = new ModelRenderer(this, 32, 0);
		blu.addBox(0F, 0F, 0F, 4, 4, 4);
		blu.setRotationPoint(4F, -8F, 4F);
		blu.setTextureSize(64, 32);
		blu.mirror = true;
		setRotation(blu, 0F, 0F, 0F);
		flu = new ModelRenderer(this, 32, 0);
		flu.addBox(0F, 0F, 0F, 4, 4, 4);
		flu.setRotationPoint(4F, -8F, -8F);
		flu.setTextureSize(64, 32);
		flu.mirror = true;
		setRotation(flu, 0F, 0F, 0F);
		frd = new ModelRenderer(this, 32, 0);
		frd.addBox(0F, 0F, 0F, 4, 4, 4);
		frd.setRotationPoint(-8F, 4F, -8F);
		frd.setTextureSize(64, 32);
		frd.mirror = true;
		setRotation(frd, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		base.render(f5);
		bld.render(f5);
		brd.render(f5);
		bru.render(f5);
		fru.render(f5);
		fld.render(f5);
		blu.render(f5);
		flu.render(f5);
		frd.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
