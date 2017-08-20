package nahama.ofalenmod.util;

import net.minecraft.entity.Entity;

public abstract class EntityRange {
	public static EntityRange getRangeByType(Entity entity, int type, double... lengths) {
		EntityPos pos = EntityPos.getPosFromEntity(entity);
		switch (type) {
		case 0:
			return new EntityRangeSphere(pos, lengths[0]);
		case 1:
			return new EntityRangeCylinder(pos, lengths[0], lengths[1]);
		case 2:
			return new EntityRangeCuboid(pos, lengths[0], lengths[1], lengths[2]);
		}
		throw new IllegalArgumentException("Invalid type of EntityRange: " + type);
	}

	public boolean isInRange(Entity entity) {
		return this.isInRange(EntityPos.getPosFromEntity(entity));
	}

	public abstract boolean isInRange(EntityPos pos);

	public static class EntityRangeSphere extends EntityRange {
		private EntityPos posStandard;
		private double radius;

		public EntityRangeSphere(EntityPos posStandard, double radius) {
			this.posStandard = posStandard;
			this.radius = radius;
		}

		@Override
		public boolean isInRange(EntityPos pos) {
			return posStandard.getDistanceSq(pos) <= radius * radius;
		}
	}

	public static class EntityRangeCylinder extends EntityRange {
		private EntityPos posStandard;
		private double radius, height;

		public EntityRangeCylinder(EntityPos posStandard, double radius, double height) {
			this.posStandard = posStandard;
			this.radius = radius;
			this.height = height;
		}

		@Override
		public boolean isInRange(EntityPos pos) {
			if (Math.abs(posStandard.getY() - pos.getY()) > height)
				return false;
			double diffX = posStandard.getX() - pos.getX();
			double diffZ = posStandard.getZ() - pos.getZ();
			return diffX * diffX + diffZ * diffZ <= radius * radius;
		}
	}

	public static class EntityRangeCuboid extends EntityRange {
		private EntityPos posStandard;
		private double lengthX, lengthY, lengthZ;

		public EntityRangeCuboid(EntityPos posStandard, double lengthX, double lengthY, double lengthZ) {
			this.posStandard = posStandard;
			this.lengthX = lengthX;
			this.lengthY = lengthY;
			this.lengthZ = lengthZ;
		}

		@Override
		public boolean isInRange(EntityPos pos) {
			return this.isInLength(posStandard.getX(), pos.getX(), lengthX) && this.isInLength(posStandard.getY(), pos.getY(), lengthY) && this.isInLength(posStandard.getZ(), pos.getZ(), lengthZ);
		}

		private boolean isInLength(double posA, double posB, double length) {
			return Math.abs(posA - posB) <= length;
		}
	}
}
