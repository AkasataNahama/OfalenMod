package nahama.ofalenmod.core;

import nahama.ofalenmod.OfalenModCore;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OfalenModConfigCore {
	// カテゴリー名
	public static final String GENERAL = "general";
	private static final String SYSTEM = GENERAL + Configuration.CATEGORY_SPLITTER + "system";
	private static final String RECIPE = GENERAL + Configuration.CATEGORY_SPLITTER + "recipe";
	private static final String ORE = GENERAL + Configuration.CATEGORY_SPLITTER + "ore";
	private static final String GENERATE = ORE + Configuration.CATEGORY_SPLITTER + "generate";
	private static final String TOOL = GENERAL + Configuration.CATEGORY_SPLITTER + "tool";
	private static final String PERFECT = TOOL + Configuration.CATEGORY_SPLITTER + "perfect";
	private static final String BALL = GENERAL + Configuration.CATEGORY_SPLITTER + "ball";
	private static final String EXPLOSION = BALL + Configuration.CATEGORY_SPLITTER + "explosion";
	private static final String MACHINE = GENERAL + Configuration.CATEGORY_SPLITTER + "machine";
	private static final String SMELTING = MACHINE + Configuration.CATEGORY_SPLITTER + "smelting";
	private static final String CONVERTING = MACHINE + Configuration.CATEGORY_SPLITTER + "converting";
	private static final String REPAIRING = MACHINE + Configuration.CATEGORY_SPLITTER + "repairing";
	private static final String FUSING = MACHINE + Configuration.CATEGORY_SPLITTER + "fusing";
	private static final String FUTURE = GENERAL + Configuration.CATEGORY_SPLITTER + "future";
	private static final String PROTECTOR = FUTURE + Configuration.CATEGORY_SPLITTER + "protector";
	private static final String TELEPORTER = FUTURE + Configuration.CATEGORY_SPLITTER + "teleporter";
	private static final String FLOATER = FUTURE + Configuration.CATEGORY_SPLITTER + "floater";
	private static final String COLLECTOR = FUTURE + Configuration.CATEGORY_SPLITTER + "collector";
	public static Configuration cfg;
	// General
	public static boolean isUpdateCheckEnabled = true;
	// Recipe
	public static byte amountProtectingIngotCrafting = 4;
	public static byte amountTeleportingPearlCrafting = 4;
	public static byte amountFloatingDustCrafting = 4;
	public static byte amountCollectingLumpCrafting = 64;
	public static byte baseOfalenSmeltingAmount = 1;
	public static byte positionStoneLumpRecipeBlank = 7;
	// Ore
	public static byte amountDrop = 3;
	// Ore.Generate
	public static boolean isGeneratorEnabled = true;
	public static byte frequencyGeneration = 3;
	public static byte limitGeneration = 8;
	public static short probLodeGeneration = 10;
	// Tool.PerfectTool
	public static byte rangeMax = 7;
	// Ball.ExplosionBall
	public static byte sizeExplosion = 2;
	// Machine
	public static short divisorBurningTime = 256;
	public static short timeTDiamondBurning = 400;
	public static short timeWhiteFuelBurning = 1600;
	// Machine.Smelting
	public static short timeSmelting = 1600;
	// Machine.Converting
	public static short timeConverting = 1600;
	// Machine.Repairing
	public static short timeRepairing = 40;
	// Machine.Fusing
	public static short timeFusing = 1600;
	// Future.Protector
	public static short amountProtectorDamage = 1;
	// Future.Teleporter
	public static short amountTeleporterDamage = 1;
	// Future.Floater
	public static short amountFloaterDamage = 1;
	public static byte intervalFloaterDamage = 20;
	// Future.Collector
	public static short amountCollectorDamageItem = 1;
	public static short amountCollectorDamageExp = 1;

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
		String unitTick = separator + " (tick)";
		String keyCategory = "config.ofalen.category.";
		String keyProp = "config.ofalen.prop.";
		List<String> propOrder;
		Property prop;
		// General
		cfg.setCategoryLanguageKey(GENERAL, keyCategory + GENERAL);
		// System
		cfg.setCategoryLanguageKey(SYSTEM, keyCategory + SYSTEM);
		cfg.setCategoryRequiresMcRestart(SYSTEM, true);
		//
		prop = cfg.get(SYSTEM, "enableUpdateCheck", isUpdateCheckEnabled);
		prop.comment = "Set this to true to enable update check of Ofalen Mod." + restart + getBoolGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.requiresMcRestart();
		isUpdateCheckEnabled = prop.getBoolean();
		// Recipe
		cfg.setCategoryLanguageKey(RECIPE, keyCategory + RECIPE);
		cfg.setCategoryRequiresMcRestart(RECIPE, true);
		//
		String diagram = separator + " -------" + separator + " |0 1 2|" + separator + " |3 4 5|" + separator + " |6 7 8|" + separator + " -------";
		prop = cfg.get(RECIPE, "positionStoneLumpRecipeBlank", positionStoneLumpRecipeBlank, null, 0, 8);
		prop.comment = "The number is position of space on recipe of \"Lump of Stone\"." + diagram + restart + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.requiresMcRestart();
		positionStoneLumpRecipeBlank = (byte) prop.getInt();
		// Ore
		cfg.setCategoryLanguageKey(ORE, keyCategory + ORE);
		//
		prop = cfg.get(ORE, "amountDrop", amountDrop, null, 1, Byte.MAX_VALUE);
		prop.comment = "Drop amount of Ofalen Fragment when Ofalen Ore is mined." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountDrop = (byte) prop.getInt();
		// Ore.Generate
		cfg.setCategoryLanguageKey(GENERATE, keyCategory + GENERATE);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(GENERATE, "enableGenerator", isGeneratorEnabled);
		prop.comment = "Set this to true to generate Ofalen Ore into newly generated chunk." + getBoolGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		isGeneratorEnabled = prop.getBoolean();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(GENERATE, "frequencyGeneration", frequencyGeneration, null, 1, Byte.MAX_VALUE);
		prop.comment = "The number of Ofalen Ore generation of each color for each chunk." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		frequencyGeneration = (byte) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(GENERATE, "limitGeneration", limitGeneration, null, 1, Byte.MAX_VALUE);
		prop.comment = "Maximum size of Ofalen Ore per generation." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		limitGeneration = (byte) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(GENERATE, "probLodeGeneration", probLodeGeneration, null, 1, 10000);
		prop.comment = "Generation probability of Huge Ofalen Ore Lode." + separator + "Calculation of probability is performed on each chunk." + separator + " (x/10000)" + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		probLodeGeneration = (short) prop.getInt();
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(GENERATE, propOrder);
		// Tool
		cfg.setCategoryLanguageKey(TOOL, keyCategory + TOOL);
		// Tool.PerfectTool
		cfg.setCategoryLanguageKey(PERFECT, keyCategory + PERFECT);
		//
		prop = cfg.get(PERFECT, "rangeMax", rangeMax, null, 0, Byte.MAX_VALUE);
		prop.comment = "Maximum range of Range Breaking Mode of Ofalen Perfect Tool." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		rangeMax = (byte) prop.getInt();
		// Ball
		cfg.setCategoryLanguageKey(BALL, keyCategory + BALL);
		// Ball.ExplosionBall
		cfg.setCategoryLanguageKey(EXPLOSION, keyCategory + EXPLOSION);
		//
		prop = cfg.get(EXPLOSION, "sizeExplosion", sizeExplosion, null, 0, Byte.MAX_VALUE);
		prop.comment = "Explosion size of Explosion Ball." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		sizeExplosion = (byte) prop.getInt();
		// Machine
		cfg.setCategoryLanguageKey(MACHINE, keyCategory + MACHINE);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(MACHINE, "divisorBurningTime", divisorBurningTime, null, 1, Short.MAX_VALUE);
		prop.comment = "Divisor of burning time when using furnace fuel for machines." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		divisorBurningTime = (short) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(MACHINE, "timeTDiamondBurning", timeTDiamondBurning, null, 1, Short.MAX_VALUE);
		prop.comment = "Burning time of Creeper Magic Stone by Takumi Craft." + unitTick + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeTDiamondBurning = (short) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(MACHINE, "timeWhiteFuelBurning", timeWhiteFuelBurning, null, 1, Short.MAX_VALUE);
		prop.comment = "Burning time of White Ofalen Fuel." + unitTick + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeWhiteFuelBurning = (short) prop.getInt();
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(MACHINE, propOrder);
		// Machine.Smelting
		cfg.setCategoryLanguageKey(SMELTING, keyCategory + SMELTING);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(SMELTING, "timeSmelting", timeSmelting, null, 1, Short.MAX_VALUE);
		prop.comment = "The time Ofalen Smelting Machine requires for every smelting." + unitTick + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeSmelting = (short) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(SMELTING, "baseOfalenSmeltingAmount", baseOfalenSmeltingAmount, null, 1, 16);
		prop.comment = "Smelting amount of Ofalen from Ofalen Ore using Ofalen Smelting Machine." + separator + "When don't use Ofalen Machine Processor." + restart + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.requiresMcRestart();
		baseOfalenSmeltingAmount = (byte) prop.getInt();
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(SMELTING, propOrder);
		// Machine.Converting
		cfg.setCategoryLanguageKey(CONVERTING, keyCategory + CONVERTING);
		//
		prop = cfg.get(CONVERTING, "timeConverting", timeConverting, null, 1, Short.MAX_VALUE);
		prop.comment = "The time Ofalen Converting Machine requires for every converting." + unitTick + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeConverting = (short) prop.getInt();
		// Machine.Repairing
		cfg.setCategoryLanguageKey(REPAIRING, keyCategory + REPAIRING);
		//
		prop = cfg.get(REPAIRING, "timeRepairing", timeRepairing, null, 1, Short.MAX_VALUE);
		prop.comment = "The time Ofalen Repairing Machine requires for every repairing." + separator + "Per 1 durability." + unitTick + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeRepairing = (short) prop.getInt();
		// Machine.Fusing
		cfg.setCategoryLanguageKey(FUSING, keyCategory + FUSING);
		//
		prop = cfg.get(FUSING, "timeFusing", timeFusing, null, 1, Short.MAX_VALUE);
		prop.comment = "The time Ofalen Fusing Machine requires for every fusing." + unitTick + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		timeFusing = (short) prop.getInt();
		// Future
		cfg.setCategoryLanguageKey(FUTURE, keyCategory + FUTURE);
		// Future.Protector
		cfg.setCategoryLanguageKey(PROTECTOR, keyCategory + PROTECTOR);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(PROTECTOR, "amountProtectingIngotCrafting", amountProtectingIngotCrafting, null, 1, 64);
		prop.comment = "Crafting amount of \"Ingot of Ofalen Protecting\" when using normal recipe." + restart + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.requiresMcRestart();
		amountProtectingIngotCrafting = (byte) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(PROTECTOR, "amountProtectorDamage", amountProtectorDamage, null, 0, 64 * 9);
		prop.comment = "Damage amount of Ofalen Protector when the player is protected." + separator + "Per 1 damage of player." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountProtectorDamage = (short) prop.getInt();
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(PROTECTOR, propOrder);
		// Future.Teleporter
		cfg.setCategoryLanguageKey(TELEPORTER, keyCategory + TELEPORTER);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(TELEPORTER, "amountTeleportingPearlCrafting", amountTeleportingPearlCrafting, null, 1, 64);
		prop.comment = "Crafting amount of \"Pearl of Ofalen Teleporting\" when using normal recipe." + restart + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.requiresMcRestart();
		amountTeleportingPearlCrafting = (byte) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(TELEPORTER, "amountTeleporterDamage", amountTeleporterDamage, null, 0, 64);
		prop.comment = "Damage amount of Ofalen Teleporter when the player teleport." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountTeleporterDamage = (short) prop.getInt();
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(TELEPORTER, propOrder);
		// Future.Floater
		cfg.setCategoryLanguageKey(FLOATER, keyCategory + FLOATER);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(FLOATER, "amountFloatingDustCrafting", amountFloatingDustCrafting, null, 1, 64);
		prop.comment = "Crafting amount of \"Dust of Ofalen Floating\" when using normal recipe." + restart + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.requiresMcRestart();
		amountFloatingDustCrafting = (byte) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(FLOATER, "amountFloaterDamage", amountFloaterDamage, null, 0, 64 * 9);
		prop.comment = "Damage amount of Ofalen Floater when the player float." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountFloaterDamage = (short) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(FLOATER, "intervalFloaterDamage", intervalFloaterDamage, null, 0, Byte.MAX_VALUE);
		prop.comment = "Damage interval of Ofalen Floater." + unitTick + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		intervalFloaterDamage = (byte) prop.getInt();
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(FLOATER, propOrder);
		// Future.Collector
		cfg.setCategoryLanguageKey(COLLECTOR, keyCategory + COLLECTOR);
		propOrder = new ArrayList<String>();
		//
		prop = cfg.get(COLLECTOR, "amountCollectingLumpCrafting", amountCollectingLumpCrafting, null, 1, 64);
		prop.comment = "Crafting amount of \"Lump of Ofalen Collecting\" when using normal recipe." + restart + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		prop.requiresMcRestart();
		amountCollectingLumpCrafting = (byte) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(COLLECTOR, "amountCollectorDamageItem", amountCollectorDamageItem, null, 0, 64 * 9);
		prop.comment = "Damage amount of Ofalen Collector when item is collected." + separator + "Per 1 item." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountCollectorDamageItem = (short) prop.getInt();
		propOrder.add(prop.getName());
		//
		prop = cfg.get(COLLECTOR, "amountCollectorDamageExp", amountCollectorDamageExp, null, 0, 64 * 9);
		prop.comment = "Damage amount of Ofalen Collector when experience orb is collected." + separator + "Per 1 exp." + getIntGuide(prop);
		prop.setLanguageKey(keyProp + prop.getName());
		amountCollectorDamageExp = (short) prop.getInt();
		propOrder.add(prop.getName());
		cfg.setCategoryPropertyOrder(COLLECTOR, propOrder);
		cfg.save();
	}

	private static String getBoolGuide(Property prop) {
		return Configuration.NEW_LINE + " [default: " + prop.getDefault() + "]";
	}

	private static String getIntGuide(Property prop) {
		return Configuration.NEW_LINE + " [range: " + prop.getMinValue() + " ~ " + prop.getMaxValue() + ", default: " + prop.getDefault() + "]";
	}
}
