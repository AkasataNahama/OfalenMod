package nahama.ofalenmod.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityLaserGreen extends EntityLaserBase {
	public EntityLaserGreen(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (position.entityHit != null && position.entityHit != this.getThrower()) {
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), 10.0F);
			this.setDead();
		} else {
			Block block = worldObj.getBlock(position.blockX, position.blockY, position.blockZ);
			if (!block.isOpaqueCube() || !block.isNormalCube() || !block.renderAsNormalBlock())
				return;
		}
		if (!worldObj.isRemote)
			worldObj.createExplosion(this.getThrower(), posX, posY, posZ, 3, false);
	}
}
