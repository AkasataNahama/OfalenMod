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

public class OfalenArmor extends ItemArmor {

	private int grade = 0;

	public OfalenArmor(ArmorMaterial material, int type, int grade) {
		super(material, 0, type);
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.grade = grade;
	}

	/** モデルのテクスチャを指定する */
	@Override
	public String getArmorTexture(ItemStack itemStack, Entity entity, int slot, String type) {
		int i = 1;
		if (this.armorType == 2)
			i = 2;
		if (this.grade == 4)
			return "ofalenmod:textures/models/armor/ofalen_P_layer_" + i + ".png";
		return "ofalenmod:textures/models/armor/ofalen_G" + grade + "_layer_" + i + ".png";
	}

	/** アップデート時の処理 */
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		// ヘルメット
		if (player.getCurrentArmor(3) != null) {
			if (player.getCurrentArmor(3).getItem() == OfalenModItemCore.helmetPerfectOfalen) {
				player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 10, 0));
			}
		}

		// チェストプレート
		if (player.getCurrentArmor(2) != null) {
			if (player.getCurrentArmor(2).getItem() == OfalenModItemCore.chestplatePerfectOfalen && !player.isPotionActive(Potion.regeneration)) {
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 20, 2));
			}
		}

		// レギンス
		if (player.getCurrentArmor(1) != null) {
			if (player.getCurrentArmor(1).getItem() == OfalenModItemCore.leggingsPerfectOfalen) {
				player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, 2));
			}
		}

		// ブーツ
		if (player.getCurrentArmor(0) != null) {
			if (player.getCurrentArmor(0).getItem() == OfalenModItemCore.bootsPerfectOfalen) {
				player.addPotionEffect(new PotionEffect(Potion.jump.id, 10, 2));
			}
		}
	}

}
