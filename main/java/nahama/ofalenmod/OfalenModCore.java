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
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModEventCore;
import nahama.ofalenmod.core.OfalenModInfoCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDicCore;
import nahama.ofalenmod.core.OfalenModRecipeCore;
import nahama.ofalenmod.creativetab.OfalenTab;
import nahama.ofalenmod.entity.EntityBlueLaser;
import nahama.ofalenmod.entity.EntityExplosionBall;
import nahama.ofalenmod.entity.EntityFlameBall;
import nahama.ofalenmod.entity.EntityGreenLaser;
import nahama.ofalenmod.entity.EntityRedLaser;
import nahama.ofalenmod.entity.EntityWhiteLaser;
import nahama.ofalenmod.generator.OfalenOreGenerator;
import nahama.ofalenmod.handler.OfalenFlightHandlerServer;
import nahama.ofalenmod.handler.OfalenModGuiHandler;
import nahama.ofalenmod.handler.OfalenShieldHandler;
import nahama.ofalenmod.handler.OfalenTeleportHandler;
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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author Akasata Nahama
 */
@Mod(modid = OfalenModCore.MODID, name = OfalenModCore.MODNAME, version = OfalenModCore.VERSION)
public class OfalenModCore {

	public static final String MODID = "OfalenMod";
	public static final String MODNAME = "Ofalen Mod";
	public static final String VERSION = "[1.7.10]1.1.0";

	/** coreクラスのインスタンス */
	@Instance(MODID)
	public static OfalenModCore instance;

	/** modの情報を登録。 */
	@Metadata(MODID)
	public static ModMetadata meta;

	/** 追加するクリエイティブタブ */
	public static final CreativeTabs tabOfalen = new OfalenTab("ofalentab");

	public static final SimpleNetworkWrapper wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(OfalenModCore.MODID);

	/** 初期化前処理。 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		OfalenModInfoCore.registerInfo(meta);
		OfalenModConfigCore.loadConfig(event);
		OfalenModItemCore.registerItem();
		OfalenModBlockCore.registerBlock();
		// 機械類のGUIを登録する。
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new OfalenModGuiHandler());
		// パケットを登録する。
		wrapper.registerMessage(MTeleporterChannel.Handler.class, MTeleporterChannel.class, 0, Side.SERVER);
		wrapper.registerMessage(MTeleporterMeta.Handler.class, MTeleporterMeta.class, 1, Side.SERVER);
		wrapper.registerMessage(MFloaterMode.Handler.class, MFloaterMode.class, 2, Side.SERVER);
		wrapper.registerMessage(MSpawnParticle.Handler.class, MSpawnParticle.class, 3, Side.CLIENT);
		// Event処理を登録する。
		MinecraftForge.EVENT_BUS.register(new OfalenModEventCore());
		FMLCommonHandler.instance().bus().register(new OfalenModEventCore());
	}

	/** 初期化処理。 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// 鉱石の生成を登録する。
		GameRegistry.registerWorldGenerator(new OfalenOreGenerator(), 1);
		OfalenModRecipeCore.registerRecipe();
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			// クライアントの初期化を行う。
			this.instance.clientInit();
		}
	}

	/** 初期化後処理。 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// 鉱石辞書からアイテムを取得するメソッドを実行。
		OfalenModOreDicCore.getItemFromOreDictionary();
	}

	/** クライアントの初期化処理。 */
	@SideOnly(Side.CLIENT)
	public void clientInit() {
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
				Log.error("Error on NEI loading", "OfalenModCore.clientInit", true);
				e.printStackTrace(System.err);
			}
		}
	}

	/** サーバー起動時の処理。 */
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		OfalenShieldHandler.init();
		OfalenTeleportHandler.init();
		OfalenFlightHandlerServer.init();
	}

}
