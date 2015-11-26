package nahama.ofalenmod.recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OfalenSmeltingRecipes {

	private static final OfalenSmeltingRecipes SMELTING_BASE = new OfalenSmeltingRecipes();

	public HashMap<ItemStack, ItemStack> smeltingList = new HashMap<ItemStack, ItemStack>();
	private Map experienceList = new HashMap();
	private int smeltingAmount = OfalenModConfigCore.amountSmelting;

	public static OfalenSmeltingRecipes smelting() {
		return SMELTING_BASE;
	}

	/** レシピを設定する */
	protected OfalenSmeltingRecipes() {
		this.putLists(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 0), new ItemStack(OfalenModItemCore.ofalen, smeltingAmount, 0), 1.0F);
		this.putLists(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 1), new ItemStack(OfalenModItemCore.ofalen, smeltingAmount, 1), 1.0F);
		this.putLists(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 2), new ItemStack(OfalenModItemCore.ofalen, smeltingAmount, 2), 1.0F);
		this.putLists(new ItemStack(OfalenModBlockCore.oreOfalen, 1, 3), new ItemStack(OfalenModItemCore.ofalen, smeltingAmount, 3), 1.0F);
		this.putLists(new ItemStack(OfalenModBlockCore.blockOfalen, 1, 0), new ItemStack(OfalenModItemCore.coreOfalen, 1, 0), 0.8F);
		this.putLists(new ItemStack(OfalenModBlockCore.blockOfalen, 1, 1), new ItemStack(OfalenModItemCore.coreOfalen, 1, 1), 0.8F);
		this.putLists(new ItemStack(OfalenModBlockCore.blockOfalen, 1, 2), new ItemStack(OfalenModItemCore.coreOfalen, 1, 2), 0.8F);
	}

	/** 新しいレシピを登録する */
	public void addRecipie(Item item, ItemStack itemStack, float experience) {
		this.addLists(item, itemStack, experience);
	}

	public void addLists(Item item, ItemStack itemStack, float experience) {
		this.putLists(new ItemStack(item, 1, 32767), itemStack, experience);
	}

	public void putLists(ItemStack itemStack, ItemStack itemStack2, float experience) {
		this.smeltingList.put(itemStack, itemStack2);
		this.experienceList.put(itemStack2, Float.valueOf(experience));
	}

	/** 受け取ったItemStackの製錬結果を返す */
	public ItemStack getSmeltingResult(ItemStack itemStack) {
		Iterator iterator = this.smeltingList.entrySet().iterator();
		Entry entry;

		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Entry) iterator.next();
		} while (!canBeSmelted(itemStack, (ItemStack) entry.getKey()));
		return (ItemStack) entry.getValue();
	}

	private boolean canBeSmelted(ItemStack itemStack, ItemStack itemStack2) {
		return itemStack2.getItem() == itemStack.getItem() && (itemStack2.getItemDamage() == 32767 || itemStack2.getItemDamage() == itemStack.getItemDamage());
	}

	public float giveExperience(ItemStack itemStack) {
		Iterator iterator = this.experienceList.entrySet().iterator();
		Entry entry;

		do {
			if (!iterator.hasNext()) {
				return 0.0f;
			}

			entry = (Entry) iterator.next();
		} while (!this.canBeSmelted(itemStack, (ItemStack) entry.getKey()));

		if (itemStack.getItem().getSmeltingExperience(itemStack) != -1) {
			return itemStack.getItem().getSmeltingExperience(itemStack);
		}

		return ((Float) entry.getValue()).floatValue();
	}

}
