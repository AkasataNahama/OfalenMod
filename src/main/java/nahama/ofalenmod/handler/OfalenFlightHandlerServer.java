package nahama.ofalenmod.handler;

import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class OfalenFlightHandlerServer {
	/** フローターを有効にしているプレイヤーの名前のリスト。 */
	private static ArrayList<String> playersFloating = new ArrayList<String>();

	/** 初期化処理。 */
	public static void init() {
		playersFloating.clear();
	}

	/** プレイヤーのフローターのモードを登録する。 */
	public static void setPlayerFlightMode(EntityPlayer player, byte mode) {
		if (mode < 1) {
			// モードが1未満（無効）ならリストから削除する。
			playersFloating.remove(player.getCommandSenderName());
		} else if (!playersFloating.contains(player.getCommandSenderName())) {
			// リストに登録されていないなら登録する。
			playersFloating.add(player.getCommandSenderName());
		}
	}

	/** プレイヤーのフローターが有効かどうか。 */
	public static boolean canFlightPlayer(EntityPlayer player) {
		return playersFloating.contains(player.getCommandSenderName());
	}
}
