package nahama.ofalenmod.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityLaserBlue extends EntityLaserBase {
	private byte power = 5;

	public EntityLaserBlue(World world, EntityLivingBase entity) {
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
