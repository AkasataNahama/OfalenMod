package nahama.ofalenmod.creativetab;

import java.util.Random;

import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OfalenTab extends CreativeTabs {

	private Random random = new Random();
	private int count = 0;
	private int meta = -1;

	public OfalenTab(String label) {
		super(label);
	}

	/** アイコンの設定 */
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		if (count < 1) {
			count = 100;
			meta++;
			if (meta > 3)
				meta = 0;
		} else {
			count--;
		}
		return new ItemStack(OfalenModItemCore.ofalen, 1, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return null;
	}

}
