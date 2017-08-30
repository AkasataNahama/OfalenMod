package nahama.ofalenmod.core;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OfalenModConfigCore {
	// カテゴリー名
	private static final String SYSTEM = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "system";
	private static final String MATERIAL = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "material";
	private static final String ORE = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "ore";
	private static final String GENERATE = ORE + Configuration.CATEGORY_SPLITTER + "generate";
	private static final String TOOL = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "tool";
	private static final String PERFECT = TOOL + Configuration.CATEGORY_SPLITTER + "perfect";
	private static final String BALL = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "ball";
	private static final String EXPLOSION = BALL + Configuration.CATEGORY_SPLITTER + "explosion";
	private static final String MACHINE = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "machine";
	private static final String SMELTING = MACHINE + Configuration.CATEGORY_SPLITTER + "smelting";
	private static final String CONVERTING = MACHINE + Configuration.CATEGORY_SPLITTER + "converting";
	private static final String REPAIRING = MACHINE + Configuration.CATEGORY_SPLITTER + "repairing";
	private static final String FUSING = MACHINE + Configuration.CATEGORY_SPLITTER + "fusing";
	private static final String FUTURE = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "future";
	private static final String PROTECTOR = FUTURE + Configuration.CATEGORY_SPLITTER + "protector";
	private static final String TELEPORTER = FUTURE + Configuration.CATEGORY_SPLITTER + "teleporter";
	private static final String FLOATER = FUTURE + Configuration.CATEGORY_SPLITTER + "floater";
	private static final String COLLECTOR = FUTURE + Configuration.CATEGORY_SPLITTER + "collector";
	private static final String WORLD_EDITOR = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "world_editor";
	public static Configuration cfg;
	// General
	public static boolean isUpdateCheckEnabled = true;
	public static boolean isPresentBoxEnabled = true;
	// Material
	public static byte positionStoneLumpRecipeBlank = 7;
	// Ore
	public static byte amountDrop = 3;
	// Ore.Generate
	public static boolean isGeneratorEnabled = true;
	public static byte frequencyGeneration = 3;
	public static byte limitGeneration = 8;
	public static double probLodeGeneration = 0.001;
	// Tool.Perfect
	public static byte rangeMax = 7;
	// Ball.Explosion
	public static byte sizeExplosion = 2;
	// Machine
	public static short divisorBurningTime = 256;
	public static short timeTDiamondBurning = 400;
	public static short timeWhiteFuelBurning = 1600;
	// Machine.Smelting
	public static short timeSmelting = 1600;
	public static byte baseOfalenSmeltingAmount = 1;
	// Machine.Converting
	public static short timeConverting = 1600;
	// Machine.Repairing
	public static short timeRepairing = 40;
	// Machine.Fusing
	public static short timeFusing = 1600;
	// Future.Protector
	public static byte amountProtectingIngotCrafting = 4;
	public static short amountProtectingIngotReference = 64;
	public static short amountProtectorDamage = 1;
	public static boolean isProtectorParticleEnabled = true;
	public static byte typeProtectorParticle = 2;
	// Future.Teleporter
	public static byte amountTeleportingPearlCrafting = 4;
	public static short amountTeleportingPearlReference = 64;
	public static short amountTeleporterDamage = 1;
	public static boolean isTeleporterParticleEnabled = true;
	public static byte typeTeleporterParticle = 2;
	// Future.Floater
	public static byte amountFloatingDustCrafting = 4;
	public static short amountFloatingDustReference = 64;
	public static short amountFloaterDamage = 1;
	public static byte intervalFloaterDamage = 20;
	public static boolean isFloaterParticleEnabled = true;
	public static byte typeFloaterParticle = 2;
	// Future.Collector
	public static byte amountCollectingLumpCrafting = 64;
	public static short amountCollectingLumpReference = 64;
	public static short amountCollectorDamageItem = 1;
	public static short amountCollectorDamageExp = 1;
	public static boolean isCollectorParticleEnabled = true;
	// WorldEditor
	public static short energyDarkFuel = 20;

	/** Configを読み込む。 */
	public static void loadConfig(File fileConfig) {
		cfg = new Configuration(fileConfig, OfalenModCore.VERSION);
		// TODO [1.7.10]2.0.0へのアップデート時、Configリセットを勧告する？
		//		if (VersionUtil.compareVersion(new VersionUtil.VersionString(OfalenModCore.MOD_ID, cfg.getLoadedConfigVersion()), OfalenModCore.VERSION_STRING))
		syncConfig();
	}

	/** Configを同期する。 */
	public static void syncConfig() {
		String separator = Configuration.NEW_LINE;
		String restart = separator + " [Minecraft Restart Required]";
		String restartWorld = separator + " [World Restart Required]";
		String unitTick = separator + " (tick)";
		String keyCategory = "config.ofalen.category.";
		String keyProp = "config.ofalen.prop.";
		List<String> propOrder;
		String category;
		Property prop;
		// General
		category = Configuration.CATEGORY_GENERAL;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		// System
		category = SYSTEM;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(category, "enableUpdateCheck", isUpdateCheckEnabled);
		prop.comment = "Set this to true to enable update check of Ofalen Mod." + restart + getBoolGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.setRequiresMcRestart(true);
		isUpdateCheckEnabled = prop.getBoolean();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "enablePresentBox", isPresentBoxEnabled);
		prop.comment = "Set this to true to enable Present Box." + restartWorld + getBoolGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.setRequiresWorldRestart(true);
		isPresentBoxEnabled = prop.getBoolean();
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(category, propOrder);
		// Material
		category = MATERIAL;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		//
		String diagram = separator + " -------" + separator + " |0 1 2|" + separator + " |3 4 5|" + separator + " |6 7 8|" + separator + " -------";
		prop = cfg.get(category, "positionStoneLumpRecipeBlank", positionStoneLumpRecipeBlank, null, 0, 8);
		prop.comment = "The number is position of space on recipe of \"Lump of Stone\"." + diagram + restart + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.setRequiresMcRestart(true);
		positionStoneLumpRecipeBlank = (byte) getInt(prop);
		// Ore
		category = ORE;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		//
		prop = cfg.get(category, "amountDrop", amountDrop, null, 0, Byte.MAX_VALUE);
		prop.comment = "Drop amount of Ofalen Fragment when Ofalen Ore is mined." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountDrop = (byte) getInt(prop);
		// Ore.Generate
		category = GENERATE;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(category, "enableGenerator", isGeneratorEnabled);
		prop.comment = "Set this to true to generate Ofalen Ore into newly generated chunk." + getBoolGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		isGeneratorEnabled = prop.getBoolean();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "frequencyGeneration", frequencyGeneration, null, 0, Byte.MAX_VALUE);
		prop.comment = "The number of Ofalen Ore generation of each color for each chunk." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		frequencyGeneration = (byte) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "limitGeneration", limitGeneration, null, 1, Byte.MAX_VALUE);
		prop.comment = "Maximum size of Ofalen Ore per generation." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		limitGeneration = (byte) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "probLodeGeneration", probLodeGeneration, null, 0.0, 1.0);
		prop.comment = "Generation probability of Huge Ofalen Ore Lode." + separator + "Calculation of probability is performed on each chunk." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		probLodeGeneration = getDouble(prop);
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(category, propOrder);
		// Tool
		category = TOOL;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		// Tool.Perfect
		category = PERFECT;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		//
		prop = cfg.get(category, "rangeMax", rangeMax, null, 0, Byte.MAX_VALUE);
		prop.comment = "Maximum range of Range Breaking Mode of Ofalen Perfect Tool." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		rangeMax = (byte) getInt(prop);
		// Ball
		category = BALL;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		// Ball.Explosion
		category = EXPLOSION;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		//
		prop = cfg.get(category, "sizeExplosion", sizeExplosion, null, 0, Byte.MAX_VALUE);
		prop.comment = "Explosion size of Explosion Ball." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		sizeExplosion = (byte) getInt(prop);
		// Machine
		category = MACHINE;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(category, "divisorBurningTime", divisorBurningTime, null, 1, Short.MAX_VALUE);
		prop.comment = "Divisor of burning time when using furnace fuel for machines." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		divisorBurningTime = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "timeTDiamondBurning", timeTDiamondBurning, null, 0, Short.MAX_VALUE);
		prop.comment = "Burning time of Creeper Magic Stone by Takumi Craft." + unitTick + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeTDiamondBurning = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "timeWhiteFuelBurning", timeWhiteFuelBurning, null, 0, Short.MAX_VALUE);
		prop.comment = "Burning time of White Ofalen Fuel." + unitTick + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeWhiteFuelBurning = (short) getInt(prop);
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(category, propOrder);
		// Machine.Smelting
		category = SMELTING;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(category, "timeSmelting", timeSmelting, null, 0, Short.MAX_VALUE);
		prop.comment = "The time Ofalen Smelting Machine requires for every smelting." + unitTick + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeSmelting = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "baseOfalenSmeltingAmount", baseOfalenSmeltingAmount, null, 1, 16);
		prop.comment = "Smelting amount of Ofalen from Ofalen Ore using Ofalen Smelting Machine." + separator + "When don't use Ofalen Machine Processor." + restart + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.setRequiresMcRestart(true);
		baseOfalenSmeltingAmount = (byte) getInt(prop);
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(category, propOrder);
		// Machine.Converting
		category = CONVERTING;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		//
		prop = cfg.get(category, "timeConverting", timeConverting, null, 0, Short.MAX_VALUE);
		prop.comment = "The time Ofalen Converting Machine requires for every converting." + unitTick + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeConverting = (short) getInt(prop);
		// Machine.Repairing
		category = REPAIRING;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		//
		prop = cfg.get(category, "timeRepairing", timeRepairing, null, 0, Short.MAX_VALUE);
		prop.comment = "The time Ofalen Repairing Machine requires for every repairing." + separator + "Per 1 durability." + unitTick + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeRepairing = (short) getInt(prop);
		// Machine.Fusing
		category = FUSING;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		//
		prop = cfg.get(category, "timeFusing", timeFusing, null, 0, Short.MAX_VALUE);
		prop.comment = "The time Ofalen Fusing Machine requires for every fusing." + unitTick + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeFusing = (short) getInt(prop);
		// Future
		category = FUTURE;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		// Future.Protector
		category = PROTECTOR;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(category, "amountProtectingIngotCrafting", amountProtectingIngotCrafting, null, 1, 64);
		prop.comment = "Crafting amount of \"Ingot of Ofalen Protecting\" when using normal recipe." + restart + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.setRequiresMcRestart(true);
		amountProtectingIngotCrafting = (byte) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "amountProtectingIngotReference", amountProtectingIngotReference, null, 0, Short.MAX_VALUE);
		prop.comment = "The amount of \"Ingot of Ofalen Protecting\" referred for rendering icon." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountProtectingIngotReference = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "amountProtectorDamage", amountProtectorDamage, null, 0, Short.MAX_VALUE);
		prop.comment = "Damage amount of Ofalen Protector when the player is protected." + separator + "Per 1 damage of player." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountProtectorDamage = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "enableProtectorParticle", isProtectorParticleEnabled);
		prop.comment = "Set this to true to enable particle spawning of Ofalen Protector." + getBoolGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		isProtectorParticleEnabled = prop.getBoolean();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "typeProtectorParticle", typeProtectorParticle, null, 1, 2);
		prop.comment = "Type of particle spawning for Ofalen Protector." + separator + "1: circle, 2: cylinder" + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		typeProtectorParticle = (byte) getInt(prop);
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(category, propOrder);
		// Future.Teleporter
		category = TELEPORTER;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(category, "amountTeleportingPearlCrafting", amountTeleportingPearlCrafting, null, 1, 64);
		prop.comment = "Crafting amount of \"Pearl of Ofalen Teleporting\" when using normal recipe." + restart + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.setRequiresMcRestart(true);
		amountTeleportingPearlCrafting = (byte) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "amountTeleportingPearlReference", amountTeleportingPearlReference, null, 0, Short.MAX_VALUE);
		prop.comment = "The amount of \"Pearl of Ofalen Teleporting\" referred for rendering icon." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountTeleportingPearlReference = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "amountTeleporterDamage", amountTeleporterDamage, null, 0, Short.MAX_VALUE);
		prop.comment = "Damage amount of Ofalen Teleporter when the player teleport." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountTeleporterDamage = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "enableTeleporterParticle", isTeleporterParticleEnabled);
		prop.comment = "Set this to true to enable particle spawning of Ofalen Teleporter." + getBoolGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		isTeleporterParticleEnabled = prop.getBoolean();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "typeTeleporterParticle", typeTeleporterParticle, null, 1, 2);
		prop.comment = "Type of particle spawning for Ofalen Teleporter." + separator + "1: circle, 2: cylinder" + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		typeTeleporterParticle = (byte) getInt(prop);
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(category, propOrder);
		// Future.Floater
		category = FLOATER;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(category, "amountFloatingDustCrafting", amountFloatingDustCrafting, null, 1, 64);
		prop.comment = "Crafting amount of \"Dust of Ofalen Floating\" when using normal recipe." + restart + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.setRequiresMcRestart(true);
		amountFloatingDustCrafting = (byte) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "amountFloatingDustReference", amountFloatingDustReference, null, 0, Short.MAX_VALUE);
		prop.comment = "The amount of \"Dust of Ofalen Floating\" referred for rendering icon." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountFloatingDustReference = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "amountFloaterDamage", amountFloaterDamage, null, 0, Short.MAX_VALUE);
		prop.comment = "Damage amount of Ofalen Floater when the player float." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountFloaterDamage = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "intervalFloaterDamage", intervalFloaterDamage, null, 0, Byte.MAX_VALUE);
		prop.comment = "Damage interval of Ofalen Floater." + unitTick + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		intervalFloaterDamage = (byte) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "enableFloaterParticle", isFloaterParticleEnabled);
		prop.comment = "Set this to true to enable particle spawning of Ofalen Floater." + getBoolGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		isFloaterParticleEnabled = prop.getBoolean();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "typeFloaterParticle", typeFloaterParticle, null, 1, 2);
		prop.comment = "Type of particle spawning for Ofalen Floater." + separator + "1: circle, 2: cylinder" + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		typeFloaterParticle = (byte) getInt(prop);
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(category, propOrder);
		// Future.Collector
		category = COLLECTOR;
		cfg.setCategoryLanguageKey(category, keyCategory + category);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(category, "amountCollectingLumpCrafting", amountCollectingLumpCrafting, null, 1, 64);
		prop.comment = "Crafting amount of \"Lump of Ofalen Collecting\" when using normal recipe." + restart + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.setRequiresMcRestart(true);
		amountCollectingLumpCrafting = (byte) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "amountCollectingLumpReference", amountCollectingLumpReference, null, 0, Short.MAX_VALUE);
		prop.comment = "The amount of \"Lump of Ofalen Collecting\" referred for rendering icon." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountCollectingLumpReference = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "amountCollectorDamageItem", amountCollectorDamageItem, null, 0, Short.MAX_VALUE);
		prop.comment = "Damage amount of Ofalen Collector when item is collected." + separator + "Per 1 item." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountCollectorDamageItem = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "amountCollectorDamageExp", amountCollectorDamageExp, null, 0, Short.MAX_VALUE);
		prop.comment = "Damage amount of Ofalen Collector when experience orb is collected." + separator + "Per 1 exp." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountCollectorDamageExp = (short) getInt(prop);
		propOrder.add(prop.getName());
		//
		prop = cfg.get(category, "enableCollectorParticle", isCollectorParticleEnabled);
		prop.comment = "Set this to true to enable particle spawning of Ofalen Collector." + getBoolGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		isCollectorParticleEnabled = prop.getBoolean();
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(category, propOrder);
		// WorldEditor
		category = WORLD_EDITOR;
		cfg.setCategoryLanguageKey(WORLD_EDITOR, keyCategory + WORLD_EDITOR);
		//
		prop = cfg.get(category, "energyDarkFuel", energyDarkFuel, null, 0, Short.MAX_VALUE);
		prop.comment = "Number of operations per Dark Fuel." + getNumGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		energyDarkFuel = (short) getInt(prop);
		cfg.save();
	}

	private static String getBoolGuide(Property prop) {
		return Configuration.NEW_LINE + " [default: " + prop.getDefault() + "]";
	}

	private static String getNumGuide(Property prop) {
		return Configuration.NEW_LINE + " [range: " + prop.getMinValue() + " ~ " + prop.getMaxValue() + ", default: " + prop.getDefault() + "]";
	}

	private static int getInt(Property prop) {
		return OfalenUtil.getValidValue(prop.getInt(), Integer.parseInt(prop.getMinValue()), Integer.parseInt(prop.getMaxValue()));
	}

	private static double getDouble(Property prop) {
		return OfalenUtil.getValidValue(prop.getDouble(), Double.parseDouble(prop.getMinValue()), Double.parseDouble(prop.getMaxValue()));
	}
}
