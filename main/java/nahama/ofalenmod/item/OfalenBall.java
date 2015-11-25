package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class OfalenBall extends ItemFood {

	/**付与するポーション効果の配列*/
	private final PotionEffect[] effects;

	/**新規コンストラクター。ポーション効果の配列を受け取る。*/
	public OfalenBall(PotionEffect[] effects) {
		super(0, 0.0F, false);
		this.effects = effects;
		this.setCreativeTab(OfalenModCore.tabOfalen);
	}

	/**食べるのにかかる時間を返す。(通常よりも少し早い)
	 * @return 20*/
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 20;
	}

	/**右クリックされた時の処理。ItemFoodの満腹度の判定を消している。*/
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		return itemStack;
	}

	/**食べる時の処理。クリエイティブモードではアイテム数が減らないようにしている。また、ポーション効果も付与する。*/
	/* EntityLivingBase.addPotionEffect(PotionEffect)では、効果時間を上書きしているらしい(?)ので、
	 * 新たにPotionEffectのオブジェクトを生成して引数として渡している。*/
	@Override
	public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
		//クリエイティブモード以外ならスタックサイズを減らす。
		if (!player.capabilities.isCreativeMode) {
			--itemStack.stackSize;
		}
		//つけるポーション効果の数だけ繰り返す。
		for (int i = 0; i < effects.length; i ++) {
			//サーバー側の処理で、情報が正常なら処理を続ける。
			if (!world.isRemote && effects[i] != null && effects[i].getPotionID() > 0) {
				//即時回復なら、
				if (effects[i].getPotionID() == Potion.heal.id) {
					//ID・効果時間を固定して、
					PotionEffect effect = new PotionEffect(Potion.heal.id, 1, effects[i].getAmplifier());
					//プレイヤーに付与。
					player.addPotionEffect(effect);
				//即時回復以外なら、
				} else {
					//効果時間のみ固定して、
					PotionEffect effect = new PotionEffect(effects[i].getPotionID(), effects[i].getDuration(), effects[i].getAmplifier());
					//プレイヤーに付与。
					player.addPotionEffect(effect);
				}
			}
		}
		world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		return itemStack;
	}

}
