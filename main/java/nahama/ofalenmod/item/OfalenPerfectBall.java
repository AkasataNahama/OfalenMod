package nahama.ofalenmod.item;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OfalenPerfectBall extends OfalenBall {

	public OfalenPerfectBall(PotionEffect[] effects) {
		super(effects);
	}

	/**ネザースターと同じエフェクトを持たせる*/
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack itemStack, int pass) {
		return true;
	}

}
