package nahama.ofalenmod;

import nahama.ofalenmod.core.CommonProxy;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModGuiHandler;
import nahama.ofalenmod.core.OfalenModInfoCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDicCore;
import nahama.ofalenmod.core.OfalenModRecipeCore;
import nahama.ofalenmod.creativetab.OfalenTab;
import nahama.ofalenmod.generator.OfalenOreGenerator;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/** @author Akasata Nahama */
@Mod(modid = OfalenModCore.MODID, name = OfalenModCore.MODNAME, version = OfalenModCore.VERSION)
public class OfalenModCore {

	public static final String MODID = "OfalenMod";
	public static final String MODNAME = "Ofalen Mod";
	public static final String VERSION = "[1.7.10]1.0.0";

	/** coreクラスのインスタンス */
	@Instance(MODID)
	public static OfalenModCore instance;

	/** Infoファイル作成用 */
	@Metadata(MODID)
	public static ModMetadata meta;

	/** Proxy */
	@SidedProxy(modId = MODID, serverSide = "nahama.ofalenmod.core.CommonProxy", clientSide = "nahama.ofalenmod.core.ClientProxy")
	public static CommonProxy proxy;

	/** 追加されたクリエイティブタブ */
	public static final CreativeTabs tabOfalen = new OfalenTab("ofalentab");

	/** 最初に行われる処理。アイテム・ブロックの追加などを行う */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Infoファイルを読み込む
		OfalenModInfoCore.loadInfo(meta);

		// configを設定するメソッドを実行
		OfalenModConfigCore.registerConfig(event);

		// アイテムを設定するメソッドを実行
		OfalenModItemCore.registerItem();

		// ブロックを設定するメソッドを実行
		OfalenModBlockCore.registerBlock();

		// 機械類のGUIを登録する
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new OfalenModGuiHandler());
	}

	/** 2番目に行われる処理。レシピの追加などを行う */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// 鉱石を生成させる
		GameRegistry.registerWorldGenerator(new OfalenOreGenerator(), 1);

		// レシピを設定するメソッドを実行
		OfalenModRecipeCore.registerRecipe();

		// entityのrenderクラスを設定するメソッドを実行
		proxy.registerRender();

		// NEIへのレシピ表示の追加
		proxy.loadNEI();
	}

	/** 最後に行われる処理。鉱石辞書から取得したアイテムを使った処理などを行う */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// 鉱石辞書からアイテムを取得するメソッドを実行
		OfalenModOreDicCore.getItemFromOreDictionary();
	}

}
