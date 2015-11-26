package nahama.ofalenmod.core;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import nahama.ofalenmod.Log;
import net.minecraftforge.common.config.Configuration;

public class OfalenModConfigCore {

	public static boolean enabledConfig;
	public static int sizeExplosion;
	public static float efficiencyPerfectTool;

	public static int amountDrop;

	public static boolean enabledGenerator;
	public static int limitGeneration;
	public static int probabilityGeneration;
	public static int probabilityGenerationLode;

	public static int timeBurnOfalen;
	public static int timeBurnStone;
	public static int timeConversion;
	public static int timeRepair;
	public static int timeSmelting;
	public static int amountSmelting;

	public static int recipeLump;

	/** configを読み込む処理。 */
	public static void loadConfig(FMLPreInitializationEvent event) {

		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile(), true);

		try {
			cfg.load();
			enabledConfig = cfg.getBoolean("General", "ConfigEnabled", false, "If true Config is valid.[false]");
			sizeExplosion = cfg.getInt("General", "ExplosionSize", 2, 0, 255, "Explosion size of Explosion Ball[2]");
			efficiencyPerfectTool = cfg.getFloat("General", "PerfectToolEfficiency", 100F, 1F, 1000000F, "Efficiency of Ofalen Perfect Tool[100.0]");

			amountDrop = cfg.getInt("General.Difficulty", "DropAmount", 3, 1, 255, "Drop amount of Ofalen Fragment from Ofalen Ore[3]");

			enabledGenerator = cfg.getBoolean("General.Generate", "GeneraterEnabled", true, "If true Ofalen Ore is generated.[true]");
			limitGeneration = cfg.getInt("General.Generate", "GenerationLimit", 8, 1, 255, "Max generation size of Ofalen Ore[8]");
			probabilityGeneration = cfg.getInt("General.Generate", "GenerationProbability", 1, 1, 255, "Generation probability of Ofalen Ore[1]");
			probabilityGenerationLode = cfg.getInt("General.Generate", "GenerationProbabilityLode", 1, 1, 100000, "Generation probability of Huge Ofalen Ore Lode[1](/10000)");

			timeBurnOfalen = cfg.getInt("General.Machine", "BurnTimeOfalen", 2000, 1, Integer.MAX_VALUE, "Burn time of Ofalen Fuel[2000](tick)");
			timeBurnStone = cfg.getInt("General.Machine", "BurnTimeStone", 200, 1, Integer.MAX_VALUE, "Burn time of Stone Fuel[200](tick)");
			timeConversion = cfg.getInt("General.Machine", "ConversionTime", 1200, 1, Integer.MAX_VALUE, "Conversion time of Ofalen Conversion Machine[1200](tick)");
			timeRepair = cfg.getInt("General.Machine", "RepairTime", 40, 1, Integer.MAX_VALUE, "Repair time of Ofalen Repair Machine[40](tick)");
			amountSmelting = cfg.getInt("General.Machine", "SmeltingAmount", 1, 1, 64, "Smelting amount of Ofalen Smelting Machine from Ofalen Ore to Ofalen[1]");
			timeSmelting = cfg.getInt("General.Machine", "SmeltingTime", 1200, 1, Integer.MAX_VALUE, "Smelting time of Ofalen Smelting Machine[1200](tick)");

			recipeLump = cfg.getInt("General.Recipe", "LumpRecipe", 7, 0, 8, "Recipe of \"Lump of Stone\". Number is location of space.[7]\n0 1 2\n3 4 5\n6 7 8");
		} catch (Exception error) {
			Log.error("Config loading error", "OfalenModConfigCore.loadConfig", true);
		} finally {
			cfg.save();
		}

		if (enabledConfig)
			return;
		sizeExplosion = 2;
		efficiencyPerfectTool = 100.0F;

		amountDrop = 3;

		enabledGenerator = true;
		limitGeneration = 8;
		probabilityGeneration = 1;
		probabilityGenerationLode = 1;

		timeBurnOfalen = 2000;
		timeBurnStone = 200;
		timeConversion = 1200;
		timeRepair = 40;
		timeSmelting = 1200;
		amountSmelting = 1;

		recipeLump = 7;
	}

}
