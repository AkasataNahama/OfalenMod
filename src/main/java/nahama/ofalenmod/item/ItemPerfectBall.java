package nahama.ofalenmod.item;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class ItemPerfectBall extends ItemOfalenBall {
	public ItemPerfectBall(PotionEffect[] effects) {
		super(effects);
	}

	/** ネザースターと同じエフェクトを持たせる。 */
	@Override
	public boolean hasEffect(ItemStack itemStack, int pass) {
		return true;
	}
}
