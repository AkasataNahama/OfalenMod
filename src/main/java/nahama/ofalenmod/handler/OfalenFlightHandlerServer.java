package nahama.ofalenmod.handler;

import nahama.ofalenmod.core.OfalenModConfigCore;
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
			// フローターでないか、NBTを持っていないなら無視。
			if (itemStack == null || !(itemStack.getItem() instanceof ItemFloater) || !itemStack.hasTagCompound())
				continue;
			// 無効か、材料不足なら無視。
			if (!itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID) || ((ItemFloater) itemStack.getItem()).getMaterialAmount(itemStack) < OfalenModConfigCore.amountFloaterDamage)
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

	/** プレイヤーがダメージを受けた時の処理。 */
	public static void onPlayerHurt(EntityPlayer player) {
		/*
		 * 速度は基本的にクライアントで管理しているが、ダメージ時にはサーバーの速度が上書きされる。
		 * クライアントからサーバーへの同期はしていないので、フローターで変更したY軸速度が反映されず、滞空時間に応じた落下速度が代入されてしまう。
		 * これを防ぐため、フローター使用中のプレイヤーがダメージを受けた時にY軸速度を消している。
		 */
		// フローターが有効なら速度を0にする。
		if (canFlightPlayer(player))
			player.motionY = 0;
	}

	private static class FloaterState {
		public byte mode;
		public byte interval;
	}
}
