package nahama.ofalenmod.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.item.*;
import nahama.ofalenmod.item.armor.ItemOfalenArmor;
import nahama.ofalenmod.item.tool.ItemOfalenAxe;
import nahama.ofalenmod.item.tool.ItemOfalenPerfectTool;
import nahama.ofalenmod.item.tool.ItemOfalenPickaxe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

public class OfalenModItemCore {
	// アーマーマテリアル
	private static final ArmorMaterial OFALEN_ARMOR_G2 = EnumHelper.addArmorMaterial("OFALEN_ARMOR_G2", 66, new int[] { 3, 9, 7, 3 }, 20);
	private static final ArmorMaterial OFALEN_ARMOR_G3 = EnumHelper.addArmorMaterial("OFALEN_ARMOR_G3", 132, new int[] { 4, 10, 7, 3 }, 40);
	private static final ArmorMaterial OFALEN_ARMOR_P = EnumHelper.addArmorMaterial("OFALEN_ARMOR_P", 264, new int[] { 4, 10, 7, 4 }, 80);
	// ツールマテリアル
	private static final ToolMaterial OFALEN_TOOL_G2 = EnumHelper.addToolMaterial("OFALEN_TOOL_G2", 4, 3123, 16.0F, 6.0F, 20);
	private static final ToolMaterial OFALEN_TOOL_G3 = EnumHelper.addToolMaterial("OFALEN_TOOL_G3", 4, 6247, 32.0F, 12.0F, 40);
	private static final ToolMaterial OFALEN_TOOL_P = EnumHelper.addToolMaterial("OFALEN_TOOL_P", 5, 12495, 64.0F, 24.0F, 80);
	private static final ToolMaterial OFALEN_CREATIVE_SWORD = EnumHelper.addToolMaterial("OFALEN_CREATIVE_SWORD", 0, 0, 0, 0, 0);
	// 基本アイテム
	public static Item gemOfalen;
	public static Item fragmentOfalen;
	public static Item coreOfalen;
	/**
	 * 0 : Machine Cover Plate<br>
	 * 1 : Grade 3 Part<br>
	 * 2 : Lump of Stone<br>
	 * 3 : White Ofalen Fuel<br>
	 * 4 : Black Ofalen Fuel<br>
	 * 5 : Laser Magazine<br>
	 * 6 : Protecting Ingot<br>
	 * 7 : Teleporting Pearl<br>
	 * 8 : Floating Dust<br>
	 * 9 : Collecting Lump<br>
	 */
	public static Item partsOfalen;
	/** 0 : White Ofalen Stick */
	public static Item partsOfalen3D;
	// 防具G1
	public static Item helmetOfalenG1;
	public static Item chestplateOfalenG1;
	public static Item leggingsOfalenG1;
	public static Item bootsOfalenG1;
	// 防具G2
	public static Item helmetOfalenG2;
	public static Item chestplateOfalenG2;
	public static Item leggingsOfalenG2;
	public static Item bootsOfalenG2;
	// 防具G3
	public static Item helmetOfalenG3;
	public static Item chestplateOfalenG3;
	public static Item leggingsOfalenG3;
	public static Item bootsOfalenG3;
	// 防具P
	public static Item helmetOfalenP;
	public static Item chestplateOfalenP;
	public static Item leggingsOfalenP;
	public static Item bootsOfalenP;
	// 玉
	public static Item ballEmpty;
	// 防御玉
	public static Item ballDefenseG1;
	public static Item ballDefenseG2;
	public static Item ballDefenseG3;
	// 攻撃玉
	public static Item ballAttackG1;
	public static Item ballAttackG2;
	public static Item ballAttackG3;
	// 回復玉
	public static Item ballRecoveryG1;
	public static Item ballRecoveryG2;
	public static Item ballRecoveryG3;
	// 耐火玉
	public static Item ballFireproofG1;
	public static Item ballFireproofG2;
	public static Item ballFireproofG3;
	// 採掘玉
	public static Item ballMiningG1;
	public static Item ballMiningG2;
	public static Item ballMiningG3;
	// 暗視玉
	public static Item ballNightVisionG1;
	public static Item ballNightVisionG2;
	public static Item ballNightVisionG3;
	// 透明玉
	public static Item ballTransparentG1;
	public static Item ballTransparentG2;
	public static Item ballTransparentG3;
	// 満腹玉
	public static Item ballFoodG1;
	public static Item ballFoodG2;
	public static Item ballFoodG3;
	// その他の玉
	public static Item ballExplosion;
	public static Item ballHungry;
	public static Item ballPerfect;
	// 道具G1
	public static Item swordOfalenG1;
	public static Item shovelOfalenG1;
	public static Item pickaxeOfalenG1;
	public static Item axeOfalenG1;
	public static Item hoeOfalenG1;
	// 道具G2
	public static Item swordOfalenG2;
	public static Item shovelOfalenG2;
	public static Item pickaxeOfalenG2;
	public static Item axeOfalenG2;
	public static Item hoeOfalenG2;
	// 道具G3
	public static Item swordOfalenG3;
	public static Item shovelOfalenG3;
	public static Item pickaxeOfalenG3;
	public static Item axeOfalenG3;
	public static Item hoeOfalenG3;
	// その他の道具
	public static Item toolOfalenP;
	@SuppressWarnings("WeakerAccess")
	public static Item swordCreative;
	// レーザー関連
	public static Item pistolLaser;
	public static Item crystalLaserEnergy;
	public static Item magazineLaserRed;
	public static Item magazineLaserGreen;
	public static Item magazineLaserBlue;
	public static Item magazineLaserWhite;
	// 未来系
	public static Item protectorOfalen;
	public static Item teleporterOfalen;
	public static Item floaterOfalen;
	public static Item collectorOfalen;
	// フィルター
	public static Item filterItem;
	public static Item installerFilter;
	// オファレン草
	public static Item seedsOfalen;
	// 測量杖
	public static Item caneSurveying;
	// 経験値の結晶
	public static Item crystalExp;

	/** アイテムを登録する。 */
	public static void registerItem() {
		CreativeTabs tab = OfalenModCore.TAB_OFALEN;
		// オファレン
		gemOfalen = new ItemOfalen().setUnlocalizedName("ofalen.gem").setTextureName("ofalenmod:gem");
		GameRegistry.registerItem(gemOfalen, "ofalen");
		OreDictionary.registerOre("gemOfalenRed", new ItemStack(gemOfalen, 1, 0));
		OreDictionary.registerOre("gemOfalenGreen", new ItemStack(gemOfalen, 1, 1));
		OreDictionary.registerOre("gemOfalenBlue", new ItemStack(gemOfalen, 1, 2));
		OreDictionary.registerOre("gemOfalenWhite", new ItemStack(gemOfalen, 1, 3));
		OreDictionary.registerOre("gemOfalenOrange", new ItemStack(gemOfalen, 1, 4));
		OreDictionary.registerOre("gemOfalenViridian", new ItemStack(gemOfalen, 1, 5));
		OreDictionary.registerOre("gemOfalenPurple", new ItemStack(gemOfalen, 1, 6));
		OreDictionary.registerOre("gemOfalenDark", new ItemStack(gemOfalen, 1, 7));
		OreDictionary.registerOre("gemOfalen", new ItemStack(gemOfalen, 1, OreDictionary.WILDCARD_VALUE));
		// オファレンの欠片
		fragmentOfalen = new ItemOfalen().setUnlocalizedName("ofalen.fragment").setTextureName("ofalenmod:fragment");
		GameRegistry.registerItem(fragmentOfalen, "fragmentOfalen");
		OreDictionary.registerOre("fragmentOfalenRed", new ItemStack(fragmentOfalen, 1, 0));
		OreDictionary.registerOre("fragmentOfalenGreen", new ItemStack(fragmentOfalen, 1, 1));
		OreDictionary.registerOre("fragmentOfalenBlue", new ItemStack(fragmentOfalen, 1, 2));
		OreDictionary.registerOre("fragmentOfalenWhite", new ItemStack(fragmentOfalen, 1, 3));
		OreDictionary.registerOre("fragmentOfalenOrange", new ItemStack(fragmentOfalen, 1, 4));
		OreDictionary.registerOre("fragmentOfalenViridian", new ItemStack(fragmentOfalen, 1, 5));
		OreDictionary.registerOre("fragmentOfalenPurple", new ItemStack(fragmentOfalen, 1, 6));
		OreDictionary.registerOre("fragmentOfalenDark", new ItemStack(fragmentOfalen, 1, 7));
		OreDictionary.registerOre("fragmentOfalen", new ItemStack(fragmentOfalen, 1, OreDictionary.WILDCARD_VALUE));
		// オファレンコア
		coreOfalen = new ItemOfalen().setUnlocalizedName("ofalen.core").setTextureName("ofalenmod:core");
		GameRegistry.registerItem(coreOfalen, "coreOfalen");
		OreDictionary.registerOre("coreOfalenRed", new ItemStack(coreOfalen, 1, 0));
		OreDictionary.registerOre("coreOfalenGreen", new ItemStack(coreOfalen, 1, 1));
		OreDictionary.registerOre("coreOfalenBlue", new ItemStack(coreOfalen, 1, 2));
		OreDictionary.registerOre("coreOfalenWhite", new ItemStack(coreOfalen, 1, 3));
		OreDictionary.registerOre("coreOfalenOrange", new ItemStack(coreOfalen, 1, 4));
		OreDictionary.registerOre("coreOfalenViridian", new ItemStack(coreOfalen, 1, 5));
		OreDictionary.registerOre("coreOfalenPurple", new ItemStack(coreOfalen, 1, 6));
		OreDictionary.registerOre("coreOfalenDark", new ItemStack(coreOfalen, 1, 7));
		// オファレンコアは色により価値が違う。
		//		OreDictionary.registerOre("coreOfalen", new ItemStack(coreOfalen, 1, OreDictionary.WILDCARD_VALUE));
		// 中間素材
		partsOfalen = new ItemParts((byte) 10).setUnlocalizedName("ofalen.parts").setTextureName("ofalenmod:parts");
		GameRegistry.registerItem(partsOfalen, "partsOfalen");
		partsOfalen3D = new ItemParts((byte) 1).setUnlocalizedName("ofalen.parts3D").setTextureName("ofalenmod:parts3D").setFull3D();
		GameRegistry.registerItem(partsOfalen3D, "partsOfalen3D");
		// 防具G1
		helmetOfalenG1 = new ItemOfalenArmor(ArmorMaterial.DIAMOND, 0, 1).setUnlocalizedName("ofalen.armor.G1.0").setTextureName("ofalenmod:armor-1-0");
		GameRegistry.registerItem(helmetOfalenG1, "helmetOfalen");
		chestplateOfalenG1 = new ItemOfalenArmor(ArmorMaterial.DIAMOND, 1, 1).setUnlocalizedName("ofalen.armor.G1.1").setTextureName("ofalenmod:armor-1-1");
		GameRegistry.registerItem(chestplateOfalenG1, "chestplateOfalen");
		leggingsOfalenG1 = new ItemOfalenArmor(ArmorMaterial.DIAMOND, 2, 1).setUnlocalizedName("ofalen.armor.G1.2").setTextureName("ofalenmod:armor-1-2");
		GameRegistry.registerItem(leggingsOfalenG1, "leggingsOfalen");
		bootsOfalenG1 = new ItemOfalenArmor(ArmorMaterial.DIAMOND, 3, 1).setUnlocalizedName("ofalen.armor.G1.3").setTextureName("ofalenmod:armor-1-3");
		GameRegistry.registerItem(bootsOfalenG1, "bootsOfalen");
		// 防具G2
		helmetOfalenG2 = new ItemOfalenArmor(OFALEN_ARMOR_G2, 0, 2).setUnlocalizedName("ofalen.armor.G2.0").setTextureName("ofalenmod:armor-2-0");
		GameRegistry.registerItem(helmetOfalenG2, "helmetOfalenG2");
		chestplateOfalenG2 = new ItemOfalenArmor(OFALEN_ARMOR_G2, 1, 2).setUnlocalizedName("ofalen.armor.G2.1").setTextureName("ofalenmod:armor-2-1");
		GameRegistry.registerItem(chestplateOfalenG2, "chestplateOfalenG2");
		leggingsOfalenG2 = new ItemOfalenArmor(OFALEN_ARMOR_G2, 2, 2).setUnlocalizedName("ofalen.armor.G2.2").setTextureName("ofalenmod:armor-2-2");
		GameRegistry.registerItem(leggingsOfalenG2, "leggingsOfalenG2");
		bootsOfalenG2 = new ItemOfalenArmor(OFALEN_ARMOR_G2, 3, 2).setUnlocalizedName("ofalen.armor.G2.3").setTextureName("ofalenmod:armor-2-3");
		GameRegistry.registerItem(bootsOfalenG2, "bootsOfalenG2");
		// 防具G3
		helmetOfalenG3 = new ItemOfalenArmor(OFALEN_ARMOR_G3, 0, 3).setUnlocalizedName("ofalen.armor.G3.0").setTextureName("ofalenmod:armor-3-0");
		GameRegistry.registerItem(helmetOfalenG3, "helmetOfalenG3");
		chestplateOfalenG3 = new ItemOfalenArmor(OFALEN_ARMOR_G3, 1, 3).setUnlocalizedName("ofalen.armor.G3.1").setTextureName("ofalenmod:armor-3-1");
		GameRegistry.registerItem(chestplateOfalenG3, "chestplateOfalenG3");
		leggingsOfalenG3 = new ItemOfalenArmor(OFALEN_ARMOR_G3, 2, 3).setUnlocalizedName("ofalen.armor.G3.2").setTextureName("ofalenmod:armor-3-2");
		GameRegistry.registerItem(leggingsOfalenG3, "leggingsOfalenG3");
		bootsOfalenG3 = new ItemOfalenArmor(OFALEN_ARMOR_G3, 3, 3).setUnlocalizedName("ofalen.armor.G3.3").setTextureName("ofalenmod:armor-3-3");
		GameRegistry.registerItem(bootsOfalenG3, "bootsOfalenG3");
		// 防具P
		helmetOfalenP = new ItemOfalenArmor(OFALEN_ARMOR_P, 0, 4).setUnlocalizedName("ofalen.armor.P.0").setTextureName("ofalenmod:armor-P-0");
		GameRegistry.registerItem(helmetOfalenP, "helmetPerfectOfalen");
		chestplateOfalenP = new ItemOfalenArmor(OFALEN_ARMOR_P, 1, 4).setUnlocalizedName("ofalen.armor.P.1").setTextureName("ofalenmod:armor-P-1");
		GameRegistry.registerItem(chestplateOfalenP, "chestplatePerfectOfalen");
		leggingsOfalenP = new ItemOfalenArmor(OFALEN_ARMOR_P, 2, 4).setUnlocalizedName("ofalen.armor.P.2").setTextureName("ofalenmod:armor-P-2");
		GameRegistry.registerItem(leggingsOfalenP, "leggingsPerfectOfalen");
		bootsOfalenP = new ItemOfalenArmor(OFALEN_ARMOR_P, 3, 4).setUnlocalizedName("ofalen.armor.P.3").setTextureName("ofalenmod:armor-P-3");
		GameRegistry.registerItem(bootsOfalenP, "bootsPerfectOfalen");
		// 玉
		ballEmpty = new ItemEmptyBall().setUnlocalizedName("ofalen.ball.empty").setTextureName("ofalenmod:ball");
		GameRegistry.registerItem(ballEmpty, "ballEmpty");
		// 防御玉
		ballDefenseG1 = new ItemOfalenBall(new PotionEffect(Potion.resistance.id, 2400, 0)).setUnlocalizedName("ofalen.ball.defense.G1").setTextureName("ofalenmod:ball-1-0");
		GameRegistry.registerItem(ballDefenseG1, "ballDefense");
		ballDefenseG2 = new ItemOfalenBall(new PotionEffect(Potion.resistance.id, 2400, 1)).setUnlocalizedName("ofalen.ball.defense.G2").setTextureName("ofalenmod:ball-2-0");
		GameRegistry.registerItem(ballDefenseG2, "ballDefenseG2");
		ballDefenseG3 = new ItemOfalenBall(new PotionEffect(Potion.resistance.id, 2400, 3)).setUnlocalizedName("ofalen.ball.defense.G3").setTextureName("ofalenmod:ball-3-0");
		GameRegistry.registerItem(ballDefenseG3, "ballDefenseG3");
		// 攻撃玉
		ballAttackG1 = new ItemOfalenBall(new PotionEffect(Potion.damageBoost.id, 2400, 0)).setUnlocalizedName("ofalen.ball.attack.G1").setTextureName("ofalenmod:ball-1-2");
		GameRegistry.registerItem(ballAttackG1, "ballAttack");
		ballAttackG2 = new ItemOfalenBall(new PotionEffect(Potion.damageBoost.id, 2400, 1)).setUnlocalizedName("ofalen.ball.attack.G2").setTextureName("ofalenmod:ball-2-2");
		GameRegistry.registerItem(ballAttackG2, "ballAttackG2");
		ballAttackG3 = new ItemOfalenBall(new PotionEffect(Potion.damageBoost.id, 2400, 3)).setUnlocalizedName("ofalen.ball.attack.G3").setTextureName("ofalenmod:ball-3-2");
		GameRegistry.registerItem(ballAttackG3, "ballAttackG3");
		// 回復玉
		ballRecoveryG1 = new ItemOfalenBall(new PotionEffect(Potion.regeneration.id, 2400, 0)).setUnlocalizedName("ofalen.ball.recovery.G1").setTextureName("ofalenmod:ball-1-3");
		GameRegistry.registerItem(ballRecoveryG1, "ballRecovery");
		ballRecoveryG2 = new ItemOfalenBall(new PotionEffect(Potion.regeneration.id, 2400, 1)).setUnlocalizedName("ofalen.ball.recovery.G2").setTextureName("ofalenmod:ball-2-3");
		GameRegistry.registerItem(ballRecoveryG2, "ballRecoveryG2");
		ballRecoveryG3 = new ItemOfalenBall(new PotionEffect(Potion.regeneration.id, 2400, 3)).setUnlocalizedName("ofalen.ball.recovery.G3").setTextureName("ofalenmod:ball-3-3");
		GameRegistry.registerItem(ballRecoveryG3, "ballRecoveryG3");
		// 耐火玉
		ballFireproofG1 = new ItemOfalenBall(new PotionEffect(Potion.fireResistance.getId(), 2400, 0));
		ballFireproofG1.setUnlocalizedName("ofalen.ball.fireproof.G1").setTextureName("ofalenmod:ball-1-4");
		GameRegistry.registerItem(ballFireproofG1, "ballFireProofG1");
		ballFireproofG2 = new ItemOfalenBall(new PotionEffect(Potion.fireResistance.getId(), 4800, 0));
		ballFireproofG2.setUnlocalizedName("ofalen.ball.fireproof.G2").setTextureName("ofalenmod:ball-2-4");
		GameRegistry.registerItem(ballFireproofG2, "ballFireproofG2");
		ballFireproofG3 = new ItemOfalenBall(new PotionEffect(Potion.fireResistance.getId(), 9600, 0));
		ballFireproofG3.setUnlocalizedName("ofalen.ball.fireproof.G3").setTextureName("ofalenmod:ball-3-4");
		GameRegistry.registerItem(ballFireproofG3, "ballFireproofG3");
		// 採掘玉
		ballMiningG1 = new ItemOfalenBall(new PotionEffect(Potion.digSpeed.getId(), 2400, 0));
		ballMiningG1.setUnlocalizedName("ofalen.ball.mining.G1").setTextureName("ofalenmod:ball-1-5");
		GameRegistry.registerItem(ballMiningG1, "ballMiningG1");
		ballMiningG2 = new ItemOfalenBall(new PotionEffect(Potion.digSpeed.getId(), 2400, 1));
		ballMiningG2.setUnlocalizedName("ofalen.ball.mining.G2").setTextureName("ofalenmod:ball-2-5");
		GameRegistry.registerItem(ballMiningG2, "ballMiningG2");
		ballMiningG3 = new ItemOfalenBall(new PotionEffect(Potion.digSpeed.getId(), 2400, 3));
		ballMiningG3.setUnlocalizedName("ofalen.ball.mining.G3").setTextureName("ofalenmod:ball-3-5");
		GameRegistry.registerItem(ballMiningG3, "ballMiningG3");
		// 暗視玉
		ballNightVisionG1 = new ItemOfalenBall(new PotionEffect(Potion.nightVision.getId(), 2400, 0));
		ballNightVisionG1.setUnlocalizedName("ofalen.ball.nightVision.G1").setTextureName("ofalenmod:ball-1-6");
		GameRegistry.registerItem(ballNightVisionG1, "ballNightVisionG1");
		ballNightVisionG2 = new ItemOfalenBall(new PotionEffect(Potion.nightVision.getId(), 4800, 0));
		ballNightVisionG2.setUnlocalizedName("ofalen.ball.nightVision.G2").setTextureName("ofalenmod:ball-2-6");
		GameRegistry.registerItem(ballNightVisionG2, "ballNightVisionG2");
		ballNightVisionG3 = new ItemOfalenBall(new PotionEffect(Potion.nightVision.getId(), 9600, 0));
		ballNightVisionG3.setUnlocalizedName("ofalen.ball.nightVision.G3").setTextureName("ofalenmod:ball-3-6");
		GameRegistry.registerItem(ballNightVisionG3, "ballNightVisionG3");
		// 透明玉
		ballTransparentG1 = new ItemOfalenBall(new PotionEffect(Potion.invisibility.getId(), 2400, 0));
		ballTransparentG1.setUnlocalizedName("ofalen.ball.transparent.G1").setTextureName("ofalenmod:ball-1-7");
		GameRegistry.registerItem(ballTransparentG1, "ballTransparentG1");
		ballTransparentG2 = new ItemOfalenBall(new PotionEffect(Potion.invisibility.getId(), 4800, 0));
		ballTransparentG2.setUnlocalizedName("ofalen.ball.transparent.G2").setTextureName("ofalenmod:ball-2-7");
		GameRegistry.registerItem(ballTransparentG2, "ballTransparentG2");
		ballTransparentG3 = new ItemOfalenBall(new PotionEffect(Potion.invisibility.getId(), 9600, 0));
		ballTransparentG3.setUnlocalizedName("ofalen.ball.transparent.G3").setTextureName("ofalenmod:ball-3-7");
		GameRegistry.registerItem(ballTransparentG3, "ballTransparentG3");
		// 満腹玉
		// ポーションID:23はSaturation（満腹度回復）
		ballFoodG1 = new ItemOfalenBall(new PotionEffect(23, 5, 0));
		ballFoodG1.setUnlocalizedName("ofalen.ball.food.G1").setTextureName("ofalenmod:ball-food-1");
		GameRegistry.registerItem(ballFoodG1, "ballFood");
		ballFoodG2 = new ItemOfalenBall(new PotionEffect(23, 10, 0));
		ballFoodG2.setUnlocalizedName("ofalen.ball.food.G2").setTextureName("ofalenmod:ball-food-2");
		GameRegistry.registerItem(ballFoodG2, "ballFoodG2");
		ballFoodG3 = new ItemOfalenBall(new PotionEffect(23, 20, 0));
		ballFoodG3.setUnlocalizedName("ofalen.ball.food.G3").setTextureName("ofalenmod:ball-food-3");
		GameRegistry.registerItem(ballFoodG3, "ballFoodG3");
		// その他の玉
		ballExplosion = new ItemExplosionBall().setUnlocalizedName("ofalen.ball.explosion").setTextureName("ofalenmod:ball-explosion");
		GameRegistry.registerItem(ballExplosion, "ballExplosion");
		ballHungry = new ItemOfalenBall(new PotionEffect(Potion.hunger.id, 40, 1599)).setUnlocalizedName("ofalen.ball.hungry").setTextureName("ofalenmod:hungry_ball");
		GameRegistry.registerItem(ballHungry, "ballHungry");
		ballPerfect = new ItemPerfectBall(new PotionEffect[] { new PotionEffect(Potion.regeneration.id, 2400, 7), new PotionEffect(Potion.damageBoost.id, 2400, 7), new PotionEffect(Potion.resistance.id, 2400, 7) }).setUnlocalizedName("ofalen.ball.perfect").setTextureName("ofalenmod:ball-2-1");
		GameRegistry.registerItem(ballPerfect, "ballPerfectOfalen");
		// 道具G1
		swordOfalenG1 = new ItemSword(ToolMaterial.EMERALD).setCreativeTab(tab).setUnlocalizedName("ofalen.sword.1").setTextureName("ofalenmod:tool-sword-1");
		GameRegistry.registerItem(swordOfalenG1, "swordOfalen");
		shovelOfalenG1 = new ItemSpade(ToolMaterial.EMERALD).setCreativeTab(tab).setUnlocalizedName("ofalen.shovel.1").setTextureName("ofalenmod:tool-shovel-1");
		GameRegistry.registerItem(shovelOfalenG1, "shovelOfalen");
		pickaxeOfalenG1 = new ItemOfalenPickaxe(ToolMaterial.EMERALD).setUnlocalizedName("ofalen.pickaxe.1").setTextureName("ofalenmod:tool-pickaxe-1");
		GameRegistry.registerItem(pickaxeOfalenG1, "pickaxeOfalen");
		axeOfalenG1 = new ItemOfalenAxe(ToolMaterial.EMERALD).setUnlocalizedName("ofalen.axe.1").setTextureName("ofalenmod:tool-axe-1");
		GameRegistry.registerItem(axeOfalenG1, "axeOfalen");
		hoeOfalenG1 = new ItemHoe(ToolMaterial.EMERALD).setCreativeTab(tab).setUnlocalizedName("ofalen.hoe.1").setTextureName("ofalenmod:tool-hoe-1");
		GameRegistry.registerItem(hoeOfalenG1, "hoeOfalen");
		// 道具G2
		swordOfalenG2 = new ItemSword(OFALEN_TOOL_G2).setCreativeTab(tab).setUnlocalizedName("ofalen.sword.2").setTextureName("ofalenmod:tool-sword-2");
		GameRegistry.registerItem(swordOfalenG2, "swordOfalenG2");
		shovelOfalenG2 = new ItemSpade(OFALEN_TOOL_G2).setCreativeTab(tab).setUnlocalizedName("ofalen.shovel.2").setTextureName("ofalenmod:tool-shovel-2");
		GameRegistry.registerItem(shovelOfalenG2, "shovelOfalenG2");
		pickaxeOfalenG2 = new ItemOfalenPickaxe(OFALEN_TOOL_G2).setUnlocalizedName("ofalen.pickaxe.2").setTextureName("ofalenmod:tool-pickaxe-2");
		GameRegistry.registerItem(pickaxeOfalenG2, "pickaxeOfalenG2");
		axeOfalenG2 = new ItemOfalenAxe(OFALEN_TOOL_G2).setUnlocalizedName("ofalen.axe.2").setTextureName("ofalenmod:tool-axe-2");
		GameRegistry.registerItem(axeOfalenG2, "axeOfalenG2");
		hoeOfalenG2 = new ItemHoe(OFALEN_TOOL_G2).setCreativeTab(tab).setUnlocalizedName("ofalen.hoe.2").setTextureName("ofalenmod:tool-hoe-2");
		GameRegistry.registerItem(hoeOfalenG2, "hoeOfalenG2");
		// 道具G3
		swordOfalenG3 = new ItemSword(OFALEN_TOOL_G3).setCreativeTab(tab).setUnlocalizedName("ofalen.sword.3").setTextureName("ofalenmod:tool-sword-3");
		GameRegistry.registerItem(swordOfalenG3, "swordOfalenG3");
		shovelOfalenG3 = new ItemSpade(OFALEN_TOOL_G3).setCreativeTab(tab).setUnlocalizedName("ofalen.shovel.3").setTextureName("ofalenmod:tool-shovel-3");
		GameRegistry.registerItem(shovelOfalenG3, "shovelOfalenG3");
		pickaxeOfalenG3 = new ItemOfalenPickaxe(OFALEN_TOOL_G3).setUnlocalizedName("ofalen.pickaxe.3").setTextureName("ofalenmod:tool-pickaxe-3");
		GameRegistry.registerItem(pickaxeOfalenG3, "pickaxeOfalenG3");
		axeOfalenG3 = new ItemOfalenAxe(OFALEN_TOOL_G3).setUnlocalizedName("ofalen.axe.3").setTextureName("ofalenmod:tool-axe-3");
		GameRegistry.registerItem(axeOfalenG3, "axeOfalenG3");
		hoeOfalenG3 = new ItemHoe(OFALEN_TOOL_G3).setCreativeTab(tab).setUnlocalizedName("ofalen.hoe.3").setTextureName("ofalenmod:tool-hoe-3");
		GameRegistry.registerItem(hoeOfalenG3, "hoeOfalenG3");
		// その他の道具
		toolOfalenP = new ItemOfalenPerfectTool(OFALEN_TOOL_P).setUnlocalizedName("ofalen.tool.P").setTextureName("ofalenmod:tool-perfect");
		GameRegistry.registerItem(toolOfalenP, "toolPerfectOfalen");
		swordCreative = new ItemCreativeSword(OFALEN_CREATIVE_SWORD).setUnlocalizedName("ofalen.sword.creative").setTextureName("ofalenmod:creative_sword");
		GameRegistry.registerItem(swordCreative, "swordSp");
		// レーザー関連
		pistolLaser = new ItemLaserPistol().setUnlocalizedName("ofalen.pistolLaser").setTextureName("ofalenmod:laser_pistol");
		GameRegistry.registerItem(pistolLaser, "pistolLaser");
		crystalLaserEnergy = new ItemParts((byte) 4).setUnlocalizedName("ofalen.crystalLaserEnergy").setTextureName("ofalenmod:laser_energy_crystal");
		GameRegistry.registerItem(crystalLaserEnergy, "crystalEnergyLaser");
		magazineLaserRed = new ItemLaserMagazine().setUnlocalizedName("ofalen.magazineLaserRed").setTextureName("ofalenmod:laser_magazine_red");
		GameRegistry.registerItem(magazineLaserRed, "magazineLaserRed");
		magazineLaserGreen = new ItemLaserMagazine().setUnlocalizedName("ofalen.magazineLaserGreen").setTextureName("ofalenmod:laser_magazine_green");
		GameRegistry.registerItem(magazineLaserGreen, "magazineLaserGreen");
		magazineLaserBlue = new ItemLaserMagazine().setUnlocalizedName("ofalen.magazineLaserBlue").setTextureName("ofalenmod:laser_magazine_blue");
		GameRegistry.registerItem(magazineLaserBlue, "magazineLaserBlue");
		magazineLaserWhite = new ItemLaserMagazine().setUnlocalizedName("ofalen.magazineLaserWhite").setTextureName("ofalenmod:laser_magazine_white");
		GameRegistry.registerItem(magazineLaserWhite, "magazineLaserWhite");
		// 未来系
		protectorOfalen = new ItemProtector().setUnlocalizedName("ofalen.protector").setTextureName("ofalenmod:future-protector");
		GameRegistry.registerItem(protectorOfalen, "shieldOfalen");
		teleporterOfalen = new ItemTeleporter().setUnlocalizedName("ofalen.teleporter").setTextureName("ofalenmod:future-teleporter");
		GameRegistry.registerItem(teleporterOfalen, "teleporterOfalen");
		floaterOfalen = new ItemFloater().setUnlocalizedName("ofalen.floater").setTextureName("ofalenmod:future-floater");
		GameRegistry.registerItem(floaterOfalen, "floaterOfalen");
		collectorOfalen = new ItemCollector().setUnlocalizedName("ofalen.collector").setTextureName("ofalenmod:future-collector");
		GameRegistry.registerItem(collectorOfalen, "collector");
		// フィルター
		filterItem = new ItemFilter().setUnlocalizedName("ofalen.filterItem").setTextureName("ofalenmod:item_filter");
		GameRegistry.registerItem(filterItem, "item_filter");
		installerFilter = new ItemFilterInstaller().setUnlocalizedName("ofalen.installerFilter").setTextureName("ofalenmod:filter_installer");
		GameRegistry.registerItem(installerFilter, "filter_installer");
		// オファレン草
		seedsOfalen = new ItemOfalenSeed(OfalenModBlockCore.grassOfalen).setUnlocalizedName("ofalen.seeds").setTextureName("ofalenmod:seeds");
		GameRegistry.registerItem(seedsOfalen, "seedsOfalen");
		// 測量杖
		caneSurveying = new ItemSurveyingCane().setUnlocalizedName("ofalen.caneSurveying").setTextureName("ofalenmod:surveying_cane");
		GameRegistry.registerItem(caneSurveying, "caneSurveying");
		// 経験値の結晶
		crystalExp = new ItemExpCrystal().setUnlocalizedName("ofalen.crystalExp").setTextureName("ofalenmod:exp_crystal");
		GameRegistry.registerItem(crystalExp, "crystalExp");
	}
}
