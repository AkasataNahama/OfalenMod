package nahama.ofalenmod.entity;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityFlameBall extends EntityThrowable {

	private int power;

	public EntityFlameBall(World world, EntityLivingBase entity, int grade) {
		super(world, entity);
		power = 5 * (grade * 2);
		if (power == 0) power = 5;
	}

	/**ブロックかエンティティに当たった時の処理*/
	@Override
	protected void onImpact(MovingObjectPosition position) {
		Random random = new Random();
		if (!this.worldObj.isRemote) {
			//所持しているパワーに応じて周りに火をつける
			for (int ix = 0; ix < power * 2; ix ++) {
				int i = position.blockX + random.nextInt(power) - random.nextInt(power);
				int j = position.blockY + random.nextInt(2) - random.nextInt(2);
				int k = position.blockZ + random.nextInt(power) - random.nextInt(power);

				if (ix == 0) {
					i = position.blockX;
					j = position.blockY;
					k = position.blockZ;
				}

				//着火処理。ItemFireBall参照
				switch (position.sideHit) {
				case 0:
					--j;
					break;
				case 1:
					++j;
					break;
				case 2:
					--k;
					break;
				case 3:
					++k;
					break;
				case 4:
					--i;
					break;
				case 5:
					++i;
				}

				if (this.worldObj.isAirBlock(i, j, k)) {
					this.worldObj.setBlock(i, j, k, Blocks.fire);
				}
				//破壊力のない爆発を起こす
				this.worldObj.newExplosion(this, i, j, k, 0.0F, true, true);
			}
		}
		//消滅させる
		this.setDead();

	}

}