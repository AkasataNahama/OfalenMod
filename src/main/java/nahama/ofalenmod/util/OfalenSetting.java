package nahama.ofalenmod.util;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public abstract class OfalenSetting {
	protected final String name;
	protected final ItemStack itemStack;
	protected final Object object;

	public OfalenSetting(String settingName, ItemStack settingItem, Object defaultValue) {
		name = settingName;
		itemStack = settingItem;
		object = defaultValue;
	}

	public String getSettingName() {
		return name;
	}

	public String getLocalizedSettingName() {
		return StatCollector.translateToLocal("info.settingDetailed.name." + name);
	}

	public ItemStack getSettingItem() {
		return itemStack;
	}

	public abstract ArrayList<ItemStack> getSelectableItems();

	public boolean isValidItem(ItemStack itemStack) {
		ArrayList<ItemStack> list = this.getSelectableItems();
		for (ItemStack stack : list) {
			if (OreDictionary.itemMatches(stack, itemStack, false))
				return true;
		}
		return false;
	}

	public Object getDefaultValue() {
		return object;
	}

	public abstract Object getSettingValue(Object currentValue, ItemStack settingItem);

	public abstract boolean hasChildSetting();

	public abstract OfalenSetting getChildSetting(ItemStack settingItem);

	public abstract NBTBase getNBTTag(Object currentValue);

	public NBTBase getDefaultNBTTag() {
		return this.getNBTTag(object);
	}

	public abstract Object getValueFromNBT(NBTBase nbt);

	public static abstract class OfalenSettingPrimitive extends OfalenSetting {
		public OfalenSettingPrimitive(String settingName, ItemStack settingItem, Object defaultValue) {
			super(settingName, settingItem, defaultValue);
		}

		@Override
		public ArrayList<ItemStack> getSelectableItems() {
			ArrayList<ItemStack> list = new ArrayList<ItemStack>();
			list.add(new ItemStack(Blocks.stone));
			list.add(new ItemStack(Blocks.cobblestone));
			list.add(new ItemStack(Blocks.dirt));
			return list;
		}

		@Override
		public boolean hasChildSetting() {
			return false;
		}

		@Override
		public OfalenSetting getChildSetting(ItemStack settingItem) {
			return null;
		}

		protected int getSettingItemType(ItemStack settingItem) {
			if (OreDictionary.itemMatches(new ItemStack(Blocks.stone), settingItem, false))
				return settingItem.stackSize;
			if (OreDictionary.itemMatches(new ItemStack(Blocks.cobblestone), settingItem, false))
				return -settingItem.stackSize;
			return 0;
		}
	}

	public static class OfalenSettingBoolean extends OfalenSettingPrimitive {
		public OfalenSettingBoolean(String settingName, ItemStack settingItem, boolean defaultValue) {
			super(settingName, settingItem, defaultValue);
		}

		@Override
		public Object getSettingValue(Object currentValue, ItemStack settingItem) {
			int i = this.getSettingItemType(settingItem);
			if (i < 0)
				return false;
			if (i > 0)
				return true;
			return object;
		}

		@Override
		public NBTBase getNBTTag(Object currentValue) {
			return new NBTTagByte((byte) ((Boolean) currentValue ? 1 : 0));
		}

		@Override
		public Object getValueFromNBT(NBTBase nbt) {
			if (nbt instanceof NBTTagByte)
				return ((NBTTagByte) nbt).func_150290_f() == 1;
			OfalenLog.debuggingInfo("Failed to getValueFromNBT", "OfalenSettingBoolean");
			return object;
		}
	}

	public static class OfalenSettingByte extends OfalenSettingPrimitive {
		protected final byte min;
		protected final byte max;

		public OfalenSettingByte(String settingName, ItemStack settingItem, int defaultValue) {
			super(settingName, settingItem, (byte) defaultValue);
			min = Byte.MIN_VALUE;
			max = Byte.MAX_VALUE;
		}

		public OfalenSettingByte(String settingName, ItemStack settingItem, int defaultValue, boolean isPositiveOnly) {
			super(settingName, settingItem, (byte) defaultValue);
			this.min = 0;
			this.max = Byte.MAX_VALUE;
		}

		public OfalenSettingByte(String settingName, ItemStack settingItem, int defaultValue, int min, int max) {
			super(settingName, settingItem, (byte) defaultValue);
			this.min = (byte) min;
			this.max = (byte) max;
		}

		@Override
		public Object getSettingValue(Object currentValue, ItemStack settingItem) {
			if (currentValue instanceof Byte) {
				int i = this.getSettingItemType(settingItem);
				if (i == 0)
					return object;
				i += (Byte) currentValue;
				if (i < min)
					return min;
				if (i > max)
					return max;
				return (byte) i;
			}
			return currentValue;
		}

		@Override
		public NBTBase getNBTTag(Object currentValue) {
			return new NBTTagByte((Byte) currentValue);
		}

		@Override
		public Object getValueFromNBT(NBTBase nbt) {
			if (nbt instanceof NBTTagByte)
				return ((NBTTagByte) nbt).func_150290_f();
			OfalenLog.debuggingInfo("Failed to getValueFromNBT", "OfalenSettingByte");
			return object;
		}
	}

	public static class OfalenSettingDouble extends OfalenSettingPrimitive {
		public OfalenSettingDouble(String settingName, ItemStack settingItem, double defaultValue) {
			super(settingName, settingItem, defaultValue);
		}

		@Override
		public Object getSettingValue(Object currentValue, ItemStack settingItem) {
			if (currentValue instanceof Double) {
				double d = this.getSettingItemType(settingItem);
				if (d == 0)
					return object;
				d = 0.1 * d + (Double) currentValue;
				return d;
			}
			return currentValue;
		}

		@Override
		public NBTBase getNBTTag(Object currentValue) {
			return new NBTTagDouble((Double) currentValue);
		}

		@Override
		public Object getValueFromNBT(NBTBase nbt) {
			if (nbt instanceof NBTTagDouble)
				return ((NBTTagDouble) nbt).func_150286_g();
			OfalenLog.debuggingInfo("Failed to getValueFromNBT", "OfalenSettingDouble");
			return object;
		}
	}

	public static class OfalenSettingList extends OfalenSetting {
		public OfalenSettingList(String settingName, ItemStack settingItem, ArrayList<OfalenSetting> defaultValue) {
			super(settingName, settingItem, defaultValue);
		}

		@Override
		public ArrayList<ItemStack> getSelectableItems() {
			ArrayList<ItemStack> list = new ArrayList<ItemStack>();
			for (OfalenSetting setting : (ArrayList<OfalenSetting>) object) {
				list.add(setting.getSettingItem());
			}
			return list;
		}

		@Override
		public Object getSettingValue(Object currentValue, ItemStack settingItem) {
			return null;
		}

		@Override
		public boolean hasChildSetting() {
			return true;
		}

		@Override
		public OfalenSetting getChildSetting(ItemStack settingItem) {
			for (OfalenSetting setting : (ArrayList<OfalenSetting>) object) {
				if (OreDictionary.itemMatches(settingItem, setting.getSettingItem(), false))
					return setting;
			}
			return null;
		}

		@Override
		public NBTBase getNBTTag(Object currentValue) {
			OfalenLog.error("OfalenSettingList is not changeable!", "OfalenSettingList");
			return null;
		}

		@Override
		public NBTBase getDefaultNBTTag() {
			NBTTagCompound nbt = new NBTTagCompound();
			for (OfalenSetting setting : (ArrayList<OfalenSetting>) object) {
				nbt.setTag(setting.getSettingName(), setting.getDefaultNBTTag());
			}
			return nbt;
		}

		@Override
		public Object getValueFromNBT(NBTBase nbt) {
			OfalenLog.error("OfalenSettingList has no value!", "OfalenSettingList");
			return object;
		}
	}
}
