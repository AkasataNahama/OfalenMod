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
	private static final String LASER = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "laser";
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
	public static boolean isUpdateCheckEnabled;
	public static boolean isPresentBoxEnabled;
	// Material
	public static byte positionStoneLumpRecipeBlank;
	public static byte amountCoverPlateCrafting;
	public static byte amountGrade3PartsCrafting;
	public static byte amountOfalenStickCrafting;
	// Ore
	public static byte amountDrop;
	public static int amountExpMin;
	public static int amountExpMax;
	// Ore.Generate
	public static boolean isGeneratorEnabled;
	public static byte frequencyGeneration;
	public static byte limitGeneration;
	public static double probLodeGeneration;
	// Tool.Perfect
	public static byte rangeMax;
	// Ball.Explosion
	public static byte sizeExplosion;
	// Laser
	public static byte amountLaserCrafting;
	public static byte amountWhiteLaserCrafting;
	public static boolean isLaserSoundEnabled;
	// Machine
	public static byte amountWhiteFuelCrafting;
	public static short divisorBurningTime;
	public static short timeTDiamondBurning;
	public static short timeWhiteFuelBurning;
	// Machine.Smelting
	public static short timeSmelting;
	public static byte baseOfalenSmeltingAmount;
	// Machine.Converting
	public static short timeConverting;
	// Machine.Repairing
	public static short timeRepairing;
	// Machine.Fusing
	public static short timeFusing;
	// Future.Protector
	public static byte amountProtectingIngotCrafting;
	public static short amountProtectingIngotReference;
	public static short amountProtectorDamage;
	public static boolean isProtectingEnabled;
	public static boolean canConsumeMaterialPerDamage;
	public static boolean isProtectorParticleEnabled;
	public static byte typeProtectorParticle;
	// Future.Teleporter
	public static byte amountTeleportingPearlCrafting;
	public static short amountTeleportingPearlReference;
	public static short amountTeleporterDamage;
	public static boolean canTeleportFromEnd;
	public static boolean isTeleporterParticleEnabled;
	public static byte typeTeleporterParticle;
	// Future.Floater
	public static byte amountFloatingDustCrafting;
	public static short amountFloatingDustReference;
	public static short amountFloaterDamage;
	public static byte intervalFloaterDamage;
	public static byte intervalInventorySearch;
	public static boolean canSwitchFloatForm;
	public static boolean isFloaterParticleEnabled;
	public static byte typeFloaterParticle;
	// Future.Collector
	public static byte amountCollectingLumpCrafting;
	public static short amountCollectingLumpReference;
	public static short amountCollectorDamageItem;
	public static short amountCollectorDamageExp;
	public static boolean isCollectorParticleEnabled;
	// WorldEditor
	public static byte amountDarkFuelCrafting;
	public static short energyDarkFuel;

	/** Configを読み込む。 */
	public static void loadConfig(File fileConfig) {
		cfg = new Configuration(fileConfig, OfalenModCore.VERSION);
		syncConfig();
	}

	/** Configを同期する。 */
	public static void syncConfig() {
		ConfigRegister register = new ConfigRegister();
		String separator = Configuration.NEW_LINE;
		String unitTick = separator + " (tick)";
		// General
		register.setCategory(Configuration.CATEGORY_GENERAL);
		// System
		register.setCategory(SYSTEM);
		//
		register.setProperty("enableUpdateCheck", true);
		register.setComment("Set this to true to enable update check of Ofalen Mod.");
		register.setRequiresMcRestart();
		isUpdateCheckEnabled = register.getBoolean();
		//
		register.setProperty("enablePresentBox", true);
		register.setComment("Set this to true to enable Present Box.");
		register.setRequiresWorldRestart();
		isPresentBoxEnabled = register.getBoolean();
		// Material
		register.setCategory(MATERIAL);
		//
		register.setProperty("positionStoneLumpRecipeBlank", 7, 0, 8);
		String diagram = separator + " -------" + separator + " |0 1 2|" + separator + " |3 4 5|" + separator + " |6 7 8|" + separator + " -------";
		register.setComment("The number is position of space on recipe of \"Lump of Stone\"." + diagram);
		register.setRequiresMcRestart();
		positionStoneLumpRecipeBlank = (byte) register.getInt();
		//
		register.setProperty("amountCoverPlateCrafting", 6, 1, 64);
		register.setComment("Crafting amount of \"Machine Cover Plate\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountCoverPlateCrafting = (byte) register.getInt();
		//
		register.setProperty("amountGrade3PartsCrafting", 1, 1, 64);
		register.setComment("Crafting amount of \"Grade 3 Parts\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountGrade3PartsCrafting = (byte) register.getInt();
		//
		register.setProperty("amountOfalenStickCrafting", 1, 1, 64);
		register.setComment("Crafting amount of \"White Ofalen Stick\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountOfalenStickCrafting = (byte) register.getInt();
		// Ore
		register.setCategory(ORE);
		//
		register.setProperty("amountDrop", 3, 0, Byte.MAX_VALUE);
		register.setComment("Drop amount of Ofalen Fragment when Ofalen Ore is mined.");
		amountDrop = (byte) register.getInt();
		//
		register.setProperty("amountExpMin", 3, 0, Integer.MAX_VALUE);
		register.setComment("Minimum amount of experience when Ofalen Ore is mined.");
		amountExpMin = register.getInt();
		//
		register.setProperty("amountExpMax", 7, 0, Integer.MAX_VALUE);
		register.setComment("Maximum amount of experience when Ofalen Ore is mined.");
		amountExpMax = register.getInt();
		// Ore.Generate
		register.setCategory(GENERATE);
		//
		register.setProperty("enableGenerator", true);
		register.setComment("Set this to true to generate Ofalen Ore into newly generated chunk.");
		isGeneratorEnabled = register.getBoolean();
		//
		register.setProperty("frequencyGeneration", 3, 0, Byte.MAX_VALUE);
		register.setComment("The number of Ofalen Ore generation of each color for each chunk.");
		frequencyGeneration = (byte) register.getInt();
		//
		register.setProperty("limitGeneration", 8, 1, Byte.MAX_VALUE);
		register.setComment("Maximum size of Ofalen Ore per generation.");
		limitGeneration = (byte) register.getInt();
		//
		register.setProperty("probLodeGeneration", 0.001, 0.0, 1.0);
		register.setComment("Generation probability of Huge Ofalen Ore Lode." + separator + "Calculation of probability is performed on each chunk.");
		probLodeGeneration = register.getDouble();
		// Tool
		register.setCategory(TOOL);
		// Tool.Perfect
		register.setCategory(PERFECT);
		//
		register.setProperty("rangeMax", 7, 0, Byte.MAX_VALUE);
		register.setComment("Maximum range of Range Breaking Mode of Ofalen Perfect Tool.");
		rangeMax = (byte) register.getInt();
		// Ball
		register.setCategory(BALL);
		// Ball.Explosion
		register.setCategory(EXPLOSION);
		//
		register.setProperty("sizeExplosion", 2, 0, Byte.MAX_VALUE);
		register.setComment("Explosion size of Explosion Ball.");
		sizeExplosion = (byte) register.getInt();
		// Laser
		register.setCategory(LASER);
		//
		register.setProperty("amountLaserCrafting", 4, 1, 64);
		register.setComment("Crafting amount of \"Ofalen Laser Energy Crystal [Red, Green, Blue]\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountLaserCrafting = (byte) register.getInt();
		//
		register.setProperty("amountWhiteLaserCrafting", 3, 1, 64);
		register.setComment("Crafting amount of \"Ofalen Laser Energy Crystal [White]\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountWhiteLaserCrafting = (byte) register.getInt();
		//
		register.setProperty("enableLaserSound", true);
		register.setComment("Set this to true to enable sound of Ofalen Laser Pistol.");
		isLaserSoundEnabled = register.getBoolean();
		// Machine
		register.setCategory(MACHINE);
		//
		register.setProperty("amountWhiteFuelCrafting", 32, 1, 64);
		register.setComment("Crafting amount of \"White Ofalen Fuel\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountWhiteFuelCrafting = (byte) register.getInt();
		//
		register.setProperty("divisorBurningTime", 256, 1, Short.MAX_VALUE);
		register.setComment("Divisor of burning time when using furnace fuel for machines.");
		divisorBurningTime = (short) register.getInt();
		//
		register.setProperty("timeTDiamondBurning", 400, 0, Short.MAX_VALUE);
		register.setComment("Burning time of Creeper Magic Stone by Takumi Craft." + unitTick);
		timeTDiamondBurning = (short) register.getInt();
		//
		register.setProperty("timeWhiteFuelBurning", 1600, 0, Short.MAX_VALUE);
		register.setComment("Burning time of White Ofalen Fuel." + unitTick);
		timeWhiteFuelBurning = (short) register.getInt();
		// Machine.Smelting
		register.setCategory(SMELTING);
		//
		register.setProperty("timeSmelting", 1600, 0, Short.MAX_VALUE);
		register.setComment("The time Ofalen Smelting Machine requires for every smelting." + unitTick);
		timeSmelting = (short) register.getInt();
		//
		register.setProperty("baseOfalenSmeltingAmount", 1, 1, 16);
		register.setComment("Smelting amount of Ofalen from Ofalen Ore using Ofalen Smelting Machine." + separator + "When don't use Ofalen Machine Processor.");
		register.setRequiresMcRestart();
		baseOfalenSmeltingAmount = (byte) register.getInt();
		// Machine.Converting
		register.setCategory(CONVERTING);
		//
		register.setProperty("timeConverting", 1600, 0, Short.MAX_VALUE);
		register.setComment("The time Ofalen Converting Machine requires for every converting." + unitTick);
		timeConverting = (short) register.getInt();
		// Machine.Repairing
		register.setCategory(REPAIRING);
		//
		register.setProperty("timeRepairing", 40, 0, Short.MAX_VALUE);
		register.setComment("The time Ofalen Repairing Machine requires for every repairing." + separator + "Per 1 durability." + unitTick);
		timeRepairing = (short) register.getInt();
		// Machine.Fusing
		register.setCategory(FUSING);
		//
		register.setProperty("timeFusing", 1600, 0, Short.MAX_VALUE);
		register.setComment("The time Ofalen Fusing Machine requires for every fusing." + unitTick);
		timeFusing = (short) register.getInt();
		// Future
		register.setCategory(FUTURE);
		// Future.Protector
		register.setCategory(PROTECTOR);
		//
		register.setProperty("amountProtectingIngotCrafting", 4, 1, 64);
		register.setComment("Crafting amount of \"Ingot of Ofalen Protecting\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountProtectingIngotCrafting = (byte) register.getInt();
		//
		register.setProperty("amountProtectingIngotReference", 64, 0, Short.MAX_VALUE);
		register.setComment("The amount of \"Ingot of Ofalen Protecting\" referred for rendering icon.");
		amountProtectingIngotReference = (short) register.getInt();
		//
		register.setProperty("amountProtectorDamage", 1, 0, Short.MAX_VALUE);
		register.setComment("The amount of material consuming when Protector is activated." + separator + "If \"consumeMaterialPerDamage\" is true, per half a heart.");
		amountProtectorDamage = (short) register.getInt();
		//
		register.setProperty("enableProtecting", true);
		register.setComment("Set this to true to enable searching inventory and protecting player.");
		isProtectingEnabled = register.getBoolean();
		//
		register.setProperty("consumeMaterialPerDamage", true);
		register.setComment("Set this to true to consume material according to damage amount.");
		canConsumeMaterialPerDamage = register.getBoolean();
		//
		register.setProperty("enableProtectorParticle", true);
		register.setComment("Set this to true to enable particle spawning of Ofalen Protector.");
		isProtectorParticleEnabled = register.getBoolean();
		//
		register.setProperty("typeProtectorParticle", 2, 1, 2);
		register.setComment("Type of particle spawning for Ofalen Protector." + separator + "1: circle, 2: cylinder");
		typeProtectorParticle = (byte) register.getInt();
		// Future.Teleporter
		register.setCategory(TELEPORTER);
		//
		register.setProperty("amountTeleportingPearlCrafting", 4, 1, 64);
		register.setComment("Crafting amount of \"Pearl of Ofalen Teleporting\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountTeleportingPearlCrafting = (byte) register.getInt();
		//
		register.setProperty("amountTeleportingPearlReference", 64, 0, Short.MAX_VALUE);
		register.setComment("The amount of \"Pearl of Ofalen Teleporting\" referred for rendering icon.");
		amountTeleportingPearlReference = (short) register.getInt();
		//
		register.setProperty("amountTeleporterDamage", 1, 0, Short.MAX_VALUE);
		register.setComment("Damage amount of Ofalen Teleporter when the player teleport.");
		amountTeleporterDamage = (short) register.getInt();
		//
		register.setProperty("canTeleportFromEnd", true);
		register.setComment("Set this to true to permit teleporting from The End.");
		canTeleportFromEnd = register.getBoolean();
		//
		register.setProperty("enableTeleporterParticle", true);
		register.setComment("Set this to true to enable particle spawning of Ofalen Teleporter.");
		isTeleporterParticleEnabled = register.getBoolean();
		//
		register.setProperty("typeTeleporterParticle", 2, 1, 2);
		register.setComment("Type of particle spawning for Ofalen Teleporter." + separator + "1: circle, 2: cylinder");
		typeTeleporterParticle = (byte) register.getInt();
		// Future.Floater
		register.setCategory(FLOATER);
		//
		register.setProperty("amountFloatingDustCrafting", 4, 1, 64);
		register.setComment("Crafting amount of \"Dust of Ofalen Floating\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountFloatingDustCrafting = (byte) register.getInt();
		//
		register.setProperty("amountFloatingDustReference", 64, 0, Short.MAX_VALUE);
		register.setComment("The amount of \"Dust of Ofalen Floating\" referred for rendering icon.");
		amountFloatingDustReference = (short) register.getInt();
		//
		register.setProperty("amountFloaterDamage", 1, 0, Short.MAX_VALUE);
		register.setComment("Damage amount of Ofalen Floater when the player float.");
		amountFloaterDamage = (short) register.getInt();
		//
		register.setProperty("intervalFloaterDamage", 20, 0, Byte.MAX_VALUE);
		register.setComment("Damage interval of Ofalen Floater." + unitTick);
		intervalFloaterDamage = (byte) register.getInt();
		//
		register.setProperty("intervalInventorySearch", 20, 0, Byte.MAX_VALUE);
		register.setComment("The search interval of inventory whose player using Ofalen Floater." + unitTick);
		intervalInventorySearch = (byte) register.getInt();
		//
		register.setProperty("canSwitchFloatForm", true);
		register.setComment("Set this to true to control Float Form by pressing the jump key twice.");
		canSwitchFloatForm = register.getBoolean();
		//
		register.setProperty("enableFloaterParticle", true);
		register.setComment("Set this to true to enable particle spawning of Ofalen Floater.");
		isFloaterParticleEnabled = register.getBoolean();
		//
		register.setProperty("typeFloaterParticle", 2, 1, 2);
		register.setComment("Type of particle spawning for Ofalen Floater." + separator + "1: circle, 2: cylinder");
		typeFloaterParticle = (byte) register.getInt();
		// Future.Collector
		register.setCategory(COLLECTOR);
		//
		register.setProperty("amountCollectingLumpCrafting", 64, 1, 64);
		register.setComment("Crafting amount of \"Lump of Ofalen Collecting\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountCollectingLumpCrafting = (byte) register.getInt();
		//
		register.setProperty("amountCollectingLumpReference", 64, 0, Short.MAX_VALUE);
		register.setComment("The amount of \"Lump of Ofalen Collecting\" referred for rendering icon.");
		amountCollectingLumpReference = (short) register.getInt();
		//
		register.setProperty("amountCollectorDamageItem", 1, 0, Short.MAX_VALUE);
		register.setComment("Damage amount of Ofalen Collector when item is collected." + separator + "Per 1 item.");
		amountCollectorDamageItem = (short) register.getInt();
		//
		register.setProperty("amountCollectorDamageExp", 1, 0, Short.MAX_VALUE);
		register.setComment("Damage amount of Ofalen Collector when experience orb is collected." + separator + "Per 1 exp.");
		amountCollectorDamageExp = (short) register.getInt();
		//
		register.setProperty("enableCollectorParticle", true);
		register.setComment("Set this to true to enable particle spawning of Ofalen Collector.");
		isCollectorParticleEnabled = register.getBoolean();
		// WorldEditor
		register.setCategory(WORLD_EDITOR);
		//
		register.setProperty("amountDarkFuelCrafting", 32, 1, 64);
		register.setComment("Crafting amount of \"Dark Ofalen Fuel\" when using normal recipe.");
		register.setRequiresMcRestart();
		amountDarkFuelCrafting = (byte) register.getInt();
		//
		register.setProperty("energyDarkFuel", 20, 0, Short.MAX_VALUE);
		register.setComment("Number of operations per Dark Fuel.");
		energyDarkFuel = (short) register.getInt();
		register.finishCategory();
		cfg.save();
	}

	private static class ConfigRegister {
		/** 現在の登録先カテゴリの名前。 */
		String category;
		/** 現在の登録先カテゴリの項目順序。 */
		List<String> propOrder;
		/** 現在登録している項目。 */
		Property property;
		/** 現在登録している項目の種類。 */
		Property.Type type;

		/** 登録先のカテゴリを設定する。 */
		public void setCategory(String name) {
			// 前のカテゴリの終了処理を行う。
			if (category != null)
				this.finishCategory();
			// カテゴリ名を代入する。
			category = name;
			// カテゴリの翻訳指定文字列を設定する。
			cfg.setCategoryLanguageKey(category, "config.ofalen.category." + category);
			// カテゴリの項目順序を初期化する。
			propOrder = new ArrayList<String>();
		}

		/** カテゴリへの登録を終了する。 */
		public void finishCategory() {
			// カテゴリの項目順序を設定する。
			cfg.setCategoryPropertyOrder(category, propOrder);
			// このインスタンスを初期化する。
			category = null;
			propOrder = null;
			property = null;
			type = null;
		}

		/** 真理値の項目を登録する。 */
		public void setProperty(String name, boolean valueDefault) {
			// 項目を登録し、取得する。
			property = cfg.get(category, name, valueDefault);
			// 項目の種類を真理値に設定する。
			type = Property.Type.BOOLEAN;
			// 項目を初期化する。
			this.initProperty();
		}

		/** 整数値の項目を登録する。 */
		public void setProperty(String name, int valueDefault, int valueMin, int valueMax) {
			// 項目を登録し、取得する。
			property = cfg.get(category, name, valueDefault, null, valueMin, valueMax);
			// 項目の種類を整数値に設定する。
			type = Property.Type.INTEGER;
			// 項目を初期化する。
			this.initProperty();
		}

		/** 浮動小数点数値の項目を登録する。 */
		public void setProperty(String name, double valueDefault, double valueMin, double valueMax) {
			// 項目を登録し、取得する。
			property = cfg.get(category, name, valueDefault, null, valueMin, valueMax);
			// 項目の種類を浮動小数点数値に設定する。
			type = Property.Type.DOUBLE;
			// 項目を初期化する。
			this.initProperty();
		}

		/** 現在登録している項目を初期化する。 */
		private void initProperty() {
			// 項目の翻訳指定文字列を設定する。
			property.setLanguageKey("config.ofalen.prop." + property.getName());
			// カテゴリの項目順序に登録する。
			propOrder.add(property.getName());
		}

		/** 項目の説明を設定する。 */
		public void setComment(String comment) {
			// 説明に設定値の案内文章を付加して登録する。
			property.comment = comment + this.getGuide();
		}

		/** 項目の案内文章を取得する。 */
		private String getGuide() {
			switch (type) {
			default:
			case BOOLEAN:
				// 真理値、その他は初期値の表示を行う。
				return Configuration.NEW_LINE + " [default: " + property.getDefault() + "]";
			case INTEGER:
			case DOUBLE:
				// 数値は範囲と初期値の表示を行う。
				return Configuration.NEW_LINE + " [range: " + property.getMinValue() + " ~ " + property.getMaxValue() + ", default: " + property.getDefault() + "]";
			}
		}

		/** ワールドの再起動が必要な項目に設定する。 */
		public void setRequiresWorldRestart() {
			// 項目の設定を行う。
			property.setRequiresWorldRestart(true);
			// 項目の説明に案内を付加する。
			property.comment += Configuration.NEW_LINE + " [World Restart Required]";
		}

		/** Minecraftの再起動が必要な項目に設定する。 */
		public void setRequiresMcRestart() {
			// 項目の設定を行う。
			property.setRequiresMcRestart(true);
			// 項目の説明に案内を付加する。
			property.comment += Configuration.NEW_LINE + " [Minecraft Restart Required]";
		}

		/** 項目から設定を真理値として取得する。 */
		public boolean getBoolean() {
			// 違う型として登録されていたら例外を投げる。
			if (type != Property.Type.BOOLEAN)
				throw new IllegalStateException("The current registering property is not a boolean type.");
			// 真理値を取得して返す。
			return property.getBoolean();
		}

		/** 項目から設定を整数値として取得する。 */
		public int getInt() {
			// 違う型として登録されていたら例外を投げる。
			if (type != Property.Type.INTEGER)
				throw new IllegalStateException("The current registering property is not an integer type.");
			// 整数値を取得し、最小値と最大値を適用して返す。
			return OfalenUtil.getValidValue(property.getInt(), Integer.parseInt(property.getMinValue()), Integer.parseInt(property.getMaxValue()));
		}

		/** 項目から設定を浮動小数点数値として取得する。 */
		public double getDouble() {
			// 違う型として登録されていたら例外を投げる。
			if (type != Property.Type.DOUBLE)
				throw new IllegalStateException("The current registering property is not a double type.");
			// 浮動小数点数値を取得し、最小値と最大値を適用して返す。
			return OfalenUtil.getValidValue(property.getDouble(), Double.parseDouble(property.getMinValue()), Double.parseDouble(property.getMaxValue()));
		}
	}
}
