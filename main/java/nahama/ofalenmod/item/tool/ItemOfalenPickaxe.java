package nahama.ofalenmod.item.tool;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.item.ItemPickaxe;

public class ItemOfalenPickaxe extends ItemPickaxe {
	public ItemOfalenPickaxe(ToolMaterial material) {
		super(material);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
	}
}
