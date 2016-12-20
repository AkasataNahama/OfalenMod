package nahama.ofalenmod;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.*;
import nahama.ofalenmod.creativetab.CreativeTabOfalen;
import nahama.ofalenmod.entity.*;
import nahama.ofalenmod.generator.WorldGenOfalenOre;
import nahama.ofalenmod.handler.*;
import nahama.ofalenmod.model.ModelLaser;
import nahama.ofalenmod.nei.OfalenModNEILoader;
import nahama.ofalenmod.network.*;
import nahama.ofalenmod.render.RenderItemPistol;
import nahama.ofalenmod.render.RenderLaser;
import nahama.ofalenmod.render.RenderTeleportingMarker;
import nahama.ofalenmod.tileentity.TileEntityTeleportingMarker;
import nahama.ofalenmod.util.OfalenLog;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

/** @author Akasata Nahama */
@Mod(modid = OfalenModCore.MODID, name = OfalenModCore.MODNAME, version = OfalenModCore.VERSION, guiFactory = "nahama.ofalenmod.gui.OfalenModGuiFactory")
public class OfalenModCore {
	// MODの基本情報
	public static final String MODID = "OfalenMod";
	public static final String MODNAME = "Ofalen Mod";
	public static final String MCVERSION = "1.7.10";
	public static final String MODVERSION = "2.0.0";
	public static final String VERSION = "[" + MCVERSION + "]" + MODVERSION;
	// TODO リリース時に変更
	public static final boolean IS_DEBUGGING = true;
	/** coreクラスのインスタンス。 */
	@Instance(MODID)
	public static OfalenModCore instance;
	/** modの情報を登録。 */
	@Metadata(MODID)
	public static ModMetadata meta;
	/** 追加するクリエイティブタブ。 */
	public static final CreativeTabs TAB_OFALEN = new CreativeTabOfalen("ofalen.tabOfalenMod");
	/** パケット通信用。 */
	public static final SimpleNetworkWrapper WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(OfalenModCore.MODID);
	/** 詳細設定キー。 */
	public static final KeyBinding KEY_OSS = new KeyBinding("key.description.ofalen.keyOSS", Keyboard.KEY_F, "key.category.ofalen");

	/** 初期化前処理。 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		OfalenModInfoCore.registerInfo(meta);
		OfalenModConfigCore.loadConfig(event);
		if (OfalenModConfigCore.isUpdateCheckEnabled)
			OfalenModUpdateCheckHandler.checkUpdate();
		OfalenModBlockCore.registerBlock();
		OfalenModItemCore.registerItem();
		// 機械類のGUIを登録する。
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new OfalenModGuiHandler());
		// パケットを登録する。
		WRAPPER.registerMessage(MTeleporterChannel.Handler.class, MTeleporterChannel.class, 0, Side.SERVER);
		WRAPPER.registerMessage(MTeleporterMeta.Handler.class, MTeleporterMeta.class, 1, Side.SERVER);
		WRAPPER.registerMessage(MFloaterMode.Handler.class, MFloaterMode.class, 2, Side.SERVER);
		WRAPPER.registerMessage(MSpawnParticle.Handler.class, MSpawnParticle.class, 3, Side.CLIENT);
		WRAPPER.registerMessage(MFilterInstaller.Handler.class, MFilterInstaller.class, 4, Side.SERVER);
		WRAPPER.registerMessage(MWorldEditorSetting.Handler.class, MWorldEditorSetting.class, 5, Side.SERVER);
		WRAPPER.registerMessage(MSpawnParticleWithRange.Handler.class, MSpawnParticleWithRange.class, 6, Side.CLIENT);
		OfalenModAnniversaryHandler.isSinglePlay = FMLCommonHandler.instance().getSide() == Side.CLIENT;
	}

	/** 初期化処理。 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// 鉱石の生成を登録する。
		GameRegistry.registerWorldGenerator(new WorldGenOfalenOre(), 1);
		OfalenModRecipeCore.registerRecipe();
		// EventHandlerを登録する。
		MinecraftForge.EVENT_BUS.register(new OfalenModEventHandler());
		FMLCommonHandler.instance().bus().register(new OfalenModEventHandler());
		// クライアントの初期化を行う。
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
			instance.clientInit();
	}

	/** 初期化後処理。 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	/** クライアントの初期化処理。 */
	@SideOnly(Side.CLIENT)
	private void clientInit() {
		// キーバインディングの登録。
		ClientRegistry.registerKeyBinding(KEY_OSS);
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
		// NEIへのレシピ表示の登録。
		if (Loader.isModLoaded("NotEnoughItems")) {
			try {
				OfalenModNEILoader.load();
			} catch (Exception e) {
				OfalenLog.error("Error on loading NEI.", "OfalenModCore");
				e.printStackTrace();
			}
		}
	}

	/** サーバー起動時の処理。 */
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		// 各種ハンドラを初期化する。
		OfalenShieldHandler.init();
		OfalenTeleportHandler.init();
		OfalenFlightHandlerServer.init();
		OfalenModAnniversaryHandler.init();
		// 最新バージョンの通知をする。
		if (OfalenModUpdateCheckHandler.isNewVersionAvailable)
			OfalenLog.info(StatCollector.translateToLocal("info.ofalen.notificationVersion") + OfalenModUpdateCheckHandler.getMessage());
	}

	/** サーバー終了時の処理。 */
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		OfalenModAnniversaryHandler.save();
	}
}
