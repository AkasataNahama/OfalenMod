package nahama.ofalenmod.nei;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import nahama.ofalenmod.gui.GuiSmeltingMachine;
import nahama.ofalenmod.recipe.OfalenSmeltingRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class CatchRecipeHandler extends TemplateRecipeHandler {

	private HashMap<ItemStack, ItemStack> newRecipe;

	private HashMap<ItemStack, ItemStack> recipeLoader() {
		if (OfalenSmeltingRecipes.smelting().smeltingList != null && !OfalenSmeltingRecipes.smelting().smeltingList.isEmpty()) {
			this.newRecipe = OfalenSmeltingRecipes.smelting().smeltingList;
		}
		return this.newRecipe;
	}

	public class recipeCacher extends CachedRecipe {

		private PositionedStack input;
		private PositionedStack result;

		public recipeCacher(ItemStack in, ItemStack out) {
			in.stackSize = 1;
			this.input = new PositionedStack(in, 51, 6);
			this.result = new PositionedStack(out, 111, 23);
		}

		@Override
		public PositionedStack getResult() {
			return this.result;
		}

		@Override
		public PositionedStack getIngredient() {
			return this.input;
		}

	}

	public PositionedStack getResult() {
		return null;
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiSmeltingMachine.class;
	}

	@Override
	public String getOverlayIdentifier() {
		return "OfalenSmeltingRecipe";
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(73, 23, 22, 15), "OfalenSmeltingRecipe"));
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("OfalenSmeltingRecipe")) {
			HashMap<ItemStack, ItemStack> recipes = this.recipeLoader();

			if (recipes == null || recipes.isEmpty())
				return;
			for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
				ItemStack item = recipe.getValue();
				ItemStack in = recipe.getKey();
				arecipes.add(new recipeCacher(in, item));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		HashMap<ItemStack, ItemStack> recipes = this.recipeLoader();

		if (recipes == null || recipes.isEmpty())
			return;
		for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
			ItemStack item = recipe.getValue();
			ItemStack in = recipe.getKey();
			if (NEIServerUtils.areStacksSameType(item, result)) {
				arecipes.add(new recipeCacher(in, item));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		HashMap<ItemStack, ItemStack> recipes = this.recipeLoader();

		if (recipes == null || recipes.isEmpty())
			return;
		for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
			ItemStack item = recipe.getValue();
			ItemStack in = recipe.getKey();
			if (ingredient.isItemEqual(in)) {
				arecipes.add(new recipeCacher(ingredient, item));
			}
		}
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("info.smeltingrecipe");
	}

	@Override
	public String getGuiTexture() {
		return "textures/gui/container/furnace.png";
	}

}
