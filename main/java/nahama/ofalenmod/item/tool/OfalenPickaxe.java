package nahama.ofalenmod.item.tool;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.item.ItemPickaxe;

public class OfalenPickaxe extends ItemPickaxe {

	public OfalenPickaxe(ToolMaterial material) {
		super(material);
		this.setCreativeTab(OfalenModCore.tabOfalen);
	}

}
