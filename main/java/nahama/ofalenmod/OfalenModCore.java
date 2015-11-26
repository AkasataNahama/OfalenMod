package nahama.ofalenmod;

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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModGuiHandler;
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
import nahama.ofalenmod.model.ModelLaser;
import nahama.ofalenmod.nei.OfalenModNEILoad;
import nahama.ofalenmod.renderer.ItemPistolRenderer;
import nahama.ofalenmod.renderer.RenderLaser;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.MinecraftForgeClient;

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

	/** 初期化前処理。 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		OfalenModInfoCore.registerInfo(meta);
		OfalenModConfigCore.loadConfig(event);
		OfalenModItemCore.registerItem();
		OfalenModBlockCore.registerBlock();
		// 機械類のGUIを登録する。
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new OfalenModGuiHandler());
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
				Log.error("NEI loading error", "OfalenModCore.clientInit", true);
				e.printStackTrace(System.err);
			}
		}
	}

}
