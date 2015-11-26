package nahama.ofalenmod.core;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class OfalenModMaterialCore {

	// ツールマテリアルの定義
	public static ToolMaterial OFALENT = EnumHelper.addToolMaterial("OFALENT", 4, 1561, 8.0F, 3.0F, 10);
	public static ToolMaterial OFALENG2T = EnumHelper.addToolMaterial("OFALENG2T", 4, 3123, 16.0F, 6.0F, 20);
	public static ToolMaterial OFALENG3T = EnumHelper.addToolMaterial("OFALENG3T", 4, 6247, 32.0F, 12.0F, 40);
	public static ToolMaterial PERFECTT = EnumHelper.addToolMaterial("PERFECTT", 5, 12495, OfalenModConfigCore.efficiencyPerfectTool, 24.0F, 80);

	// アーマーマテリアルの定義
	public static ArmorMaterial OFALENA = EnumHelper.addArmorMaterial("OFALENA", 33, new int[] { 3, 8, 6, 3 }, 10);
	public static ArmorMaterial OFALENG2A = EnumHelper.addArmorMaterial("OFALENG2A", 66, new int[] { 3, 8, 6, 3 }, 20);
	public static ArmorMaterial OFALENG3A = EnumHelper.addArmorMaterial("OFALENG3A", 132, new int[] { 3, 8, 6, 3 }, 40);
	public static ArmorMaterial PERFECTA = EnumHelper.addArmorMaterial("PERFECTA", 264, new int[] { 3, 8, 6, 3 }, 80);

}
