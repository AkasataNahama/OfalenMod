package nahama.ofalenmod.core;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OfalenModOreDicCore {

	public static ItemStack ofalenCreeper;
	public static boolean isTakumiCraftLoaded;

	/** 鉱石辞書からアイテムを取得する処理。 */
	public static void getItemFromOreDictionary() {
		// 鉱石辞書からItemStackのリストを取得する
		ArrayList<ItemStack> listOfalenCreeper = OreDictionary.getOres("CreeperOfalenBlock");

		// ItemStackを取得できていれば、代入する
		if (listOfalenCreeper.size() > 0) {
			ofalenCreeper = new ItemStack(listOfalenCreeper.get(0).getItem(), 1, listOfalenCreeper.get(0).getItemDamage());
			isTakumiCraftLoaded = true;
		} else {
			ofalenCreeper = null;
			isTakumiCraftLoaded = false;
		}
	}

}
