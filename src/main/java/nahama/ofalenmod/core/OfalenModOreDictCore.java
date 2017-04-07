package nahama.ofalenmod.core;

import nahama.ofalenmod.recipe.OfalenSmeltingManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class OfalenModOreDictCore {
	public static ArrayList<ItemStack> listCreeperOfalenBlock;
	public static ArrayList<ItemStack> listTDiamond;
	public static ArrayList<ItemStack> listGunPowderOre;

	public static void init() {
		listCreeperOfalenBlock = OreDictionary.getOres("CreeperOfalenBlock");
		listTDiamond = OreDictionary.getOres("TDiamond");
		listGunPowderOre = OreDictionary.getOres("oreGunPowder");
		OfalenSmeltingManager.getInstance().addDefaultOreDictRecipes();
	}

	public static boolean isMatchedItemStack(List<ItemStack> inputs, ItemStack... targets) {
		if (inputs == null || inputs.isEmpty())
			return false;
		for (ItemStack input : inputs) {
			for (ItemStack target : targets) {
				if (OreDictionary.itemMatches(target, input, false))
					return true;
			}
		}
		return false;
	}

	public static ItemStack getItem(List<ItemStack> list, int index) {
		if (list == null || list.isEmpty())
			return null;
		index %= list.size();
		return new ItemStack(list.get(index).getItem(), 1, list.get(index).getItemDamage());
	}
}
