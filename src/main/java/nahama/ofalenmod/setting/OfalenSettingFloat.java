package nahama.ofalenmod.setting;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;

import java.util.ArrayList;
import java.util.List;

public class OfalenSettingFloat extends OfalenSettingContent<Float> {
	private final float valueMin, valueMax;

	public OfalenSettingFloat(String name, ItemStack stack, double valueDefault) {
		this(name, stack, valueDefault, Float.MIN_VALUE);
	}

	public OfalenSettingFloat(String name, ItemStack stack, double valueDefault, double valueMin) {
		this(name, stack, valueDefault, valueMin, Float.MAX_VALUE);
	}

	public OfalenSettingFloat(String name, ItemStack stack, double valueDefault, double valueMin, double valueMax) {
		super(name, stack, (float) valueDefault);
		this.valueMin = (float) valueMin;
		this.valueMax = (float) valueMax;
	}

	@Override
	public NBTBase getTagFromValue(Float value) {
		return new NBTTagFloat(value);
	}

	@Override
	public Float getValueFromTag(NBTBase nbt) {
		double value = valueDefault;
		if (nbt instanceof NBTBase.NBTPrimitive)
			value = ((NBTBase.NBTPrimitive) nbt).func_150288_h();
		return getValidValue(value);
	}

	@Override
	public Float getChangedValue(Float current, ItemStack stackSpecifier) {
		double value = valueDefault;
		if (stackSpecifier.getItem() == Item.getItemFromBlock(Blocks.stone)) {
			value = (Math.round(current * 10.0D) + stackSpecifier.stackSize) / 10.0D;
		} else if (stackSpecifier.getItem() == Item.getItemFromBlock(Blocks.cobblestone)) {
			value = (Math.round(current * 10.0D) - stackSpecifier.stackSize) / 10.0D;
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

	private float getValidValue(double value) {
		value = Math.max(valueMin, value);
		value = Math.min(valueMax, value);
		return (float) value;
	}
}
