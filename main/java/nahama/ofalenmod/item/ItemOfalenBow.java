package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.item.ItemBow;

public class ItemOfalenBow extends ItemBow {

	public ItemOfalenBow() {
		super();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setMaxDamage(1536);
	}

	@Override
	public int getItemEnchantability() {
		return 4;
	}
}
