package nahama.ofalenmod.item;

import net.minecraft.util.IIcon;

public class ItemOfalen extends ItemParts {
	public ItemOfalen() {
		super((byte) 8);
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons[meta % 8];
	}
}
