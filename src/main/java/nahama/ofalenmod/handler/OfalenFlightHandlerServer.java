package nahama.ofalenmod.handler;

import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.item.ItemFloater;
import nahama.ofalenmod.network.MFloaterMode;
import nahama.ofalenmod.setting.IItemOfalenSettable;
import nahama.ofalenmod.setting.OfalenSettingCategory;
import nahama.ofalenmod.setting.OfalenSettingFloat;
import nahama.ofalenmod.setting.OfalenSettingString;
import nahama.ofalenmod.util.FloaterMode;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class OfalenFlightHandlerServer {
	/** フローターを有効にしているプレイヤーの名前と状態のマップ。 */
	private static Map<String, FloaterState> playersFloating = new HashMap<String, FloaterState>();

	/** 初期化処理。 */
	public static void init() {
		playersFloating.clear();
	}

	/** プレイヤーが更新された時の処理。 */
	public static void onUpdatePlayer(EntityPlayer player) {
		// フローターを有効にしているプレイヤーなら、一定間隔でインベントリを確認する。
		FloaterState state = playersFloating.get(player.getCommandSenderName());
		if (state == null)
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
		byte modeSelected = 0;
		ItemStack stackFloater = null;
		// プレイヤーのインベントリを調査する。
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			// フローターでないか、NBTを持っていないなら無視。
			if (itemStack == null || !(itemStack.getItem() instanceof ItemFloater) || !itemStack.hasTagCompound())
				continue;
			// フローターが無効なら無視。
			if (!((ItemFloater) itemStack.getItem()).isValidFloater(itemStack))
				continue;
			// 有効なフローターを見つけたら調査を終了する。
			modeSelected = itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE);
			stackFloater = itemStack;
			break;
		}
		// モード変更を適用する。
		setPlayerFloaterMode(player, modeSelected, stackFloater);
	}

	/** プレイヤーのフローターのモードを登録する。 */
	private static void setPlayerFloaterMode(EntityPlayer player, byte modeSelected, ItemStack itemStack) {
		// 変更前の状態を取得する。
		FloaterState state = playersFloating.get(player.getCommandSenderName());
		if (state == null)
			state = new FloaterState();
		// 変更後の状態を取得する。
		FloaterMode modeFloater = loadFloaterModeFromSetting(modeSelected, itemStack);
		// 変更がないなら終了。
		if (state.mode != null && state.mode.equals(modeFloater))
			return;
		// モード変更をClientHandlerに通知する。
		OfalenModPacketCore.WRAPPER.sendTo(new MFloaterMode(modeFloater, player.onGround), (EntityPlayerMP) player);
		if (modeSelected < 1 || modeFloater.getFlightForm() < 1) {
			// モードが1未満（無効）なら削除する。
			if (playersFloating.containsKey(player.getCommandSenderName())) {
				// チャットに通知する。
				OfalenUtil.addChatTranslationMessage(player, "info.ofalen.floater.modeChanged.handler.0");
				playersFloating.remove(player.getCommandSenderName());
			}
		} else {
			// チャットに通知する。
			OfalenUtil.addChatTranslationMessage(player, "info.ofalen.floater.modeChanged.handler", modeFloater.getName());
			// 有効なら上書きする。
			state.mode = modeFloater;
			playersFloating.put(player.getCommandSenderName(), state);
		}
	}

	/** フローターの選択中モードと詳細設定からFloaterModeのインスタンスを生成し返す。 */
	private static FloaterMode loadFloaterModeFromSetting(byte modeSelected, ItemStack floater) {
		// 無効ならデフォルトを返す。
		if (floater == null || modeSelected == 0)
			return new FloaterMode(null, (byte) 0, 0.0F);
		// 詳細設定の選択中モードのカテゴリを取得する。
		OfalenSettingCategory category = ((IItemOfalenSettable) floater.getItem()).getSetting().getChildCategory("Mode-" + modeSelected);
		// モード名。
		String name = ((OfalenSettingString) category.getChildSetting("Name")).getValueByStack(floater);
		// 飛行形態。
		byte modeFlight = ((ItemFloater.OfalenSettingFlightForm) category.getChildSetting("FlightForm")).getValueByStack(floater);
		// 移動速度などのパラメータ。
		float factor = ((OfalenSettingFloat) category.getChildSetting("Parameter-HorizontalSpeed")).getValueByStack(floater);
		float param1 = ((OfalenSettingFloat) category.getChildSetting("Parameter-1")).getValueByStack(floater);
		float param2 = ((OfalenSettingFloat) category.getChildSetting("Parameter-2")).getValueByStack(floater);
		float param3 = ((OfalenSettingFloat) category.getChildSetting("Parameter-3")).getValueByStack(floater);
		return new FloaterMode(name, modeFlight, factor, param1, param2, param3);
	}

	/** プレイヤーのフローターが有効かどうか。 */
	public static boolean canFlightPlayer(EntityPlayer player) {
		// マップに登録されていたらtrue。
		return playersFloating.get(player.getCommandSenderName()) != null;
	}

	/** プレイヤーがダメージを受けた時の処理。 */
	public static void onPlayerHurt(EntityPlayer player) {
		/*
		 * 速度は基本的にクライアントで管理しているが、ダメージ時にはサーバーの速度で上書きされる。
		 * クライアントからサーバーへの同期はしていないので、フローターで変更したY軸速度が反映されず、滞空時間に応じた落下速度が代入されてしまう。
		 * これを防ぐため、フローター使用中のプレイヤーがダメージを受けた時にY軸速度を消している。
		 */
		// フローターが有効なら速度を0にする。
		if (canFlightPlayer(player))
			player.motionY = 0;
	}

	/** フローター使用中のプレイヤーの状態。 */
	private static class FloaterState {
		public FloaterMode mode;
		public byte interval;
	}
}
