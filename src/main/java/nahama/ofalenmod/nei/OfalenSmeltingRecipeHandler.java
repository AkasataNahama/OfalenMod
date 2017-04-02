package nahama.ofalenmod.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import nahama.ofalenmod.gui.GuiSmeltingMachine;
import nahama.ofalenmod.recipe.OfalenSmeltingManager;
import nahama.ofalenmod.recipe.OfalenSmeltingManager.OfalenSmeltingRecipe;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.awt.*;

public class OfalenSmeltingRecipeHandler extends TemplateRecipeHandler {
	public static OfalenSmeltingRecipeHandler instance = new OfalenSmeltingRecipeHandler();

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
		if (!outputId.equals("OfalenSmeltingRecipe")) {
			super.loadCraftingRecipes(outputId, results);
			return;
		}
		if (!OfalenSmeltingManager.getInstance().isValid())
			return;
		for (OfalenSmeltingRecipe recipe : OfalenSmeltingManager.getInstance().recipes) {
			ItemStack in = recipe.material;
			ItemStack out = recipe.result;
			arecipes.add(new CachedOfalenSmeltingRecipe(in, out));
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if (!OfalenSmeltingManager.getInstance().isValid())
			return;
		for (OfalenSmeltingRecipe recipe : OfalenSmeltingManager.getInstance().recipes) {
			ItemStack in = recipe.material;
			ItemStack out = recipe.result;
			if (NEIServerUtils.areStacksSameType(out, result))
				arecipes.add(new CachedOfalenSmeltingRecipe(in, out));
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if (!OfalenSmeltingManager.getInstance().isValid())
			return;
		for (OfalenSmeltingRecipe recipe : OfalenSmeltingManager.getInstance().recipes) {
			ItemStack in = recipe.material;
			ItemStack out = recipe.result;
			if (ingredient.isItemEqual(in))
				arecipes.add(new CachedOfalenSmeltingRecipe(in, out));
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

	public class CachedOfalenSmeltingRecipe extends CachedRecipe {
		private PositionedStack material;
		private PositionedStack result;

		public CachedOfalenSmeltingRecipe(ItemStack in, ItemStack out) {
			in.stackSize = 1;
			material = new PositionedStack(in, 51, 6);
			result = new PositionedStack(out, 111, 24);
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}

		@Override
		public PositionedStack getIngredient() {
			return material;
		}
	}
}
