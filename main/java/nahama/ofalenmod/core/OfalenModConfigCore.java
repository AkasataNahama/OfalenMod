package nahama.ofalenmod.core;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import nahama.ofalenmod.Log;
import net.minecraftforge.common.config.Configuration;

public class OfalenModConfigCore {

	public static boolean isConfigEnabled = false;
	public static int sizeExplosion = 2;
	public static float efficiencyPerfectTool = 100;

	public static int amountDrop = 3;

	public static boolean isGeneratorEnabled = true;
	public static int limitGeneration = 8;
	public static int frequencyGeneration = 3;
	public static int probabilityGenerationLode = 1;

	public static int factorTimeBurnFuelFurnace = 256;
	public static int timeBurnOfalen = 4800;
	public static int timeBurnStone = 400;
	public static int timeConversion = 1200;
	public static int timeRepair = 40;
	public static int amountSmelting = 1;
	public static int timeSmelting = 1200;

	public static int recipeLump = 7;
	public static int amountIngotShield = 4;
	public static int amountPearlTeleport = 4;
	public static int amountDustFloat = 4;

	public static int amountDamageShield = 1;
	public static int amountDamageTeleporter = 1;
	public static int amountDamageFloater = 1;
	public static byte intervalDamageFloater = 20;

	private static final String GENERAL = "General";
	private static final String DIFFICULTY = GENERAL + ".Difficulty";
	private static final String GENERATE = GENERAL + ".Generate";
	private static final String MACHINE = GENERAL + ".Machine";
	private static final String RECIPE = GENERAL + ".Recipe";
	private static final String FUTURE = GENERAL + ".Future";

	/** configを読み込む処理。 */
	public static void loadConfig(FMLPreInitializationEvent event) {

		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile(), true);

		try {
			cfg.load();
			isConfigEnabled = cfg.getBoolean("ConfigIsEnabled", GENERAL, isConfigEnabled, "Config is valid when true.");
			sizeExplosion = cfg.getInt("ExplosionSize", GENERAL, sizeExplosion, 0, 255, "Explosion size of Explosion Ball");
			efficiencyPerfectTool = cfg.getFloat("PerfectToolEfficiency", GENERAL, efficiencyPerfectTool, 1, Float.MAX_VALUE, "Efficiency of Ofalen Perfect Tool");

			amountDrop = cfg.getInt("DropAmount", DIFFICULTY, amountDrop, 1, 255, "Drop amount of Ofalen Fragment from Ofalen Ore");

			isGeneratorEnabled = cfg.getBoolean("GeneratorEnabled", GENERATE, isGeneratorEnabled, "Ofalen Ore is generated when true.");
			limitGeneration = cfg.getInt("GenerationLimit", GENERATE, limitGeneration, 1, 255, "Max generation size of Ofalen Ore");
			frequencyGeneration = cfg.getInt("GenerationFrequency", GENERATE, frequencyGeneration, 1, 255, "Generation Frequency of Ofalen Ore");
			probabilityGenerationLode = cfg.getInt("GenerationProbabilityLode", GENERATE, probabilityGenerationLode, 1, 10000, "Generation probability of Huge Ofalen Ore Lode(/10000)");

			factorTimeBurnFuelFurnace = cfg.getInt("BurnTimeFactorFurnaceFuel", MACHINE, factorTimeBurnFuelFurnace, 1, Integer.MAX_VALUE, "Burn time factor of furnace fuel");
			timeBurnOfalen = cfg.getInt("BurnTimeOfalen", MACHINE, timeBurnOfalen, 1, Integer.MAX_VALUE, "Burn time of Ofalen Fuel(tick)");
			timeBurnStone = cfg.getInt("BurnTimeStone", MACHINE, timeBurnStone, 1, Integer.MAX_VALUE, "Burn time of Stone Fuel(tick)");
			timeConversion = cfg.getInt("ConversionTime", MACHINE, timeConversion, 1, Integer.MAX_VALUE, "Conversion time of Ofalen Conversion Machine(tick)");
			timeRepair = cfg.getInt("RepairTime", MACHINE, timeRepair, 1, Integer.MAX_VALUE, "Repair time of Ofalen Repair Machine(tick)");
			amountSmelting = cfg.getInt("SmeltingAmount", MACHINE, amountSmelting, 1, 64, "Smelting amount of Ofalen Smelting Machine from Ofalen Ore to Ofalen");
			timeSmelting = cfg.getInt("SmeltingTime", MACHINE, timeSmelting, 1, Integer.MAX_VALUE, "Smelting time of Ofalen Smelting Machine(tick)");

			recipeLump = cfg.getInt("LumpRecipe", RECIPE, recipeLump, 0, 8, "Recipe of \"Lump of Stone\". Number is location of space.\n0 1 2\n3 4 5\n6 7 8\n");
			amountIngotShield = cfg.getInt("ShieldIngotAmount", RECIPE, amountIngotShield, 1, 64, "Amount of Ofalen Shield Ingot when made by normal recipe");
			amountPearlTeleport = cfg.getInt("TeleportPearlAmount", RECIPE, amountPearlTeleport, 1, 64, "Amount of Ofalen Teleport Pearl when made by normal recipe");
			amountDustFloat = cfg.getInt("FloatDustAmount", RECIPE, amountDustFloat, 1, 64, "Amount of Ofalen Float Dust when made by normal recipe");

			amountDamageShield = cfg.getInt("ShieldDamageAmount", FUTURE, amountDamageShield, 0, 64 * 9, "Damage amount of Ofalen Shield when protect player");
			amountDamageTeleporter = cfg.getInt("TeleporterDamageAmount", FUTURE, amountDamageTeleporter, 0, 64, "Damage amount of Ofalen Teleporter when player teleport");
			amountDamageFloater = cfg.getInt("FloaterDamageAmount", FUTURE, amountDamageFloater, 0, 64 * 9, "Damage amount of Ofalen Floater when float player");
			intervalDamageFloater = (byte) cfg.getInt("FloaterDamageInterval", FUTURE, intervalDamageFloater, 0, Byte.MAX_VALUE, "Damage interval of Ofalen Floater(tick)");
		} catch (Exception error) {
			Log.error("Error on loading config!", "OfalenModConfigCore.loadConfig", true);
		} finally {
			cfg.save();
		}
		if (isConfigEnabled)
			return;
		sizeExplosion = 2;
		efficiencyPerfectTool = 100;

		amountDrop = 3;

		isGeneratorEnabled = true;
		limitGeneration = 8;
		frequencyGeneration = 3;
		probabilityGenerationLode = 1;

		factorTimeBurnFuelFurnace = 256;
		timeBurnOfalen = 4800;
		timeBurnStone = 400;
		timeConversion = 1200;
		timeRepair = 40;
		amountSmelting = 1;
		timeSmelting = 1200;

		recipeLump = 7;
		amountIngotShield = 4;
		amountPearlTeleport = 4;
		amountDustFloat = 4;

		amountDamageShield = 1;
		amountDamageTeleporter = 1;
		amountDamageFloater = 1;
		intervalDamageFloater = 20;
	}

}
