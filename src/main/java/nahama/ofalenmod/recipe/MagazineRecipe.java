package nahama.ofalenmod.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class MagazineRecipe implements IRecipe {
	private ItemStack output = null;
	private ArrayList<ItemStack> input = new ArrayList<>();

	public MagazineRecipe(ItemStack result, ItemStack magazine, ItemStack crystal, int amount) {
		output = result.copy();
		input.add(magazine.copy());
		for (int i = 0; i < amount; i++) {
			input.add(crystal.copy());
		}
	}

	@Override
	public int getRecipeSize() {
		return input.size();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return output.copy();
	}

	/** クラフティングインベントリがレシピに適合しているか。 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		ArrayList<ItemStack> required = new ArrayList<>(input);
		for (int x = 0; x < inv.getSizeInventory(); x++) {
			ItemStack slot = inv.getStackInSlot(x);
			if (slot == null)
				continue;
			boolean inRecipe = false;
			for (ItemStack aRequired : required) {
				if (!OreDictionary.itemMatches(aRequired, slot, false))
					continue;
				inRecipe = true;
				required.remove(aRequired);
				break;
			}
			if (!inRecipe) {
				return false;
			}
		}
		return required.isEmpty();
	}
}
