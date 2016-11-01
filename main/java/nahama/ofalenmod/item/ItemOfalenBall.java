package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemOfalenBall extends ItemFood {
	/** 付与するポーション効果の配列。 */
	private final PotionEffect[] effects;

	public ItemOfalenBall(PotionEffect[] effects) {
		super(0, 0.0F, false);
		this.effects = effects;
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
	}

	/** 食べるのにかかる時間を返す。 */
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		// 少し早い。
		return 20;
	}

	/** 右クリックされた時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		// ItemFoodの満腹度の判定を消している。
		player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		return itemStack;
	}

	/** 食べる時の処理。 */
	/*
	 * EntityLivingBase.addPotionEffect(PotionEffect)では、効果時間を上書きしているらしい(?)ので、
	 * 新たにPotionEffectのオブジェクトを生成して引数として渡している。
	。 */
	@Override
	public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
		// クリエイティブモード以外ならスタックサイズを減らす。
		if (!player.capabilities.isCreativeMode) {
			--itemStack.stackSize;
		}
		// つけるポーション効果の数だけ繰り返す。
		if (!world.isRemote) {
			for (PotionEffect effect1 : effects) {
				if (effect1 != null && effect1.getPotionID() > 0) {
					PotionEffect effect;
					if (effect1.getPotionID() == Potion.heal.id) {
						effect = new PotionEffect(Potion.heal.id, 1, effect1.getAmplifier());
					} else {
						effect = new PotionEffect(effect1.getPotionID(), effect1.getDuration(), effect1.getAmplifier());
					}
					player.addPotionEffect(effect);
				}
			}
		}
		world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		return itemStack;
	}
}
