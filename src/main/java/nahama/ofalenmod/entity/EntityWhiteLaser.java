package nahama.ofalenmod.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityWhiteLaser extends EntityLaserBase {
	/** 残りの貫通力。 */
	private byte power = 5;
	/** ダメージを与えたEntityのリスト。 */
	private List<Integer> listDamagedEntity = new ArrayList<Integer>();

	public EntityWhiteLaser(World world, EntityLivingBase entity, int dif) {
		super(world);
		thrower = entity;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, entity.rotationYaw + (dif * 5), entity.rotationPitch);
		/*
		 * posX -= (double)(MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		 * posZ -= (double)(MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		 */
		posY -= 0.10000000149011612D;
		this.setPosition(posX, posY, posZ);
		startX = posX;
		startY = posY;
		startZ = posZ;
		yOffset = 0.0F;
		motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
		motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
		motionY = (-MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI));
		this.setThrowableHeading(motionX, motionY, motionZ, this.getSpeed());
	}

	/** 衝突時の処理。 */
	@Override
	protected void onImpact(MovingObjectPosition position) {
		// 射出者以外のEntityで、まだダメージを与えていないものに当たったら、
		if (position.entityHit != null && position.entityHit != this.getThrower() && !listDamagedEntity.contains(position.entityHit.getEntityId())) {
			// 減衰させ、ダメージを与える。
			power--;
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), 30.0F);
			listDamagedEntity.add(position.entityHit.getEntityId());
		} else {
			Block block = worldObj.getBlock(position.blockX, position.blockY, position.blockZ);
			if (!block.isOpaqueCube() || !block.isNormalCube() || !block.renderAsNormalBlock())
				return;
		}
		// サーバー側なら、爆発させる。
		if (!worldObj.isRemote)
			worldObj.createExplosion(this.getThrower(), posX, posY, posZ, 3, false);
		// 一定回数以上Entityに当たっていたら消す。
		if (power < 1)
			this.setDead();
	}
}
