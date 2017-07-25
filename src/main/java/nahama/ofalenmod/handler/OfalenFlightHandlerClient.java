package nahama.ofalenmod.handler;

import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

public class OfalenFlightHandlerClient {
	/** フローターのモード。 */
	private static byte mode;

	/** フローターのモードを更新する。 */
	public static void setMode(byte newMode) {
		// モードを設定する。
		mode = newMode;
	}

	/** 浮遊が許可されているかどうか。 */
	public static boolean canFloat() {
		return mode > 0;
	}

	/** Entityがプレイヤーかどうか。 */
	public static boolean isPlayer(Entity entity) {
		return Minecraft.getMinecraft().thePlayer == entity;
	}

	/** 空中移動速度の係数を返す。 */
	private static float getFactor(byte mode) {
		switch (mode) {
		case 1:
			return 2;
		case 2:
			return 4;
		case 3:
			return 2;
		case 4:
			return 4;
		case 5:
			return 1;
		}
		return 1;
	}

	/** プレイヤーを浮遊させる。 */
	public static void floatPlayer() {
		// 空中移動係数を変更する。
		Minecraft.getMinecraft().thePlayer.jumpMovementFactor *= getFactor(mode);
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		switch (mode) {
		case 0:
			return;
		case 1:
			jetFlight(player);
			break;
		case 2:
			glideFlight(player);
			break;
		case 3:
			jumpFlight(player);
			break;
		case 4:
			quietFlight(player);
			break;
		case 5:
			horizontalFlight(player);
			break;
		}
		// プレイヤーが空中にいるならパーティクルを表示する。
		if (!player.onGround)
			Minecraft.getMinecraft().theWorld.spawnParticle("reddust", player.posX, player.posY - 1.6D - (OfalenUtil.random.nextDouble() / 2), player.posZ, 0.4D, 0.8D, 1.0D);
	}

	/** ジェットモードで浮遊させる。 */
	private static void jetFlight(EntityPlayerSP player) {
		// 入力情報を更新。
		player.movementInput.updatePlayerMoveState();
		if (player.movementInput.jump) {
			// ジャンプキーに入力があれば上昇。
			player.motionY += 0.2D;
			if (player.motionY > 1.0D)
				player.motionY = 1.0D;
		}
	}

	/** グライドモードで浮遊させる。 */
	private static void glideFlight(EntityPlayerSP player) {
		if (player.motionY < -0.1D) {
			player.motionY = -0.1D;
		}
	}

	/** ジャンプモードで浮遊させる。 */
	private static void jumpFlight(EntityPlayerSP player) {
		// ジャンプキーに入力があったかどうかを保持。
		boolean jump = player.movementInput.jump;
		// 入力情報を更新。
		player.movementInput.updatePlayerMoveState();
		if (!jump && player.movementInput.jump) {
			// ジャンプキーが押された瞬間なら上昇。
			player.motionY = 0.8D;
		}
	}

	/** クアイエットモードで浮遊させる。 */
	private static void quietFlight(EntityPlayerSP player) {
		// 入力情報を更新。
		player.movementInput.updatePlayerMoveState();
		// 上下移動をキャンセル。
		player.motionY = 0.0D;
		if (player.movementInput.sneak) {
			// スニークキーに入力があれば下降。
			player.motionY -= 0.4D;
		}
		if (player.movementInput.jump) {
			// ジャンプキーに入力があれば上昇。
			player.motionY += 0.4D;
		}
	}

	/** ホリゾンタルモードで浮遊させる。 */
	private static void horizontalFlight(EntityPlayerSP player) {
		// 上下移動をキャンセル。
		player.motionY = 0.0D;
	}
}
