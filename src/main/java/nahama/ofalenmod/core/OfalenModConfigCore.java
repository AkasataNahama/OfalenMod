package nahama.ofalenmod.core;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import nahama.ofalenmod.util.OfalenLog;
import net.minecraftforge.common.config.Configuration;

public class OfalenModConfigCore {
	public static Configuration cfg;
	// General
	public static boolean isUpdateCheckEnabled = true;
	public static float efficiencyPerfectTool = 100;
	public static byte sizeExplosion = 2;
	// Difficulty
	public static byte amountDrop = 3;
	// Generate
	public static boolean isGeneratorEnabled = true;
	public static byte limitGeneration = 8;
	public static byte frequencyGeneration = 3;
	public static short probabilityLodeGeneration = 1;
	// Machine
	public static short factorFurnaceFuelBurningTime = 256;
	// TODO バランス調整：半分に
	public static short timeStoneFuelBurning = 400;
	public static short timeOfalenFuelBurning = 4800;
	public static short timeSmelting = 1200;
	public static short timeConverting = 1200;
	public static short timeRepairing = 40;
	public static short timeFusing = 1200;
	public static byte baseOfalenSmeltingAmount = 1;
	// Recipe
	public static byte blankLumpRecipe = 7;
	public static byte amountShieldIngotRecipe = 32;
	public static byte amountTeleportPearlRecipe = 32;
	public static byte amountFloatDustRecipe = 32;
	public static byte amountCollectingLampRecipe = 64;
	// Future
	public static short amountShieldDamage = 1;
	public static short amountTeleporterDamage = 1;
	public static short amountFloaterDamage = 1;
	public static short amountCollectorDamageItem = 1;
	public static short amountCollectorDamageExp = 1;
	public static byte intervalFloaterDamage = 20;
	// Category
	public static final String GENERAL = "General";
	private static final String DIFFICULTY = GENERAL + ".Difficulty";
	private static final String GENERATE = GENERAL + ".Generate";
	private static final String MACHINE = GENERAL + ".Machine";
	private static final String RECIPE = GENERAL + ".Recipe";
	private static final String FUTURE = GENERAL + ".Future";

	/** Configを読み込む。 */
	public static void loadConfig(FMLPreInitializationEvent event) {
		cfg = new Configuration(event.getSuggestedConfigurationFile(), true);
		cfg.load();
		syncConfig();
	}

	/** Configを同期する。 */
	public static void syncConfig() {
		String separator = System.lineSeparator();
		String restart = separator + "Please restart Minecraft to reflect this setting." + separator;
		try {
			// General
			isUpdateCheckEnabled = cfg.getBoolean("enabledUpdateCheck", GENERAL, isUpdateCheckEnabled, "Update Check is valid when true." + restart);
			efficiencyPerfectTool = cfg.getFloat("efficiencyPerfectTool", GENERAL, efficiencyPerfectTool, 1, Float.MAX_VALUE, "Efficiency of Ofalen Perfect Tool." + restart);
			sizeExplosion = (byte) cfg.getInt("sizeExplosion", GENERAL, sizeExplosion, 0, Byte.MAX_VALUE, "Explosion size of Explosion Ball.");
			// Difficulty
			amountDrop = (byte) cfg.getInt("amountDrop", DIFFICULTY, amountDrop, 1, Byte.MAX_VALUE, "Drop amount of Ofalen Fragment from Ofalen Ore.");
			// Generate
			isGeneratorEnabled = cfg.getBoolean("enabledGenerator", GENERATE, isGeneratorEnabled, "Ofalen Ore will be generated when true.");
			limitGeneration = (byte) cfg.getInt("limitGeneration", GENERATE, limitGeneration, 1, Byte.MAX_VALUE, "Maximum generation size of Ofalen Ore.");
			frequencyGeneration = (byte) cfg.getInt("GenerationFrequency", GENERATE, frequencyGeneration, 1, Byte.MAX_VALUE, "Generation Frequency of Ofalen Ore.");
			probabilityLodeGeneration = (short) cfg.getInt("probabilityLodeGeneration", GENERATE, probabilityLodeGeneration, 1, 10000, "Generation probability of Huge Ofalen Ore Lode.(/10000)");
			// Machine
			factorFurnaceFuelBurningTime = (short) cfg.getInt("factorFurnaceFuelBurningTime", MACHINE, factorFurnaceFuelBurningTime, 1, Short.MAX_VALUE, "Burning time factor of furnace fuel.");
			timeStoneFuelBurning = (short) cfg.getInt("timeStoneFuelBurning", MACHINE, timeStoneFuelBurning, 1, Short.MAX_VALUE, "Burning time of Stone Fuel.(tick)");
			timeOfalenFuelBurning = (short) cfg.getInt("timeOfalenFuelBurning", MACHINE, timeOfalenFuelBurning, 1, Short.MAX_VALUE, "Burning time of Ofalen Fuel.(tick)");
			timeSmelting = (short) cfg.getInt("timeSmelting", MACHINE, timeSmelting, 1, Short.MAX_VALUE, "Working time of Ofalen Smelting Machine.(tick)");
			timeConverting = (short) cfg.getInt("timeConverting", MACHINE, timeConverting, 1, Short.MAX_VALUE, "Working time of Ofalen Converting Machine.(tick)");
			timeRepairing = (short) cfg.getInt("timeRepairing", MACHINE, timeRepairing, 1, Short.MAX_VALUE, "Working time of Ofalen Repairing Machine.(tick)");
			timeFusing = (short) cfg.getInt("timeFusing", MACHINE, timeFusing, 1, Short.MAX_VALUE, "Working time of Ofalen Fusing Machine.(tick)");
			baseOfalenSmeltingAmount = (byte) cfg.getInt("baseOfalenSmeltingAmount", MACHINE, baseOfalenSmeltingAmount, 1, 64, "Smelting amount of Ofalen Smelting Machine from Ofalen Ore to Ofalen." + restart);
			// Recipe
			blankLumpRecipe = (byte) cfg.getInt("blankLumpRecipe", RECIPE, blankLumpRecipe, 0, 8, "Recipe of \"Lump of Stone\". Number is location of space." + separator + "0 1 2" + separator + "3 4 5" + separator + "6 7 8" + restart);
			amountShieldIngotRecipe = (byte) cfg.getInt("amountShieldIngotRecipe", RECIPE, amountShieldIngotRecipe, 1, 64, "Amount of Ofalen Shield Ingot when made by normal recipe." + restart);
			amountTeleportPearlRecipe = (byte) cfg.getInt("amountTeleportPearlRecipe", RECIPE, amountTeleportPearlRecipe, 1, 64, "Amount of Ofalen Teleport Pearl when made by normal recipe." + restart);
			amountFloatDustRecipe = (byte) cfg.getInt("amountFloatDustRecipe", RECIPE, amountFloatDustRecipe, 1, 64, "Amount of Ofalen Float Dust when made by normal recipe." + restart);
			amountCollectingLampRecipe = (byte) cfg.getInt("amountCollectingLampRecipe", RECIPE, amountCollectingLampRecipe, 1, 64, "Amount of Ofalen Collecting Dust when made by normal recipe." + restart);
			// Future
			amountShieldDamage = (short) cfg.getInt("amountShieldDamage", FUTURE, amountShieldDamage, 0, 64 * 9, "Damage amount of Ofalen Shield when protect player.");
			amountTeleporterDamage = (short) cfg.getInt("amountTeleporterDamage", FUTURE, amountTeleporterDamage, 0, 64, "Damage amount of Ofalen Teleporter when teleport player.");
			amountFloaterDamage = (short) cfg.getInt("amountFloaterDamage", FUTURE, amountFloaterDamage, 0, 64 * 9, "Damage amount of Ofalen Floater when float player.");
			amountCollectorDamageItem = (short) cfg.getInt("amountCollectorDamageItem", FUTURE, amountCollectorDamageItem, 0, 64 * 9, "Damage amount of Ofalen Collector when collect item.");
			amountCollectorDamageExp = (short) cfg.getInt("amountCollectorDamageExp", FUTURE, amountCollectorDamageExp, 0, 64 * 9, "Damage amount of Ofalen Collector when collect experience orb.");
			intervalFloaterDamage = (byte) cfg.getInt("intervalFloaterDamage", FUTURE, intervalFloaterDamage, 0, Byte.MAX_VALUE, "Damage interval of Ofalen Floater.(tick)");
		} catch (Exception e) {
			OfalenLog.error("Error on loading config.", "OfalenModConfigCore");
		} finally {
			cfg.save();
		}
	}
}
