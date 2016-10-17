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
import nahama.ofalenmod.creativetab.OfalenTab;
import nahama.ofalenmod.entity.*;
import nahama.ofalenmod.generator.OfalenOreGenerator;
import nahama.ofalenmod.handler.*;
import nahama.ofalenmod.model.ModelLaser;
import nahama.ofalenmod.nei.OfalenModNEILoad;
import nahama.ofalenmod.network.MFloaterMode;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.network.MTeleporterChannel;
import nahama.ofalenmod.network.MTeleporterMeta;
import nahama.ofalenmod.render.ItemPistolRenderer;
import nahama.ofalenmod.render.RenderLaser;
import nahama.ofalenmod.render.RenderTeleportMarker;
import nahama.ofalenmod.tileentity.TileEntityTeleportMarker;
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

	public static final String MODID = "OfalenMod";
	public static final String MODNAME = "Ofalen Mod";
	public static final String MCVERSION = "1.7.10";
	public static final String MODVERSION = "1.1.2";
	public static final String VERSION = "[" + MCVERSION + "]" + MODVERSION;

	/** coreクラスのインスタンス */
	@Instance(MODID)
	public static OfalenModCore instance;

	/** modの情報を登録。 */
	@Metadata(MODID)
	public static ModMetadata meta;

	/** 追加するクリエイティブタブ */
	public static final CreativeTabs tabOfalen = new OfalenTab("ofalentab");

	public static final SimpleNetworkWrapper wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(OfalenModCore.MODID);

	public static final KeyBinding keyOSS = new KeyBinding("OfalenMod.OSSKey", Keyboard.KEY_F, "Ofalen Mod");

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
		wrapper.registerMessage(MTeleporterChannel.Handler.class, MTeleporterChannel.class, 0, Side.SERVER);
		wrapper.registerMessage(MTeleporterMeta.Handler.class, MTeleporterMeta.class, 1, Side.SERVER);
		wrapper.registerMessage(MFloaterMode.Handler.class, MFloaterMode.class, 2, Side.SERVER);
		wrapper.registerMessage(MSpawnParticle.Handler.class, MSpawnParticle.class, 3, Side.CLIENT);
		OfalenModAnniversaryHandler.isSinglePlay = FMLCommonHandler.instance().getSide() == Side.CLIENT;
	}

	/** 初期化処理。 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// 鉱石の生成を登録する。
		GameRegistry.registerWorldGenerator(new OfalenOreGenerator(), 1);
		OfalenModRecipeCore.registerRecipe();
		// Event処理を登録する。
		MinecraftForge.EVENT_BUS.register(new OfalenModEventHandler());
		FMLCommonHandler.instance().bus().register(new OfalenModEventHandler());
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			// クライアントの初期化を行う。
			instance.clientInit();
		}
	}

	/** 初期化後処理。 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	/** クライアントの初期化処理。 */
	@SideOnly(Side.CLIENT)
	private void clientInit() {
		// キーバインディングの登録
		ClientRegistry.registerKeyBinding(keyOSS);
		// タイルエンティティとレンダラーの紐づけ
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeleportMarker.class, new RenderTeleportMarker());
		// エンティティとレンダラーの紐付け
		RenderingRegistry.registerEntityRenderingHandler(EntityExplosionBall.class, new RenderSnowball(OfalenModItemCore.ballExplosion));
		RenderingRegistry.registerEntityRenderingHandler(EntityFlameBall.class, new RenderSnowball(OfalenModItemCore.ballFlame));
		RenderingRegistry.registerEntityRenderingHandler(EntityRedLaser.class, new RenderLaser(new ModelLaser(), "red"));
		RenderingRegistry.registerEntityRenderingHandler(EntityGreenLaser.class, new RenderLaser(new ModelLaser(), "green"));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlueLaser.class, new RenderLaser(new ModelLaser(), "blue"));
		RenderingRegistry.registerEntityRenderingHandler(EntityWhiteLaser.class, new RenderLaser(new ModelLaser(), "white"));
		// アイテムとモデルの紐付け
		MinecraftForgeClient.registerItemRenderer(OfalenModItemCore.pistolLaser, new ItemPistolRenderer());
		// NEIへのレシピ表示の登録。
		if (Loader.isModLoaded("NotEnoughItems")) {
			try {
				OfalenModNEILoad.load();
			} catch (Exception e) {
				Util.error("Error on loading NEI.", "OfalenModCore.clientInit");
				e.printStackTrace();
			}
		}
	}

	/** サーバー起動時の処理。 */
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		// 各種ハンドラーを初期化する。
		OfalenShieldHandler.init();
		OfalenTeleportHandler.init();
		OfalenFlightHandlerServer.init();
		OfalenModAnniversaryHandler.init();
		// 最新バージョンの通知をする。
		if (OfalenModUpdateCheckHandler.isAvailableNewVersion) {
			Util.info(StatCollector.translateToLocal("info.OfalenMod.NewVersionIsAvailable") + OfalenModUpdateCheckHandler.getMessage());
		}
	}

	/** サーバー終了時の処理。 */
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		OfalenModAnniversaryHandler.save();
	}

}
