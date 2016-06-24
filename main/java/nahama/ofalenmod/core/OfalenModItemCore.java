package nahama.ofalenmod.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.item.*;
import nahama.ofalenmod.item.armor.ItemOfalenArmor;
import nahama.ofalenmod.item.tool.ItemOfalenAxe;
import nahama.ofalenmod.item.tool.ItemOfalenPerfectTool;
import nahama.ofalenmod.item.tool.ItemOfalenPickaxe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.oredict.OreDictionary;

public class OfalenModItemCore {

	public static Item ofalen;
	public static Item fragmentOfalen;
	public static Item coreOfalen;
	/** 0:Machine Cover Plate, 1:Grade 3 Part, 2:Lump of Stone, 3:Stone Fuel, 4:Ofalen Fuel, 5:Laser Magazine, 6:Shield Ingot, 7:Teleport Pearl, 8:Float Dust */
	public static Item partsOfalen;
	/** 0: Iron Stick */
	public static Item partsOfalen3D;
	public static ItemStack plate;
	public static ItemStack part;
	public static ItemStack lump;
	public static ItemStack fuelStone;
	public static ItemStack fuelOfalen;
	public static ItemStack magazine;
	public static ItemStack ingot;
	public static ItemStack pearl;
	public static ItemStack dust;
	public static ItemStack stickIron;

	public static Item helmetOfalen;
	public static Item chestplateOfalen;
	public static Item leggingsOfalen;
	public static Item bootsOfalen;

	public static Item helmetOfalenG2;
	public static Item chestplateOfalenG2;
	public static Item leggingsOfalenG2;
	public static Item bootsOfalenG2;

	public static Item helmetOfalenG3;
	public static Item chestplateOfalenG3;
	public static Item leggingsOfalenG3;
	public static Item bootsOfalenG3;

	public static Item helmetPerfectOfalen;
	public static Item chestplatePerfectOfalen;
	public static Item leggingsPerfectOfalen;
	public static Item bootsPerfectOfalen;

	public static Item ballEmpty;

	public static Item ballDefense;
	public static Item ballDefenseG2;
	public static Item ballDefenseG3;

	public static Item ballRecovery;
	public static Item ballRecoveryG2;
	public static Item ballRecoveryG3;

	public static Item ballAttack;
	public static Item ballAttackG2;
	public static Item ballAttackG3;

	public static Item ballExplosion;
	public static Item ballFlame;
	public static Item ballHungry;
	public static Item ballFood;
	public static Item ballPerfectOfalen;

	public static Item swordOfalen;
	public static Item shovelOfalen;
	public static Item pickaxeOfalen;
	public static Item axeOfalen;
	public static Item hoeOfalen;

	public static Item swordOfalenG2;
	public static Item shovelOfalenG2;
	public static Item pickaxeOfalenG2;
	public static Item axeOfalenG2;
	public static Item hoeOfalenG2;

	public static Item swordOfalenG3;
	public static Item shovelOfalenG3;
	public static Item pickaxeOfalenG3;
	public static Item axeOfalenG3;
	public static Item hoeOfalenG3;

	public static Item toolPerfectOfalen;
	public static Item swordSp;

	public static Item pistolLaser;
	public static Item crystalEnergyLaser;
	public static Item magazineLaserRed;
	public static Item magazineLaserGreen;
	public static Item magazineLaserBlue;
	public static Item magazineLaserWhite;

	public static Item shieldOfalen;
	public static Item teleporterOfalen;
	public static Item floaterOfalen;

	public static Item seedOfalen;

	/** アイテムを登録する処理。 */
	public static void registerItem() {
		CreativeTabs tab = OfalenModCore.tabOfalen;

		ofalen = new ItemOfalen()
				.setUnlocalizedName("ofalen")
				.setTextureName("ofalenmod:ofalen");
		GameRegistry.registerItem(ofalen, "ofalen");
		OreDictionary.registerOre("gemOfalen", new ItemStack(ofalen, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("gemOfalenRed", new ItemStack(ofalen, 1, 0));
		OreDictionary.registerOre("gemOfalenGreen", new ItemStack(ofalen, 1, 1));
		OreDictionary.registerOre("gemOfalenBlue", new ItemStack(ofalen, 1, 2));
		OreDictionary.registerOre("gemOfalenWhite", new ItemStack(ofalen, 1, 3));

		fragmentOfalen = new ItemOfalen()
				.setUnlocalizedName("fragmentOfalen")
				.setTextureName("ofalenmod:ofalen_fragment");
		GameRegistry.registerItem(fragmentOfalen, "fragmentOfalen");
		OreDictionary.registerOre("fragmentOfalen", new ItemStack(fragmentOfalen, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("fragmentOfalenRed", new ItemStack(fragmentOfalen, 1, 0));
		OreDictionary.registerOre("fragmentOfalenGreen", new ItemStack(fragmentOfalen, 1, 1));
		OreDictionary.registerOre("fragmentOfalenBlue", new ItemStack(fragmentOfalen, 1, 2));
		OreDictionary.registerOre("fragmentOfalenWhite", new ItemStack(fragmentOfalen, 1, 3));

		coreOfalen = new ItemOfalen()
				.setUnlocalizedName("coreOfalen")
				.setTextureName("ofalenmod:ofalen_core");
		GameRegistry.registerItem(coreOfalen, "coreOfalen");
		OreDictionary.registerOre("coreOfalen", new ItemStack(coreOfalen, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("coreOfalenRed", new ItemStack(coreOfalen, 1, 0));
		OreDictionary.registerOre("coreOfalenGreen", new ItemStack(coreOfalen, 1, 1));
		OreDictionary.registerOre("coreOfalenBlue", new ItemStack(coreOfalen, 1, 2));
		OreDictionary.registerOre("coreOfalenWhite", new ItemStack(coreOfalen, 1, 3));

		partsOfalen = new ItemParts(9)
				.setUnlocalizedName("partsOfalen")
				.setTextureName("ofalenmod:parts");
		GameRegistry.registerItem(partsOfalen, "partsOfalen");

		partsOfalen3D = new ItemParts(1)
				.setUnlocalizedName("partsOfalen3D")
				.setTextureName("ofalenmod:parts3D")
				.setFull3D();
		GameRegistry.registerItem(partsOfalen3D, "partsOfalen3D");

		plate = new ItemStack(partsOfalen, 1, 0);
		part = new ItemStack(partsOfalen, 1, 1);
		lump = new ItemStack(partsOfalen, 1, 2);
		fuelStone = new ItemStack(partsOfalen, 1, 3);
		fuelOfalen = new ItemStack(partsOfalen, 1, 4);
		magazine = new ItemStack(partsOfalen, 1, 5);
		ingot = new ItemStack(partsOfalen, 1, 6);
		pearl = new ItemStack(partsOfalen, 1, 7);
		dust = new ItemStack(partsOfalen, 1, 8);
		stickIron = new ItemStack(partsOfalen3D, 1, 0);

		// アーマー
		helmetOfalen = new ItemOfalenArmor(OfalenModMaterialCore.OFALENA, 0, 1)
				.setUnlocalizedName("helmetOfalen")
				.setTextureName("ofalenmod:ofalen_helmet");
		GameRegistry.registerItem(helmetOfalen, "helmetOfalen");

		chestplateOfalen = new ItemOfalenArmor(OfalenModMaterialCore.OFALENA, 1, 1)
				.setUnlocalizedName("chestplateOfalen")
				.setTextureName("ofalenmod:ofalen_chestplate");
		GameRegistry.registerItem(chestplateOfalen, "chestplateOfalen");

		leggingsOfalen = new ItemOfalenArmor(OfalenModMaterialCore.OFALENA, 2, 1)
				.setUnlocalizedName("leggingsOfalen")
				.setTextureName("ofalenmod:ofalen_leggings");
		GameRegistry.registerItem(leggingsOfalen, "leggingsOfalen");

		bootsOfalen = new ItemOfalenArmor(OfalenModMaterialCore.OFALENA, 3, 1)
				.setUnlocalizedName("bootsOfalen")
				.setTextureName("ofalenmod:ofalen_boots");
		GameRegistry.registerItem(bootsOfalen, "bootsOfalen");

		helmetOfalenG2 = new ItemOfalenArmor(OfalenModMaterialCore.OFALENG2A, 0, 2)
				.setUnlocalizedName("helmetOfalenG2")
				.setTextureName("ofalenmod:ofalen_helmet_G2");
		GameRegistry.registerItem(helmetOfalenG2, "helmetOfalenG2");

		chestplateOfalenG2 = new ItemOfalenArmor(OfalenModMaterialCore.OFALENG2A, 1, 2)
				.setUnlocalizedName("chestplateOfalenG2")
				.setTextureName("ofalenmod:ofalen_chestplate_G2");
		GameRegistry.registerItem(chestplateOfalenG2, "chestplateOfalenG2");

		leggingsOfalenG2 = new ItemOfalenArmor(OfalenModMaterialCore.OFALENG2A, 2, 2)
				.setUnlocalizedName("leggingsOfalenG2")
				.setTextureName("ofalenmod:ofalen_leggings_G2");
		GameRegistry.registerItem(leggingsOfalenG2, "leggingsOfalenG2");

		bootsOfalenG2 = new ItemOfalenArmor(OfalenModMaterialCore.OFALENG2A, 3, 2)
				.setUnlocalizedName("bootsOfalenG2")
				.setTextureName("ofalenmod:ofalen_boots_G2");
		GameRegistry.registerItem(bootsOfalenG2, "bootsOfalenG2");

		helmetOfalenG3 = new ItemOfalenArmor(OfalenModMaterialCore.OFALENG3A, 0, 3)
				.setUnlocalizedName("helmetOfalenG3")
				.setTextureName("ofalenmod:ofalen_helmet_G3");
		GameRegistry.registerItem(helmetOfalenG3, "helmetOfalenG3");

		chestplateOfalenG3 = new ItemOfalenArmor(OfalenModMaterialCore.OFALENG3A, 1, 3)
				.setUnlocalizedName("chestplateOfalenG3")
				.setTextureName("ofalenmod:ofalen_chestplate_G3");
		GameRegistry.registerItem(chestplateOfalenG3, "chestplateOfalenG3");

		leggingsOfalenG3 = new ItemOfalenArmor(OfalenModMaterialCore.OFALENG3A, 2, 3)
				.setUnlocalizedName("leggingsOfalenG3")
				.setTextureName("ofalenmod:ofalen_leggings_G3");
		GameRegistry.registerItem(leggingsOfalenG3, "leggingsOfalenG3");

		bootsOfalenG3 = new ItemOfalenArmor(OfalenModMaterialCore.OFALENG3A, 3, 3)
				.setUnlocalizedName("bootsOfalenG3")
				.setTextureName("ofalenmod:ofalen_boots_G3");
		GameRegistry.registerItem(bootsOfalenG3, "bootsOfalenG3");

		helmetPerfectOfalen = new ItemOfalenArmor(OfalenModMaterialCore.PERFECTA, 0, 4)
				.setUnlocalizedName("helmetPerfectOfalen")
				.setTextureName("ofalenmod:ofalen_helmet_P");
		GameRegistry.registerItem(helmetPerfectOfalen, "helmetPerfectOfalen");

		chestplatePerfectOfalen = new ItemOfalenArmor(OfalenModMaterialCore.PERFECTA, 1, 4)
				.setUnlocalizedName("chestplatePerfectOfalen")
				.setTextureName("ofalenmod:ofalen_chestplate_P");
		GameRegistry.registerItem(chestplatePerfectOfalen, "chestplatePerfectOfalen");

		leggingsPerfectOfalen = new ItemOfalenArmor(OfalenModMaterialCore.PERFECTA, 2, 4)
				.setUnlocalizedName("leggingsPerfectOfalen")
				.setTextureName("ofalenmod:ofalen_leggings_P");
		GameRegistry.registerItem(leggingsPerfectOfalen, "leggingsPerfectOfalen");

		bootsPerfectOfalen = new ItemOfalenArmor(OfalenModMaterialCore.PERFECTA, 3, 4)
				.setUnlocalizedName("bootsPerfectOfalen")
				.setTextureName("ofalenmod:ofalen_boots_P");
		GameRegistry.registerItem(bootsPerfectOfalen, "bootsPerfectOfalen");

		// ボール
		ballEmpty = new ItemEmptyBall()
				.setUnlocalizedName("ballEmpty")
				.setTextureName("ofalenmod:empty_ball-");
		GameRegistry.registerItem(ballEmpty, "ballEmpty");

		ballDefense = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.resistance.id, 2400, 0) })
				.setUnlocalizedName("ballDefense")
				.setTextureName("ofalenmod:defense_ball");
		GameRegistry.registerItem(ballDefense, "ballDefense");

		ballDefenseG2 = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.resistance.id, 2400, 1) })
				.setUnlocalizedName("ballDefenseG2")
				.setTextureName("ofalenmod:defense_ball_G2");
		GameRegistry.registerItem(ballDefenseG2, "ballDefenseG2");

		ballDefenseG3 = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.resistance.id, 2400, 3) })
				.setUnlocalizedName("ballDefenseG3")
				.setTextureName("ofalenmod:defense_ball_G3");
		GameRegistry.registerItem(ballDefenseG3, "ballDefenseG3");

		ballAttack = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.damageBoost.id, 2400, 0) })
				.setUnlocalizedName("ballAttack")
				.setTextureName("ofalenmod:attack_ball");
		GameRegistry.registerItem(ballAttack, "ballAttack");

		ballAttackG2 = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.damageBoost.id, 2400, 1) })
				.setUnlocalizedName("ballAttackG2")
				.setTextureName("ofalenmod:attack_ball_G2");
		GameRegistry.registerItem(ballAttackG2, "ballAttackG2");

		ballAttackG3 = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.damageBoost.id, 2400, 3) })
				.setUnlocalizedName("ballAttackG3")
				.setTextureName("ofalenmod:attack_ball_G3");
		GameRegistry.registerItem(ballAttackG3, "ballAttackG3");

		ballRecovery = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.heal.id, 1, 0) })
				.setUnlocalizedName("ballRecovery")
				.setTextureName("ofalenmod:recovery_ball");
		GameRegistry.registerItem(ballRecovery, "ballRecovery");

		ballRecoveryG2 = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.heal.id, 1, 1) })
				.setUnlocalizedName("ballRecoveryG2")
				.setTextureName("ofalenmod:recovery_ball_G2");
		GameRegistry.registerItem(ballRecoveryG2, "ballRecoveryG2");

		ballRecoveryG3 = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.heal.id, 1, 3) })
				.setUnlocalizedName("ballRecoveryG3")
				.setTextureName("ofalenmod:recovery_ball_G3");
		GameRegistry.registerItem(ballRecoveryG3, "ballRecoveryG3");

		ballExplosion = new ItemExplosionBall()
				.setUnlocalizedName("ballExplosion")
				.setTextureName("ofalenmod:explosion_ball-");
		GameRegistry.registerItem(ballExplosion, "ballExplosion");

		ballFlame = new ItemExplosionBall()
				.setUnlocalizedName("ballFlame")
				.setTextureName("ofalenmod:flame_ball-");
		GameRegistry.registerItem(ballFlame, "ballFlame");

		ballHungry = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(Potion.hunger.id, 2400, 19) })
				.setUnlocalizedName("ballHungry")
				.setTextureName("ofalenmod:hungry_ball");
		GameRegistry.registerItem(ballHungry, "ballHungry");

		ballFood = new ItemOfalenBall(new PotionEffect[] { new PotionEffect(23, 2400, 0) })
				.setUnlocalizedName("ballFood")
				.setTextureName("ofalenmod:food_ball");
		GameRegistry.registerItem(ballFood, "ballFood");

		ballPerfectOfalen = new ItemOfalenPerfectBall(new PotionEffect[] {
				new PotionEffect(Potion.heal.id, 1, 7),
				new PotionEffect(Potion.damageBoost.id, 2400, 7),
				new PotionEffect(Potion.resistance.id, 2400, 7) })
				.setUnlocalizedName("ballPerfectOfalen")
				.setTextureName("ofalenmod:empty_ball-2");
		GameRegistry.registerItem(ballPerfectOfalen, "ballPerfectOfalen");

		// ツール
		swordOfalen = new ItemSword(OfalenModMaterialCore.OFALENT)
				.setCreativeTab(tab)
				.setUnlocalizedName("swordOfalen")
				.setTextureName("ofalenmod:ofalen_sword");
		GameRegistry.registerItem(swordOfalen, "swordOfalen");

		shovelOfalen = new ItemSpade(OfalenModMaterialCore.OFALENT)
				.setCreativeTab(tab)
				.setUnlocalizedName("shovelOfalen")
				.setTextureName("ofalenmod:ofalen_shovel");
		GameRegistry.registerItem(shovelOfalen, "shovelOfalen");

		pickaxeOfalen = new ItemOfalenPickaxe(OfalenModMaterialCore.OFALENT)
				.setUnlocalizedName("pickaxeOfalen")
				.setTextureName("ofalenmod:ofalen_pickaxe");
		GameRegistry.registerItem(pickaxeOfalen, "pickaxeOfalen");

		axeOfalen = new ItemOfalenAxe(OfalenModMaterialCore.OFALENT)
				.setUnlocalizedName("axeOfalen")
				.setTextureName("ofalenmod:ofalen_axe");
		GameRegistry.registerItem(axeOfalen, "axeOfalen");

		hoeOfalen = new ItemHoe(OfalenModMaterialCore.OFALENT)
				.setCreativeTab(tab)
				.setUnlocalizedName("hoeOfalen")
				.setTextureName("ofalenmod:ofalen_hoe");
		GameRegistry.registerItem(hoeOfalen, "hoeOfalen");

		swordOfalenG2 = new ItemSword(OfalenModMaterialCore.OFALENG2T)
				.setCreativeTab(tab)
				.setUnlocalizedName("swordOfalenG2")
				.setTextureName("ofalenmod:ofalen_sword_G2");
		GameRegistry.registerItem(swordOfalenG2, "swordOfalenG2");

		shovelOfalenG2 = new ItemSpade(OfalenModMaterialCore.OFALENG2T)
				.setCreativeTab(tab)
				.setUnlocalizedName("shovelOfalenG2")
				.setTextureName("ofalenmod:ofalen_shovel_G2");
		GameRegistry.registerItem(shovelOfalenG2, "shovelOfalenG2");

		pickaxeOfalenG2 = new ItemOfalenPickaxe(OfalenModMaterialCore.OFALENG2T)
				.setUnlocalizedName("pickaxeOfalenG2")
				.setTextureName("ofalenmod:ofalen_pickaxe_G2");
		GameRegistry.registerItem(pickaxeOfalenG2, "pickaxeOfalenG2");

		axeOfalenG2 = new ItemOfalenAxe(OfalenModMaterialCore.OFALENG2T)
				.setUnlocalizedName("axeOfalenG2")
				.setTextureName("ofalenmod:ofalen_axe_G2");
		GameRegistry.registerItem(axeOfalenG2, "axeOfalenG2");

		hoeOfalenG2 = new ItemHoe(OfalenModMaterialCore.OFALENG2T)
				.setCreativeTab(tab)
				.setUnlocalizedName("hoeOfalenG2")
				.setTextureName("ofalenmod:ofalen_hoe_G2");
		GameRegistry.registerItem(hoeOfalenG2, "hoeOfalenG2");

		swordOfalenG3 = new ItemSword(OfalenModMaterialCore.OFALENG3T)
				.setCreativeTab(tab)
				.setUnlocalizedName("swordOfalenG3")
				.setTextureName("ofalenmod:ofalen_sword_G3");
		GameRegistry.registerItem(swordOfalenG3, "swordOfalenG3");

		shovelOfalenG3 = new ItemSpade(OfalenModMaterialCore.OFALENG3T)
				.setCreativeTab(tab)
				.setUnlocalizedName("shovelOfalenG3")
				.setTextureName("ofalenmod:ofalen_shovel_G3");
		GameRegistry.registerItem(shovelOfalenG3, "shovelOfalenG3");

		pickaxeOfalenG3 = new ItemOfalenPickaxe(OfalenModMaterialCore.OFALENG3T)
				.setUnlocalizedName("pickaxeOfalenG3")
				.setTextureName("ofalenmod:ofalen_pickaxe_G3");
		GameRegistry.registerItem(pickaxeOfalenG3, "pickaxeOfalenG3");

		axeOfalenG3 = new ItemOfalenAxe(OfalenModMaterialCore.OFALENG3T)
				.setUnlocalizedName("axeOfalenG3")
				.setTextureName("ofalenmod:ofalen_axe_G3");
		GameRegistry.registerItem(axeOfalenG3, "axeOfalenG3");

		hoeOfalenG3 = new ItemHoe(OfalenModMaterialCore.OFALENG3T)
				.setCreativeTab(tab)
				.setUnlocalizedName("hoeOfalenG3")
				.setTextureName("ofalenmod:ofalen_hoe_G3");
		GameRegistry.registerItem(hoeOfalenG3, "hoeOfalenG3");

		toolPerfectOfalen = new ItemOfalenPerfectTool(OfalenModMaterialCore.PERFECTT)
				.setUnlocalizedName("toolPerfectOfalen")
				.setTextureName("ofalenmod:ofalen_perfect_tool");
		GameRegistry.registerItem(toolPerfectOfalen, "toolPerfectOfalen");

		swordSp = new ItemCreativeSword()
				.setUnlocalizedName("swordSp")
				.setTextureName("ofalenmod:creative_sword");
		GameRegistry.registerItem(swordSp, "swordSp");

		// レーザーピストル関連
		pistolLaser = new ItemLaserPistol()
				.setUnlocalizedName("pistolLaser")
				.setTextureName("ofalenmod:laser_pistol");
		GameRegistry.registerItem(pistolLaser, "pistolLaser");

		crystalEnergyLaser = new ItemParts(4)
				.setUnlocalizedName("crystalEnergyLaser")
				.setTextureName("ofalenmod:laser_energy_crystal");
		GameRegistry.registerItem(crystalEnergyLaser, "crystalEnergyLaser");

		magazineLaserRed = new ItemLaserMagazine()
				.setUnlocalizedName("magazineLaserRed")
				.setTextureName("ofalenmod:laser_magazine_red");
		GameRegistry.registerItem(magazineLaserRed, "magazineLaserRed");

		magazineLaserGreen = new ItemLaserMagazine()
				.setUnlocalizedName("magazineLaserGreen")
				.setTextureName("ofalenmod:laser_magazine_green");
		GameRegistry.registerItem(magazineLaserGreen, "magazineLaserGreen");

		magazineLaserBlue = new ItemLaserMagazine()
				.setUnlocalizedName("magazineLaserBlue")
				.setTextureName("ofalenmod:laser_magazine_blue");
		GameRegistry.registerItem(magazineLaserBlue, "magazineLaserBlue");

		magazineLaserWhite = new ItemLaserMagazine()
				.setUnlocalizedName("magazineLaserWhite")
				.setTextureName("ofalenmod:laser_magazine_white");
		GameRegistry.registerItem(magazineLaserWhite, "magazineLaserWhite");

		// 未来道具
		shieldOfalen = new ItemShield()
				.setUnlocalizedName("shieldOfalen")
				.setTextureName("ofalenmod:shield");
		GameRegistry.registerItem(shieldOfalen, "shieldOfalen");

		teleporterOfalen = new ItemTeleporter()
				.setUnlocalizedName("teleporterOfalen")
				.setTextureName("ofalenmod:teleporter");
		GameRegistry.registerItem(teleporterOfalen, "teleporterOfalen");

		floaterOfalen = new ItemFloater()
				.setUnlocalizedName("floaterOfalen")
				.setTextureName("ofalenmod:floater");
		GameRegistry.registerItem(floaterOfalen, "floaterOfalen");

		//オファレン草
		seedOfalen = new ItemSeedOfalen(OfalenModBlockCore.grassOfalen)
				.setUnlocalizedName("seedOfalen")
				.setTextureName("ofalenmod:seed");
		GameRegistry.registerItem(seedOfalen, "seedOfalen");
	}

}
