package nahama.ofalenmod.nei;

import codechicken.nei.api.API;
import nahama.ofalenmod.gui.GuiSmeltingMachine;
import nahama.ofalenmod.util.OfalenTimer;

public class OfalenModNEILoader {
	public static OfalenSmeltingRecipeHandler catchRecipe;

	public static void load() {
		OfalenTimer.start("OfalenModNEILoader.load");
		catchRecipe = new OfalenSmeltingRecipeHandler();
		API.registerRecipeHandler(catchRecipe);
		API.registerUsageHandler(catchRecipe);
		API.registerGuiOverlay(GuiSmeltingMachine.class, catchRecipe.getOverlayIdentifier(), 5, 10);
		OfalenTimer.watchAndLog("OfalenModNEILoader.load");
	}
}
