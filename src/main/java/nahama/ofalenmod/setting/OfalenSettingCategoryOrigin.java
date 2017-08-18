package nahama.ofalenmod.setting;

import net.minecraft.item.ItemStack;

public class OfalenSettingCategoryOrigin extends OfalenSettingCategory {
	public OfalenSettingCategoryOrigin(String name, ItemStack stack) {
		super(name, stack);
		this.setPath(name);
	}

	@Override
	public String getLocalizedName() {
		return this.getSpecifierStack().getDisplayName();
	}
}
