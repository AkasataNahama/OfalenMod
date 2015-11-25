package nahama.ofalenmod.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityRedLaser extends EntityLaser {

	public EntityRedLaser(World world, EntityLivingBase entity, int dif) {
		super(world);
		this.thrower = entity;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ, entity.rotationYaw + (dif * 5), entity.rotationPitch);
		/*
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		*/
		this.posY -= 0.10000000149011612D;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.startX = this.posX;
		this.startY = this.posY;
		this.startZ = this.posZ;
		this.yOffset = 0.0F;
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.getSpeed(), 1.0F);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (position.entityHit != null) {
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), 10.0F);
			this.setDead();
		}
	}

}
