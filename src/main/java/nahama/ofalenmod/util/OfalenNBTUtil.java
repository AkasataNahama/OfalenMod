package nahama.ofalenmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

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
	// 万能ツール
	public static final String RANGE_LENGTH_ADDITION = "RangeLengthAddition";
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
	public static final String INTERVAL_RIGHT_CLICK = "IntervalRightClick";
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
	// 世界系 (INTERVALも使用)
	public static final String IS_RANGE_SAVING_ABSOLUTE = "IsRangeSavingAbsolute";
	public static final String RANGE = "Range";
	public static final String WORKING_COORD = "WorkingCoord";
	public static final String PROCESSING_INTERVAL = "ProcessingInterval";
	public static final String RESTARTING_INTERVAL = "RestartingInterval";
	public static final String FUEL_AMOUNT = "FuelAmount";
	public static final String CAN_RESTART = "CanRestart";
	public static final String IS_WORKING = "IsWorking";
	public static final String TILE_ENTITY_WORLD_EDITOR_BASE = "TileEntityWorldEditorBase";
	public static final String IS_SURVEYING = "IsSurveying";
	public static final String REMAINING_ENERGY = "RemainingEnergy";
	// 移動機
	public static final String IS_REMOVING_DISABLED = "IsRemovingDisabled";
	public static final String IS_PLACING_DISABLED = "IsPlacingDisabled";
	public static final String CAN_REMOVE_LIQUID = "CanRemoveLiquid";
	public static final String CAN_MOVE_TILE_ENTITY = "CanMoveTileEntity";
	public static final String MOVING_BLOCKS = "MovingBlocks";
	public static final String BLOCK_POS = "BlockPos";
	public static final String BLOCK_DATA = "BlockData";
	public static final String META = "Meta";
	public static final String TILE_ENTITY = "TileEntity";
	// 破壊機
	public static final String CAN_DELETE_BROKEN_BLOCK = "CanDeleteBrokenBlock";
	public static final String CAN_DELETE_LIQUID = "CanDeleteLiquid";
	// 収集機
	public static final String CAN_DELETE_ITEM = "CanDeleteItem";
	public static final String CAN_DELETE_EXP = "CanDeleteExp";
	// 詳細設定
	public static final String IS_SET_IN_DETAIL = "IsSetInDetail";
	public static final String DETAILED_SETTING = "DetailedSetting";
	// BlockPos, BlockRange
	public static final String X = "X";
	public static final String Y = "Y";
	public static final String Z = "Z";
	public static final String MIN_POS = "MinPos";
	public static final String MAX_POS = "MaxPos";
	public static final String POS_A = "PosA";
	public static final String POS_B = "PosB";

	public static boolean containsNBT(NBTTagList list, NBTTagCompound nbt) {
		for (int i = 0; i < list.tagCount(); i++) {
			if (list.getCompoundTagAt(i).equals(nbt))
				return true;
		}
		return false;
	}

	public static NBTTagList writeItemStacksToNBTTagList(ItemStack[] itemStacks) {
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < itemStacks.length; i++) {
			if (itemStacks[i] == null)
				continue;
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setByte(OfalenNBTUtil.SLOT, (byte) i);
			itemStacks[i].writeToNBT(nbt1);
			nbtTagList.appendTag(nbt1);
		}
		return nbtTagList;
	}

	public static ItemStack[] loadItemStacksFromNBTTagList(NBTTagList nbtTagList, int length) {
		ItemStack[] itemStacks = new ItemStack[length];
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			NBTTagCompound nbt1 = nbtTagList.getCompoundTagAt(i);
			byte b0 = nbt1.getByte(OfalenNBTUtil.SLOT);
			if (0 <= b0 && b0 < itemStacks.length)
				itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt1);
		}
		return itemStacks;
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

		public static void onUpdateFilter(ItemStack itemStack) {
			if (!isAvailableFilterTag(itemStack))
				initFilterTag(itemStack);
		}

		public static void initFilterTag(ItemStack itemStack) {
			NBTTagCompound nbt = new NBTTagCompound();
			if (itemStack.hasTagCompound())
				nbt = itemStack.getTagCompound();
			nbt.setTag(ITEM_FILTER, getInitializedFilterTag());
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
			return itemStack.getTagCompound().getCompoundTag(ITEM_FILTER);
		}

		public static NBTTagCompound getInitializedFilterTag() {
			NBTTagCompound nbtFilter = new NBTTagCompound();
			nbtFilter.setTag(SELECTING, new NBTTagList());
			return nbtFilter;
		}

		public static boolean isWhiteList(NBTTagCompound nbtFilter) {
			return nbtFilter.getBoolean(IS_WHITE);
		}

		public static NBTTagList getSelectingItemList(NBTTagCompound nbtFilter) {
			return nbtFilter.getTagList(SELECTING, 10);
		}

		/** フィルターの情報を返す。ToolTip表示用。 */
		public static ArrayList<String> getFilterInformation(ItemStack itemStack) {
			ArrayList<String> ret = new ArrayList<String>();
			// Shiftが押されていなければフィルターに対応していることを表示。
			if (!OfalenUtil.isKeyPressed(Minecraft.getMinecraft().gameSettings.keyBindSneak)) {
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

		public static void installFilterToTileEntity(World world, int x, int y, int z, NBTTagCompound filter) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity == null || !(tileEntity instanceof IFilterable))
				return;
			IFilterable filterable = (IFilterable) tileEntity;
			filterable.setTagItemFilter(filter);
		}

		public interface IFilterable {
			void setTagItemFilter(NBTTagCompound filter);
		}
	}
}
