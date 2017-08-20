package nahama.ofalenmod.util;

import net.minecraft.entity.Entity;

public class EntityPos {
	private final double x, y, z;

	public EntityPos(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static EntityPos getPosFromEntity(Entity entity) {
		return new EntityPos(entity.posX, entity.posY, entity.posZ);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EntityPos) {
			EntityPos pos = (EntityPos) obj;
			return this.x == pos.x && this.y == pos.y && this.z == pos.z;
		}
		return false;
	}

	public double getDistanceSq(double x, double y, double z) {
		double diffX = this.x - x;
		double diffY = this.y - y;
		double diffZ = this.z - z;
		return diffX * diffX + diffY * diffY + diffZ * diffZ;
	}

	public double getDistanceSq(EntityPos pos) {
		return this.getDistanceSq(pos.x, pos.y, pos.z);
	}
}
