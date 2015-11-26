package nahama.ofalenmod.core;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.recipe.MagazineRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class OfalenModRecipeCore {

	public static final OfalenModBlockCore BLOCK = new OfalenModBlockCore();
	public static final OfalenModItemCore ITEM = new OfalenModItemCore();

	public static final String[] gem = { "gemOfalenRed", "gemOfalenGreen", "gemOfalenBlue", "gemOfalenWhite" };
	public static final String[] frag = { "fragmentOfalenRed", "fragmentOfalenGreen", "fragmentOfalenBlue", "fragmentOfalenWhite" };
	public static final String[] core = { "coreOfalenRed", "coreOfalenGreen", "coreOfalenBlue", "coreOfalenWhite" };
	public static final String[] block = { "blockOfalenRed", "blockOfalenGreen", "blockOfalenBlue", "blockOfalenWhite" };

	public static final ItemStack[] crystal = { new ItemStack(ITEM.crystalEnergyLaser, 1, 0), new ItemStack(ITEM.crystalEnergyLaser, 1, 1), new ItemStack(ITEM.crystalEnergyLaser, 1, 2), new ItemStack(ITEM.crystalEnergyLaser, 1, 3) };

	/** レシピを登録する処理。 */
	public static void registerRecipe() {
		String[] recipeArray = new String[] { "XXX", "XXX", "XXX" };
		String recipeType = "X X";
		switch (OfalenModConfigCore.recipeLump % 3) {
		case 0:
			recipeType = " XX";
			break;
		case 2:
			recipeType = "XX ";
		}
		recipeArray[OfalenModConfigCore.recipeLump / 3] = recipeType;

		// ブロック・欠片関連
		for (int i = 0; i < 4; i++) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.blockOfalen, 1, i),
					"XXX", "XXX", "XXX", 'X', gem[i]));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ofalen, 1, i),
					"XXX", "XXX", "XXX", 'X', frag[i]));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ofalen, 9, i),
					"X", 'X', block[i]));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.fragmentOfalen, 9, i),
					"X", 'X', gem[i]));
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BLOCK.blockOfalen, 3, 3),
				block[0], block[1], block[2]));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.coreOfalen, 1, 3),
				core[0], core[1], core[2]));

		// 中間素材・機械類
		// 鉄の棒
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.partsOfalen3D, 1, 0),
				"X", "X", 'X', Items.iron_ingot));

		// 機械の部品
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.partsOfalen, 1, 0),
				"XXX", "XYX", "XXX", 'X', new ItemStack(ITEM.partsOfalen, 1, 2), 'Y', gem[3]));

		// Grade 3の部品
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.partsOfalen, 1, 1),
				"XYX", "YZY", "XYX", 'X', Items.iron_ingot, 'Y', Items.diamond, 'Z', block[3]));

		// 製錬機
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.machineSmelting,
				"XYX", "XZX", "XYX", 'X', new ItemStack(ITEM.partsOfalen, 1, 0), 'Y', Blocks.furnace, 'Z', block[0]));

		// 変換機
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.machineConversion,
				"XYX", "XZX", "XYX", 'X', new ItemStack(ITEM.partsOfalen, 1, 0), 'Y', Blocks.enchanting_table, 'Z', block[1]));

		// 修繕機
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.machineRepair,
				"XYX", "XZX", "XYX", 'X', new ItemStack(ITEM.partsOfalen, 1, 0), 'Y', Blocks.anvil, 'Z', block[2]));

		// 処理装置
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.processorMachine,
				"RXG", "YZY", "BXW", 'X', Items.diamond, 'Y', Items.gold_ingot, 'Z', new ItemStack(BLOCK.casingProcessor),
				'R', gem[0], 'G', gem[1], 'B', gem[2], 'W', gem[3]));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.processorMachine, 1, 1),
				"RIG", "SMS", "BIW", 'I', Items.iron_ingot, 'S', new ItemStack(ITEM.partsOfalen, 1, 2), 'M', new ItemStack(BLOCK.processorMachine),
				'R', gem[0], 'G', gem[1], 'B', gem[2], 'W', gem[3]));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.processorMachine, 1, 2),
				"RDG", "IMI", "BOW", 'D', Items.diamond, 'O', Items.gold_ingot, 'I', Items.iron_ingot, 'M', new ItemStack(BLOCK.processorMachine, 1, 1),
				'R', gem[0], 'G', gem[1], 'B', gem[2], 'W', gem[3]));

		// 筐体
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.casingProcessor,
				"XYX", "XZX", "XYX", 'X', new ItemStack(ITEM.partsOfalen, 1, 0), 'Y', Items.iron_ingot, 'Z', gem[3]));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.casingProcessor, 1, 1),
				"SIS", "OCO", "SIS", 'S', new ItemStack(ITEM.partsOfalen, 1, 2), 'I', Items.iron_ingot, 'O', frag[3], 'C', new ItemStack(BLOCK.casingProcessor)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.casingProcessor, 1, 2),
				"IDI", "OCO", "IDI", 'D', Items.diamond, 'I', Items.iron_ingot, 'O', gem[3], 'C', new ItemStack(BLOCK.casingProcessor, 1, 1)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.casingProcessor, 1, 4),
				"XYX", "XZX", "XYX", 'X', new ItemStack(ITEM.partsOfalen, 1, 0), 'Y', gem[3], 'Z', Blocks.iron_block));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.casingProcessor, 1, 5),
				"IOI", "SCS", "IOI", 'I', Items.iron_ingot, 'S', new ItemStack(ITEM.partsOfalen, 1, 2), 'O', gem[3], 'C', new ItemStack(BLOCK.casingProcessor, 1, 4)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.casingProcessor, 1, 6),
				"OIO", "DCD", "OIO", 'D', Items.diamond, 'I', Items.iron_ingot, 'O', gem[3], 'C', new ItemStack(BLOCK.casingProcessor, 1, 5)));

		// 石の塊
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.partsOfalen, 1, 2),
				recipeArray, 'X', Blocks.cobblestone));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.partsOfalen, 1, 2),
				recipeArray, 'X', Blocks.stone));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.cobblestone, 8),
				"X", 'X', new ItemStack(ITEM.partsOfalen, 1, 2)));

		// 石燃料
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.partsOfalen, 1, 3),
				" XX", "XXX", "XXX", 'X', new ItemStack(ITEM.partsOfalen, 1, 2)));

		// オファレン燃料
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.partsOfalen, 1, 4),
				"XXX", "XYX", "XXX", 'X', new ItemStack(ITEM.partsOfalen, 1, 3), 'Y', gem[0]));

		// 防具
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.helmetOfalen,
				"XXX", "X X", 'X', "gemOfalenRed"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.chestplateOfalen,
				"X X", "XXX", "XXX", 'X', "gemOfalenRed"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.leggingsOfalen,
				"XXX", "X X", "X X", 'X', "gemOfalenRed"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.bootsOfalen,
				"X X", "X X", 'X', "gemOfalenRed"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.helmetOfalenG2,
				" X ", "YZY", " X ", 'X', "gemOfalenRed", 'Y', "blockOfalenRed", 'Z', ITEM.helmetOfalen));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.chestplateOfalenG2,
				" X ", "YZY", " X ", 'X', "gemOfalenRed", 'Y', "blockOfalenRed", 'Z', ITEM.chestplateOfalen));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.leggingsOfalenG2,
				" X ", "YZY", " X ", 'X', "gemOfalenRed", 'Y', "blockOfalenRed", 'Z', ITEM.leggingsOfalen));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.bootsOfalenG2,
				" X ", "YZY", " X ", 'X', "gemOfalenRed", 'Y', "blockOfalenRed", 'Z', ITEM.bootsOfalen));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.helmetOfalenG3,
				"ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEM.partsOfalen, 1, 1), 'Y', ITEM.helmetOfalenG2, 'Z', "gemOfalenRed"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.chestplateOfalenG3,
				"ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEM.partsOfalen, 1, 1), 'Y', ITEM.chestplateOfalenG2, 'Z', "gemOfalenRed"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.leggingsOfalenG3,
				"ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEM.partsOfalen, 1, 1), 'Y', ITEM.leggingsOfalenG2, 'Z', "gemOfalenRed"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.bootsOfalenG3,
				"ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEM.partsOfalen, 1, 1), 'Y', ITEM.bootsOfalenG2, 'Z', "gemOfalenRed"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.helmetPerfectOfalen,
				"BCB", "DAD", "BCB",
				'A', ITEM.helmetOfalenG3,
				'B', "blockOfalenRed",
				'C', "coreOfalenRed",
				'D', "coreOfalenWhite"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.chestplatePerfectOfalen,
				"BCB", "DAD", "BCB",
				'A', ITEM.chestplateOfalenG3,
				'B', "blockOfalenRed",
				'C', "coreOfalenRed",
				'D', "coreOfalenWhite"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.leggingsPerfectOfalen,
				"BCB", "DAD", "BCB",
				'A', ITEM.leggingsOfalenG3,
				'B', "blockOfalenRed",
				'C', "coreOfalenRed",
				'D', "coreOfalenWhite"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.bootsPerfectOfalen,
				"BCB", "DAD", "BCB",
				'A', ITEM.bootsOfalenG3,
				'B', "blockOfalenRed",
				'C', "coreOfalenRed",
				'D', "coreOfalenWhite"));

		// ボール
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballEmpty, 4),
				" X ", "X X", " X ", 'X', "gemOfalenGreen"));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballEmpty, 1, 1),
				" X ", "XYX", " X ", 'X', "gemOfalenGreen", 'Y', ITEM.ballEmpty));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballEmpty, 2, 2),
				" Z ", "XYX", " Z ", 'X', new ItemStack(ITEM.ballEmpty, 1, 1), 'Y', "gemOfalenGreen", 'Z', new ItemStack(ITEM.partsOfalen, 1, 1)));

		addBallRecipe("gemOfalenRed", ITEM.ballDefense, ITEM.ballDefenseG2, ITEM.ballDefenseG3);
		addBallRecipe("gemOfalenBlue", ITEM.ballAttack, ITEM.ballAttackG2, ITEM.ballAttackG3);
		addBallRecipe("gemOfalenWhite", ITEM.ballRecovery, ITEM.ballRecoveryG2, ITEM.ballRecoveryG3);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballExplosion, 4),
				" X ", "XYX", " X ", 'X', ITEM.ballEmpty, 'Y', Items.gunpowder));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballExplosion, 4, 1),
				" X ", "XYX", " X ", 'X', new ItemStack(ITEM.ballEmpty, 1, 1), 'Y', Items.gunpowder));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballExplosion, 4, 2),
				" X ", "XYX", " X ", 'X', new ItemStack(ITEM.ballEmpty, 1, 2), 'Y', Items.gunpowder));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballFlame, 4),
				" X ", "XYX", " X ", 'X', ITEM.ballEmpty, 'Y', Items.blaze_powder));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballFlame, 4, 1),
				" X ", "XYX", " X ", 'X', new ItemStack(ITEM.ballEmpty, 1, 1), 'Y', Items.blaze_powder));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballFlame, 4, 2),
				" X ", "XYX", " X ", 'X', new ItemStack(ITEM.ballEmpty, 1, 2), 'Y', Items.blaze_powder));

		for (int i = 0; i < 3; i++) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.ballExplosion, 1, i),
					new ItemStack(ITEM.ballEmpty, 1, i), Items.gunpowder));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.ballFlame, 1, i),
					new ItemStack(ITEM.ballEmpty, 1, i), Items.blaze_powder));
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(ITEM.ballHungry,
				ITEM.ballEmpty, Items.rotten_flesh));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.ballHungry, 4),
				" X ", "XYX", " X ", 'X', ITEM.ballEmpty, 'Y', Items.rotten_flesh));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.ballPerfectOfalen,
				"XAX", "BYC", "XZX",
				'A', ITEM.ballRecoveryG3,
				'B', ITEM.ballDefenseG3,
				'C', ITEM.ballAttackG3,
				'X', "blockOfalenGreen",
				'Y', "coreOfalenGreen",
				'Z', "coreOfalenWhite"));

		// 道具
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.pickaxeOfalen,
				"XXX", " Y ", " Y ", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEM.partsOfalen3D, 1, 0)));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.shovelOfalen,
				"X", "Y", "Y", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEM.partsOfalen3D, 1, 0)));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.hoeOfalen,
				"XX", " Y", " Y", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEM.partsOfalen3D, 1, 0)));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.axeOfalen,
				"XX", "XY", " Y", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEM.partsOfalen3D, 1, 0)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.swordOfalen),
				"X", "X", "Y", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEM.partsOfalen3D, 1, 0)));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.pickaxeOfalenG2,
				" X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEM.pickaxeOfalen));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.shovelOfalenG2,
				" X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEM.shovelOfalen));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.hoeOfalenG2,
				" X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEM.hoeOfalen));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.axeOfalenG2,
				" X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEM.axeOfalen));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.swordOfalenG2,
				" X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEM.swordOfalen));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.pickaxeOfalenG3,
				"ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEM.partsOfalen, 1, 1), 'Y', ITEM.pickaxeOfalenG2, 'Z', "gemOfalenBlue"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.shovelOfalenG3,
				"ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEM.partsOfalen, 1, 1), 'Y', ITEM.shovelOfalenG2, 'Z', "gemOfalenBlue"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.hoeOfalenG3,
				"ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEM.partsOfalen, 1, 1), 'Y', ITEM.hoeOfalenG2, 'Z', "gemOfalenBlue"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.axeOfalenG3,
				"ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEM.partsOfalen, 1, 1), 'Y', ITEM.axeOfalenG2, 'Z', "gemOfalenBlue"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.swordOfalenG3,
				"ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEM.partsOfalen, 1, 1), 'Y', ITEM.swordOfalenG2, 'Z', "gemOfalenBlue"));

		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.toolPerfectOfalen,
				"ABC", "XZX", "DYE",
				'A', ITEM.shovelOfalenG3,
				'B', ITEM.swordOfalenG3,
				'C', ITEM.axeOfalenG3,
				'D', ITEM.pickaxeOfalenG3,
				'E', ITEM.hoeOfalenG3,
				'X', "coreOfalenWhite",
				'Y', "blockOfalenBlue",
				'Z', "coreOfalenBlue"));

		// レーザーピストル関連
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.partsOfalen, 1, 5),
				"X Y", "X Y", "XXX", 'X', gem[3], 'Y', Items.iron_ingot));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.magazineLaserRed, 1, 1024),
				new ItemStack(ITEM.partsOfalen, 1, 5), gem[0]));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.magazineLaserGreen, 1, 1024),
				new ItemStack(ITEM.partsOfalen, 1, 5), gem[1]));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.magazineLaserBlue, 1, 1024),
				new ItemStack(ITEM.partsOfalen, 1, 5), gem[2]));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.magazineLaserWhite, 1, 1024),
				new ItemStack(ITEM.partsOfalen, 1, 5), gem[3]));

		for (int i = 0; i < 3; i++) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.crystalEnergyLaser, 4, i),
					"XYY", 'X', Items.gold_nugget, 'Y', frag[i]));
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEM.crystalEnergyLaser, 1, 3),
				new ItemStack(ITEM.crystalEnergyLaser, 1, 0), new ItemStack(ITEM.crystalEnergyLaser, 1, 1), new ItemStack(ITEM.crystalEnergyLaser, 1, 2), "gemOfalenWhite"));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.pistolLaser, 1, 1024),
				"OOO", "OII", "GC ", 'O', gem[3], 'I', Items.iron_ingot, 'G', Items.gold_ingot, 'C', core[3]));

		RecipeSorter.register("ofalenmod:magazine", MagazineRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
		for (int i = 0; i < 1024; i += 32) {
			for (int j = 1; j < 9; j++) {
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(ITEM.magazineLaserRed, 1, i),
						new ItemStack(ITEM.magazineLaserRed, 1, i + (32 * j)), crystal[0], j));
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(ITEM.magazineLaserGreen, 1, i),
						new ItemStack(ITEM.magazineLaserGreen, 1, i + (32 * j)), crystal[1], j));
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(ITEM.magazineLaserBlue, 1, i),
						new ItemStack(ITEM.magazineLaserBlue, 1, i + (32 * j)), crystal[2], j));
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(ITEM.magazineLaserWhite, 1, i),
						new ItemStack(ITEM.magazineLaserWhite, 1, i + (32 * j)), crystal[3], j));
			}
		}

		// 燃料の登録
		GameRegistry.registerFuelHandler(new IFuelHandler() {
			@Override
			public int getBurnTime(ItemStack fuel) {
				if (fuel.isItemEqual(new ItemStack(ITEM.ofalen, 1, 0))) {
					return 4000;
				}
				return 0;
			}
		});
	}

	private static void addBallRecipe(Object material, Item... result) {
		for (int i = 0; i < 3; i++) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(result[i]),
					new ItemStack(ITEM.ballEmpty, 1, i), material));
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(result[0], 4),
				" X ", "XYX", " X ", 'X', ITEM.ballEmpty, 'Y', material));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(result[1], 4),
				" X ", "XYX", " X ", 'X', new ItemStack(ITEM.ballEmpty, 1, 1), 'Y', material));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(result[2], 4),
				" X ", "XYX", " X ", 'X', new ItemStack(ITEM.ballEmpty, 1, 2), 'Y', material));
	}

}
