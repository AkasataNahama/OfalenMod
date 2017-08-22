package nahama.ofalenmod.setting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;

public class OfalenSettingString extends OfalenSettingContent<String> {
	public OfalenSettingString(String name, ItemStack stack, String valueDefault) {
		super(name, stack, valueDefault);
	}

	@Override
	protected NBTBase getTagFromValue(String value) {
		return new NBTTagString(value);
	}

	@Override
	protected String getValueFromTag(NBTBase nbt) {
		if (nbt instanceof NBTTagString)
			return ((NBTTagString) nbt).func_150285_a_();
		return valueDefault;
	}

	@Override
	protected String getChangedValue(String current, ItemStack stackSpecifier) {
		if (stackSpecifier.hasDisplayName())
			return stackSpecifier.getDisplayName();
		return valueDefault;
	}

	@Override
	public List<ItemStack> getSelectableItemList() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(Items.name_tag));
		list.add(new ItemStack(Blocks.dirt));
		return list;
	}
}
