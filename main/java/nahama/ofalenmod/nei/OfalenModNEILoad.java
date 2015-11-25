package nahama.ofalenmod.nei;

import nahama.ofalenmod.gui.GuiSmeltingMachine;
import codechicken.nei.api.API;

public class OfalenModNEILoad {

	/*別クラスで作るレシピ表示用インターフェースの取得*/
	public static CatchRecipeHandler catchRecipe;

	public static void load() {
		catchRecipe = new CatchRecipeHandler();

		API.registerRecipeHandler(catchRecipe);
		API.registerUsageHandler(catchRecipe);
		API.registerGuiOverlay(GuiSmeltingMachine.class, catchRecipe.getOverlayIdentifier(), 0, 0);
	}

}
