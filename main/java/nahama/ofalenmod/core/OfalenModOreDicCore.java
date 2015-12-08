package nahama.ofalenmod.core;

import java.util.ArrayList;

import nahama.ofalenmod.Log;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OfalenModOreDicCore {

	public static ArrayList<ItemStack> listCreeperOfalenBlock = OreDictionary.getOres("CreeperOfalenBlock");
	public static ArrayList<ItemStack> listTDiamond = OreDictionary.getOres("TDiamond");
	public static boolean isTakumiCraftLoaded;

	/** 鉱石辞書からアイテムを取得する処理。 */
	public static void getItemFromOreDictionary() {
		// 鉱石辞書からItemStackのリストを取得する
		// ItemStackを取得できていれば、代入する
		isTakumiCraftLoaded = true;
		if (listCreeperOfalenBlock.size() < 1 || listTDiamond.size() < 1) {
			isTakumiCraftLoaded = false;
			Log.info("Takumi Craft was not loaded.", "OfalenModOreDicCore", true);
		}
	}

	public static boolean isCreeperOfalenBlock(ItemStack itemStack) {
		if (!isTakumiCraftLoaded)
			return false;
		for (int i = 0; i < listCreeperOfalenBlock.size(); i++) {
			if (itemStack.isItemEqual(listCreeperOfalenBlock.get(i)))
				return true;
		}
		return false;
	}

}
