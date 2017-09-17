package nahama.ofalenmod.creativetab;

import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabOfalen extends CreativeTabs {
	private byte count;
	private byte meta = -1;
	private short lastSecond;

	public CreativeTabOfalen(String label) {
		super(label);
	}

	/** クリエイティブタブのアイコンを返す。 */
	@Override
	public ItemStack getIconItemStack() {
		short current = (short) (System.currentTimeMillis() / 1000 % 10000);
		if (current != lastSecond) {
			if (count > 0)
				count--;
			lastSecond = current;
		}
		if (count < 1) {
			count = 2;
			meta++;
			if (meta > 8)
				meta = 0;
		}
		return new ItemStack(OfalenModItemCore.gemOfalen, 1, meta);
	}

	@Override
	public Item getTabIconItem() {
		return null;
	}
}
