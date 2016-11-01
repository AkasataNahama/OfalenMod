package nahama.ofalenmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public class ItemOfalen extends ItemParts {
	public ItemOfalen() {
		super((byte) 8);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[meta & 7];
	}

	@Override
	public int getMetadata(int meta) {
		return meta & 7;
	}
}
