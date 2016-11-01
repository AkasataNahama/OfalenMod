package nahama.ofalenmod.item.tool;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.item.ItemAxe;

public class ItemOfalenAxe extends ItemAxe {
	public ItemOfalenAxe(ToolMaterial material) {
		super(material);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
	}
}
