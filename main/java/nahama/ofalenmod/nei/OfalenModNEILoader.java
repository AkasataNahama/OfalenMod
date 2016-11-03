package nahama.ofalenmod.nei;

import codechicken.nei.api.API;
import nahama.ofalenmod.gui.GuiSmeltingMachine;

public class OfalenModNEILoader {
	public static OfalenSmeltingRecipeHandler catchRecipe;

	public static void load() {
		catchRecipe = new OfalenSmeltingRecipeHandler();
		API.registerRecipeHandler(catchRecipe);
		API.registerUsageHandler(catchRecipe);
		API.registerGuiOverlay(GuiSmeltingMachine.class, catchRecipe.getOverlayIdentifier(), 5, 10);
	}
}
