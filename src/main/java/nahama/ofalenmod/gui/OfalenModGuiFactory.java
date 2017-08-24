package nahama.ofalenmod.gui;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.Set;

@SuppressWarnings("unused")
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
			super(parent, (new ConfigElement<Object>(OfalenModConfigCore.cfg.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(), OfalenModCore.MOD_ID, false, false, StatCollector.translateToLocal(OfalenModCore.UNLOCALIZED_MOD_NAME));
		}
	}
}
