package nahama.ofalenmod.recipe;

import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class MagazineRecipe implements IRecipe {
	private Item magazine;
	private byte color;

	public MagazineRecipe(Item magazine, byte color) {
		this.magazine = magazine;
		this.color = color;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(magazine);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		int damage = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack == null)
				continue;
			if (stack.getItem() == magazine) {
				damage += stack.getItemDamage();
			} else if (stack.getItem() == OfalenModItemCore.crystalLaserEnergy) {
				damage -= 32;
			}
		}
		return new ItemStack(magazine, 1, damage);
	}

	/** クラフティングインベントリがレシピに適合しているか。 */
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		boolean hasMagazine = false;
		boolean hasCrystal = false;
		int damageMagazine = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack == null)
				continue;
			if (stack.getItem() == magazine) {
				// マガジンが2個以上あったら不適合。
				if (hasMagazine)
					return false;
				hasMagazine = true;
				damageMagazine = stack.getItemDamage();
			} else if (stack.getItem() == OfalenModItemCore.crystalLaserEnergy) {
				// 違う色のクリスタルがあったら不適合。
				if (stack.getItemDamage() != color)
					return false;
				damageMagazine -= 32;
				hasCrystal = true;
			} else {
				// 他のアイテムがあったら不適合。
				return false;
			}
		}
		// マガジンとクリスタルがあり、クリスタルが多すぎなければ適合。
		return hasMagazine && hasCrystal && damageMagazine >= 0;
	}
}
