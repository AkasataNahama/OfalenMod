package nahama.ofalenmod.core;

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
		final String DUST_GLOWSTONE = "dustGlowstone";
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
		addShapeless(new ItemStack(blockOfalen, 1, 3), BLOCK[0], BLOCK[1], BLOCK[2]);
		// 中間素材・機械類
		// オファレンの棒
		addShaped(new ItemStack(partsOfalen3D, 1, 0), "W", "W", 'W', GEM[3]);
		// 機械用カバープレート
		addShaped(new ItemStack(partsOfalen, 6, 0), "LOL", "OQO", "LOL", 'L', getParts(2), 'Q', GEM_QUARTZ, 'O', FRAG[3]);
		// Grade 3の部品
		addShaped(getParts(1), "DWD", "WIW", "DID", 'W', GEM[3], 'D', GEM_DIAMOND, 'I', INGOT_IRON);
		// 製錬機
		addShaped(machineSmelting, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[0], 'Z', Blocks.furnace);
		// 変換機
		addShaped(machineConverting, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[1], 'Z', Blocks.enchanting_table);
		// 修繕機
		addShaped(machineRepairing, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[2], 'Z', Blocks.anvil);
		// 融合機
		addShaped(machineFusing, "XRX", "XGX", "XBX", 'X', getParts(0), 'R', CORE[0], 'G', CORE[1], 'B', CORE[2]);
		// 処理装置
		addShaped(processor, "RXG", "YMY", "BXW", 'X', INGOT_IRON, 'Y', INGOT_GOLD, 'M', new ItemStack(casingProcessor), 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', GEM[3]);
		addShaped(new ItemStack(processor, 1, 1), "RXG", "YMY", "BZW", 'X', INGOT_IRON, 'Y', INGOT_GOLD, 'Z', GEM_DIAMOND, 'M', new ItemStack(processor), 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', GEM[3]);
		addShaped(new ItemStack(processor, 1, 2), "RZG", "YMY", "BZW", 'Y', INGOT_GOLD, 'Z', GEM_DIAMOND, 'M', new ItemStack(processor, 1, 1), 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', GEM[3]);
		// 筐体
		addShaped(casingProcessor, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', INGOT_IRON, 'Z', GEM[3]);
		addShaped(new ItemStack(casingProcessor, 1, 1), "LIL", "OMO", "LIL", 'L', getParts(2), 'I', INGOT_IRON, 'O', GEM[3], 'M', new ItemStack(casingProcessor));
		addShaped(new ItemStack(casingProcessor, 1, 2), "OIO", "DMD", "OIO", 'D', GEM_DIAMOND, 'I', INGOT_IRON, 'O', GEM[3], 'M', new ItemStack(casingProcessor, 1, 1));
		// 固定ブロック
		addShaped(new ItemStack(casingProcessor, 1, 4), "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[3], 'Z', BLOCK_IRON);
		addShaped(new ItemStack(casingProcessor, 1, 5), "LIL", "OMO", "LIL", 'L', getParts(2), 'I', INGOT_IRON, 'O', GEM[3], 'M', new ItemStack(casingProcessor, 1, 4));
		addShaped(new ItemStack(casingProcessor, 1, 6), "OIO", "DMD", "OIO", 'D', GEM_DIAMOND, 'I', INGOT_IRON, 'O', GEM[3], 'M', new ItemStack(casingProcessor, 1, 5));
		// 石の塊
		addShaped(getParts(2), recipeArray, 'X', COBBLESTONE);
		addShaped(getParts(2), recipeArray, 'X', STONE);
		addShaped(new ItemStack(Blocks.cobblestone, 8), "X", 'X', getParts(2));
		// オファレン燃料
		addShaped(new ItemStack(partsOfalen, 32, 3), "OLO", "LQL", "OLO", 'L', getParts(2), 'O', GEM[3], 'Q', GEM_QUARTZ);
		addShaped(new ItemStack(partsOfalen, 32, 4), "OLO", "LQL", "OLO", 'L', getParts(2), 'O', GEM[7], 'Q', GEM_QUARTZ);
		// 防具
		addShaped(helmetOfalenG1, "RRR", "R R", 'R', GEM[0]);
		addShaped(chestplateOfalenG1, "R R", "RRR", "RRR", 'R', GEM[0]);
		addShaped(leggingsOfalenG1, "RRR", "R R", "R R", 'R', GEM[0]);
		addShaped(bootsOfalenG1, "R R", "R R", 'R', GEM[0]);
		addShaped(helmetOfalenG2, "RRR", "RXR", "RRR", 'R', GEM[0], 'X', helmetOfalenG1);
		addShaped(chestplateOfalenG2, "RRR", "RXR", "RRR", 'R', GEM[0], 'X', chestplateOfalenG1);
		addShaped(leggingsOfalenG2, "RRR", "RXR", "RRR", 'R', GEM[0], 'X', leggingsOfalenG1);
		addShaped(bootsOfalenG2, "RRR", "RXR", "RRR", 'R', GEM[0], 'X', bootsOfalenG1);
		addShaped(helmetOfalenG3, "RPR", "PXP", "RPR", 'P', getParts(1), 'R', GEM[0], 'X', helmetOfalenG2);
		addShaped(chestplateOfalenG3, "RPR", "PXP", "RPR", 'P', getParts(1), 'R', GEM[0], 'X', chestplateOfalenG2);
		addShaped(leggingsOfalenG3, "RPR", "PXP", "RPR", 'P', getParts(1), 'R', GEM[0], 'X', leggingsOfalenG2);
		addShaped(bootsOfalenG3, "RPR", "PXP", "RPR", 'P', getParts(1), 'R', GEM[0], 'X', bootsOfalenG2);
		addShaped(helmetOfalenP, " R ", "WXW", " R ", 'R', CORE[0], 'W', CORE[3], 'X', helmetOfalenG3);
		addShaped(chestplateOfalenP, " R ", "WXW", " R ", 'R', CORE[0], 'W', CORE[3], 'X', chestplateOfalenG3);
		addShaped(leggingsOfalenP, " R ", "WXW", " R ", 'R', CORE[0], 'W', CORE[3], 'X', leggingsOfalenG3);
		addShaped(bootsOfalenP, " R ", "WXW", " R ", 'R', CORE[0], 'W', CORE[3], 'X', bootsOfalenG3);
		// 玉
		addShaped(new ItemStack(ballEmpty, 4), " G ", "G G", " G ", 'G', GEM[1]);
		addShaped(new ItemStack(ballEmpty, 4, 1), "GXG", "X X", "GXG", 'G', GEM[1], 'X', ballEmpty);
		addShaped(new ItemStack(ballEmpty, 4, 2), "XGX", "P P", "XGX", 'P', getParts(1), 'G', GEM[1], 'X', new ItemStack(ballEmpty, 1, 1));
		addGradedBallRecipe(GEM[0], ballDefenseG1, ballDefenseG2, ballDefenseG3);
		addGradedBallRecipe(GEM[2], ballAttackG1, ballAttackG2, ballAttackG3);
		addGradedBallRecipe(GEM[3], ballRecoveryG1, ballRecoveryG2, ballRecoveryG3);
		for (int i = 0; i < 3; i++) {
			addBallRecipe(new ItemStack(ballExplosion, 1, i), Items.gunpowder, i);
		}
		addSingleBallRecipe(ballHungry, Items.rotten_flesh);
		addSingleBallRecipe(ballFood, INGOT_GOLD);
		addShaped(ballPerfect, "XWX", "R B", "YGY", 'X', CORE[1], 'Y', CORE[3], 'W', ballRecoveryG3, 'R', ballDefenseG3, 'G', new ItemStack(ballEmpty, 1, 2), 'B', ballAttackG3);
		// 道具
		addShaped(swordOfalenG1, "X", "X", "Y", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(shovelOfalenG1, "X", "Y", "Y", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(pickaxeOfalenG1, "XXX", " Y ", " Y ", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(axeOfalenG1, "XX", "XY", " Y", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(hoeOfalenG1, "XX", " Y", " Y", 'X', GEM[2], 'Y', new ItemStack(partsOfalen3D, 1, 0));
		addShaped(swordOfalenG2, "BBB", "BXB", "BBB", 'B', GEM[2], 'X', swordOfalenG1);
		addShaped(shovelOfalenG2, "BBB", "BXB", "BBB", 'B', GEM[2], 'X', shovelOfalenG1);
		addShaped(pickaxeOfalenG2, "BBB", "BXB", "BBB", 'B', GEM[2], 'X', pickaxeOfalenG1);
		addShaped(axeOfalenG2, "BBB", "BXB", "BBB", 'B', GEM[2], 'X', axeOfalenG1);
		addShaped(hoeOfalenG2, "BBB", "BXB", "BBB", 'B', GEM[2], 'X', hoeOfalenG1);
		addShaped(swordOfalenG3, "BPB", "PXP", "BPB", 'P', getParts(1), 'B', GEM[2], 'X', swordOfalenG2);
		addShaped(shovelOfalenG3, "BPB", "PXP", "BPB", 'P', getParts(1), 'B', GEM[2], 'X', shovelOfalenG2);
		addShaped(pickaxeOfalenG3, "BPB", "PXP", "BPB", 'P', getParts(1), 'B', GEM[2], 'X', pickaxeOfalenG2);
		addShaped(axeOfalenG3, "BPB", "PXP", "BPB", 'P', getParts(1), 'B', GEM[2], 'X', axeOfalenG2);
		addShaped(hoeOfalenG3, "BPB", "PXP", "BPB", 'P', getParts(1), 'B', GEM[2], 'X', hoeOfalenG2);
		addShaped(toolOfalenP, "ABC", "DXE", " Y ", 'A', shovelOfalenG3, 'B', swordOfalenG3, 'C', pickaxeOfalenG3, 'D', axeOfalenG3, 'E', hoeOfalenG3, 'X', CORE[2], 'Y', CORE[3]);
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
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(crystalLaserEnergy, 3, 3), CRYSTAL[0], CRYSTAL[1], CRYSTAL[2], GEM[3]));
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
		addShaped(new ItemStack(partsOfalen, OfalenModConfigCore.amountShieldIngotRecipe, 6), "GOG", "OMO", "GOG", 'G', NUGGET_GOLD, 'O', FRAG[0], 'M', INGOT_IRON);
		addShaped(new ItemStack(shieldOfalen, 1, shieldOfalen.getMaxDamage()), "IOI", "OEO", "IOI", 'I', INGOT_GOLD, 'O', CORE[0], 'E', getParts(6));
		// テレポーター関連
		addShaped(new ItemStack(partsOfalen, OfalenModConfigCore.amountTeleportPearlRecipe, 7), "GOG", "OMO", "GOG", 'G', NUGGET_GOLD, 'O', FRAG[1], 'M', Items.ender_pearl);
		addShaped(teleporterOfalen, "IOI", "OEO", "IOI", 'I', INGOT_GOLD, 'O', CORE[1], 'E', getParts(7));
		addShaped(markerTeleporting, "GTG", "OCO", "GTG", 'G', INGOT_GOLD, 'T', getParts(7), 'O', GEM[1], 'C', CORE[1]);
		// フローター関連
		addShaped(new ItemStack(partsOfalen, OfalenModConfigCore.amountFloatDustRecipe, 8), "GOG", "OMO", "GOG", 'G', NUGGET_GOLD, 'O', FRAG[2], 'M', DUST_GLOWSTONE);
		addShaped(new ItemStack(floaterOfalen, 1, floaterOfalen.getMaxDamage()), "IOI", "OEO", "IOI", 'I', INGOT_GOLD, 'O', CORE[2], 'E', getParts(8));
		// コレクター関連
		addShaped(new ItemStack(partsOfalen, OfalenModConfigCore.amountCollectingLampRecipe, 9), "GOG", "OMO", "GOG", 'G', NUGGET_GOLD, 'O', FRAG[7], 'M', getParts(2));
		addShaped(new ItemStack(collectorOfalen, 1, collectorOfalen.getMaxDamage()), "IOI", "OEO", "IOI", 'I', INGOT_GOLD, 'O', CORE[7], 'E', getParts(9));
		// フィルター関連
		addShaped(installerFilter, "XYX", "YZY", "XYX", 'X', getParts(2), 'Y', Items.string, 'Z', Blocks.hopper);
		addShaped(filterItem, "XYX", "YZY", "XYX", 'X', getParts(2), 'Y', Items.string, 'Z', Blocks.chest);
		// オファレン草
		for (int i = 0; i < 4; i++) {
			addShaped(new ItemStack(seedsOfalen, 1, i), "SSS", "SOS", "SSS", 'S', Items.wheat_seeds, 'O', GEM[i]);
		}
		// 世界系
		addShaped(placerOfalen, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[4], 'Z', BLOCK[4]);
		addShaped(moverOfalen, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[5], 'Z', BLOCK[5]);
		addShaped(breakerOfalen, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[6], 'Z', BLOCK[6]);
		addShaped(blockCollectorOfalen, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[7], 'Z', BLOCK[7]);
		// NBT初期化レシピ
		addShaped(placerOfalen, "X", 'X', new ItemStack(placerOfalen));
		addShaped(moverOfalen, "X", 'X', new ItemStack(moverOfalen));
		addShaped(breakerOfalen, "X", 'X', new ItemStack(breakerOfalen));
		addShaped(blockCollectorOfalen, "X", 'X', new ItemStack(blockCollectorOfalen));
		// 測量器
		addShaped(surveyorOfalen, "DRD", "OWP", "GVB", 'R', GEM[0], 'G', GEM[1], 'B', GEM[2], 'W', BLOCK[3], 'O', GEM[4], 'V', GEM[5], 'P', GEM[6], 'D', GEM[7]);
		// 測量杖
		addShaped(caneSurveying, "  B", " S ", "S  ", 'S', Items.stick, 'B', surveyorOfalen);
		// 詳細設定器
		addShaped(setterDetailed, "XYX", "XZX", "XYX", 'X', getParts(0), 'Y', GEM[7], 'Z', Blocks.crafting_table);
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

	private static void addBallRecipe(ItemStack ball, Object material, int grade) {
		ball = ball.copy();
		ball.stackSize = 1;
		addShapeless(ball, new ItemStack(ballEmpty, 1, grade), material);
		ball = ball.copy();
		ball.stackSize = 4;
		addShaped(ball, " X ", "XYX", " X ", 'X', new ItemStack(ballEmpty, 1, grade), 'Y', material);
	}

	private static void addGradedBallRecipe(Object material, Item... balls) {
		for (int i = 0; i < 3; i++) {
			addBallRecipe(new ItemStack(balls[i]), material, i);
		}
	}

	private static void addSingleBallRecipe(Item ball, Object material) {
		addBallRecipe(new ItemStack(ball), material, 0);
	}
}
