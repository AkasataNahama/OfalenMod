package nahama.ofalenmod.entity;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityLaser extends Entity {

	protected int xTile = -1;
	protected int yTile = -1;
	protected int zTile = -1;
	protected Block inBlock;
	protected boolean inGround;
	public int throwableShake;
	protected EntityLivingBase thrower;
	protected String throwerName;
	protected int ticksInGround;
	protected int ticksInAir;
	protected double startX;
	protected double startY;
	protected double startZ;

	@Override
	protected void entityInit() {}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double range) {
		double d1 = boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return range < d1 * d1;
	}

	protected EntityLaser(World world) {
		super(world);
	}

	public EntityLaser(World world, EntityLivingBase entity) {
		super(world);
		thrower = entity;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
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
		this.setThrowableHeading(motionX, motionY, motionZ, this.getSpeed(), 1.0F);
	}

	protected float getSpeed() {
		return 1.5F;
	}

	/** IProjectileのオーバーライド。ディスペンサーなどで利用される。(?) */
	public void setThrowableHeading(double x, double y, double z, float speed, float par5) {
		float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
		x /= f2;
		y /= f2;
		z /= f2;
		// ランダム性をなくす。
		/*
		 * x += rand.nextGaussian() * 0.007499999832361937D * (double)par5;
		 * y += rand.nextGaussian() * 0.007499999832361937D * (double)par5;
		 * z += rand.nextGaussian() * 0.007499999832361937D * (double)par5;
		 */
		x *= speed;
		y *= speed;
		z *= speed;
		motionX = x;
		motionY = y;
		motionZ = z;
		float f3 = MathHelper.sqrt_double(x * x + z * z);
		prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float) (Math.atan2(y, f3) * 180.0D / Math.PI);
		ticksInGround = 0;
	}

	/** 速度の処理 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(x * x + z * z);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(y, f) * 180.0D / Math.PI);
		}
	}

	/** 更新時の処理 */
	@Override
	public void onUpdate() {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		super.onUpdate();

		if (throwableShake > 0) {
			--throwableShake;
		}

		if (inGround) {
			if (worldObj.getBlock(xTile, yTile, zTile) == inBlock) {
				++ticksInGround;

				if (ticksInGround == 1200) {
					this.setDead();
				}

				return;
			}

			inGround = false;
			ticksInGround = 0;
			ticksInAir = 0;
		} else {
			++ticksInAir;
		}

		Vec3 vec3 = Vec3.createVectorHelper(posX, posY, posZ);
		Vec3 vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3, vec31);
		vec3 = Vec3.createVectorHelper(posX, posY, posZ);
		vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);

		if (movingobjectposition != null) {
			vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}

		if (!worldObj.isRemote) {
			Entity entity = null;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			EntityLivingBase entitylivingbase = this.getThrower();

			for (int j = 0; j < list.size(); ++j) {
				Entity entity1 = (Entity) list.get(j);

				if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || ticksInAir >= 5)) {
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if (movingobjectposition1 != null) {
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}
		}

		if (movingobjectposition != null) {
			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal) {
				this.setInPortal();
			} else {
				this.onImpact(movingobjectposition);
			}
		}

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

		for (rotationPitch = (float) (Math.atan2(motionY, f1) * 180.0D / Math.PI); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F) {
			;
		}

		while (rotationPitch - prevRotationPitch >= 180.0F) {
			prevRotationPitch += 360.0F;
		}

		while (rotationYaw - prevRotationYaw < -180.0F) {
			prevRotationYaw -= 360.0F;
		}

		while (rotationYaw - prevRotationYaw >= 180.0F) {
			prevRotationYaw += 360.0F;
		}

		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
		float f2 = 0.99F;

		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f4 = 0.25F;
				worldObj.spawnParticle("bubble", posX - motionX * f4, posY - motionY * f4, posZ - motionZ * f4, motionX, motionY, motionZ);
			}

			f2 = 0.8F;
		}

		if (Math.abs(startX - posX) > 200) {
			this.setDead();
		} else if (Math.abs(startY - posY) > 200) {
			this.setDead();
		} else if (Math.abs(startZ - posZ) > 200) {
			this.setDead();
		} else if (posY < 0 || posY > 255) {
			this.setDead();
		}

		motionX *= 1.2D;
		motionY *= 1.2D;
		motionZ *= 1.2D;

		this.setPosition(posX, posY, posZ);
	}

	protected abstract void onImpact(MovingObjectPosition position);

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setShort("xTile", (short) xTile);
		nbt.setShort("yTile", (short) yTile);
		nbt.setShort("zTile", (short) zTile);
		nbt.setByte("inTile", (byte) Block.getIdFromBlock(inBlock));
		nbt.setByte("shake", (byte) throwableShake);
		nbt.setByte("inGround", (byte) (inGround ? 1 : 0));

		if ((throwerName == null || throwerName.length() == 0) && thrower != null && thrower instanceof EntityPlayer) {
			throwerName = thrower.getCommandSenderName();
		}

		nbt.setString("ownerName", throwerName == null ? "" : throwerName);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		xTile = nbt.getShort("xTile");
		yTile = nbt.getShort("yTile");
		zTile = nbt.getShort("zTile");
		inBlock = Block.getBlockById(nbt.getByte("inTile") & 255);
		throwableShake = nbt.getByte("shake") & 255;
		inGround = nbt.getByte("inGround") == 1;
		throwerName = nbt.getString("ownerName");

		if (throwerName != null && throwerName.length() == 0) {
			throwerName = null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	public EntityLivingBase getThrower() {
		if (thrower == null && throwerName != null && throwerName.length() > 0) {
			thrower = worldObj.getPlayerEntityByName(throwerName);
		}

		return thrower;
	}

}
