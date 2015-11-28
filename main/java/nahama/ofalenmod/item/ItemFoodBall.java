package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemFoodBall extends ItemFood {

	public ItemFoodBall() {
		super(10, 10F, false);
		this.setCreativeTab(OfalenModCore.tabOfalen);
	}

	/** 食べるのにかかる時間を返す。 */
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		// 少し早い。
		return 20;
	}

}
