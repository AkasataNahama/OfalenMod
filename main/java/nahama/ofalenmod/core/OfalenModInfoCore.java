package nahama.ofalenmod.core;

import cpw.mods.fml.common.ModMetadata;
import nahama.ofalenmod.OfalenModCore;
import net.minecraft.util.StatCollector;

public class OfalenModInfoCore {

	/** modの情報を登録する処理。 */
	public static void registerInfo(ModMetadata meta) {
		meta.modId = OfalenModCore.MODID;
		meta.name = OfalenModCore.MODNAME;
		meta.description = StatCollector.translateToLocal("info.OfalenMod.metadescription");
		meta.version = OfalenModCore.VERSION;
		meta.url = "http://www63.atwiki.jp/akasatanahama/pages/15.html";
		meta.updateUrl = "https://dl.dropboxusercontent.com/s/cdp8031ki7jz9rl/versions.txt";
		meta.authorList.add("Akasata Nahama");
		meta.credits = "Logo was designed by Kaneryu. \"Takumi Craft\" is making by Tom Kate.";
		meta.logoFile = "assets/ofalenmod/logo.png";
		meta.autogenerated = false;
	}

}
