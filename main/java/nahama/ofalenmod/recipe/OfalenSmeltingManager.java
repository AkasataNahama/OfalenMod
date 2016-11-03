package nahama.ofalenmod.recipe;

import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDictCore;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class OfalenSmeltingManager {
	private static OfalenSmeltingManager instance;
	public ArrayList<OfalenSmeltingRecipe> recipes;

	static {
		instance = new OfalenSmeltingManager();
		instance.recipes = new ArrayList<>();
		instance.addDefaultRecipes();
	}

	/** インスタンスを返す。 */
	public static OfalenSmeltingManager getInstance() {
		return instance;
	}

	public boolean isValid() {
		return recipes != null && !recipes.isEmpty();
	}

	/** デフォルトのレシピを設定する。 */
	private void addDefaultRecipes() {
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
		recipes.add(new OfalenSmeltingRecipe(material, result, canIncrease));
	}

	/** アイテムの製錬結果を返す。 */
	public ItemStack getSmeltingResult(ItemStack itemStack) {
		OfalenSmeltingRecipe recipe = this.getMatchedRecipe(itemStack);
		if (recipe == null)
			return null;
		return recipe.result;
	}

	/** グレードによってスタック数を変えた製錬結果を返す。 */
	public ItemStack getSmeltingResultWithGrade(ItemStack itemStack, int grade) {
		OfalenSmeltingRecipe recipe = this.getMatchedRecipe(itemStack);
		if (recipe == null)
			return null;
		ItemStack resultStack = recipe.result;
		int size = resultStack.stackSize;
		if (recipe.canIncrease)
			size *= (grade + 1);
		return new ItemStack(resultStack.getItem(), size, resultStack.getItemDamage());
	}

	private OfalenSmeltingRecipe getMatchedRecipe(ItemStack itemStack) {
		for (OfalenSmeltingRecipe recipe : recipes) {
			if (OreDictionary.itemMatches(itemStack, recipe.material, false))
				return recipe;
		}
		return null;
	}

	public static class OfalenSmeltingRecipe {
		public ItemStack material;
		public ItemStack result;
		public boolean canIncrease;

		public OfalenSmeltingRecipe(ItemStack material, ItemStack result, boolean canIncrease) {
			this.material = material;
			this.result = result;
			this.canIncrease = canIncrease;
		}
	}
}
