package nahama.ofalenmod.entity;

import nahama.ofalenmod.core.OfalenModConfigCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityExplosionBall extends EntityThrowable {

	private int power;

	public EntityExplosionBall(World world, EntityLivingBase entity, int grade) {
		super(world, entity);
		//Configで設定された値のgrade*2倍を爆発力に代入。
		power = OfalenModConfigCore.sizeExplosion * (grade * 2);
		if (power == 0) power = OfalenModConfigCore.sizeExplosion;
	}

	/**ブロックかエンティティに当たった時の処理*/
	@Override
	protected void onImpact(MovingObjectPosition position) {
		if (!worldObj.isRemote) {
			//所持している爆発力に応じて爆発を起こす
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)power, true);
		}

		power --;

		//爆発力がなくなったら消滅。
		if (power == 0) {
			this.setDead();
		}
	}

}