package nahama.ofalenmod.core;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraftforge.common.config.Configuration.NEW_LINE;

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
	private static final String GRASS = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "grass";
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
	public static short amountExpMin;
	public static short amountExpMax;
	// Ore.Generate
	public static boolean isGeneratorEnabled;
	public static byte frequencyGeneration;
	public static byte limitGeneration;
	public static double probLodeGeneration;
	// Tool
	public static short durabilityCreativeSword;
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
	// Future
	public static double sizeDurabilityBar;
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
	public static boolean isTeleportFromEndPermitted;
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
	// Grass
	public static double probNaturalGrowing;

	/** Configを読み込む。 */
	public static void loadConfig(File fileConfig) {
		cfg = new Configuration(fileConfig, OfalenModCore.VERSION);
		syncConfig();
	}

	/** Configを同期する。 */
	public static void syncConfig() {
		ConfigRegister register = new ConfigRegister();
		// General
		register.setCategory(Configuration.CATEGORY_GENERAL);
		// System
		register.setCategory(SYSTEM);
		//
		register.setProperty("enableUpdateCheck", true);
		register.setRequiresMcRestart();
		isUpdateCheckEnabled = register.getBoolean();
		//
		register.setProperty("enablePresentBox", true);
		register.setRequiresWorldRestart();
		isPresentBoxEnabled = register.getBoolean();
		// Ore
		register.setCategory(ORE);
		//
		register.setProperty("amountDrop", 3, 0, Byte.MAX_VALUE);
		amountDrop = (byte) register.getInt();
		//
		register.setProperty("amountExpMin", 3, 0, Short.MAX_VALUE);
		amountExpMin = (short) register.getInt();
		//
		register.setProperty("amountExpMax", 7, 0, Short.MAX_VALUE);
		amountExpMax = (short) register.getInt();
		// Ore.Generate
		register.setCategory(GENERATE);
		//
		register.setProperty("enableGenerator", true);
		isGeneratorEnabled = register.getBoolean();
		//
		register.setProperty("frequencyGeneration", 3, 0, Byte.MAX_VALUE);
		frequencyGeneration = (byte) register.getInt();
		//
		register.setProperty("limitGeneration", 8, 1, Byte.MAX_VALUE);
		limitGeneration = (byte) register.getInt();
		//
		register.setProperty("probLodeGeneration", 0.001, 0.0, 1.0);
		probLodeGeneration = register.getDouble();
		// Material
		register.setCategory(MATERIAL);
		//
		register.setProperty("amountCoverPlateCrafting", 6, 1, 64);
		register.setRequiresMcRestart();
		amountCoverPlateCrafting = (byte) register.getInt();
		//
		register.setProperty("amountGrade3PartsCrafting", 2, 1, 64);
		register.setRequiresMcRestart();
		amountGrade3PartsCrafting = (byte) register.getInt();
		//
		register.setProperty("positionStoneLumpRecipeBlank", 7, 0, 8);
		register.setRequiresMcRestart();
		positionStoneLumpRecipeBlank = (byte) register.getInt();
		// Machine
		register.setCategory(MACHINE);
		//
		register.setProperty("amountWhiteFuelCrafting", 8, 1, 64);
		register.setRequiresMcRestart();
		amountWhiteFuelCrafting = (byte) register.getInt();
		//
		register.setProperty("timeWhiteFuelBurning", 6400, 0, Short.MAX_VALUE);
		timeWhiteFuelBurning = (short) register.getInt();
		//
		register.setProperty("timeTDiamondBurning", 400, 0, Short.MAX_VALUE);
		timeTDiamondBurning = (short) register.getInt();
		//
		register.setProperty("divisorBurningTime", 256, 1, Short.MAX_VALUE);
		divisorBurningTime = (short) register.getInt();
		// Machine.Smelting
		register.setCategory(SMELTING);
		//
		register.setProperty("timeSmelting", 1600, 0, Short.MAX_VALUE);
		timeSmelting = (short) register.getInt();
		//
		register.setProperty("baseOfalenSmeltingAmount", 1, 1, 16);
		register.setRequiresMcRestart();
		baseOfalenSmeltingAmount = (byte) register.getInt();
		// Machine.Converting
		register.setCategory(CONVERTING);
		//
		register.setProperty("timeConverting", 1600, 0, Short.MAX_VALUE);
		timeConverting = (short) register.getInt();
		// Machine.Repairing
		register.setCategory(REPAIRING);
		//
		register.setProperty("timeRepairing", 40, 0, Short.MAX_VALUE);
		timeRepairing = (short) register.getInt();
		// Machine.Fusing
		register.setCategory(FUSING);
		//
		register.setProperty("timeFusing", 1600, 0, Short.MAX_VALUE);
		timeFusing = (short) register.getInt();
		// Ball
		register.setCategory(BALL);
		// Ball.Explosion
		register.setCategory(EXPLOSION);
		//
		register.setProperty("sizeExplosion", 2, 0, Byte.MAX_VALUE);
		sizeExplosion = (byte) register.getInt();
		// Tool
		register.setCategory(TOOL);
		//
		register.setProperty("amountOfalenStickCrafting", 1, 1, 64);
		register.setRequiresMcRestart();
		amountOfalenStickCrafting = (byte) register.getInt();
		//
		register.setProperty("durabilityCreativeSword", 0, 0, Short.MAX_VALUE);
		register.setRequiresMcRestart();
		durabilityCreativeSword = (short) register.getInt();
		// Tool.Perfect
		register.setCategory(PERFECT);
		//
		register.setProperty("rangeMax", 7, 0, Byte.MAX_VALUE);
		rangeMax = (byte) register.getInt();
		// Laser
		register.setCategory(LASER);
		//
		register.setProperty("amountLaserCrafting", 4, 1, 64);
		register.setRequiresMcRestart();
		amountLaserCrafting = (byte) register.getInt();
		//
		register.setProperty("amountWhiteLaserCrafting", 3, 1, 64);
		register.setRequiresMcRestart();
		amountWhiteLaserCrafting = (byte) register.getInt();
		//
		register.setProperty("enableLaserSound", true);
		isLaserSoundEnabled = register.getBoolean();
		// Future
		register.setCategory(FUTURE);
		//
		register.setProperty("sizeDurabilityBar", 2.0, 0.0, Double.MAX_VALUE);
		sizeDurabilityBar = register.getDouble();
		// Future.Protector
		register.setCategory(PROTECTOR);
		//
		register.setProperty("amountProtectingIngotCrafting", 4, 1, 64);
		register.setRequiresMcRestart();
		amountProtectingIngotCrafting = (byte) register.getInt();
		//
		register.setProperty("amountProtectingIngotReference", 64, 0, Short.MAX_VALUE);
		amountProtectingIngotReference = (short) register.getInt();
		//
		register.setProperty("amountProtectorDamage", 1, 0, Short.MAX_VALUE);
		amountProtectorDamage = (short) register.getInt();
		//
		register.setProperty("enableProtecting", true);
		isProtectingEnabled = register.getBoolean();
		//
		register.setProperty("consumeMaterialPerDamage", true);
		canConsumeMaterialPerDamage = register.getBoolean();
		//
		register.setProperty("enableProtectorParticle", true);
		isProtectorParticleEnabled = register.getBoolean();
		//
		register.setProperty("typeProtectorParticle", 2, 1, 2);
		typeProtectorParticle = (byte) register.getInt();
		// Future.Teleporter
		register.setCategory(TELEPORTER);
		//
		register.setProperty("amountTeleportingPearlCrafting", 4, 1, 64);
		register.setRequiresMcRestart();
		amountTeleportingPearlCrafting = (byte) register.getInt();
		//
		register.setProperty("amountTeleportingPearlReference", 64, 0, Short.MAX_VALUE);
		amountTeleportingPearlReference = (short) register.getInt();
		//
		register.setProperty("amountTeleporterDamage", 1, 0, Short.MAX_VALUE);
		amountTeleporterDamage = (short) register.getInt();
		//
		register.setProperty("permitTeleportFromEnd", true);
		isTeleportFromEndPermitted = register.getBoolean();
		//
		register.setProperty("enableTeleporterParticle", true);
		isTeleporterParticleEnabled = register.getBoolean();
		//
		register.setProperty("typeTeleporterParticle", 2, 1, 2);
		typeTeleporterParticle = (byte) register.getInt();
		// Future.Floater
		register.setCategory(FLOATER);
		//
		register.setProperty("amountFloatingDustCrafting", 4, 1, 64);
		register.setRequiresMcRestart();
		amountFloatingDustCrafting = (byte) register.getInt();
		//
		register.setProperty("amountFloatingDustReference", 64, 0, Short.MAX_VALUE);
		amountFloatingDustReference = (short) register.getInt();
		//
		register.setProperty("amountFloaterDamage", 1, 0, Short.MAX_VALUE);
		amountFloaterDamage = (short) register.getInt();
		//
		register.setProperty("intervalFloaterDamage", 20, 0, Byte.MAX_VALUE);
		intervalFloaterDamage = (byte) register.getInt();
		//
		register.setProperty("intervalInventorySearch", 20, 0, Byte.MAX_VALUE);
		intervalInventorySearch = (byte) register.getInt();
		//
		register.setProperty("canSwitchFloatForm", true);
		canSwitchFloatForm = register.getBoolean();
		//
		register.setProperty("enableFloaterParticle", true);
		isFloaterParticleEnabled = register.getBoolean();
		//
		register.setProperty("typeFloaterParticle", 2, 1, 2);
		typeFloaterParticle = (byte) register.getInt();
		// Future.Collector
		register.setCategory(COLLECTOR);
		//
		register.setProperty("amountCollectingLumpCrafting", 64, 1, 64);
		register.setRequiresMcRestart();
		amountCollectingLumpCrafting = (byte) register.getInt();
		//
		register.setProperty("amountCollectingLumpReference", 64, 0, Short.MAX_VALUE);
		amountCollectingLumpReference = (short) register.getInt();
		//
		register.setProperty("amountCollectorDamageItem", 1, 0, Short.MAX_VALUE);
		amountCollectorDamageItem = (short) register.getInt();
		//
		register.setProperty("amountCollectorDamageExp", 1, 0, Short.MAX_VALUE);
		amountCollectorDamageExp = (short) register.getInt();
		//
		register.setProperty("enableCollectorParticle", true);
		isCollectorParticleEnabled = register.getBoolean();
		// WorldEditor
		register.setCategory(WORLD_EDITOR);
		//
		register.setProperty("amountDarkFuelCrafting", 8, 1, 64);
		register.setRequiresMcRestart();
		amountDarkFuelCrafting = (byte) register.getInt();
		//
		register.setProperty("energyDarkFuel", 20, 0, Short.MAX_VALUE);
		energyDarkFuel = (short) register.getInt();
		// Grass
		register.setCategory(GRASS);
		//
		register.setProperty("probNaturalGrowing", 0.1, 0.0, 1.0);
		probNaturalGrowing = register.getDouble();
		register.finishCategory();
		cfg.save();
	}

	private static class ConfigRegister {
		private static Map<String, String> englishName;

		static {
			englishName = new HashMap<String, String>();
			englishName.putAll(StringTranslate.parseLangFile(OfalenModCore.class.getResourceAsStream("/assets/ofalenmod/lang/en_US.lang")));
		}

		/** 現在の登録先カテゴリの名前。 */
		private String category;
		/** 現在の登録先カテゴリの項目順序。 */
		private List<String> propOrder;
		/** 現在登録している項目。 */
		private Property property;
		/** 現在登録している項目の種類。 */
		private Property.Type type;

		/** 登録先のカテゴリを設定する。 */
		public void setCategory(String name) {
			// 前のカテゴリの終了処理を行う。
			if (category != null)
				this.finishCategory();
			// カテゴリ名を代入する。
			category = name;
			// カテゴリの翻訳指定文字列を設定する。
			cfg.setCategoryLanguageKey(category, "config.ofalen.category." + category);
			// カテゴリの説明文を設定する。
			String key = "config.ofalen.category." + category;
			cfg.setCategoryComment(category, this.translate(key) + NEW_LINE + this.translate(key + ".tooltip"));
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
			// 項目の説明文を設定する。
			property.comment = this.translate(property.getLanguageKey()) + NEW_LINE + this.translate(property.getLanguageKey() + ".tooltip") + this.getGuide();
			// カテゴリの項目順序に登録する。
			propOrder.add(property.getName());
		}

		/** 英語へと翻訳し、改行処理をしてから返す。 */
		private String translate(String key) {
			return englishName.get(key).replace("\\n", NEW_LINE).replace(". (", "." + NEW_LINE + " (").replace(". ", "." + NEW_LINE);
		}

		/** 項目の案内文章を取得する。 */
		private String getGuide() {
			switch (type) {
			default:
			case BOOLEAN:
				// 真理値、その他は初期値の表示を行う。
				return NEW_LINE + " [default: " + property.getDefault() + "]";
			case INTEGER:
			case DOUBLE:
				// 数値は範囲と初期値の表示を行う。
				return NEW_LINE + " [range: " + property.getMinValue() + " ~ " + property.getMaxValue() + ", default: " + property.getDefault() + "]";
			}
		}

		/** ワールドの再起動が必要な項目に設定する。 */
		public void setRequiresWorldRestart() {
			// 項目の設定を行う。
			property.setRequiresWorldRestart(true);
			// 項目の説明に案内を付加する。
			property.comment += NEW_LINE + " [World Restart Required]";
		}

		/** Minecraftの再起動が必要な項目に設定する。 */
		public void setRequiresMcRestart() {
			// 項目の設定を行う。
			property.setRequiresMcRestart(true);
			// 項目の説明に案内を付加する。
			property.comment += NEW_LINE + " [Minecraft Restart Required]";
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
