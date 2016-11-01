package nahama.ofalenmod.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import nahama.ofalenmod.gui.GuiSmeltingMachine;
import nahama.ofalenmod.recipe.OfalenSmeltingRecipe;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class CatchRecipeHandler extends TemplateRecipeHandler {
	private HashMap<ItemStack, ItemStack> newRecipe;

	private HashMap<ItemStack, ItemStack> recipeLoader() {
		if (OfalenSmeltingRecipe.getInstance().listRecipe != null && !OfalenSmeltingRecipe.getInstance().listRecipe.isEmpty()) {
			newRecipe = new HashMap<>();
			Iterator<Entry<ItemStack[], Boolean>> iterator = OfalenSmeltingRecipe.getInstance().listRecipe.entrySet().iterator();
			Entry<ItemStack[], Boolean> entry;
			while (iterator.hasNext()) {
				entry = iterator.next();
				newRecipe.put(entry.getKey()[0], entry.getKey()[1]);
			}
		}
		return newRecipe;
	}

	public class recipeCatcher extends CachedRecipe {
		private PositionedStack input;
		private PositionedStack result;

		public recipeCatcher(ItemStack in, ItemStack out) {
			in.stackSize = 1;
			input = new PositionedStack(in, 51, 6);
			result = new PositionedStack(out, 111, 23);
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}

		@Override
		public PositionedStack getIngredient() {
			return input;
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
		transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(74, 23, 24, 18), "OfalenSmeltingRecipe"));
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
				arecipes.add(new recipeCatcher(in, item));
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
				arecipes.add(new recipeCatcher(in, item));
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
				arecipes.add(new recipeCatcher(ingredient, item));
			}
		}
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("info.ofalen.recipeSmelting");
	}

	@Override
	public String getGuiTexture() {
		return "textures/gui/container/furnace.png";
	}
}
