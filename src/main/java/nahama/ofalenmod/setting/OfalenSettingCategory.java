package nahama.ofalenmod.setting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OfalenSettingCategory extends OfalenSetting {
	private final List<OfalenSetting> listSetting = new ArrayList<OfalenSetting>();

	public OfalenSettingCategory(String name, ItemStack stack) {
		super(name, stack);
	}

	@Override
	public List<ItemStack> getSelectableItemList() {
		List<ItemStack> listStack = new ArrayList<ItemStack>();
		for (OfalenSetting setting : listSetting) {
			listStack.add(setting.getSpecifierStack());
		}
		return listStack;
	}

	public void registerChildSetting(OfalenSetting setting) {
		setting.setPath(this.getPath() + PATH_SEPARATOR + setting.getName());
		listSetting.add(setting);
	}

	public OfalenSetting getChildSetting(ItemStack settingItem) {
		for (OfalenSetting setting : listSetting) {
			if (setting.isSpecifierStack(settingItem))
				return setting;
		}
		return null;
	}

	public OfalenSetting getChildSetting(String name) {
		for (OfalenSetting setting : listSetting) {
			if (setting.getName().equals(name))
				return setting;
		}
		return null;
	}
}
