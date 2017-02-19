package nahama.ofalenmod.item.armor;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemOfalenArmor extends ItemArmor {
	private byte grade = 0;

	public ItemOfalenArmor(ArmorMaterial material, int type, int grade) {
		super(material, 0, type);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.grade = (byte) grade;
	}

	/** モデルのテクスチャを指定する。 */
	@Override
	public String getArmorTexture(ItemStack itemStack, Entity entity, int slot, String type) {
		int i = 1;
		if (this.armorType == 2)
			i = 2;
		if (this.grade == 4)
			return "ofalenmod:textures/models/armor/ofalen_P_layer_" + i + ".png";
		return "ofalenmod:textures/models/armor/ofalen_G" + grade + "_layer_" + i + ".png";
	}

	/** アップデート時の処理。 */
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (world.isRemote)
			return;
		// ヘルメット
		if (player.getCurrentArmor(3) != null) {
			if (player.getCurrentArmor(3).getItem() == OfalenModItemCore.helmetOfalenP) {
				// 水中呼吸、10 tick、Lv. 1。
				player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 10, 0));
			}
		}
		// チェストプレート
		if (player.getCurrentArmor(2) != null) {
			if (player.getCurrentArmor(2).getItem() == OfalenModItemCore.chestplateOfalenP) {
				// addPotionEffectで体力がデフォルト最大値まで減らされるので保存しておく。
				float health = player.getHealth();
				// 体力増強（バニラ未実装）、10 tick、Lv. 5（+20.0F）。
				player.addPotionEffect(new PotionEffect(Potion.field_76434_w.getId(), 10, 4));
				player.setHealth(health);
			}
		}
		// レギンス
		if (player.getCurrentArmor(1) != null) {
			if (player.getCurrentArmor(1).getItem() == OfalenModItemCore.leggingsOfalenP) {
				// 移動速度上昇、10 tick、Lv. 3。
				player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, 2));
			}
		}
		// ブーツ
		if (player.getCurrentArmor(0) != null) {
			if (player.getCurrentArmor(0).getItem() == OfalenModItemCore.bootsOfalenP) {
				// 跳躍力上昇、10 tick、Lv. 3。
				player.addPotionEffect(new PotionEffect(Potion.jump.id, 10, 2));
			}
		}
	}
}
