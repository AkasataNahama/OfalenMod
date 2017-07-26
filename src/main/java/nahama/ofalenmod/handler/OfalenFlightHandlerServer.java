package nahama.ofalenmod.handler;

import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.item.ItemFloater;
import nahama.ofalenmod.network.MFloaterMode;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class OfalenFlightHandlerServer {
	/** フローターを有効にしているプレイヤーの名前のリスト。 */
	private static Map<String, FloaterState> playersFloating = new HashMap<String, FloaterState>();

	/** 初期化処理。 */
	public static void init() {
		playersFloating.clear();
	}

	public static void onUpdatePlayer(EntityPlayer player) {
		FloaterState state = playersFloating.get(player.getCommandSenderName());
		if (state == null || state.mode == 0)
			return;
		if (state.interval > 0)
			state.interval--;
		if (state.interval < 1) {
			state.interval = 20;
			checkPlayer(player);
		}
	}

	/** プレイヤーがフローターを有効にしているか確認する。 */
	public static void checkPlayer(EntityPlayer player) {
		byte newMode = 0;
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null || !(itemStack.getItem() instanceof ItemFloater) || !itemStack.hasTagCompound() || !itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID))
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
		FloaterState state = playersFloating.get(player.getCommandSenderName());
		if (state == null)
			state = new FloaterState();
		if (state.mode == mode)
			return;
		// モード変更をClientHandlerとチャットに通知する。
		OfalenModPacketCore.WRAPPER.sendTo(new MFloaterMode(mode), (EntityPlayerMP) player);
		OfalenUtil.addChatTranslationMessage(player, "info.ofalen.floater.modeChanged.handler", mode);
		if (mode < 1) {
			// モードが1未満（無効）なら削除する。
			playersFloating.remove(player.getCommandSenderName());
		} else {
			// 有効なら上書きする。
			state.mode = mode;
			playersFloating.put(player.getCommandSenderName(), state);
		}
	}

	/** プレイヤーのフローターが有効かどうか。 */
	public static boolean canFlightPlayer(EntityPlayer player) {
		FloaterState state = playersFloating.get(player.getCommandSenderName());
		return state != null && state.mode > 0;
	}

	private static class FloaterState {
		public byte mode;
		public byte interval;
	}
}
