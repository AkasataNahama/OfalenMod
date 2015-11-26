package nahama.ofalenmod.creativetab;

import java.util.Calendar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OfalenTab extends CreativeTabs {

	private int count;
	private int meta = -1;
	private int lastSecond;

	public OfalenTab(String label) {
		super(label);
	}

	/** クリエイティブタブのアイコンを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		Calendar cal = Calendar.getInstance();
		int second = cal.get(Calendar.SECOND);
		if (count < 1) {
			count = 2;
			meta++;
			if (meta > 3)
				meta = 0;
		} else if (second != lastSecond) {
			count--;
			lastSecond = second;
		}
		return new ItemStack(OfalenModItemCore.ofalen, 1, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return null;
	}

}
