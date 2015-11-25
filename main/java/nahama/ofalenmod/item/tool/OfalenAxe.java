package nahama.ofalenmod.item.tool;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.item.ItemAxe;

public class OfalenAxe extends ItemAxe {

	public OfalenAxe(ToolMaterial material) {
		super(material);
		this.setCreativeTab(OfalenModCore.tabOfalen);
	}

}
