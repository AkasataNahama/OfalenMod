package nahama.ofalenmod;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModInfoCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDictCore;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.core.OfalenModRecipeCore;
import nahama.ofalenmod.creativetab.CreativeTabOfalen;
import nahama.ofalenmod.entity.EntityExplosionBall;
import nahama.ofalenmod.entity.EntityLaserBlue;
import nahama.ofalenmod.entity.EntityLaserGreen;
import nahama.ofalenmod.entity.EntityLaserRed;
import nahama.ofalenmod.entity.EntityWhiteLaser;
import nahama.ofalenmod.generator.WorldGenOfalenOre;
import nahama.ofalenmod.handler.OfalenFlightHandlerServer;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.handler.OfalenModAnniversaryHandler;
import nahama.ofalenmod.handler.OfalenModEventHandler;
import nahama.ofalenmod.handler.OfalenModGuiHandler;
import nahama.ofalenmod.handler.OfalenModUpdateCheckHandler;
import nahama.ofalenmod.handler.OfalenTeleportHandler;
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

import java.util.UUID;

/** @author Akasata Nahama */
@Mod(modid = OfalenModCore.MOD_ID, name = OfalenModCore.MOD_NAME, version = OfalenModCore.VERSION, guiFactory = "nahama.ofalenmod.gui.OfalenModGuiFactory")
public class OfalenModCore {
	// MODの基本情報
	public static final String MOD_ID = "OfalenMod";
	public static final String MOD_NAME = "Ofalen Mod";
	public static final String UNLOCALIZED_MOD_NAME = "mod.ofalen.name";
	@SuppressWarnings("WeakerAccess")
	public static final String MINECRAFT_VERSION = "1.7.10";
	@SuppressWarnings("WeakerAccess")
	public static final String MOD_VERSION = "2.0.0";
	public static final String VERSION = "[" + MINECRAFT_VERSION + "]" + MOD_VERSION;
	public static final VersionUtil.VersionString VERSION_STRING = new VersionUtil.VersionString(OfalenModCore.MOD_ID, OfalenModCore.VERSION);
	public static final boolean IS_DEBUGGING = false;
	/** 追加するクリエイティブタブ。 */
	public static final CreativeTabs TAB_OFALEN = new CreativeTabOfalen("ofalen.tabOfalenMod");
	/** coreクラスのインスタンス。 */
	@Instance(MOD_ID)
	public static OfalenModCore instance;
	/** modの情報を登録。 */
	@Metadata(MOD_ID)
	public static ModMetadata meta;
	public static GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes("ofalenmod".getBytes()), "[OfalenMod]");

	/** 初期化前処理。 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		OfalenTimer.start("OfalenModCore.preInit");
		OfalenModInfoCore.registerInfo(meta);
		OfalenModConfigCore.loadConfig(event.getSuggestedConfigurationFile());
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
		OfalenModOreDictCore.init();
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
		OfalenTeleportHandler.init();
		OfalenFlightHandlerServer.init();
		OfalenModAnniversaryHandler.init();
		OfalenKeyHandler.onServerStarting();
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
