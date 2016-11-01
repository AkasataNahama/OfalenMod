package nahama.ofalenmod.recipe;

import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDictCore;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map.Entry;

public class OfalenSmeltingRecipe {
	private static OfalenSmeltingRecipe instance;
	public HashMap<ItemStack, OfalenSmeltingResultSet> listRecipe;

	static {
		instance = new OfalenSmeltingRecipe();
		instance.listRecipe = new HashMap<>();
		instance.addDefaultRecipes();
	}

	/** インスタンスを返す。 */
	public static OfalenSmeltingRecipe getInstance() {
		return instance;
	}

	/** デフォルトのレシピを設定する。 */
	public void addDefaultRecipes() {
		this.addRecipe(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 0), new ItemStack(OfalenModItemCore.gemOfalen, OfalenModConfigCore.baseOfalenSmeltingAmount, 0), true);
		this.addRecipe(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 1), new ItemStack(OfalenModItemCore.gemOfalen, OfalenModConfigCore.baseOfalenSmeltingAmount, 1), true);
		this.addRecipe(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 2), new ItemStack(OfalenModItemCore.gemOfalen, OfalenModConfigCore.baseOfalenSmeltingAmount, 2), true);
		this.addRecipe(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 3), new ItemStack(OfalenModItemCore.gemOfalen, OfalenModConfigCore.baseOfalenSmeltingAmount, 3), true);
		this.addRecipe(new ItemStack(OfalenModBlockCore.blockOfalen, 1, 0), new ItemStack(OfalenModItemCore.coreOfalen, 1, 0), false);
		this.addRecipe(new ItemStack(OfalenModBlockCore.blockOfalen, 1, 1), new ItemStack(OfalenModItemCore.coreOfalen, 1, 1), false);
		this.addRecipe(new ItemStack(OfalenModBlockCore.blockOfalen, 1, 2), new ItemStack(OfalenModItemCore.coreOfalen, 1, 2), false);
		this.addRecipe(new ItemStack(Blocks.iron_ore), new ItemStack(Items.iron_ingot), true);
		this.addRecipe(new ItemStack(Blocks.gold_ore), new ItemStack(Items.gold_ingot), true);
		this.addRecipe(new ItemStack(Blocks.quartz_ore), new ItemStack(Items.quartz), true);
		for (int i = 0; i < OfalenModOreDictCore.listGunPowderOre.size(); i++) {
			this.addRecipe(OfalenModOreDictCore.getItem(OfalenModOreDictCore.listGunPowderOre, i), new ItemStack(Items.gunpowder), true);
		}
	}

	/** 新しくレシピを登録する。 */
	public void addRecipe(ItemStack material, ItemStack result, boolean canIncrease) {
		listRecipe.put(material, new OfalenSmeltingResultSet(result, canIncrease));
	}

	/** アイテムの製錬結果を返す。 */
	public ItemStack getSmeltingResult(ItemStack itemStack) {
		OfalenSmeltingResultSet set = this.getMatchedResult(itemStack);
		if (set == null)
			return null;
		return set.result;
	}

	/** グレードによってスタック数を変えた製錬結果を返す。 */
	public ItemStack getSmeltingResultFromGrade(ItemStack itemStack, int grade) {
		OfalenSmeltingResultSet set = this.getMatchedResult(itemStack);
		if (set == null)
			return null;
		ItemStack resultStack = set.result;
		int size = resultStack.stackSize;
		if (set.canIncrease)
			size *= (grade + 1);
		return new ItemStack(resultStack.getItem(), size, resultStack.getItemDamage());
	}

	private OfalenSmeltingResultSet getMatchedResult(ItemStack itemStack) {
		for (Entry<ItemStack, OfalenSmeltingResultSet> entry : listRecipe.entrySet()) {
			if (OreDictionary.itemMatches(itemStack, entry.getKey(), false))
				return entry.getValue();
		}
		return null;
	}

	public static class OfalenSmeltingResultSet {
		public ItemStack result;
		public boolean canIncrease;

		public OfalenSmeltingResultSet(ItemStack result, boolean canIncrease) {
			this.result = result;
			this.canIncrease = canIncrease;
		}
	}
}
