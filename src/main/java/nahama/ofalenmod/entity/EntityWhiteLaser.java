package nahama.ofalenmod.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityWhiteLaser extends EntityLaserBase {
	private byte power = 5;

	public EntityWhiteLaser(World world, EntityLivingBase entity, int dif) {
		super(world);
		thrower = entity;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, entity.rotationYaw + (dif * 5), entity.rotationPitch);
		/*
		 * posX -= (double)(MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		 * posZ -= (double)(MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		。 */
		posY -= 0.10000000149011612D;
		this.setPosition(posX, posY, posZ);
		startX = posX;
		startY = posY;
		startZ = posZ;
		yOffset = 0.0F;
		motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
		motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
		motionY = (-MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI));
		this.setThrowableHeading(motionX, motionY, motionZ, this.getSpeed(), 1.0F);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (position.entityHit != null && position.entityHit != this.getThrower()) {
			power--;
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), 30.0F);
		}
		Block block = worldObj.getBlock(position.blockX, position.blockY, position.blockZ);
		if (!block.isOpaqueCube() || !block.isNormalCube() || !block.renderAsNormalBlock()) {
			return;
		} else if (block == Blocks.tallgrass) {
			return;
		}
		worldObj.createExplosion(this.getThrower(), posX, posY, posZ, 3, false);
		if (power <= 0) {
			this.setDead();
		}
	}
}
