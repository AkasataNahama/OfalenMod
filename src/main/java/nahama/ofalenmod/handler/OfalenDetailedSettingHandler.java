package nahama.ofalenmod.handler;

import nahama.ofalenmod.util.IItemOfalenSettable;
import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class OfalenDetailedSettingHandler {
	//	private static HashMap<String, OfalenSetting> listSettings = new HashMap<>();
	//	static {
	//		ArrayList<OfalenSetting> list;
	//		ArrayList<OfalenSetting> list1;
	//		ArrayList<OfalenSetting> list2;
	//		// 万能ツール。
	//		list = new ArrayList<>();
	//		// 通常破壊。採掘速度・フィルターを適用するか。
	//		list1 = new ArrayList<>();
	//		list1.add(new OfalenSetting("Efficiency", 64.0F));
	//		list1.add(new OfalenSetting("Filter", false));
	//		list.add(new OfalenSetting("NormalBreaking", list1));
	//		// 単一破壊。選択可能か・フィルターを適用するか。
	//		list1 = new ArrayList<>();
	//		list1.add(new OfalenSetting("Selectable", true));
	//		list1.add(new OfalenSetting("Filter", false));
	//		list.add(new OfalenSetting("SingleBreaking", list1));
	//		// 範囲破壊。選択可能か・フィルターを適用するか・基準範囲・拡縮強度・視線で向きを変えるか。
	//		list1 = new ArrayList<>();
	//		list1.add(new OfalenSetting("Selectable", true));
	//		list1.add(new OfalenSetting("Filter", true));
	//		list2 = new ArrayList<>();
	//		list2.add(new OfalenSetting("LengthX+", (byte) 1));
	//		list2.add(new OfalenSetting("LengthX-", (byte) 1));
	//		list2.add(new OfalenSetting("LengthY+", (byte) 1));
	//		list2.add(new OfalenSetting("LengthY-", (byte) 1));
	//		list2.add(new OfalenSetting("LengthZ+", (byte) 1));
	//		list2.add(new OfalenSetting("LengthZ-", (byte) 1));
	//		list1.add(new OfalenSetting("BaseRange", list2));
	//		list2 = new ArrayList<>();
	//		list2.add(new OfalenSetting("StrengthX+", (byte) 1));
	//		list2.add(new OfalenSetting("StrengthX-", (byte) 1));
	//		list2.add(new OfalenSetting("StrengthY+", (byte) 1));
	//		list2.add(new OfalenSetting("StrengthY-", (byte) 1));
	//		list2.add(new OfalenSetting("StrengthZ+", (byte) 1));
	//		list2.add(new OfalenSetting("StrengthZ-", (byte) 1));
	//		list1.add(new OfalenSetting("ExpandingStrength", list2));
	//		list1.add(new OfalenSetting("RespondEye", true));
	//		list.add(new OfalenSetting("RangeBreaking", list1));
	//		// クワ。選択可能か。
	//		list.add(new OfalenSetting("Hoe.Selectable", true));
	//		// 防御。選択可能か。
	//		list.add(new OfalenSetting("SwordGuard.Selectable", true));
	//		listSettings.put("OfalenPerfectTool", new OfalenSetting("PerfectTool", list));
	//		// フローター。
	//		list = new ArrayList<>();
	//		// モード1。デフォルトは「ジェットフライト」。
	//		list1 = new ArrayList<>();
	//		list1.add(new OfalenSetting("FlightMode", (byte) 1));
	//		list1.add(new OfalenSetting("AscentSpeed", 0.2D));
	//		list1.add(new OfalenSetting("HorizontalSpeed", 0.04D));
	//		list1.add(new OfalenSetting("MaxAscentSpeed", 1.0D));
	//		list.add(new OfalenSetting("FloaterMode-1", list1));
	//		// モード2。デフォルトは「グライドフライト」。
	//		list1 = new ArrayList<>();
	//		list1.add(new OfalenSetting("FlightMode", (byte) 1));
	//		list1.add(new OfalenSetting("AscentSpeed", 0.0D));
	//		list1.add(new OfalenSetting("HorizontalSpeed", 0.08D));
	//		list1.add(new OfalenSetting("MinAscentSpeed", -0.1D));
	//		list.add(new OfalenSetting("FloaterMode-2", list1));
	//		// モード3。デフォルトは「ジャンプフライド」。
	//		list1 = new ArrayList<>();
	//		list1.add(new OfalenSetting("FlightMode", (byte) 2));
	//		list1.add(new OfalenSetting("AscentSpeed", 0.8D));
	//		list1.add(new OfalenSetting("HorizontalSpeed", 0.04D));
	//		list.add(new OfalenSetting("FloaterMode-3", list1));
	//		// モード4。デフォルトは「クアイエットフライト」。
	//		list1 = new ArrayList<>();
	//		list1.add(new OfalenSetting("FlightMode", (byte) 3));
	//		list1.add(new OfalenSetting("AscentSpeed", 0.4D));
	//		list1.add(new OfalenSetting("DescentSpeed", 0.4D));
	//		list1.add(new OfalenSetting("HorizontalSpeed", 0.08D));
	//		list.add(new OfalenSetting("FloaterMode-4", list1));
	//		// モード5。デフォルトは「ホリザンタルフライト」。
	//		list1 = new ArrayList<>();
	//		list1.add(new OfalenSetting("FlightMode", (byte) 3));
	//		list1.add(new OfalenSetting("AscentSpeed", 0.0D));
	//		list1.add(new OfalenSetting("DescentSpeed", 0.0D));
	//		list1.add(new OfalenSetting("HorizontalSpeed", 0.02D));
	//		list.add(new OfalenSetting("FloaterMode-5", list1));
	//		listSettings.put("OfalenFloater", new OfalenSetting("Floater", list));
	//	}

	public static NBTTagCompound getSettingTag(ItemStack itemStack) {
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbtItem = itemStack.getTagCompound();
		NBTTagCompound nbt = nbtItem.getCompoundTag(OfalenNBTUtil.DETAILED_SETTING);
		if (!nbtItem.hasKey(OfalenNBTUtil.DETAILED_SETTING)) {
			nbt = (NBTTagCompound) ((IItemOfalenSettable) itemStack.getItem()).getSetting().getDefaultNBTTag();
			nbtItem.setTag(OfalenNBTUtil.DETAILED_SETTING, nbt);
		}
		return nbt;
	}

	public static Object getCurrentValueFromNBT(NBTTagCompound nbtSetting, String nameFormatted, OfalenSetting setting) {
		String[] array = nameFormatted.split("/");
		NBTBase nbt = nbtSetting;
		for (String s : array) {
			if (nbt instanceof NBTTagCompound) {
				nbt = ((NBTTagCompound) nbt).getTag(s);
			} else {
				break;
			}
		}
		return setting.getValueFromNBT(nbt);
	}

	public static void applySettingToNBT(NBTTagCompound nbtSetting, String nameFormatted, OfalenSetting setting, ItemStack settingItem) {
		String[] array = nameFormatted.split("/");
		NBTBase nbt = nbtSetting;
		for (int i = 0; i < array.length - 1; i++) {
			if (nbt instanceof NBTTagCompound) {
				nbt = ((NBTTagCompound) nbt).getTag(array[i]);
			} else {
				OfalenLog.debuggingInfo("Failed to apply setting to nbt! path : " + nameFormatted, "OfalenDetailedSettingHandler");
				return;
			}
		}
		if (nbt instanceof NBTTagCompound && array[array.length - 1].equals(setting.getSettingName())) {
			NBTTagCompound tagCompound = (NBTTagCompound) nbt;
			tagCompound.setTag(setting.getSettingName(), setting.getNBTTag(setting.getSettingValue(setting.getValueFromNBT(tagCompound.getTag(setting.getSettingName())), settingItem)));
		} else {
			OfalenLog.debuggingInfo("Failed to apply setting to nbt!(final) path : " + nameFormatted, "OfalenDetailedSettingHandler");
		}
	}
}
