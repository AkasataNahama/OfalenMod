package nahama.ofalenmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class ItemPerfectBall extends ItemOfalenBall {
	public ItemPerfectBall(PotionEffect[] effects) {
		super(effects);
	}

	/** ネザースターと同じエフェクトを持たせる。 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack itemStack, int pass) {
		return true;
	}
}