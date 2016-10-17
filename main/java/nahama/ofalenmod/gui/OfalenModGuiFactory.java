package nahama.ofalenmod.gui;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import java.util.Set;

public class OfalenModGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return OfalenModConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

	public static class OfalenModConfigGui extends GuiConfig {
		public OfalenModConfigGui(GuiScreen parent) {
			super(parent, new ConfigElement(OfalenModConfigCore.cfg.getCategory(OfalenModConfigCore.GENERAL)).getChildElements(), OfalenModCore.MODID, false, false, OfalenModCore.MODNAME);
		}
	}

}
