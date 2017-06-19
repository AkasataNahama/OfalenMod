package nahama.ofalenmod.handler;

import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.inventory.ContainerItemShield;
import nahama.ofalenmod.item.ItemShield;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class OfalenShieldHandler {
	/** ダメージを無効にした時の処理。 */
	public static boolean onProtect(EntityPlayer player) {
		// シールドのGUIを開いていたら、閉じさせる。
		if (player.openContainer != null && player.openContainer instanceof ContainerItemShield) {
			player.closeScreen();
		}
		// プレイヤーのインベントリを調査し、有効になっているシールドがあれば耐久値を減らす。
		boolean flag = false;
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null || !(itemStack.getItem() instanceof ItemShield) || !itemStack.hasTagCompound())
				continue;
			if (!itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID))
				continue;
			if (itemStack.getItemDamage() + OfalenModConfigCore.amountShieldDamage > itemStack.getMaxDamage()) {
				// 材料が足りなかったら無効にする。
				itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, false);
				continue;
			}
			flag = true;
			// クリエイティブなら材料を消費しない。
			if (player.capabilities.isCreativeMode)
				break;
			itemStack.setItemDamage(itemStack.getItemDamage() + OfalenModConfigCore.amountShieldDamage);
			if (itemStack.getItemDamage() + OfalenModConfigCore.amountShieldDamage > itemStack.getMaxDamage()) {
				// ダメージが最大になったら、無効にする。
				itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, false);
				OfalenUtil.addChatTranslationMessage(player, "info.ofalen.shield.brokenShield");
			}
			break;
		}
		if (flag) {
			// パーティクルを表示させるようパケットを送る。
			OfalenModPacketCore.WRAPPER.sendToAll(new MSpawnParticle(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, (byte) 0));
		}
		return flag;
	}
}
