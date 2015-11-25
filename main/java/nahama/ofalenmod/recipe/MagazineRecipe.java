package nahama.ofalenmod.recipe;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class MagazineRecipe implements IRecipe {

	private ItemStack output = null;
	private ArrayList<Object> input = new ArrayList<Object>();

	public MagazineRecipe(ItemStack result, ItemStack magazine, ItemStack crystal, int amount) {
		output = result.copy();
		input.add(magazine.copy());
		for (int i = 0; i < amount; i ++) {
			input.add((crystal).copy());
		}
	}

	@Override
	public int getRecipeSize(){
		return input.size();
	}

	@Override
	public ItemStack getRecipeOutput(){
		return output;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv){
		return output.copy();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		ArrayList<Object> required = new ArrayList<Object>(input);

		for (int x = 0; x < inv.getSizeInventory(); x++) {
			ItemStack slot = inv.getStackInSlot(x);

			if (slot != null) {
				boolean inRecipe = false;
				Iterator<Object> req = required.iterator();

				while (req.hasNext()) {
					boolean match = false;

					Object next = req.next();

					if (next instanceof ItemStack) {
						match = OreDictionary.itemMatches((ItemStack)next, slot, false);
					} else if (next instanceof ArrayList) {
						Iterator<ItemStack> itr = ((ArrayList<ItemStack>)next).iterator();
						while (itr.hasNext() && !match) {
							match = OreDictionary.itemMatches(itr.next(), slot, false);
						}
					}

					if (match) {
						inRecipe = true;
						required.remove(next);
						break;
					}
				}

				if (!inRecipe) {
					return false;
				}
			}
		}

		return required.isEmpty();
	}

}
