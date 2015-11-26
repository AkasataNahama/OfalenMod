package nahama.ofalenmod.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBlueLaser extends EntityLaser {

	private int power = 5;

	public EntityBlueLaser(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (position.entityHit != null && position.entityHit != this.getThrower()) {
			power--;
			position.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), 30.0F);
		}
		if (power < 1) {
			this.setDead();
		}
	}

}
