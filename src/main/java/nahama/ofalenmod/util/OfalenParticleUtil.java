package nahama.ofalenmod.util;

import net.minecraft.world.World;

import static nahama.ofalenmod.util.OfalenUtil.random;

public class OfalenParticleUtil {
	private static final double COLOR_1 = 1.0;
	private static final double COLOR_2 = 0.578;
	private static final double COLOR_3 = 0.127;

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
		case 0:
			color[0] = COLOR_1;
			color[1] = COLOR_3;
			color[2] = COLOR_2;
			break;
		case 1:
			color[0] = COLOR_2;
			color[1] = COLOR_1;
			color[2] = COLOR_3;
			break;
		case 2:
			color[0] = COLOR_3;
			color[1] = COLOR_2;
			color[2] = COLOR_1;
			break;
		case 4:
			color[0] = COLOR_1;
			color[1] = COLOR_2;
			color[2] = COLOR_3;
			break;
		case 5:
			color[0] = COLOR_3;
			color[1] = COLOR_1;
			color[2] = COLOR_2;
			break;
		case 6:
			color[0] = COLOR_2;
			color[1] = COLOR_3;
			color[2] = COLOR_1;
			break;
		case 7:
			color[0] = COLOR_3;
			color[1] = COLOR_3;
			color[2] = COLOR_3;
			break;
		default:
			color[0] = COLOR_1;
			color[1] = COLOR_1;
			color[2] = COLOR_1;
		}
		return color;
	}
}
