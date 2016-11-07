package nahama.ofalenmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;

public class OfalenNBTUtil {
	// TileEntity
	public static final String SLOT = "Slot";
	public static final String ITEMS = "Items";
	public static final String CUSTOM_NAME = "CustomName";
	// 機械系
	public static final String GRADE = "Grade";
	public static final String WORKING_TIME = "WorkingTime";
	public static final String BURNING_TIME = "BurningTime";
	// 変換機
	public static final String SAMPLE = "Sample";
	// 修繕機
	public static final String IS_IRREPARABLE = "IsIrreparableOfalen";
	// レーザー
	public static final String LASER_COLOR = "LaserColor";
	public static final String TILE_X = "TileX";
	public static final String TILE_Y = "TileY";
	public static final String TILE_Z = "TileZ";
	public static final String TILE_ID = "TileId";
	public static final String SHAKE = "Shake";
	public static final String IS_IN_GROUND = "IsInGround";
	public static final String OWNER_NAME = "OwnerName";
	// 未来系
	public static final String INTERVAL = "Interval";
	public static final String IS_VALID = "IsValid";
	// テレポーター
	public static final String MATERIAL = "Material";
	// テレポーティングマーカー
	public static final String CHANNEL = "Channel";
	// フローター
	public static final String MODE = "Mode";
	// コレクター
	public static final String IS_ITEM_DISABLED = "IsItemDisabled";
	public static final String IS_EXP_DISABLED = "IsExpDisabled";
	public static final String ITEM_RANGE = "ItemRange";
	public static final String EXP_RANGE = "ExpRange";
	// 詳細設定
	public static final String IS_SET_IN_DETAIL = "IsSetInDetail";

	public static boolean containsNBT(NBTTagList list, NBTTagCompound nbt) {
		for (int i = 0; i < list.tagCount(); i++) {
			if (list.getCompoundTagAt(i).equals(nbt))
				return true;
		}
		return false;
	}

	public static class FilterUtil {
		public static final String ITEM_FILTER = "OfalenItemFilter";
		public static final String IS_WHITE = "IsWhiteOfalenFilter";
		public static final String SELECTING = "OfalenFilterSelectingItems";

		public static boolean canItemFilterThrough(NBTTagCompound filter, ItemStack checking) {
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
			return !isWhite;
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

		public static void copyFilterTag(NBTTagCompound nbtFilterCopying, NBTTagCompound nbtFilterModel) {
			nbtFilterCopying.setBoolean(IS_WHITE, isWhiteList(nbtFilterModel));
			nbtFilterCopying.setTag(SELECTING, getSelectingItemList(nbtFilterModel));
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

		/** フィルターの情報を返す。ToolTip表示用。 */
		public static ArrayList<String> getFilterInformation(ItemStack itemStack) {
			ArrayList<String> ret = new ArrayList<>();
			// Shiftが押されていなければフィルターに対応していることを表示。
			if (!Util.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
				ret.add(StatCollector.translateToLocal("info.ofalen.filter.toolTip"));
				return ret;
			}
			// フィルターが無効なら終了。
			if (!isAvailableFilterTag(itemStack))
				return ret;
			NBTTagCompound nbtFilter = FilterUtil.getFilterTag(itemStack);
			ret.add(StatCollector.translateToLocal("info.ofalen.filter.item.installed"));
			ret.add("  " + StatCollector.translateToLocal("info.ofalen.filter." + (FilterUtil.isWhiteList(nbtFilter) ? "white" : "black")));
			NBTTagList nbtTagList = FilterUtil.getSelectingItemList(nbtFilter);
			for (int i = 0; i < 27 && i < nbtTagList.tagCount(); i++) {
				NBTTagCompound nbt = nbtTagList.getCompoundTagAt(i);
				if (nbt == null)
					continue;
				ItemStack itemStack1 = ItemStack.loadItemStackFromNBT(nbt);
				if (itemStack1 != null)
					ret.add("    " + itemStack1.getDisplayName() + " (" + Item.getIdFromItem(itemStack1.getItem()) + ", " + itemStack1.getItemDamage() + ")");
			}
			return ret;
		}
	}
}
