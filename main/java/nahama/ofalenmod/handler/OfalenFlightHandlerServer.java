package nahama.ofalenmod.handler;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

public class OfalenFlightHandlerServer {

	/** フローターを有効にしているプレイヤーの名前のリスト。 */
	private static ArrayList<String> floatingPlayers = new ArrayList<String>();

	/** 初期化処理。 */
	public static void init() {
		floatingPlayers.clear();
	}

	/** プレイヤーのフローターのモードを登録する。 */
	public static void setPlayerFlightMode(EntityPlayer player, byte mode) {
		if (mode < 1) {
			// モードが1未満（無効）ならリストから削除する。
			floatingPlayers.remove(player.getCommandSenderName());
		} else if (!floatingPlayers.contains(player.getCommandSenderName())) {
			// リストに登録されていないなら登録する。
			floatingPlayers.add(player.getCommandSenderName());
		}
	}

	/** プレイヤーのフローターが有効かどうか。 */
	public static boolean canFlightPlayer(EntityPlayer player) {
		return floatingPlayers.contains(player.getCommandSenderName());
	}

}
