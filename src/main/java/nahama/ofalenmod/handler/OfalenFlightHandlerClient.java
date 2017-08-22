package nahama.ofalenmod.handler;

import nahama.ofalenmod.util.FloaterMode;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

public class OfalenFlightHandlerClient {
	/** フローターのモード。 */
	private static FloaterMode mode;

	/** フローターのモードを設定する。 */
	public static void setMode(FloaterMode newMode) {
		// モードを設定する。
		mode = newMode;
	}

	/** 浮遊が許可されているかどうか。 */
	public static boolean canFloat() {
		return mode != null && mode.getFlightForm() > 0;
	}

	/** Entityがプレイヤーかどうか。 */
	public static boolean isPlayer(Entity entity) {
		return Minecraft.getMinecraft().thePlayer == entity;
	}

	/** プレイヤーを浮遊させる。 */
	public static void floatPlayer() {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		// モードごとに更新時の移動処理を行う。
		switch (mode.getFlightForm()) {
		default:
			return;
		case 1:
			jetFlight(player, mode);
			break;
		case 2:
			jumpFlight(player, mode);
			break;
		case 3:
			floatFlight(player, mode);
			break;
		}
		// 空中移動係数を変更する。
		player.jumpMovementFactor *= mode.getFactor();
		// プレイヤーが空中にいるならパーティクルを表示する。
		if (!player.onGround)
			Minecraft.getMinecraft().theWorld.spawnParticle("reddust", player.posX, player.posY - 1.6D - (OfalenUtil.random.nextDouble() / 2), player.posZ, 0.4D, 0.8D, 1.0D);
	}

	/** ジェットモードで浮遊させる。 */
	private static void jetFlight(EntityPlayerSP player, FloaterMode mode) {
		// 入力情報を更新。
		player.movementInput.updatePlayerMoveState();
		// ジャンプキーに入力があれば上昇。
		if (player.movementInput.jump)
			player.motionY += mode.getParam(0);
		// 上昇速度制限があれば適用する。
		float limit = mode.getParam(1);
		if (limit > 0 && player.motionY > limit)
			player.motionY = limit;
		// 下降速度制限があれば適用する。
		limit = mode.getParam(2);
		if (limit > 0 && player.motionY < -limit)
			player.motionY = -limit;
	}

	/** ジャンプモードで浮遊させる。 */
	private static void jumpFlight(EntityPlayerSP player, FloaterMode mode) {
		// ジャンプキーに入力があったかどうかを保持。
		boolean jump = player.movementInput.jump;
		// 入力情報を更新。
		player.movementInput.updatePlayerMoveState();
		if (!jump && player.movementInput.jump) {
			// ジャンプキーが押された瞬間なら上昇。
			player.jump();
			player.motionY *= mode.getParam(0);
		}
	}

	/** フロートモードで浮遊させる。 */
	private static void floatFlight(EntityPlayerSP player, FloaterMode mode) {
		// 入力情報を更新。
		player.movementInput.updatePlayerMoveState();
		// 上下移動をキャンセル。
		player.motionY = 0.0D;
		// スニークキーに入力があれば下降。
		if (player.movementInput.sneak)
			player.motionY -= mode.getParam(0);
		// ジャンプキーに入力があれば上昇。
		if (player.movementInput.jump)
			player.motionY += mode.getParam(1);
	}
}
