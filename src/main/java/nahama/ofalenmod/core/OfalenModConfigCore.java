package nahama.ofalenmod.core;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import nahama.ofalenmod.OfalenModCore;
import net.minecraftforge.common.config.Configuration;

public class OfalenModConfigCore {
	// カテゴリー名
	public static final String GENERAL = "General";
	private static final String CATEGORY_SEPARATOR = ".";
	private static final String RECIPE = GENERAL + CATEGORY_SEPARATOR + "Recipe";
	private static final String ORE = GENERAL + CATEGORY_SEPARATOR + "Ore";
	private static final String GENERATE = ORE + CATEGORY_SEPARATOR + "Generate";
	private static final String TOOL = GENERAL + CATEGORY_SEPARATOR + "Tool";
	private static final String PERFECT_TOOL = TOOL + CATEGORY_SEPARATOR + "Tool";
	private static final String BALL = GENERAL + CATEGORY_SEPARATOR + "Ball";
	private static final String EXPLOSION_BALL = BALL + CATEGORY_SEPARATOR + "ExplosionBall";
	private static final String MACHINE = GENERAL + CATEGORY_SEPARATOR + "Machine";
	private static final String SMELTING = MACHINE + CATEGORY_SEPARATOR + "Smelting";
	private static final String CONVERTING = MACHINE + CATEGORY_SEPARATOR + "Converting";
	private static final String REPAIRING = MACHINE + CATEGORY_SEPARATOR + "Repairing";
	private static final String FUSING = MACHINE + CATEGORY_SEPARATOR + "Fusing";
	private static final String FUTURE = GENERAL + CATEGORY_SEPARATOR + "Future";
	private static final String SHIELD = FUTURE + CATEGORY_SEPARATOR + "Shield";
	private static final String TELEPORTER = FUTURE + CATEGORY_SEPARATOR + "Teleporter";
	private static final String FLOATER = FUTURE + CATEGORY_SEPARATOR + "Floater";
	private static final String COLLECTOR = FUTURE + CATEGORY_SEPARATOR + "Collector";
	public static Configuration cfg;
	// General
	public static boolean isUpdateCheckEnabled = true;
	// Recipe
	public static byte amountShieldIngotCrafting = 4;
	public static byte amountTeleportPearlCrafting = 4;
	public static byte amountFloatDustCrafting = 4;
	public static byte amountCollectingLampCrafting = 64;
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
	// Future.Shield
	public static short amountShieldDamage = 1;
	// Future.Teleporter
	public static short amountTeleporterDamage = 1;
	// Future.Floater
	public static short amountFloaterDamage = 1;
	public static byte intervalFloaterDamage = 20;
	// Future.Collector
	public static short amountCollectorDamageItem = 1;
	public static short amountCollectorDamageExp = 1;

	/** Configを読み込む。 */
	public static void loadConfig(FMLPreInitializationEvent event) {
		cfg = new Configuration(event.getSuggestedConfigurationFile(), OfalenModCore.VERSION, true);
		// TODO [1.7.10]2.0.0へのアップデート時、Configリセットを勧告する？
		//		if (VersionUtil.compareVersion(new VersionUtil.VersionString(OfalenModCore.MOD_ID, cfg.getLoadedConfigVersion()), OfalenModCore.VERSION_STRING))
		syncConfig();
	}

	/** Configを同期する。 */
	public static void syncConfig() {
		String separator = Configuration.NEW_LINE;
		String restart = separator + " [Minecraft Restart Required]" + separator;
		String keyCategory = "config.ofalen.category.";
		String keyProp = "config.ofalen.prop.";
		// General
		cfg.setCategoryLanguageKey(GENERAL, keyCategory + "general");
		cfg.setCategoryRequiresMcRestart(GENERAL, true);
		isUpdateCheckEnabled = cfg.getBoolean("enableUpdateCheck", GENERAL, isUpdateCheckEnabled, "Set this to true to enable update check of Ofalen Mod." + restart, keyProp + "isUpdateCheckEnabled");
		// Recipe
		cfg.setCategoryLanguageKey(RECIPE, keyCategory + "recipe");
		cfg.setCategoryRequiresMcRestart(RECIPE, true);
		amountShieldIngotCrafting = (byte) cfg.getInt("amountShieldIngotCrafting", RECIPE, amountShieldIngotCrafting, 1, 64, "Crafting amount of Ofalen Shield Ingot when using normal recipe." + restart, keyProp + "amountShieldIngotCrafting");
		amountTeleportPearlCrafting = (byte) cfg.getInt("amountTeleportPearlCrafting", RECIPE, amountTeleportPearlCrafting, 1, 64, "Crafting amount of Ofalen Teleport Pearl when using normal recipe." + restart, keyProp + "amountTeleportPearlCrafting");
		amountFloatDustCrafting = (byte) cfg.getInt("amountFloatDustCrafting", RECIPE, amountFloatDustCrafting, 1, 64, "Crafting amount of Ofalen Float Dust when using normal recipe." + restart, keyProp + "amountFloatDustCrafting");
		amountCollectingLampCrafting = (byte) cfg.getInt("amountCollectingLampCrafting", RECIPE, amountCollectingLampCrafting, 1, 64, "Crafting amount of Ofalen Collecting Dust when using normal recipe." + restart, keyProp + "amountCollectingLampCrafting");
		baseOfalenSmeltingAmount = (byte) cfg.getInt("baseOfalenSmeltingAmount", RECIPE, baseOfalenSmeltingAmount, 1, 16, "Smelting amount of Ofalen from Ofalen Ore using Ofalen Smelting Machine." + separator + "When don't use Ofalen Machine Processor." + restart, keyProp + "baseOfalenSmeltingAmount");
		String diagram = separator + " -------" + separator + " |0 1 2|" + separator + " |3 4 5|" + separator + " |6 7 8|" + separator + " -------";
		positionStoneLumpRecipeBlank = (byte) cfg.getInt("positionStoneLumpRecipeBlank", RECIPE, positionStoneLumpRecipeBlank, 0, 8, "The number is position of space on recipe of Lump of Stone." + diagram + restart, keyProp + "positionStoneLumpRecipeBlank");
		// Ore
		cfg.setCategoryLanguageKey(ORE, keyCategory + "ore");
		amountDrop = (byte) cfg.getInt("amountDrop", ORE, amountDrop, 1, Byte.MAX_VALUE, "Drop amount of Ofalen Fragment when Ofalen Ore is mined." + separator, keyProp + "amountDrop");
		// Ore.Generate
		cfg.setCategoryLanguageKey(GENERATE, keyCategory + "generate");
		isGeneratorEnabled = cfg.getBoolean("enableGenerator", GENERATE, isGeneratorEnabled, "Set this to true to generate Ofalen Ore into newly generated chunk." + separator, keyProp + "isGeneratorEnabled");
		frequencyGeneration = (byte) cfg.getInt("frequencyGeneration", GENERATE, frequencyGeneration, 1, Byte.MAX_VALUE, "The number of Ofalen Ore generation of each color for each chunk." + separator, keyProp + "frequencyGeneration");
		limitGeneration = (byte) cfg.getInt("limitGeneration", GENERATE, limitGeneration, 1, Byte.MAX_VALUE, "Maximum size of Ofalen Ore per generation." + separator, keyProp + "limitGeneration");
		probLodeGeneration = (short) cfg.getInt("probLodeGeneration", GENERATE, probLodeGeneration, 1, 10000, "Generation probability of Huge Ofalen Ore Lode." + separator + "Calculation of probability is performed on each chunk." + separator + " (x/10000)" + separator, keyProp + "probLodeGeneration");
		// Tool
		cfg.setCategoryLanguageKey(TOOL, keyCategory + "tool");
		// Tool.PerfectTool
		cfg.setCategoryLanguageKey(PERFECT_TOOL, keyCategory + "toolPerfect");
		rangeMax = (byte) cfg.getInt("rangeMax", PERFECT_TOOL, rangeMax, 0, Byte.MAX_VALUE, "Maximum range of Range Breaking Mode of Ofalen Perfect Tool.", keyProp + "rangeMax");
		// Ball
		cfg.setCategoryLanguageKey(BALL, keyCategory + "ball");
		// Ball.ExplosionBall
		cfg.setCategoryLanguageKey(EXPLOSION_BALL, keyCategory + "ballExplosion");
		sizeExplosion = (byte) cfg.getInt("sizeExplosion", EXPLOSION_BALL, sizeExplosion, 0, Byte.MAX_VALUE, "Explosion size of Explosion Ball." + separator, keyProp + "sizeExplosion");
		// Machine
		cfg.setCategoryLanguageKey(MACHINE, keyCategory + "machine");
		divisorBurningTime = (short) cfg.getInt("divisorBurningTime", MACHINE, divisorBurningTime, 1, Short.MAX_VALUE, "Divisor of burning time when using furnace fuel for machines." + separator, keyProp + "divisorBurningTime");
		timeTDiamondBurning = (short) cfg.getInt("timeTDiamondBurning", MACHINE, timeTDiamondBurning, 1, Short.MAX_VALUE, "Burning time of Creeper Magic Stone by Takumi Craft." + separator + " (tick)" + separator, keyProp + "timeTDiamondBurning");
		timeWhiteFuelBurning = (short) cfg.getInt("timeWhiteFuelBurning", MACHINE, timeWhiteFuelBurning, 1, Short.MAX_VALUE, "Burning time of White Ofalen Fuel." + separator + " (tick)" + separator, keyProp + "timeWhiteFuelBurning");
		// Machine.Smelting
		cfg.setCategoryLanguageKey(SMELTING, keyCategory + "smelting");
		timeSmelting = (short) cfg.getInt("timeSmelting", SMELTING, timeSmelting, 1, Short.MAX_VALUE, "The time Ofalen Smelting Machine requires for every smelting." + separator + " (tick)" + separator, keyProp + "timeSmelting");
		// Machine.Converting
		cfg.setCategoryLanguageKey(CONVERTING, keyCategory + "converting");
		timeConverting = (short) cfg.getInt("timeConverting", CONVERTING, timeConverting, 1, Short.MAX_VALUE, "The time Ofalen Converting Machine requires for every converting." + separator + " (tick)" + separator, keyProp + "timeConverting");
		// Machine.Repairing
		cfg.setCategoryLanguageKey(REPAIRING, keyCategory + "repairing");
		timeRepairing = (short) cfg.getInt("timeRepairing", REPAIRING, timeRepairing, 1, Short.MAX_VALUE, "The time Ofalen Repairing Machine requires for every repairing." + separator + "Per 1 durability." + separator + " (tick)" + separator, keyProp + "timeRepairing");
		// Machine.Fusing
		cfg.setCategoryLanguageKey(FUSING, keyCategory + "fusing");
		timeFusing = (short) cfg.getInt("timeFusing", FUSING, timeFusing, 1, Short.MAX_VALUE, "The time Ofalen Fusing Machine requires for every fusing." + separator + " (tick)" + separator, keyProp + "timeFusing");
		// Future
		cfg.setCategoryLanguageKey(FUTURE, keyCategory + "future");
		// Future.Shield
		cfg.setCategoryLanguageKey(SHIELD, keyCategory + "shield");
		amountShieldDamage = (short) cfg.getInt("amountShieldDamage", SHIELD, amountShieldDamage, 0, 64 * 9, "Damage amount of Ofalen Shield when the player is protected." + separator + "Per 1 damage of player." + separator, keyProp + "amountShieldDamage");
		// Future.Teleporter
		cfg.setCategoryLanguageKey(TELEPORTER, keyCategory + "teleporter");
		amountTeleporterDamage = (short) cfg.getInt("amountTeleporterDamage", TELEPORTER, amountTeleporterDamage, 0, 64, "Damage amount of Ofalen Teleporter when the player teleport." + separator, keyProp + "amountTeleporterDamage");
		// Future.Floater
		cfg.setCategoryLanguageKey(FLOATER, keyCategory + "floater");
		amountFloaterDamage = (short) cfg.getInt("amountFloaterDamage", FLOATER, amountFloaterDamage, 0, 64 * 9, "Damage amount of Ofalen Floater when the player float." + separator, keyProp + "amountFloaterDamage");
		intervalFloaterDamage = (byte) cfg.getInt("intervalFloaterDamage", FLOATER, intervalFloaterDamage, 0, Byte.MAX_VALUE, "Damage interval of Ofalen Floater." + separator + " (tick)", keyProp + "intervalFloaterDamage");
		// Future.Collector
		cfg.setCategoryLanguageKey(COLLECTOR, keyCategory + "collector");
		amountCollectorDamageItem = (short) cfg.getInt("amountCollectorDamageItem", COLLECTOR, amountCollectorDamageItem, 0, 64 * 9, "Damage amount of Ofalen Collector when item is collected." + separator + "Per 1 item." + separator, keyProp + "amountCollectorDamageItem");
		amountCollectorDamageExp = (short) cfg.getInt("amountCollectorDamageExp", COLLECTOR, amountCollectorDamageExp, 0, 64 * 9, "Damage amount of Ofalen Collector when experience orb is collected." + separator + "Per 1 exp." + separator, keyProp + "amountCollectorDamageExp");
		cfg.save();
	}
}
