package nahama.ofalenmod.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityGreenLaser extends EntityLaser {

	public EntityGreenLaser(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (position.entityHit != null) {
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), 10.0F);
		}

		Block block = worldObj.getBlock(position.blockX, position.blockY, position.blockZ);
		if (!(block.isOpaqueCube() || block.isNormalCube() || block.renderAsNormalBlock())) {
			return;
		} else if (block == Blocks.tallgrass) {
			return;
		}
		this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3, false);
		this.setDead();
	}

}
