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

import static nahama.ofalenmod.core.OfalenModBlockCore.*;
import static nahama.ofalenmod.core.OfalenModItemCore.*;

public class OfalenModRecipeCore {
	/** レシピを登録する。 */
	public static void registerRecipe() {
		// 鉱石辞書名を定数化。
		final String[] GEM = { "gemOfalenRed", "gemOfalenGreen", "gemOfalenBlue", "gemOfalenWhite", "gemOfalenOrange", "gemOfalenViridian", "gemOfalenPurple", "gemOfalenDark" };
		final String[] FRAG = { "fragmentOfalenRed", "fragmentOfalenGreen", "fragmentOfalenBlue", "fragmentOfalenWhite", "fragmentOfalenOrange", "fragmentOfalenViridian", "fragmentOfalenPurple", "fragmentOfalenDark" };
		final String[] CORE = { "coreOfalenRed", "coreOfalenGreen", "coreOfalenBlue", "coreOfalenWhite", "coreOfalenOrange", "coreOfalenViridian", "coreOfalenPurple", "coreOfalenDark" };
		final String[] BLOCK = { "blockOfalenRed", "blockOfalenGreen", "blockOfalenBlue", "blockOfalenWhite", "blockOfalenOrange", "blockOfalenViridian", "blockOfalenPurple", "blockOfalenDark" };
		final String INGOT_IRON = "ingotIron";
		final String BLOCK_IRON = "blockIron";
		final String INGOT_GOLD = "ingotGold";
		final String NUGGET_GOLD = "nuggetGold";
		final String GEM_QUARTZ = "gemQuartz";
		final String GEM_DIAMOND = "gemDiamond";
		final String STONE = "stone";
		final String COBBLESTONE = "cobblestone";
		// 石の塊レシピのConfig設定を反映。
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
			addShaped(new ItemStack(blockOfalen, 1, i), "XXX", "XXX", "XXX", 'X', GEM[i]);
			addShaped(new ItemStack(gemOfalen, 1, i), "XXX", "XXX", "XXX", 'X', FRAG[i]);
			addShaped(new ItemStack(gemOfalen, 9, i), "X", 'X', BLOCK[i]);
			addShaped(new ItemStack(fragmentOfalen, 9, i), "X", 'X', GEM[i]);
		}
		addShapeless(new ItemStack(blockOfalen, 3, 3), BLOCK[0], BLOCK[1], BLOCK[2]);
		addShapeless(new ItemStack(coreOfalen, 1, 3), CORE[0], CORE[1], CORE[2]);
		// 中間素材・機械類
		// 鉄の棒
		addShaped(new ItemStack(partsOfalen3D, 1, 0), "X", "X", 'X', INGOT_IRON);
		// 機械用カバープレート
		addShaped(getParts(0), "LQL", "QOQ", "LQL", 'L', getParts(2), 'Q', GEM_QUARTZ, 'O', FRAG[3]);
		// Grade 3の部品
		addShaped(getParts(1), "XYX", "YZY", "XYX", 'X', INGOT_IRON, 'Y', GEM_DIAMOND, 'Z', BLOCK[3]);
		// 製錬機
		addShaped(machineSmelting, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', Blocks.furnace, 'Z', BLOCK[0]);
		// 変換機
		addShaped(machineConverting, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', Blocks.enchanting_table, 'Z', BLOCK[1]);
		// 修繕機
		addShaped(machineRepairing, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', Blocks.anvil, 'Z', BLOCK[2]);
		// 融合機
		addShaped(machineFusing, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', CORE[3], 'Z', BLOCK[3]);
		// 処理装置
		addShaped(processor, "RXG", "YZY", "BXW", 'X', GEM_DIAMOND, 'Y', INGOT_GOLD, 'Z', casingProcessor, 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', GEM[3]);
		addShaped(new ItemStack(processor, 1, 1), "RIG", "SMS", "BIW", 'I', INGOT_IRON, 'S', getParts(2), 'M', processor, 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', GEM[3]);
		addShaped(new ItemStack(processor, 1, 2), "RDG", "IMI", "BOW", 'D', GEM_DIAMOND, 'O', INGOT_GOLD, 'I', INGOT_IRON, 'M', new ItemStack(processor, 1, 1), 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', GEM[3]);
		// 筐体
		addShaped(casingProcessor, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', INGOT_IRON, 'Z', GEM[3]);
		addShaped(new ItemStack(casingProcessor, 1, 1), "SIS", "OCO", "SIS", 'S', getParts(2), 'I', INGOT_IRON, 'O', FRAG[3], 'C', casingProcessor);
		addShaped(new ItemStack(casingProcessor, 1, 2), "IDI", "OCO", "IDI", 'D', GEM_DIAMOND, 'I', INGOT_IRON, 'O', GEM[3], 'C', new ItemStack(casingProcessor, 1, 1));
		// 固定ブロック
		addShaped(new ItemStack(casingProcessor, 1, 4), "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[3], 'Z', BLOCK_IRON);
		addShaped(new ItemStack(casingProcessor, 1, 5), "IOI", "SCS", "IOI", 'I', INGOT_IRON, 'S', getParts(2), 'O', GEM[3], 'C', new ItemStack(casingProcessor, 1, 4));
		addShaped(new ItemStack(casingProcessor, 1, 6), "OIO", "DCD", "OIO", 'D', GEM_DIAMOND, 'I', INGOT_IRON, 'O', GEM[3], 'C', new ItemStack(casingProcessor, 1, 5));
		// 石の塊
		addShaped(getParts(2), recipeArray, 'X', COBBLESTONE);
		addShaped(getParts(2), recipeArray, 'X', STONE);
		addShaped(new ItemStack(Blocks.cobblestone, 8), "X", 'X', getParts(2));
		// 石燃料
		addShaped(getParts(3), " XX", "XXX", "XXX", 'X', getParts(2));
		// オファレン燃料
		addShaped(getParts(4), "XXX", "XYX", "XXX", 'X', getParts(3), 'Y', GEM[0]);
		// 防具
		addShaped(helmetOfalenG1, "XXX", "X X", 'X', GEM[0]);
		addShaped(chestplateOfalenG1, "X X", "XXX", "XXX", 'X', GEM[0]);
		addShaped(leggingsOfalenG1, "XXX", "X X", "X X", 'X', GEM[0]);
		addShaped(bootsOfalenG1, "X X", "X X", 'X', GEM[0]);
		addShaped(helmetOfalenG2, " X ", "YZY", " X ", 'X', GEM[0], 'Y', BLOCK[0], 'Z', helmetOfalenG1);
		addShaped(chestplateOfalenG2, " X ", "YZY", " X ", 'X', GEM[0], 'Y', BLOCK[0], 'Z', chestplateOfalenG1);
		addShaped(leggingsOfalenG2, " X ", "YZY", " X ", 'X', GEM[0], 'Y', BLOCK[0], 'Z', leggingsOfalenG1);
		addShaped(bootsOfalenG2, " X ", "YZY", " X ", 'X', GEM[0], 'Y', BLOCK[0], 'Z', bootsOfalenG1);
		addShaped(helmetOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', getParts(1), 'Y', helmetOfalenG2, 'Z', GEM[0]);
		addShaped(chestplateOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', getParts(1), 'Y', chestplateOfalenG2, 'Z', GEM[0]);
		addShaped(leggingsOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', getParts(1), 'Y', leggingsOfalenG2, 'Z', GEM[0]);
		addShaped(bootsOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', getParts(1), 'Y', bootsOfalenG2, 'Z', GEM[0]);
		addShaped(helmetOfalenP, "BCB", "DAD", "BCB", 'A', helmetOfalenG3, 'B', BLOCK[0], 'C', CORE[0], 'D', CORE[3]);
		addShaped(chestplateOfalenP, "BCB", "DAD", "BCB", 'A', chestplateOfalenG3, 'B', BLOCK[0], 'C', CORE[0], 'D', CORE[3]);
		addShaped(leggingsOfalenP, "BCB", "DAD", "BCB", 'A', leggingsOfalenG3, 'B', BLOCK[0], 'C', CORE[0], 'D', CORE[3]);
		addShaped(bootsOfalenP, "BCB", "DAD", "BCB", 'A', bootsOfalenG3, 'B', BLOCK[0], 'C', CORE[0], 'D', CORE[3]);
		// 玉
		addShaped(new ItemStack(ballEmpty, 4), " X ", "X X", " X ", 'X', GEM[1]);
		addShaped(new ItemStack(ballEmpty, 1, 1), " X ", "XYX", " X ", 'X', GEM[1], 'Y', ballEmpty);
		addShaped(new ItemStack(ballEmpty, 2, 2), " Z ", "XYX", " Z ", 'X', new ItemStack(ballEmpty, 1, 1), 'Y', GEM[1], 'Z', getParts(1));
		addBallRecipe(GEM[0], ballDefenseG1, ballDefenseG2, ballDefenseG3);
		addBallRecipe(GEM[2], ballAttackG1, ballAttackG2, ballAttackG3);
		addBallRecipe(GEM[3], ballRecoveryG1, ballRecoveryG2, ballRecoveryG3);
		addShaped(new ItemStack(ballExplosion, 4), " X ", "XYX", " X ", 'X', ballEmpty, 'Y', Items.gunpowder);
		addShaped(new ItemStack(ballExplosion, 4, 1), " X ", "XYX", " X ", 'X', new ItemStack(ballEmpty, 1, 1), 'Y', Items.gunpowder);
		addShaped(new ItemStack(ballExplosion, 4, 2), " X ", "XYX", " X ", 'X', new ItemStack(ballEmpty, 1, 2), 'Y', Items.gunpowder);
		addShaped(new ItemStack(ballFlame, 4), " X ", "XYX", " X ", 'X', ballEmpty, 'Y', Items.blaze_powder);
		addShaped(new ItemStack(ballFlame, 4, 1), " X ", "XYX", " X ", 'X', new ItemStack(ballEmpty, 1, 1), 'Y', Items.blaze_powder);
		addShaped(new ItemStack(ballFlame, 4, 2), " X ", "XYX", " X ", 'X', new ItemStack(ballEmpty, 1, 2), 'Y', Items.blaze_powder);
		for (int i = 0; i < 3; i++) {
			addShapeless(new ItemStack(ballExplosion, 1, i), new ItemStack(ballEmpty, 1, i), Items.gunpowder);
			addShapeless(new ItemStack(ballFlame, 1, i), new ItemStack(ballEmpty, 1, i), Items.blaze_powder);
		}
		addShapeless(ballHungry, ballEmpty, Items.rotten_flesh);
		addShaped(new ItemStack(ballHungry, 4), " X ", "XYX", " X ", 'X', ballEmpty, 'Y', Items.rotten_flesh);
		addShapeless(ballFood, ballEmpty, INGOT_GOLD);
		addShaped(new ItemStack(ballFood, 4), " X ", "XYX", " X ", 'X', ballEmpty, 'Y', INGOT_GOLD);
		addShaped(ballPerfect, "XAX", "BYC", "XZX", 'A', ballRecoveryG3, 'B', ballDefenseG3, 'C', ballAttackG3, 'X', BLOCK[1], 'Y', CORE[1], 'Z', CORE[3]);
		// 道具
		addShaped(pickaxeOfalenG1, "XXX", " Y ", " Y ", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(shovelOfalenG1, "X", "Y", "Y", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(hoeOfalenG1, "XX", " Y", " Y", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(axeOfalenG1, "XX", "XY", " Y", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(new ItemStack(swordOfalenG1), "X", "X", "Y", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(pickaxeOfalenG2, " X ", "YZY", " X ", 'X', GEM[2], 'Y', BLOCK[2], 'Z', pickaxeOfalenG1);
		addShaped(shovelOfalenG2, " X ", "YZY", " X ", 'X', GEM[2], 'Y', BLOCK[2], 'Z', shovelOfalenG1);
		addShaped(hoeOfalenG2, " X ", "YZY", " X ", 'X', GEM[2], 'Y', BLOCK[2], 'Z', hoeOfalenG1);
		addShaped(axeOfalenG2, " X ", "YZY", " X ", 'X', GEM[2], 'Y', BLOCK[2], 'Z', axeOfalenG1);
		addShaped(swordOfalenG2, " X ", "YZY", " X ", 'X', GEM[2], 'Y', BLOCK[2], 'Z', swordOfalenG1);
		addShaped(pickaxeOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', getParts(1), 'Y', pickaxeOfalenG2, 'Z', GEM[2]);
		addShaped(shovelOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', getParts(1), 'Y', shovelOfalenG2, 'Z', GEM[2]);
		addShaped(hoeOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', getParts(1), 'Y', hoeOfalenG2, 'Z', GEM[2]);
		addShaped(axeOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', getParts(1), 'Y', axeOfalenG2, 'Z', GEM[2]);
		addShaped(swordOfalenG3, "ZXZ", "XYX", "ZXZ", 'X', getParts(1), 'Y', swordOfalenG2, 'Z', GEM[2]);
		addShaped(toolOfalenP, "ABC", "XZX", "DYE", 'A', shovelOfalenG3, 'B', swordOfalenG3, 'C', axeOfalenG3, 'D', pickaxeOfalenG3, 'E', hoeOfalenG3, 'X', CORE[3], 'Y', BLOCK[2], 'Z', CORE[2]);
		// レーザー関連
		addShaped(getParts(5), "X Y", "X Y", "XXX", 'X', GEM[3], 'Y', INGOT_IRON);
		addShapeless(new ItemStack(magazineLaserRed, 1, 1024), getParts(5), GEM[0]);
		addShapeless(new ItemStack(magazineLaserGreen, 1, 1024), getParts(5), GEM[1]);
		addShapeless(new ItemStack(magazineLaserBlue, 1, 1024), getParts(5), GEM[2]);
		addShapeless(new ItemStack(magazineLaserWhite, 1, 1024), getParts(5), GEM[3]);
		for (int i = 0; i < 3; i++) {
			addShaped(new ItemStack(crystalLaserEnergy, 4, i), "XYY", 'X', NUGGET_GOLD, 'Y', FRAG[i]);
		}
		final ItemStack[] CRYSTAL = { new ItemStack(crystalLaserEnergy, 1, 0), new ItemStack(crystalLaserEnergy, 1, 1), new ItemStack(crystalLaserEnergy, 1, 2), new ItemStack(crystalLaserEnergy, 1, 3) };
		GameRegistry.addRecipe(new ShapelessOreRecipe(CRYSTAL[3], CRYSTAL[0], CRYSTAL[1], CRYSTAL[2], GEM[3]));
		addShaped(new ItemStack(pistolLaser, 1, 1024), "OOO", "OII", "GC ", 'O', GEM[3], 'I', INGOT_IRON, 'G', INGOT_GOLD, 'C', CORE[3]);
		// マガジンへのクリスタル格納レシピ
		RecipeSorter.register("ofalenmod:magazine", MagazineRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
		for (int i = 0; i < 1024; i += 32) {
			for (int j = 1; j < 9; j++) {
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(magazineLaserRed, 1, i), new ItemStack(magazineLaserRed, 1, i + (32 * j)), CRYSTAL[0], j));
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(magazineLaserGreen, 1, i), new ItemStack(magazineLaserGreen, 1, i + (32 * j)), CRYSTAL[1], j));
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(magazineLaserBlue, 1, i), new ItemStack(magazineLaserBlue, 1, i + (32 * j)), CRYSTAL[2], j));
				GameRegistry.addRecipe(new MagazineRecipe(new ItemStack(magazineLaserWhite, 1, i), new ItemStack(magazineLaserWhite, 1, i + (32 * j)), CRYSTAL[3], j));
			}
		}
		// シールド関連
		addShaped(new ItemStack(partsOfalen, OfalenModConfigCore.amountShieldIngotRecipe, 6), "GOG", "OEO", "GOG", 'G', INGOT_GOLD, 'O', FRAG[0], 'E', INGOT_IRON);
		addShaped(new ItemStack(shieldOfalen, 1, shieldOfalen.getMaxDamage()), "IOI", "OEO", "IOI", 'I', INGOT_GOLD, 'O', CORE[0], 'E', getParts(6));
		// テレポーター関連
		addShaped(new ItemStack(partsOfalen, OfalenModConfigCore.amountTeleportPearlRecipe, 7), "GOG", "OEO", "GOG", 'G', INGOT_GOLD, 'O', FRAG[1], 'E', Items.ender_pearl);
		addShaped(teleporterOfalen, "IOI", "OEO", "IOI", 'I', INGOT_GOLD, 'O', CORE[1], 'E', getParts(7));
		addShaped(markerTeleporting, "GTG", "OCO", "GTG", 'G', INGOT_GOLD, 'T', getParts(7), 'O', GEM[1], 'C', CORE[1]);
		// フローター関連
		addShaped(new ItemStack(partsOfalen, OfalenModConfigCore.amountFloatDustRecipe, 8), "GOG", "ODO", "GOG", 'G', INGOT_GOLD, 'O', FRAG[2], 'D', "dustGlowstone");
		addShaped(new ItemStack(floaterOfalen, 1, floaterOfalen.getMaxDamage()), "IOI", "OEO", "IOI", 'I', INGOT_GOLD, 'O', CORE[2], 'E', getParts(8));
		// コレクター関連
		addShaped(new ItemStack(partsOfalen, OfalenModConfigCore.amountCollectingLampRecipe, 9), "GOG", "ODO", "GOG", 'G', INGOT_GOLD, 'O', FRAG[7], 'D', getParts(2));
		addShaped(new ItemStack(collectorOfalen, 1, collectorOfalen.getMaxDamage()), "IOI", "OEO", "IOI", 'I', INGOT_GOLD, 'O', CORE[7], 'E', getParts(9));
		// フィルター関連
		addShaped(installerFilter, "XYX", "YZY", "XYX", 'X', getParts(2), 'Y', Items.string, 'Z', Blocks.hopper);
		addShaped(filterItem, "XYX", "YZY", "XYX", 'X', getParts(2), 'Y', Items.string, 'Z', Blocks.chest);
		// オファレン草
		for (int i = 0; i < 4; i++) {
			addShaped(new ItemStack(seedOfalen, 1, i), "SSS", "SOS", "SSS", 'S', Items.wheat_seeds, 'O', GEM[i]);
		}
		// 燃料の登録
		GameRegistry.registerFuelHandler(new IFuelHandler() {
			@Override
			public int getBurnTime(ItemStack fuel) {
				if (fuel.isItemEqual(new ItemStack(gemOfalen, 1, 0))) {
					return 4000;
				}
				return 0;
			}
		});
	}

	/** @return new ItemStack({@link OfalenModItemCore#partsOfalen}, 1, meta) */
	private static ItemStack getParts(int meta) {
		return new ItemStack(partsOfalen, 1, meta);
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
			addShapeless(new ItemStack(balls[i]), new ItemStack(ballEmpty, 1, i), material);
		}
		addShaped(new ItemStack(balls[0], 4), " X ", "XYX", " X ", 'X', ballEmpty, 'Y', material);
		addShaped(new ItemStack(balls[1], 4), " X ", "XYX", " X ", 'X', new ItemStack(ballEmpty, 1, 1), 'Y', material);
		addShaped(new ItemStack(balls[2], 4), " X ", "XYX", " X ", 'X', new ItemStack(ballEmpty, 1, 2), 'Y', material);
	}
}
