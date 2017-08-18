package nahama.ofalenmod.setting;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class OfalenSettingBoolean extends OfalenSettingContent<Boolean> {
	public OfalenSettingBoolean(String name, ItemStack stack, boolean valueDefault) {
		super(name, stack, valueDefault);
	}

	@Override
	public NBTBase getTagFromValue(Boolean value) {
		return new NBTTagByte((byte) (value ? 1 : 0));
	}

	@Override
	public Boolean getValueFromTag(NBTBase nbt) {
		if (nbt instanceof NBTBase.NBTPrimitive)
			return ((NBTBase.NBTPrimitive) nbt).func_150290_f() != 0;
		return valueDefault;
	}

	@Override
	public Boolean getChangedValue(Boolean current, ItemStack stackSpecifier) {
		if (stackSpecifier.getItem() == Item.getItemFromBlock(Blocks.stone))
			return !current;
		return valueDefault;
	}

	@Override
	public List<ItemStack> getSelectableItemList() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(Blocks.stone));
		list.add(new ItemStack(Blocks.dirt));
		return list;
	}

	@Override
	protected String getMessageFromValue(Boolean value) {
		if (value != null)
			return StatCollector.translateToLocal(LANGUAGE_PREFIX + "boolean." + value);
		return super.getMessageFromValue(null);
	}
}
