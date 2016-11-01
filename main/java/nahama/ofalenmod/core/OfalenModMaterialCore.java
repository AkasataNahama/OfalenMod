package nahama.ofalenmod.core;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class OfalenModMaterialCore {
	// TODO バランス調整
	// ツールマテリアルの定義
	public static final ToolMaterial OFALEN_TOOL_G1 = EnumHelper.addToolMaterial("OFALEN_TOOL_G1", 4, 1561, 8.0F, 3.0F, 10);
	public static final ToolMaterial OFALEN_TOOL_G2 = EnumHelper.addToolMaterial("OFALEN_TOOL_G2", 4, 3123, 16.0F, 6.0F, 20);
	public static final ToolMaterial OFALEN_TOOL_G3 = EnumHelper.addToolMaterial("OFALEN_TOOL_G3", 4, 6247, 32.0F, 12.0F, 40);
	public static final ToolMaterial OFALEN_TOOL_P = EnumHelper.addToolMaterial("OFALEN_TOOL_P", 5, 12495, OfalenModConfigCore.efficiencyPerfectTool, 24.0F, 80);
	// アーマーマテリアルの定義
	public static final ArmorMaterial OFALEN_ARMOR_G1 = EnumHelper.addArmorMaterial("OFALEN_ARMOR_G1", 33, new int[] { 3, 8, 6, 3 }, 10);
	public static final ArmorMaterial OFALEN_ARMOR_G2 = EnumHelper.addArmorMaterial("OFALEN_ARMOR_G2", 66, new int[] { 3, 8, 6, 3 }, 20);
	public static final ArmorMaterial OFALEN_ARMOR_G3 = EnumHelper.addArmorMaterial("OFALEN_ARMOR_G3", 132, new int[] { 3, 8, 6, 3 }, 40);
	public static final ArmorMaterial OFALEN_ARMOR_P = EnumHelper.addArmorMaterial("OFALEN_ARMOR_P", 264, new int[] { 3, 8, 6, 3 }, 80);
}
