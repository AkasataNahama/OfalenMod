package nahama.ofalenmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

import java.util.List;

public class OfalenNBTUtil {
	public static final String INTERVAL = "Interval";
	public static final String IS_ITEM_DISABLED = "IsItemDisabled";
	public static final String IS_EXP_DISABLED = "IsExpDisabled";

	public static final String ITEM_RANGE = "ItemRange";
	public static final String EXP_RANGE = "ExpRange";

	public static final String IS_SET_IN_DETAIL = "IsSetInDetail";

	public static class FilterUtil {
		public static final String ITEM_FILTER = "OfalenItemFilter";
		public static final String IS_WHITE = "IsWhiteOfalenFilter";
		public static final String SELECTING = "OfalenFilterSelectingItems";

		public static boolean isEnabledItem(NBTTagCompound filter, ItemStack checking) {
			boolean isWhite = isWhiteList(filter);
			NBTTagList nbtTagList = getSelectingItemList(filter);
			for (int i = 0; i < nbtTagList.tagCount(); i++) {
				NBTTagCompound nbt = nbtTagList.getCompoundTagAt(i);
				if (nbt == null)
					continue;
				ItemStack itemStack = ItemStack.loadItemStackFromNBT(nbt);
				if (itemStack == null)
					continue;
				if (itemStack.isItemEqual(checking))
					return isWhite;
			}
			return false;
		}

		public static void initFilterTag(ItemStack itemStack) {
			NBTTagCompound nbt = new NBTTagCompound();
			if (itemStack.hasTagCompound())
				nbt = itemStack.getTagCompound();
			NBTTagCompound nbtFilter = new NBTTagCompound();
			nbtFilter.setTag(SELECTING, new NBTTagList());
			nbt.setTag(ITEM_FILTER, nbtFilter);
			itemStack.setTagCompound(nbt);
		}

		public static void setFilterTag(NBTTagCompound nbtFilterable, NBTTagCompound nbtFilter) {
			nbtFilterable.setBoolean(IS_WHITE, isWhiteList(nbtFilter));
			nbtFilterable.setTag(SELECTING, getSelectingItemList(nbtFilter));
		}

		public static boolean isAvailableFilterTag(ItemStack itemStack) {
			if (!itemStack.hasTagCompound())
				return false;
			NBTTagCompound nbtFilter = getFilterTag(itemStack);
			return nbtFilter != null && getSelectingItemList(nbtFilter) != null;
		}

		public static NBTTagCompound getFilterTag(ItemStack itemStack) {
			return (NBTTagCompound) itemStack.getTagCompound().getTag(ITEM_FILTER);
		}

		public static boolean isWhiteList(NBTTagCompound nbtFilter) {
			return nbtFilter.getBoolean(IS_WHITE);
		}

		public static NBTTagList getSelectingItemList(NBTTagCompound nbtFilter) {
			return nbtFilter.getTagList(SELECTING, 10);
		}

		public static void addFilterInformation(ItemStack itemStack, List list) {
			if (!Util.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
				list.add(StatCollector.translateToLocal("info.OfalenMod.PressShift"));
				return;
			} else if (!isAvailableFilterTag(itemStack)) {
				return;
			}
			NBTTagCompound nbtFilter = FilterUtil.getFilterTag(itemStack);
			list.add("info.OfalenMod.ItemFilter." + (FilterUtil.isWhiteList(nbtFilter) ? "White" : "Black"));
			NBTTagList nbtTagList = FilterUtil.getSelectingItemList(nbtFilter);
			for (int i = 0; i < 27 && i < nbtTagList.tagCount(); i++) {
				NBTTagCompound nbt = nbtTagList.getCompoundTagAt(i);
				if (nbt == null)
					continue;
				ItemStack itemStack1 = ItemStack.loadItemStackFromNBT(nbt);
				try {
					list.add(itemStack1.getDisplayName() + " (" + Item.getIdFromItem(itemStack1.getItem()) + ", " + itemStack1.getItemDamage() + ")");
				} catch (Exception e) {
					Util.error("Error on getting filter information.", "ItemFilter");
				}
			}
		}
	}
}
