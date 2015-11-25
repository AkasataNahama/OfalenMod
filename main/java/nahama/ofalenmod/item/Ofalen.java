package nahama.ofalenmod.item;

import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Ofalen extends Parts {

	public Ofalen () {
		super (4);
	}

	/**メタデータにより返すIIconを変える。*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta & 3];
	}

	/**メタデータを返す。*/
	@Override
	public int getMetadata(int meta) {
		return meta & 3;
	}

}
