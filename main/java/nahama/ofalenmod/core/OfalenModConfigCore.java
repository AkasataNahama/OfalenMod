package nahama.ofalenmod.core;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import nahama.ofalenmod.Log;
import net.minecraftforge.common.config.Configuration;

public class OfalenModConfigCore {

	public static boolean isConfigEnabled;
	public static int sizeExplosion;
	public static float efficiencyPerfectTool;

	public static int amountDrop;

	public static boolean isGeneratorEnabled;
	public static int limitGeneration;
	public static int frequencyGeneration;
	public static int probabilityGenerationLode;

	public static int timeBurnOfalen;
	public static int timeBurnStone;
	public static int timeConversion;
	public static int timeRepair;
	public static int timeSmelting;
	public static int amountSmelting;

	public static int recipeLump;

	private static final String GENERAL = "General";
	private static final String DIFFICULTY = GENERAL + ".Difficulty";
	private static final String GENERATE = GENERAL + ".Generate";
	private static final String MACHINE = GENERAL + ".Machine";
	private static final String RECIPE = GENERAL + ".Recipe";

	/** configを読み込む処理。 */
	public static void loadConfig(FMLPreInitializationEvent event) {

		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile(), true);

		try {
			cfg.load();
			isConfigEnabled = cfg.getBoolean("ConfigIsEnabled", GENERAL, false, "Config is valid when true.");
			sizeExplosion = cfg.getInt("ExplosionSize", GENERAL, 2, 0, 255, "Explosion size of Explosion Ball");
			efficiencyPerfectTool = cfg.getFloat("PerfectToolEfficiency", GENERAL, 100, 1, Float.MAX_VALUE, "Efficiency of Ofalen Perfect Tool");

			amountDrop = cfg.getInt("DropAmount", DIFFICULTY, 3, 1, 255, "Drop amount of Ofalen Fragment from Ofalen Ore");

			isGeneratorEnabled = cfg.getBoolean("GeneratorEnabled", GENERATE, true, "Ofalen Ore is generated when true.");
			limitGeneration = cfg.getInt("GenerationLimit", GENERATE, 8, 1, 255, "Max generation size of Ofalen Ore");
			frequencyGeneration = cfg.getInt("GenerationFrequency", GENERATE, 3, 1, 255, "Generation Frequency of Ofalen Ore");
			probabilityGenerationLode = cfg.getInt("GenerationProbabilityLode", GENERATE, 1, 1, 10000, "Generation probability of Huge Ofalen Ore Lode(/10000)");

			timeBurnOfalen = cfg.getInt("BurnTimeOfalen", MACHINE, 2000, 1, Integer.MAX_VALUE, "Burn time of Ofalen Fuel(tick)");
			timeBurnStone = cfg.getInt("BurnTimeStone", MACHINE, 200, 1, Integer.MAX_VALUE, "Burn time of Stone Fuel(tick)");
			timeConversion = cfg.getInt("ConversionTime", MACHINE, 1200, 1, Integer.MAX_VALUE, "Conversion time of Ofalen Conversion Machine(tick)");
			timeRepair = cfg.getInt("RepairTime", MACHINE, 40, 1, Integer.MAX_VALUE, "Repair time of Ofalen Repair Machine(tick)");
			amountSmelting = cfg.getInt("SmeltingAmount", MACHINE, 1, 1, 64, "Smelting amount of Ofalen Smelting Machine from Ofalen Ore to Ofalen");
			timeSmelting = cfg.getInt("SmeltingTime", MACHINE, 1200, 1, Integer.MAX_VALUE, "Smelting time of Ofalen Smelting Machine(tick)");

			recipeLump = cfg.getInt("LumpRecipe", RECIPE, 7, 0, 8, "Recipe of \"Lump of Stone\". Number is location of space.\n0 1 2\n3 4 5\n6 7 8\n");
		} catch (Exception error) {
			Log.error("Error on config loading", "OfalenModConfigCore.loadConfig", true);
		} finally {
			cfg.save();
		}

		if (isConfigEnabled)
			return;
		sizeExplosion = 2;
		efficiencyPerfectTool = 100F;

		amountDrop = 3;

		isGeneratorEnabled = true;
		limitGeneration = 8;
		frequencyGeneration = 3;
		probabilityGenerationLode = 1;

		timeBurnOfalen = 2000;
		timeBurnStone = 200;
		timeConversion = 1200;
		timeRepair = 40;
		amountSmelting = 1;
		timeSmelting = 1200;

		recipeLump = 7;
	}

}
