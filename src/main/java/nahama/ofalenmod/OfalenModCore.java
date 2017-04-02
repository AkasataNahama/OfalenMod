package nahama.ofalenmod;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.*;
import nahama.ofalenmod.creativetab.CreativeTabOfalen;
import nahama.ofalenmod.entity.*;
import nahama.ofalenmod.generator.WorldGenOfalenOre;
import nahama.ofalenmod.handler.*;
import nahama.ofalenmod.model.ModelLaser;
import nahama.ofalenmod.render.RenderItemPistol;
import nahama.ofalenmod.render.RenderLaser;
import nahama.ofalenmod.render.RenderTeleportingMarker;
import nahama.ofalenmod.tileentity.TileEntityTeleportingMarker;
import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenTimer;
import nahama.ofalenmod.util.OfalenUtil;
import nahama.ofalenmod.util.VersionUtil;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

/** @author Akasata Nahama */
@Mod(modid = OfalenModCore.MOD_ID, name = OfalenModCore.MOD_NAME, version = OfalenModCore.VERSION, guiFactory = "nahama.ofalenmod.gui.OfalenModGuiFactory")
public class OfalenModCore {
	// MODの基本情報
	public static final String MOD_ID = "OfalenMod";
	public static final String MOD_NAME = "Ofalen Mod";
	public static final String UNLOCALIZED_MOD_NAME = "mod.ofalen.name";
	public static final String MINECRAFT_VERSION = "1.7.10";
	public static final String MOD_VERSION = "2.0.0";
	public static final String VERSION = "[" + MINECRAFT_VERSION + "]" + MOD_VERSION;
	public static final VersionUtil.VersionString VERSION_STRING = new VersionUtil.VersionString(OfalenModCore.MOD_ID, OfalenModCore.VERSION);
	// TODO リリース時に変更
	public static final boolean IS_DEBUGGING = true;
	/** coreクラスのインスタンス。 */
	@Instance(MOD_ID)
	public static OfalenModCore instance;
	/** modの情報を登録。 */
	@Metadata(MOD_ID)
	public static ModMetadata meta;
	/** 追加するクリエイティブタブ。 */
	public static final CreativeTabs TAB_OFALEN = new CreativeTabOfalen("ofalen.tabOfalenMod");

	/** 初期化前処理。 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		OfalenTimer.start("OfalenModCore.preInit");
		OfalenModInfoCore.registerInfo(meta);
		OfalenModConfigCore.loadConfig(event);
		if (OfalenModConfigCore.isUpdateCheckEnabled)
			OfalenModUpdateCheckHandler.checkUpdate();
		OfalenModBlockCore.registerBlock();
		OfalenModItemCore.registerItem();
		// 機械類のGUIを登録する。
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new OfalenModGuiHandler());
		// パケットを登録する。
		OfalenModPacketCore.registerPacket();
		OfalenTimer.watchAndLog("OfalenModCore.preInit");
	}

	/** 初期化処理。 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		OfalenTimer.start("OfalenModCore.init");
		// 鉱石の生成を登録する。
		GameRegistry.registerWorldGenerator(new WorldGenOfalenOre(), 1);
		OfalenModRecipeCore.registerRecipe();
		// EventHandlerを登録する。
		MinecraftForge.EVENT_BUS.register(new OfalenModEventHandler());
		FMLCommonHandler.instance().bus().register(new OfalenModEventHandler());
		// クライアント側の初期化を行う。
		if (OfalenUtil.isClient())
			instance.clientInit();
		OfalenTimer.watchAndLog("OfalenModCore.init");
	}

	/** 初期化後処理。 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		OfalenTimer.start("OfalenModCore.postInit");
		OfalenTimer.watchAndLog("OfalenModCore.postInit");
	}

	/** クライアントの初期化処理。 */
	@SideOnly(Side.CLIENT)
	private void clientInit() {
		OfalenTimer.start("OfalenModCore.clientInit");
		// キーハンドラーの初期化。
		OfalenKeyHandler.init();
		// タイルエンティティとレンダラーの紐づけ。
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeleportingMarker.class, new RenderTeleportingMarker());
		// エンティティとレンダラーの紐付け。
		RenderingRegistry.registerEntityRenderingHandler(EntityExplosionBall.class, new RenderSnowball(OfalenModItemCore.ballExplosion));
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserRed.class, new RenderLaser(new ModelLaser(), "red"));
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserGreen.class, new RenderLaser(new ModelLaser(), "green"));
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserBlue.class, new RenderLaser(new ModelLaser(), "blue"));
		RenderingRegistry.registerEntityRenderingHandler(EntityWhiteLaser.class, new RenderLaser(new ModelLaser(), "white"));
		// アイテムとモデルの紐付け。
		MinecraftForgeClient.registerItemRenderer(OfalenModItemCore.pistolLaser, new RenderItemPistol());
		OfalenTimer.watchAndLog("OfalenModCore.clientInit");
	}

	/** サーバー起動時の処理。 */
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		OfalenTimer.start("OfalenModCore.serverStarting");
		// 各種ハンドラを初期化する。
		OfalenShieldHandler.init();
		OfalenTeleportHandler.init();
		OfalenFlightHandlerServer.init();
		OfalenModAnniversaryHandler.init();
		// 最新バージョンの通知をする。
		if (OfalenModUpdateCheckHandler.isNewVersionAvailable)
			OfalenLog.info(StatCollector.translateToLocal("info.ofalen.notificationVersion") + OfalenModUpdateCheckHandler.getMessage());
		OfalenTimer.watchAndLog("OfalenModCore.serverStarting");
	}

	/** サーバー終了時の処理。 */
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		OfalenModAnniversaryHandler.save();
	}
}
