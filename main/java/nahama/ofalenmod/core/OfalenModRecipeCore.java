package nahama.ofalenmod.core;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.recipe.MagazineRecipe;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class OfalenModRecipeCore {
	private static final OfalenModBlockCore BLOCKS = new OfalenModBlockCore();
	private static final OfalenModItemCore ITEMS = new OfalenModItemCore();
	public static final String[] GEM = { "gemOfalenRed", "gemOfalenGreen", "gemOfalenBlue", "gemOfalenWhite", "gemOfalenOrange", "gemOfalenViridian", "gemOfalenPurple", "gemOfalenDark" };
	public static final String[] FRAG = { "fragmentOfalenRed", "fragmentOfalenGreen", "fragmentOfalenBlue", "fragmentOfalenWhite", "fragmentOfalenOrange", "fragmentOfalenViridian", "fragmentOfalenPurple", "fragmentOfalenDark" };
	public static final String[] CORE = { "coreOfalenRed", "coreOfalenGreen", "coreOfalenBlue", "coreOfalenWhite", "coreOfalenOrange", "coreOfalenViridian", "coreOfalenPurple", "coreOfalenDark" };
	public static final String[] BLOCK = { "blockOfalenRed", "blockOfalenGreen", "blockOfalenBlue", "blockOfalenWhite", "blockOfalenOrange", "blockOfalenViridian", "blockOfalenPurple", "blockOfalenDark" };
	public static final ItemStack[] CRYSTALS = { new ItemStack(ITEMS.crystalLaserEnergy, 1, 0), new ItemStack(ITEMS.crystalLaserEnergy, 1, 1), new ItemStack(ITEMS.crystalLaserEnergy, 1, 2), new ItemStack(ITEMS.crystalLaserEnergy, 1, 3) };

	/** レシピを登録する。 */
	public static void registerRecipe() {
		String[] recipeArray = new String[] { "XXX", "XXX", "XXX" };
		String recipeType = "X X";
		switch (OfalenModConfigCore.blankLumpRecipe % 3) {
		case 0:
			recipeType = " XX";
			break;
		case 2:
			recipeType = "XX ";
		}
		recipeArray[OfalenModConfigCore.blankLumpRecipe / 3] = recipeType;
		// ブロック・欠片関連
		for (int i = 0; i < 8; i++) {
			addShaped(new ItemStack(BLOCKS.blockOfalen, 1, i), "XXX", "XXX", "XXX", 'X', GEM[i]);
			addShaped(new ItemStack(ITEMS.gemOfalen, 1, i), "XXX", "XXX", "XXX", 'X', FRAG[i]);
			addShaped(new ItemStack(ITEMS.gemOfalen, 9, i), "X", 'X', BLOCK[i]);
			addShaped(new ItemStack(ITEMS.fragmentOfalen, 9, i), "X", 'X', GEM[i]);
		}
		addShapeless(new ItemStack(BLOCKS.blockOfalen, 3, 3), BLOCK[0], BLOCK[1], BLOCK[2]);
		addShapeless(new ItemStack(ITEMS.coreOfalen, 1, 3), CORE[0], CORE[1], CORE[2]);
		// 中間素材・機械類
		// 鉄の棒
		addShaped(new ItemStack(ITEMS.partsOfalen3D, 1, 0), "X", "X", 'X', Items.iron_ingot);
		// 機械用カバープレート
		addShaped(new ItemStack(ITEMS.partsOfalen, 1, 0), "LQL", "QOQ", "LQL", 'L', new ItemStack(ITEMS.partsOfalen, 1, 2), 'Q', "gemQuartz", 'O', FRAG[3]);
		// Grade 3の部品
		addShaped(new ItemStack(ITEMS.partsOfalen, 1, 1), "XYX", "YZY", "XYX", 'X', Items.iron_ingot, 'Y', Items.diamond, 'Z', BLOCK[3]);
		// 製錬機
		addShaped(BLOCKS.machineSmelting, "XYX", "XZX", "XYX", 'X', new ItemStack(ITEMS.partsOfalen, 1, 0), 'Y', Blocks.furnace, 'Z', BLOCK[0]);
		// 変換機
		addShaped(BLOCKS.machineConverting, "XYX", "XZX", "XYX", 'X', new ItemStack(ITEMS.partsOfalen, 1, 0), 'Y', Blocks.enchanting_table, 'Z', BLOCK[1]);
		// 修繕機
		addShaped(BLOCKS.machineRepairing, "XYX", "XZX", "XYX", 'X', new ItemStack(ITEMS.partsOfalen, 1, 0), 'Y', Blocks.anvil, 'Z', BLOCK[2]);
		// 融合機
		addShaped(BLOCKS.machineFusing, "XYX", "XZX", "XYX", 'X', new ItemStack(ITEMS.partsOfalen, 1, 0), 'Y', CORE[3], 'Z', BLOCK[3]);
		// 処理装置
		addShaped(BLOCKS.processor, "RXG", "YZY", "BXW", 'X', Items.diamond, 'Y', Items.gold_ingot, 'Z', new ItemStack(BLOCKS.casingProcessor), 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', GEM[3]);
		addShaped(new ItemStack(BLOCKS.processor, 1, 1), "RIG", "SMS", "BIW", 'I', Items.iron_ingot, 'S', new ItemStack(ITEMS.partsOfalen, 1, 2), 'M', new ItemStack(BLOCKS.processor), 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', GEM[3]);
		addShaped(new ItemStack(BLOCKS.processor, 1, 2), "RDG", "IMI", "BOW", 'D', Items.diamond, 'O', Items.gold_ingot, 'I', Items.iron_ingot, 'M', new ItemStack(BLOCKS.processor, 1, 1), 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', GEM[3]);
		// 筐体
		addShaped(BLOCKS.casingProcessor, "XYX", "XZX", "XYX", 'X', new ItemStack(ITEMS.partsOfalen, 1, 0), 'Y', Items.iron_ingot, 'Z', GEM[3]);
		addShaped(new ItemStack(BLOCKS.casingProcessor, 1, 1), "SIS", "OCO", "SIS", 'S', new ItemStack(ITEMS.partsOfalen, 1, 2), 'I', Items.iron_ingot, 'O', FRAG[3], 'C', new ItemStack(BLOCKS.casingProcessor));
		addShaped(new ItemStack(BLOCKS.casingProcessor, 1, 2), "IDI", "OCO", "IDI", 'D', Items.diamond, 'I', Items.iron_ingot, 'O', GEM[3], 'C', new ItemStack(BLOCKS.casingProcessor, 1, 1));
		addShaped(new ItemStack(BLOCKS.casingProcessor, 1, 4), "XYX", "XZX", "XYX", 'X', new ItemStack(ITEMS.partsOfalen, 1, 0), 'Y', GEM[3], 'Z', Blocks.iron_block);
		addShaped(new ItemStack(BLOCKS.casingProcessor, 1, 5), "IOI", "SCS", "IOI", 'I', Items.iron_ingot, 'S', new ItemStack(ITEMS.partsOfalen, 1, 2), 'O', GEM[3], 'C', new ItemStack(BLOCKS.casingProcessor, 1, 4));
		addShaped(new ItemStack(BLOCKS.casingProcessor, 1, 6), "OIO", "DCD", "OIO", 'D', Items.diamond, 'I', Items.iron_ingot, 'O', GEM[3], 'C', new ItemStack(BLOCKS.casingProcessor, 1, 5));
		// 石の塊
		addShaped(new ItemStack(ITEMS.partsOfalen, 1, 2), recipeArray, 'X', Blocks.cobblestone);
		addShaped(new ItemStack(ITEMS.partsOfalen, 1, 2), recipeArray, 'X', Blocks.stone);
		addShaped(new ItemStack(Blocks.cobblestone, 8), "X", 'X', new ItemStack(ITEMS.partsOfalen, 1, 2));
		// 石燃料
		addShaped(new ItemStack(ITEMS.partsOfalen, 1, 3), " XX", "XXX", "XXX", 'X', new ItemStack(ITEMS.partsOfalen, 1, 2));
		// オファレン燃料
		addShaped(new ItemStack(ITEMS.partsOfalen, 1, 4), "XXX", "XYX", "XXX", 'X', new ItemStack(ITEMS.partsOfalen, 1, 3), 'Y', GEM[0]);
		// 防具
		addShaped(ITEMS.helmetOfalenG1, "XXX", "X X", 'X', "gemOfalenRed");
		addShaped(ITEMS.chestplateOfalenG1, "X X", "XXX", "XXX", 'X', "gemOfalenRed");
		addShaped(ITEMS.leggingsOfalenG1, "XXX", "X X", "X X", 'X', "gemOfalenRed");
		addShaped(ITEMS.bootsOfalenG1, "X X", "X X", 'X', "gemOfalenRed");
		addShaped(ITEMS.helmetOfalenG2, " X ", "YZY", " X ", 'X', "gemOfalenRed", 'Y', "blockOfalenRed", 'Z', ITEMS.helmetOfalenG1);
		addShaped(ITEMS.chestplateOfalenG2, " X ", "YZY", " X ", 'X', "gemOfalenRed", 'Y', "blockOfalenRed", 'Z', ITEMS.chestplateOfalenG1);
		addShaped(ITEMS.leggingsOfalenG2, " X ", "YZY", " X ", 'X', "gemOfalenRed", 'Y', "blockOfalenRed", 'Z', ITEMS.leggingsOfalenG1);
		addShaped(ITEMS.bootsOfalenG2, " X ", "YZY", " X ", 'X', "gemOfalenRed", 'Y', "blockOfalenRed", 'Z', ITEMS.bootsOfalenG1);
		addShaped(ITEMS.helmetOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEMS.partsOfalen, 1, 1), 'Y', ITEMS.helmetOfalenG2, 'Z', "gemOfalenRed");
		addShaped(ITEMS.chestplateOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEMS.partsOfalen, 1, 1), 'Y', ITEMS.chestplateOfalenG2, 'Z', "gemOfalenRed");
		addShaped(ITEMS.leggingsOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEMS.partsOfalen, 1, 1), 'Y', ITEMS.leggingsOfalenG2, 'Z', "gemOfalenRed");
		addShaped(ITEMS.bootsOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEMS.partsOfalen, 1, 1), 'Y', ITEMS.bootsOfalenG2, 'Z', "gemOfalenRed");
		addShaped(ITEMS.helmetOfalenP, "BCB", "DAD", "BCB", 'A', ITEMS.helmetOfalenG3, 'B', "blockOfalenRed", 'C', "coreOfalenRed", 'D', "coreOfalenWhite");
		addShaped(ITEMS.chestplateOfalenP, "BCB", "DAD", "BCB", 'A', ITEMS.chestplateOfalenG3, 'B', "blockOfalenRed", 'C', "coreOfalenRed", 'D', "coreOfalenWhite");
		addShaped(ITEMS.leggingsOfalenP, "BCB", "DAD", "BCB", 'A', ITEMS.leggingsOfalenG3, 'B', "blockOfalenRed", 'C', "coreOfalenRed", 'D', "coreOfalenWhite");
		addShaped(ITEMS.bootsOfalenP, "BCB", "DAD", "BCB", 'A', ITEMS.bootsOfalenG3, 'B', "blockOfalenRed", 'C', "coreOfalenRed", 'D', "coreOfalenWhite");
		// 玉
		addShaped(new ItemStack(ITEMS.ballEmpty, 4), " X ", "X X", " X ", 'X', "gemOfalenGreen");
		addShaped(new ItemStack(ITEMS.ballEmpty, 1, 1), " X ", "XYX", " X ", 'X', "gemOfalenGreen", 'Y', ITEMS.ballEmpty);
		addShaped(new ItemStack(ITEMS.ballEmpty, 2, 2), " Z ", "XYX", " Z ", 'X', new ItemStack(ITEMS.ballEmpty, 1, 1), 'Y', "gemOfalenGreen", 'Z', new ItemStack(ITEMS.partsOfalen, 1, 1));
		addBallRecipe("gemOfalenRed", ITEMS.ballDefenseG1, ITEMS.ballDefenseG2, ITEMS.ballDefenseG3);
		addBallRecipe("gemOfalenBlue", ITEMS.ballAttackG1, ITEMS.ballAttackG2, ITEMS.ballAttackG3);
		addBallRecipe("gemOfalenWhite", ITEMS.ballRecoveryG1, ITEMS.ballRecoveryG2, ITEMS.ballRecoveryG3);
		addShaped(new ItemStack(ITEMS.ballExplosion, 4), " X ", "XYX", " X ", 'X', ITEMS.ballEmpty, 'Y', Items.gunpowder);
		addShaped(new ItemStack(ITEMS.ballExplosion, 4, 1), " X ", "XYX", " X ", 'X', new ItemStack(ITEMS.ballEmpty, 1, 1), 'Y', Items.gunpowder);
		addShaped(new ItemStack(ITEMS.ballExplosion, 4, 2), " X ", "XYX", " X ", 'X', new ItemStack(ITEMS.ballEmpty, 1, 2), 'Y', Items.gunpowder);
		addShaped(new ItemStack(ITEMS.ballFlame, 4), " X ", "XYX", " X ", 'X', ITEMS.ballEmpty, 'Y', Items.blaze_powder);
		addShaped(new ItemStack(ITEMS.ballFlame, 4, 1), " X ", "XYX", " X ", 'X', new ItemStack(ITEMS.ballEmpty, 1, 1), 'Y', Items.blaze_powder);
		addShaped(new ItemStack(ITEMS.ballFlame, 4, 2), " X ", "XYX", " X ", 'X', new ItemStack(ITEMS.ballEmpty, 1, 2), 'Y', Items.blaze_powder);
		for (int i = 0; i < 3; i++) {
			addShapeless(new ItemStack(ITEMS.ballExplosion, 1, i), new ItemStack(ITEMS.ballEmpty, 1, i), Items.gunpowder);
			addShapeless(new ItemStack(ITEMS.ballFlame, 1, i), new ItemStack(ITEMS.ballEmpty, 1, i), Items.blaze_powder);
		}
		addShapeless(ITEMS.ballHungry, ITEMS.ballEmpty, Items.rotten_flesh);
		addShaped(new ItemStack(ITEMS.ballHungry, 4), " X ", "XYX", " X ", 'X', ITEMS.ballEmpty, 'Y', Items.rotten_flesh);
		addShapeless(ITEMS.ballFood, ITEMS.ballEmpty, "ingotGold");
		addShaped(new ItemStack(ITEMS.ballFood, 4), " X ", "XYX", " X ", 'X', ITEMS.ballEmpty, 'Y', "ingotGold");
		addShaped(ITEMS.ballPerfect, "XAX", "BYC", "XZX", 'A', ITEMS.ballRecoveryG3, 'B', ITEMS.ballDefenseG3, 'C', ITEMS.ballAttackG3, 'X', "blockOfalenGreen", 'Y', "coreOfalenGreen", 'Z', "coreOfalenWhite");
		// 道具
		addShaped(ITEMS.pickaxeOfalenG1, "XXX", " Y ", " Y ", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEMS.partsOfalen3D, 1, 0));
		addShaped(ITEMS.shovelOfalenG1, "X", "Y", "Y", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEMS.partsOfalen3D, 1, 0));
		addShaped(ITEMS.hoeOfalenG1, "XX", " Y", " Y", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEMS.partsOfalen3D, 1, 0));
		addShaped(ITEMS.axeOfalenG1, "XX", "XY", " Y", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEMS.partsOfalen3D, 1, 0));
		addShaped(new ItemStack(ITEMS.swordOfalenG1), "X", "X", "Y", 'X', "gemOfalenBlue", 'Y', new ItemStack(ITEMS.partsOfalen3D, 1, 0));
		addShaped(ITEMS.pickaxeOfalenG2, " X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEMS.pickaxeOfalenG1);
		addShaped(ITEMS.shovelOfalenG2, " X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEMS.shovelOfalenG1);
		addShaped(ITEMS.hoeOfalenG2, " X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEMS.hoeOfalenG1);
		addShaped(ITEMS.axeOfalenG2, " X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEMS.axeOfalenG1);
		addShaped(ITEMS.swordOfalenG2, " X ", "YZY", " X ", 'X', "gemOfalenBlue", 'Y', "blockOfalenBlue", 'Z', ITEMS.swordOfalenG1);
		addShaped(ITEMS.pickaxeOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEMS.partsOfalen, 1, 1), 'Y', ITEMS.pickaxeOfalenG2, 'Z', "gemOfalenBlue");
		addShaped(ITEMS.shovelOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEMS.partsOfalen, 1, 1), 'Y', ITEMS.shovelOfalenG2, 'Z', "gemOfalenBlue");
		addShaped(ITEMS.hoeOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEMS.partsOfalen, 1, 1), 'Y', ITEMS.hoeOfalenG2, 'Z', "gemOfalenBlue");
		addShaped(ITEMS.axeOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEMS.partsOfalen, 1, 1), 'Y', ITEMS.axeOfalenG2, 'Z', "gemOfalenBlue");
		addShaped(ITEMS.swordOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', new ItemStack(ITEMS.partsOfalen, 1, 1), 'Y', ITEMS.swordOfalenG2, 'Z', "gemOfalenBlue");
		addShaped(ITEMS.toolOfalenP, "ABC", "XZX", "DYE", 'A', ITEMS.shovelOfalenG3, 'B', ITEMS.swordOfalenG3, 'C', ITEMS.axeOfalenG3, 'D', ITEMS.pickaxeOfalenG3, 'E', ITEMS.hoeOfalenG3, 'X', "coreOfalenWhite", 'Y', "blockOfalenBlue", 'Z', "coreOfalenBlue");
		// レーザー関連
		addShaped(new ItemStack(ITEMS.partsOfalen, 1, 5), "X Y", "X Y", "XXX", 'X', GEM[3], 'Y', Items.iron_ingot);
		addShapeless(new ItemStack(ITEMS.magazineLaserRed, 1, 1024), new ItemStack(ITEMS.partsOfalen, 1, 5), GEM[0]);
		addShapeless(new ItemStack(ITEMS.magazineLaserGreen, 1, 1024), new ItemStack(ITEMS.partsOfalen, 1, 5), GEM[1]);
		addShapeless(new ItemStack(ITEMS.magazineLaserBlue, 1, 1024), new ItemStack(ITEMS.partsOfalen, 1, 5), GEM[2]);
		addShapeless(new ItemStack(ITEMS.magazineLaserWhite, 1, 1024), new ItemStack(ITEMS.partsOfalen, 1, 5), GEM[3]);
		for (int i = 0; i < 3; i++) {
			addShaped(new ItemStack(ITEMS.crystalLaserEnergy, 4, i), "XYY", 'X', Items.gold_nugget, 'Y', FRAG[i]);
		}
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ITEMS.crystalLaserEnergy, 1, 3), new ItemStack(ITEMS.crystalLaserEnergy, 1, 0), new ItemStack(ITEMS.crystalLaserEnergy, 1, 1), new ItemStack(ITEMS.crystalLaserEnergy, 1, 2), "gemOfalenWhite"));
		addShaped(new ItemStack(ITEMS.pistolLaser, 1, 1024), "OOO", "OII", "GC ", 'O', GEM[3], 'I', Items.iron_ingot, 'G', Items.gold_ingot, 'C', CORE[3]);
		RecipeSorter.register("ofalenmod:magazine", MagazineRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
		for (int i = 0; i < 1024; i += 32) {
			for (int j = 1; j < 9; j++) {
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(ITEMS.magazineLaserRed, 1, i), new ItemStack(ITEMS.magazineLaserRed, 1, i + (32 * j)), CRYSTALS[0], j));
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(ITEMS.magazineLaserGreen, 1, i), new ItemStack(ITEMS.magazineLaserGreen, 1, i + (32 * j)), CRYSTALS[1], j));
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(ITEMS.magazineLaserBlue, 1, i), new ItemStack(ITEMS.magazineLaserBlue, 1, i + (32 * j)), CRYSTALS[2], j));
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(ITEMS.magazineLaserWhite, 1, i), new ItemStack(ITEMS.magazineLaserWhite, 1, i + (32 * j)), CRYSTALS[3], j));
			}
		}
		// シールド関連
		addShaped(new ItemStack(ITEMS.partsOfalen, OfalenModConfigCore.amountShieldIngotRecipe, 6), "GOG", "OEO", "GOG", 'G', "ingotGold", 'O', FRAG[0], 'E', "ingotIron");
		addShaped(new ItemStack(ITEMS.shieldOfalen, 1, 576),// TODO レシピ修正
				"IOI", "OEO", "IOI", 'I', "ingotGold", 'O', CORE[0], 'E', new ItemStack(OfalenModItemCore.partsOfalen, 1, 6));
		// テレポーター関連
		addShaped(new ItemStack(ITEMS.partsOfalen, OfalenModConfigCore.amountTeleportPearlRecipe, 7), "GOG", "OEO", "GOG", 'G', "ingotGold", 'O', FRAG[1], 'E', Items.ender_pearl);
		addShaped(ITEMS.teleporterOfalen, "IOI", "OEO", "IOI", 'I', "ingotGold", 'O', CORE[1], 'E', new ItemStack(OfalenModItemCore.partsOfalen, 1, 7));
		addShaped(BLOCKS.markerTeleporting, "GTG", "OCO", "GTG", 'G', "ingotGold", 'T', new ItemStack(OfalenModItemCore.partsOfalen, 1, 7), 'O', GEM[1], 'C', CORE[1]);
		// フローター関連
		addShaped(new ItemStack(ITEMS.partsOfalen, OfalenModConfigCore.amountFloatDustRecipe, 8), "GOG", "ODO", "GOG", 'G', "ingotGold", 'O', FRAG[2], 'D', "dustGlowstone");
		addShaped(new ItemStack(ITEMS.floaterOfalen, 1, 576), "IOI", "OEO", "IOI", 'I', "ingotGold", 'O', CORE[2], 'E', new ItemStack(OfalenModItemCore.partsOfalen, 1, 8));
		// コレクター関連
		addShaped(new ItemStack(ITEMS.partsOfalen, OfalenModConfigCore.amountFloatDustRecipe, 9), "GOG", "ODO", "GOG", 'G', "Gold", 'O', FRAG[2], 'D', "dustGlowstone");// TODO コンフィグ
		addShaped(new ItemStack(ITEMS.collectorOfalen, 1, 8), "IOI", "OEO", "IOI", 'I', "ingotGold", 'O', CORE[2], 'E', new ItemStack(OfalenModItemCore.partsOfalen, 1, 8));
		// フィルター
		// 		addShaped(ITEMS.filterItem,
		// 				"", "", "");
		// オファレン草
		for (int i = 0; i < 4; i++) {
			addShaped(new ItemStack(ITEMS.seedOfalen, 1, i), "SSS", "SOS", "SSS", 'S', Items.wheat_seeds, 'O', GEM[i]);
		}
		// 燃料の登録
		GameRegistry.registerFuelHandler(new IFuelHandler() {
			@Override
			public int getBurnTime(ItemStack fuel) {
				if (fuel.isItemEqual(new ItemStack(ITEMS.gemOfalen, 1, 0))) {
					return 4000;
				}
				return 0;
			}
		});
	}

	private static void addShaped(Block result, Object... recipe) {
		addShaped(new ItemStack(result), recipe);
	}

	private static void addShaped(Item result, Object... recipe) {
		addShaped(new ItemStack(result), recipe);
	}

	private static void addShaped(ItemStack result, Object... recipe) {
		GameRegistry.addRecipe(new ShapedOreRecipe(result, recipe));
	}

	private static void addShapeless(Item result, Object... recipe) {
		addShapeless(new ItemStack(result), recipe);
	}

	private static void addShapeless(ItemStack result, Object... recipe) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(result, recipe));
	}

	private static void addBallRecipe(Object material, Item... balls) {
		for (int i = 0; i < 3; i++) {
			addShapeless(new ItemStack(balls[i]), new ItemStack(ITEMS.ballEmpty, 1, i), material);
		}
		addShaped(new ItemStack(balls[0], 4), " X ", "XYX", " X ", 'X', ITEMS.ballEmpty, 'Y', material);
		addShaped(new ItemStack(balls[1], 4), " X ", "XYX", " X ", 'X', new ItemStack(ITEMS.ballEmpty, 1, 1), 'Y', material);
		addShaped(new ItemStack(balls[2], 4), " X ", "XYX", " X ", 'X', new ItemStack(ITEMS.ballEmpty, 1, 2), 'Y', material);
	}
}
