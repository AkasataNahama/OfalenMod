package nahama.ofalenmod.util;

import net.minecraft.world.World;

import static nahama.ofalenmod.util.Util.random;

public class OfalenParticleUtil {
	public static void spawnParticleWithBlockRange(World world, BlockRange range, int type) {
		double[] color = getColorWithTypeForParticle(type);
		spawnParticleWithBlockRange(world, range, color[0], color[1], color[2]);
	}

	public static void spawnParticleWithBlockRange(World world, BlockRange range, double colorR, double colorG, double colorB) {
		BlockPos posMin = range.posMin;
		BlockPos posMax = range.posMax;
		for (double dx = posMin.x; dx <= posMax.x + 1; dx += 0.5) {
			for (double dy = posMin.y; dy <= posMax.y + 1; dy += 0.5) {
				if (random.nextBoolean())
					world.spawnParticle("reddust", dx, dy, posMin.z, colorR, colorG, colorB);
				if (random.nextBoolean())
					world.spawnParticle("reddust", dx, dy, posMax.z + 1, colorR, colorG, colorB);
			}
		}
		for (double dx = posMin.x; dx <= posMax.x + 1; dx += 0.5) {
			for (double dz = posMin.z; dz <= posMax.z + 1; dz += 0.5) {
				if (random.nextBoolean())
					world.spawnParticle("reddust", dx, posMin.y, dz, colorR, colorG, colorB);
				if (random.nextBoolean())
					world.spawnParticle("reddust", dx, posMax.y + 1, dz, colorR, colorG, colorB);
			}
		}
		for (double dy = posMin.y; dy <= posMax.y + 1; dy += 0.5) {
			for (double dz = posMin.z; dz <= posMax.z + 1; dz += 0.5) {
				if (random.nextBoolean())
					world.spawnParticle("reddust", posMin.x, dy, dz, colorR, colorG, colorB);
				if (random.nextBoolean())
					world.spawnParticle("reddust", posMax.x + 1, dy, dz, colorR, colorG, colorB);
			}
		}
	}

	public static void spawnParticleWithBlockRange(World world, BlockRangeWithStandard range, int type) {
		spawnParticleWithBlockRange(world, range.convertToNormal(), type);
		spawnParticleAroundBlock(world, range.posA, type);
		spawnParticleAroundBlock(world, range.posB, type);
	}

	public static void spawnParticleAroundBlock(World world, BlockPos pos, int type) {
		double[] color = getColorWithTypeForParticle(type);
		spawnParticleAroundBlock(world, pos, color[0], color[1], color[2]);
	}

	public static void spawnParticleAroundBlock(World world, BlockPos pos, double colorR, double colorG, double colorB) {
		for (double dx = pos.x; dx <= pos.x + 1; dx += 0.2) {
			for (double dy = pos.y; dy <= pos.y + 1; dy += 0.2) {
				if (random.nextBoolean())
					world.spawnParticle("reddust", dx, dy, pos.z, colorR, colorG, colorB);
				if (random.nextBoolean())
					world.spawnParticle("reddust", dx, dy, pos.z + 1, colorR, colorG, colorB);
			}
		}
		for (double dx = pos.x; dx <= pos.x + 1; dx += 0.2) {
			for (double dz = pos.z; dz <= pos.z + 1; dz += 0.2) {
				if (random.nextBoolean())
					world.spawnParticle("reddust", dx, pos.y, dz, colorR, colorG, colorB);
				if (random.nextBoolean())
					world.spawnParticle("reddust", dx, pos.y + 1, dz, colorR, colorG, colorB);
			}
		}
		for (double dy = pos.y; dy <= pos.y + 1; dy += 0.2) {
			for (double dz = pos.z; dz <= pos.z + 1; dz += 0.2) {
				if (random.nextBoolean())
					world.spawnParticle("reddust", pos.x, dy, dz, colorR, colorG, colorB);
				if (random.nextBoolean())
					world.spawnParticle("reddust", pos.x + 1, dy, dz, colorR, colorG, colorB);
			}
		}
	}

	public static double[] getColorWithTypeForParticle(int type) {
		double[] color = new double[3];
		switch (type) {
		case 2:
			color[0] = 0.127D;
			color[1] = 0.578D;
			color[2] = 1.0D;
		case 4:
			color[0] = 1.0D;
			color[1] = 0.578D;
			color[2] = 0.127D;
			break;
		case 5:
			color[0] = 0.127D;
			color[1] = 1.0D;
			color[2] = 0.578D;
			break;
		case 6:
			color[0] = 0.578D;
			color[1] = 0.127D;
			color[2] = 1.0D;
			break;
		case 7:
			color[0] = 0.127D;
			color[1] = 0.127D;
			color[2] = 0.127D;
			break;
		default:
			color[0] = 1.0D;
			color[1] = 1.0D;
			color[2] = 1.0D;
		}
		return color;
	}
}
