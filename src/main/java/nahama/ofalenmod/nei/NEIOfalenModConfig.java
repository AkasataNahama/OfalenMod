package nahama.ofalenmod.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.gui.GuiSmeltingMachine;

public class NEIOfalenModConfig implements IConfigureNEI {
	@Override
	public void loadConfig() {
		// NEI<something>Configという名前でIConfigureNEIを実装しておくと自動で呼ばれる。
		// NEIにオファレン精錬機のレシピを登録する。
		API.registerRecipeHandler(OfalenSmeltingRecipeHandler.instance);
		API.registerUsageHandler(OfalenSmeltingRecipeHandler.instance);
		API.registerGuiOverlay(GuiSmeltingMachine.class, OfalenSmeltingRecipeHandler.instance.getOverlayIdentifier(), 5, 10);
	}

	@Override
	public String getName() {
		return OfalenModCore.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return OfalenModCore.VERSION;
	}
}
