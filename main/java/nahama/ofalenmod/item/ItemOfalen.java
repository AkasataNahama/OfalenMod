package nahama.ofalenmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public class ItemOfalen extends ItemParts {

	public ItemOfalen() {
		super(8);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta & 7];
	}

	@Override
	public int getMetadata(int meta) {
		return meta & 7;
	}

}
