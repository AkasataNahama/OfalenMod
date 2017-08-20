package nahama.ofalenmod.setting;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;

import java.util.ArrayList;
import java.util.List;

public class OfalenSettingInteger extends OfalenSettingContent<Integer> {
	private final int valueMin, valueMax;

	public OfalenSettingInteger(String name, ItemStack stack, int valueDefault) {
		this(name, stack, valueDefault, Integer.MIN_VALUE);
	}

	public OfalenSettingInteger(String name, ItemStack stack, int valueDefault, int valueMin) {
		this(name, stack, valueDefault, valueMin, Integer.MAX_VALUE);
	}

	public OfalenSettingInteger(String name, ItemStack stack, int valueDefault, int valueMin, int valueMax) {
		super(name, stack, valueDefault);
		this.valueMin = valueMin;
		this.valueMax = valueMax;
	}

	@Override
	protected NBTBase getTagFromValue(Integer value) {
		return new NBTTagInt(value);
	}

	@Override
	protected Integer getValueFromTag(NBTBase nbt) {
		int value = valueDefault;
		if (nbt instanceof NBTBase.NBTPrimitive)
			value = ((NBTBase.NBTPrimitive) nbt).func_150287_d();
		return getValidValue(value);
	}

	@Override
	protected Integer getChangedValue(Integer current, ItemStack stackSpecifier) {
		int value = valueDefault;
		if (stackSpecifier.getItem() == Item.getItemFromBlock(Blocks.stone)) {
			value = current + stackSpecifier.stackSize;
		} else if (stackSpecifier.getItem() == Item.getItemFromBlock(Blocks.cobblestone)) {
			value = current - stackSpecifier.stackSize;
		}
		return getValidValue(value);
	}

	@Override
	public List<ItemStack> getSelectableItemList() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(Blocks.stone));
		list.add(new ItemStack(Blocks.cobblestone));
		list.add(new ItemStack(Blocks.dirt));
		return list;
	}

	private int getValidValue(int value) {
		value = Math.max(valueMin, value);
		value = Math.min(valueMax, value);
		return value;
	}
}
