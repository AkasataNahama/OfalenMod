package nahama.ofalenmod.handler;

import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.util.FloaterMode;
import nahama.ofalenmod.util.OfalenParticleUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

public class OfalenFlightHandlerClient {
	/** フローターのモード。 */
	private static FloaterMode mode;
	/** 二連続ジャンプ判定の残り時間。 */
	private static byte timeRemaining;
	/** フローターの機能で飛行しているか。 */
	private static boolean isFlightEnabled;

	/** 初期化処理。 */
	public static void init() {
		isFlightEnabled = true;
	}

	/** フローターのモードを設定する。ワールド起動直後なら有効判定を上書きする。 */
	public static void setMode(FloaterMode newMode, boolean onGround) {
		// モードを設定する。
		mode = newMode;
		if (Minecraft.getMinecraft().thePlayer.onGround != onGround)
			isFlightEnabled = !onGround;
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
		if (player.ridingEntity != null)
			return;
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
		if (isFlightEnabled) {
			// 空中移動係数を変更する。
			player.jumpMovementFactor *= mode.getFactor();
			// プレイヤーが空中にいるならパーティクルを表示する。
			if (!player.onGround) {
				double[] color = OfalenParticleUtil.getColorWithTypeForParticle(2);
				Minecraft.getMinecraft().theWorld.spawnParticle("reddust", player.posX, player.posY - 1.62 - (OfalenUtil.random.nextDouble() / 2), player.posZ, color[0], color[1], color[2]);
			}
		}
	}

	/** ジェットモードで浮遊させる。 */
	private static void jetFlight(EntityPlayerSP player, FloaterMode mode) {
		isFlightEnabled = true;
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
		isFlightEnabled = true;
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
		// ジャンプキーに入力があったかどうかを保持。
		boolean jump = player.movementInput.jump;
		// 入力情報を更新。
		player.movementInput.updatePlayerMoveState();
		if (OfalenModConfigCore.canSwitchFloatForm) {
			if (!jump && player.movementInput.jump) {
				if (timeRemaining > 0) {
					isFlightEnabled = !isFlightEnabled;
					timeRemaining = 0;
				} else {
					timeRemaining = 7;
				}
			}
			if (timeRemaining > 0)
				timeRemaining--;
			if (player.onGround && isFlightEnabled)
				isFlightEnabled = false;
		} else {
			isFlightEnabled = true;
		}
		if (isFlightEnabled) {
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
}
