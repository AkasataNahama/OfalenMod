package nahama.ofalenmod.generator;

import cpw.mods.fml.common.IWorldGenerator;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

public class WorldGenOfalenOre implements IWorldGenerator {
	private static void generateOfalenOre(int color, int limit, World world, Random random, int genX, int genY, int genZ) {
		new WorldGenMinable(OfalenModBlockCore.oreOfalen, color, limit, Blocks.stone).generate(world, random, genX, genY, genZ);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (OfalenModConfigCore.isGeneratorEnabled) {
			if (world.provider instanceof WorldProviderSurface) {
				this.generateOre(0, world, random, chunkX << 4, chunkZ << 4);
				this.generateOre(1, world, random, chunkX << 4, chunkZ << 4);
				this.generateOre(2, world, random, chunkX << 4, chunkZ << 4);
				this.generateLoad(world, random, chunkX << 4, chunkZ << 4);
			}
		}
	}

	private void generateOre(int color, World world, Random random, int x, int z) {
		for (int i = 0; i < OfalenModConfigCore.frequencyGeneration; i++) {
			int genX = x + random.nextInt(16);
			int genY = 1 + random.nextInt(15);
			int genZ = z + random.nextInt(16);
			generateOfalenOre(color, OfalenModConfigCore.limitGeneration, world, random, genX, genY, genZ);
		}
	}

	private void generateLoad(World world, Random random, int x, int z) {
		if (random.nextDouble() < OfalenModConfigCore.probLodeGeneration) {
			int genX = x + random.nextInt(16);
			int genY = 1 + random.nextInt(15);
			int genZ = z + random.nextInt(16);
			int type = random.nextInt(19) / 3;
			switch (type) {
			case 0:
				generateOfalenOre(0, 40, world, random, genX, genY, genZ);
				generateOfalenOre(1, 40, world, random, genX, genY, genZ);
				generateOfalenOre(2, 40, world, random, genX, genY, genZ);
				break;
			case 1:
				generateOfalenOre(0, 40, world, random, genX, genY, genZ);
				generateOfalenOre(2, 40, world, random, genX, genY, genZ);
				generateOfalenOre(1, 40, world, random, genX, genY, genZ);
				break;
			case 2:
				generateOfalenOre(1, 40, world, random, genX, genY, genZ);
				generateOfalenOre(0, 40, world, random, genX, genY, genZ);
				generateOfalenOre(2, 40, world, random, genX, genY, genZ);
				break;
			case 3:
				generateOfalenOre(1, 40, world, random, genX, genY, genZ);
				generateOfalenOre(2, 40, world, random, genX, genY, genZ);
				generateOfalenOre(0, 40, world, random, genX, genY, genZ);
				break;
			case 4:
				generateOfalenOre(2, 40, world, random, genX, genY, genZ);
				generateOfalenOre(0, 40, world, random, genX, genY, genZ);
				generateOfalenOre(1, 40, world, random, genX, genY, genZ);
				break;
			case 5:
				generateOfalenOre(2, 40, world, random, genX, genY, genZ);
				generateOfalenOre(1, 40, world, random, genX, genY, genZ);
				generateOfalenOre(0, 40, world, random, genX, genY, genZ);
				break;
			case 6:
				generateOfalenOre(3, 40, world, random, genX, genY, genZ);
			}
			generateOfalenOre(3, 40, world, random, genX, genY, genZ);
		}
	}
}
