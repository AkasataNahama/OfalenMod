package nahama.ofalenmod.handler;

import cpw.mods.fml.common.network.NetworkRegistry;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.item.ItemProtector;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class OfalenProtectHandler {
	/** ダメージを無効にした時の処理。 */
	public static float onProtect(EntityPlayer player, float amountDamage) {
		if (!OfalenModConfigCore.isProtectingEnabled)
			return amountDamage;
		int amount = (int) (amountDamage * OfalenModConfigCore.amountProtectorDamage);
		float remainingDamage = amountDamage - amount;
		// プレイヤーのインベントリを調査し、有効になっているプロテクターがあれば耐久値を減らす。
		boolean flag = false;
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null || !(itemStack.getItem() instanceof ItemProtector) || !itemStack.hasTagCompound())
				continue;
			if (!itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID))
				continue;
			ItemProtector item = (ItemProtector) itemStack.getItem();
			// 材料が足りなかったら次へ。
			if (item.getMaterialAmount(itemStack) < OfalenModConfigCore.amountProtectorDamage)
				continue;
			flag = true;
			// クリエイティブなら材料を消費しない。
			if (player.capabilities.isCreativeMode)
				break;
			int limit = Math.min(amount, item.getMaterialAmount(itemStack));
			item.consumeMaterial(itemStack, limit);
			amount -= limit;
			if (item.getMaterialAmount(itemStack) < OfalenModConfigCore.amountProtectorDamage) {
				// ダメージが最大になったら、チャットに通知する。
				OfalenUtil.addChatTranslationMessage(player, "info.ofalen.protector.brokenProtector");
			}
			// ダメージをすべて軽減したら終了。
			if (amount < 1)
				break;
		}
		// パーティクルを表示させるようパケットを送る。
		if (flag && OfalenModConfigCore.isProtectorParticleEnabled)
			OfalenModPacketCore.WRAPPER.sendToAllAround(new MSpawnParticle(player.posX, player.posY, player.posZ, (byte) 0), new NetworkRegistry.TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, 32.0));
		return remainingDamage + amount;
	}
}
