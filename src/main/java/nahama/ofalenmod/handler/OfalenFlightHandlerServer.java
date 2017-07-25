package nahama.ofalenmod.handler;

import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.item.ItemFloater;
import nahama.ofalenmod.network.MFloaterMode;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class OfalenFlightHandlerServer {
	/** フローターを有効にしているプレイヤーの名前のリスト。 */
	private static Map<String, Byte> playersFloating = new HashMap<String, Byte>();

	/** 初期化処理。 */
	public static void init() {
		playersFloating.clear();
	}

	public static void onUpdatePlayer(EntityPlayer player) {
		if (canFlightPlayer(player) && player.worldObj.rand.nextInt(40) == 0)
			checkPlayer(player);
	}

	/** プレイヤーがフローターを有効にしているか確認する。 */
	public static void checkPlayer(EntityPlayer player) {
		byte newMode = 0;
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null || !(itemStack.getItem() instanceof ItemFloater) || !itemStack.hasTagCompound())
				continue;
			newMode = itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE);
			// 有効なフローターを見つけたら調査を終了する。
			if (newMode > 0)
				break;
		}
		setPlayerFlightMode(player, newMode);
	}

	/** プレイヤーのフローターのモードを登録する。 */
	private static void setPlayerFlightMode(EntityPlayer player, byte mode) {
		Byte lastMode = playersFloating.get(player.getCommandSenderName());
		if (lastMode == null)
			lastMode = 0;
		if (lastMode == mode)
			return;
		OfalenModPacketCore.WRAPPER.sendTo(new MFloaterMode(mode), (EntityPlayerMP) player);
		if (mode < 1) {
			// モードが1未満（無効）なら削除する。
			playersFloating.remove(player.getCommandSenderName());
		} else {
			// 有効なら上書きする。
			playersFloating.put(player.getCommandSenderName(), mode);
		}
	}

	/** プレイヤーのフローターが有効かどうか。 */
	public static boolean canFlightPlayer(EntityPlayer player) {
		Byte mode = playersFloating.get(player.getCommandSenderName());
		return mode != null && mode > 0;
	}
}
