package nahama.ofalenmod.generator;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class OfalenOreGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (OfalenModConfigCore.isGeneratorEnabled) {
			if (world.provider instanceof WorldProviderSurface) {
				this.generateOreRed(world, random, chunkX << 4, chunkZ << 4);
				this.generateOreGreen(world, random, chunkX << 4, chunkZ << 4);
				this.generateOreBlue(world, random, chunkX << 4, chunkZ << 4);
				this.generateOreBig(world, random, chunkX << 4, chunkZ << 4);
			}
		}
	}

	private void generateOreRed(World world, Random random, int x, int z) {
		for (int i = 0; i < OfalenModConfigCore.frequencyGeneration; i++) {
			int genX = x + random.nextInt(16);
			int genY = 1 + random.nextInt(15);
			int genZ = z + random.nextInt(16);
			new WorldGenMinable(OfalenModBlockCore.oreOfalen, 0, OfalenModConfigCore.limitGeneration, Blocks.stone).generate(world, random, genX, genY, genZ);
		}
	}

	private void generateOreGreen(World world, Random random, int x, int z) {
		for (int i = 0; i < OfalenModConfigCore.frequencyGeneration; i++) {
			int genX = x + random.nextInt(16);
			int genY = 1 + random.nextInt(15);
			int genZ = z + random.nextInt(16);
			new WorldGenMinable(OfalenModBlockCore.oreOfalen, 1, OfalenModConfigCore.limitGeneration, Blocks.stone).generate(world, random, genX, genY, genZ);
		}
	}

	private void generateOreBlue(World world, Random random, int x, int z) {
		for (int i = 0; i < OfalenModConfigCore.frequencyGeneration; i++) {
			int genX = x + random.nextInt(16);
			int genY = 1 + random.nextInt(15);
			int genZ = z + random.nextInt(16);
			new WorldGenMinable(OfalenModBlockCore.oreOfalen, 2, OfalenModConfigCore.limitGeneration, Blocks.stone).generate(world, random, genX, genY, genZ);
		}
	}

	private void generateOreBig(World world, Random random, int x, int z) {
		int i = random.nextInt(10000);
		if (i < OfalenModConfigCore.probabilityGenerationLode) {
			int genX = x + random.nextInt(16);
			int genY = 1 + random.nextInt(15);
			int genZ = z + random.nextInt(16);

			int type = random.nextInt(19) / 3;
			switch (type) {
			case 0:
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 0, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 1, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 2, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				break;

			case 1:
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 0, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 2, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 1, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				break;

			case 2:
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 1, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 0, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 2, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				break;

			case 3:
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 1, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 2, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 0, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				break;

			case 4:
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 2, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 0, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 1, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				break;

			case 5:
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 2, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 1, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 0, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
				break;

			case 6:
				new WorldGenMinable(OfalenModBlockCore.oreOfalen, 3, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
			}
			new WorldGenMinable(OfalenModBlockCore.oreOfalen, 3, 40, Blocks.stone).generate(world, random, genX, genY, genZ);
		}
	}

}
