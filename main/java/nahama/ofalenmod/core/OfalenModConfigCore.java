package nahama.ofalenmod.core;

import nahama.ofalenmod.Log;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class OfalenModConfigCore {

	//config設定項目の定義
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

	/**configを設定する*/
	public static void registerConfig (FMLPreInitializationEvent event) {

		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile(), true);

		try {
			cfg.load();
			enabledConfig = cfg.get("General", "ConfigEnabled", false, "If true config enabled[false]").getBoolean();
			sizeExplosion = cfg.get("General", "ExplosionSize", 2, "Explosion size of Explosion Ball[2]").getInt();
			efficiencyPerfectTool = (float) cfg.get("General", "PerfectToolEfficiency", 100.0, "Efficiency of Ofalen Perfect Tool[100.0]").getDouble();

			amountDrop = cfg.get("General.Difficulty", "DropAmount", 3, "Drop amount of Ofalen Fragment from Ofalen Ore[3]").getInt();

			enabledGenerator = cfg.get("General.Generate", "GeneraterEnabled", true, "If true Ofalen Ore is generated[true]").getBoolean();
			limitGeneration = cfg.get("General.Generate", "GenerationLimit", 8, "Generation limit size of Ofalen Ore[8]").getInt();
			probabilityGeneration = cfg.get("General.Generate", "GenerationProbability", 1, "Generation probability of Ofalen Ore[1]").getInt();
			probabilityGenerationLode = cfg.get("General.Generate", "GenerationProbabilityLode", 1, "Generation probability of Huge Ofalen Ore Lode{1~10000}[1](/10000)").getInt();

			timeBurnOfalen = cfg.get("General.Machine", "BurnTimeOfalen", 2000, "Burn time of Ofalen Fuel[2000](tick)").getInt();
			timeBurnStone = cfg.get("General.Machine", "BurnTimeStone", 200, "Burn time of Stone Fuel[200](tick)").getInt();
			timeConversion = cfg.get("General.Machine", "ConversionTime", 1200, "Conversion time of Ofalen Conversion Machine[1200](tick)").getInt();
			timeRepair = cfg.get("General.Machine", "RepairTime", 40, "Repair time of Ofalen Repair Machine[40](tick)").getInt();
			amountSmelting = cfg.get("General.Machine", "SmeltingAmount", 1, "Smelting size of Ofalen Smelting Machine from Ofalen Ore to Ofalen[1]").getInt();
			timeSmelting = cfg.get("General.Machine", "SmeltingTime", 1200, "Smelting time of Ofalen Smelting Machine[1200](tick)").getInt();

			recipeLump = cfg.get("General.Recipe", "LumpRecipe", 7, "Recipe of Lump of Stone. Number is location of space[7]\n0 1 2\n3 4 5\n6 7 8").getInt();
		} catch (Exception error) {
			Log.error("Config loading error", "OfalenModConfigCore", true);
		} finally {
			cfg.save();
		}

		if (!enabledConfig) {
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
		if (amountDrop < 1) amountDrop = 1;
		if (recipeLump < 0 || recipeLump >= 9) recipeLump = 7;

	}
}
