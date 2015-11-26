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
		if (power == 0)
			power = 5;
	}

	/** ブロックかエンティティに当たった時の処理 */
	@Override
	protected void onImpact(MovingObjectPosition position) {
		Random random = new Random();
		if (!this.worldObj.isRemote) {
			// 所持しているパワーに応じて周りに火をつける
			for (int i = 0; i < power * 2; i++) {
				int ix = position.blockX + random.nextInt(power) - random.nextInt(power);
				int iy = position.blockY + random.nextInt(2) - random.nextInt(2);
				int iz = position.blockZ + random.nextInt(power) - random.nextInt(power);

				if (i == 0) {
					ix = position.blockX;
					iy = position.blockY;
					iz = position.blockZ;
				}

				// 着火処理。ItemFireBall参照
				switch (position.sideHit) {
				case 0:
					--iy;
					break;
				case 1:
					++iy;
					break;
				case 2:
					--iz;
					break;
				case 3:
					++iz;
					break;
				case 4:
					--ix;
					break;
				case 5:
					++ix;
				}

				if (this.worldObj.isAirBlock(ix, iy, iz)) {
					this.worldObj.setBlock(ix, iy, iz, Blocks.fire);
				}
				// 破壊力のない爆発を起こす
				this.worldObj.newExplosion(this, ix, iy, iz, 0.0F, true, true);
			}
		}
		// 消滅させる
		this.setDead();
	}

}
