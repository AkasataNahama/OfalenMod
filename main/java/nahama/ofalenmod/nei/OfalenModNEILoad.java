package nahama.ofalenmod.nei;

import codechicken.nei.api.API;
import nahama.ofalenmod.gui.GuiSmeltingMachine;

public class OfalenModNEILoad {

	public static CatchRecipeHandler catchRecipe;

	public static void load() {
		catchRecipe = new CatchRecipeHandler();

		API.registerRecipeHandler(catchRecipe);
		API.registerUsageHandler(catchRecipe);
		API.registerGuiOverlay(GuiSmeltingMachine.class, catchRecipe.getOverlayIdentifier(), 0, 0);
	}

}
