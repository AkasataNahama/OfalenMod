package nahama.ofalenmod.handler;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.inventory.ContainerItemShield;
import nahama.ofalenmod.item.ItemShield;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;

public class OfalenShieldHandler {
	/** シールドが有効になっているプレイヤーの名前のリスト。 */
	private static ArrayList<String> playersProtected = new ArrayList<>();

	/** 初期化処理。 */
	public static void init() {
		// リストをリセットする。
		playersProtected.clear();
	}

	/** プレイヤーがシールドを有効にしているか確認する。 */
	public static void checkPlayer(EntityPlayer player) {
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null || !(itemStack.getItem() instanceof ItemShield) || !itemStack.hasTagCompound())
				continue;
			if (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID))
				protectPlayer(player);
		}
	}

	/** プレイヤーをリストに登録する。 */
	public static void protectPlayer(EntityPlayer player) {
		playersProtected.add(player.getCommandSenderName());
	}

	/** プレイヤーをリストから削除する。 */
	public static void unprotectPlayer(EntityPlayer player) {
		playersProtected.remove(player.getCommandSenderName());
	}

	/** プレイヤーがリストに登録されているかどうか。 */
	public static boolean isProtecting(EntityPlayer player) {
		return playersProtected.contains(player.getCommandSenderName());
	}

	/** ダメージを無効にした時の処理。 */
	public static void onProtect(EntityPlayer player) {
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
				itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, false);
				continue;
			}
			itemStack.setItemDamage(itemStack.getItemDamage() + OfalenModConfigCore.amountShieldDamage);
			flag = true;
			if (itemStack.getItemDamage() + OfalenModConfigCore.amountShieldDamage > itemStack.getMaxDamage()) {
				// ダメージが最大になったら、無効にする。
				itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, false);
			}
			break;
		}
		if (!flag) {
			// 有効になっているシールドがなければプレイヤーの保護をやめる。
			unprotectPlayer(player);
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.ofalen.shield.brokenShield")));
		}
		// パーティクルを表示させるようパケットを送る。。
		OfalenModCore.WRAPPER.sendToAll(new MSpawnParticle(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, (byte) 0));
	}
}
