package nahama.ofalenmod.setting;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

import java.util.ArrayList;
import java.util.List;

public class OfalenSettingByte extends OfalenSettingContent<Byte> {
	private final byte valueMin, valueMax;

	public OfalenSettingByte(String name, ItemStack stack, int valueDefault) {
		this(name, stack, valueDefault, Byte.MIN_VALUE);
	}

	public OfalenSettingByte(String name, ItemStack stack, int valueDefault, int valueMin) {
		this(name, stack, valueDefault, valueMin, Byte.MAX_VALUE);
	}

	public OfalenSettingByte(String name, ItemStack stack, int valueDefault, int valueMin, int valueMax) {
		super(name, stack, (byte) valueDefault);
		this.valueMin = (byte) valueMin;
		this.valueMax = (byte) valueMax;
	}

	@Override
	public NBTBase getTagFromValue(Byte value) {
		return new NBTTagByte(value);
	}

	@Override
	public Byte getValueFromTag(NBTBase nbt) {
		byte value = valueDefault;
		if (nbt instanceof NBTBase.NBTPrimitive)
			value = ((NBTBase.NBTPrimitive) nbt).func_150290_f();
		return getValidValue(value);
	}

	@Override
	public Byte getChangedValue(Byte current, ItemStack stackSpecifier) {
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

	private byte getValidValue(int value) {
		value = Math.max(valueMin, value);
		value = Math.min(valueMax, value);
		return (byte) value;
	}
}
