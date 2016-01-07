package nahama.ofalenmod.recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDicCore;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class OfalenSmeltingRecipes {

	private static final OfalenSmeltingRecipes instance = new OfalenSmeltingRecipes();

	public HashMap<ItemStack[], Boolean> smeltingList = new HashMap<ItemStack[], Boolean>();
	private int smeltingAmount = OfalenModConfigCore.amountSmelting;

	/** インスタンスを返す。 */
	public static OfalenSmeltingRecipes getInstance() {
		return instance;
	}

	protected OfalenSmeltingRecipes() {
		// デフォルトのレシピを設定する。
		this.addRecipe(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 0), new ItemStack(OfalenModItemCore.ofalen, smeltingAmount, 0), true);
		this.addRecipe(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 1), new ItemStack(OfalenModItemCore.ofalen, smeltingAmount, 1), true);
		this.addRecipe(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 2), new ItemStack(OfalenModItemCore.ofalen, smeltingAmount, 2), true);
		this.addRecipe(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 3), new ItemStack(OfalenModItemCore.ofalen, smeltingAmount, 3), true);
		this.addRecipe(new ItemStack(OfalenModBlockCore.blockOfalen, 1, 0), new ItemStack(OfalenModItemCore.coreOfalen, 1, 0), false);
		this.addRecipe(new ItemStack(OfalenModBlockCore.blockOfalen, 1, 1), new ItemStack(OfalenModItemCore.coreOfalen, 1, 1), false);
		this.addRecipe(new ItemStack(OfalenModBlockCore.blockOfalen, 1, 2), new ItemStack(OfalenModItemCore.coreOfalen, 1, 2), false);
		this.addRecipe(new ItemStack(Blocks.iron_ore), new ItemStack(Items.iron_ingot), true);
		this.addRecipe(new ItemStack(Blocks.gold_ore), new ItemStack(Items.gold_ingot), true);
		this.addRecipe(new ItemStack(Blocks.quartz_ore), new ItemStack(Items.quartz), true);
		for (int i = 0; i < OfalenModOreDicCore.listOreGunPowder.size(); i++) {
			this.addRecipe(OfalenModOreDicCore.getItem(OfalenModOreDicCore.listOreGunPowder, i), new ItemStack(Items.gunpowder), true);
		}
	}

	/** 新しくレシピを登録する。 */
	public void addRecipe(ItemStack material, ItemStack result, boolean canIncrease) {
		smeltingList.put(new ItemStack[] { material, result }, canIncrease);
	}

	/** アイテムの製錬結果を返す。 */
	public ItemStack getSmeltingResult(ItemStack itemStack) {
		Entry<ItemStack[], Boolean> entry = this.getMatchedEntry(itemStack);
		if (entry == null)
			return null;
		return entry.getKey()[1];
	}

	/** グレードによってスタック数を変えた製錬結果を返す。 */
	public ItemStack getSmeltingResultFromGrade(ItemStack itemStack, int grade) {
		Iterator<Entry<ItemStack[], Boolean>> iterator = smeltingList.entrySet().iterator();
		Entry<ItemStack[], Boolean> entry = this.getMatchedEntry(itemStack);
		if (entry == null)
			return null;
		ItemStack result = entry.getKey()[1];
		int size = result.stackSize;
		if (entry.getValue())
			size *= (grade + 1);
		return new ItemStack(result.getItem(), size, result.getItemDamage());
	}

	private Entry<ItemStack[], Boolean> getMatchedEntry(ItemStack itemStack) {
		Iterator<Entry<ItemStack[], Boolean>> iterator = smeltingList.entrySet().iterator();
		Entry<ItemStack[], Boolean> entry;
		do {
			if (!iterator.hasNext())
				return null;
			entry = iterator.next();
		} while (!itemStack.isItemEqual(entry.getKey()[0]));
		return entry;
	}

}
