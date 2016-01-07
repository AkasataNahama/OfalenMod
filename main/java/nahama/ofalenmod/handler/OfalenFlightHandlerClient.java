package nahama.ofalenmod.handler;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.item.ItemFloater;
import nahama.ofalenmod.network.MFloaterMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class OfalenFlightHandlerClient {

	/** フローターのモード。 */
	private static byte flightMode;
	private static byte time;
	private static EntityPlayerSP player;
	private static Random random = new Random();

	/** 初期化処理。 */
	public static void init() {
		// プレイヤーのインスタンスを代入する。
		player = Minecraft.getMinecraft().thePlayer;
		flightMode = -1;
	}

	/** プレイヤーがフローターを有効にしているか確認する。 */
	public static void checkPlayer() {
		flightMode = 0;
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null || !(itemStack.getItem() instanceof ItemFloater) || !itemStack.hasTagCompound())
				continue;
			flightMode = itemStack.getTagCompound().getByte("Mode");
		}
		allowPlayerToFloat(flightMode);
		time = 0;
	}

	/** フローターのモードを更新する。 */
	public static void allowPlayerToFloat(byte mode) {
		if (mode < 0) {
			// 無効にされたら、飛行を禁止する。
			forbidPlayerToFloat();
			return;
		}
		// モードを設定し、サーバーに通知する。
		flightMode = mode;
		OfalenModCore.wrapper.sendToServer(new MFloaterMode(flightMode, false));
	}

	/** プレイヤーの浮遊を禁止する。 */
	public static void forbidPlayerToFloat() {
		// モードを0にし、サーバーに通知する。
		flightMode = 0;
		OfalenModCore.wrapper.sendToServer(new MFloaterMode(flightMode, false));
		// 滞空時移動速度をもとに戻す。
		player.jumpMovementFactor = 0.02F;
	}

	/** 浮遊が許可されているかどうか。 */
	public static boolean canFloat() {
		// 初期化直後ならプレイヤーを調査する。
		if (flightMode == -1)
			checkPlayer();
		return flightMode > 0;
	}

	/** Entityがプレイヤーかどうか。 */
	public static boolean isPlayer(Entity entity) {
		return player.getCommandSenderName().equals(entity.getCommandSenderName());
	}

	/** プレイヤーを浮遊させる。 */
	public static void floatPlayer() {
		switch (flightMode) {
		case 0:
			return;
		case 1:
			jetFlight();
			break;
		case 2:
			glideFlight();
			break;
		case 3:
			jumpFlight();
			break;
		case 4:
			quietFlight();
			break;
		case 5:
			horizontalFlight();
			break;
		}
		time++;
		if (time > 20)
			checkPlayer();
		// プレイヤーが空中にいるならパーティクルを表示する。
		if (!player.onGround)
			Minecraft.getMinecraft().theWorld.spawnParticle("reddust", player.posX, player.posY - 1.6D - (random.nextDouble() / 2), player.posZ, 0.4D, 0.8D, 1.0D);
	}

	/** ジェットモードで浮遊させる。 */
	private static void jetFlight() {
		// 滞空時移動速度を上書き。
		player.jumpMovementFactor = 0.04F;
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
	private static void glideFlight() {
		// 滞空時移動速度を上書き。
		player.jumpMovementFactor = 0.08F;
		if (player.motionY < -0.1D) {
			player.motionY = -0.1D;
		}
	}

	/** ジャンプモードで浮遊させる。 */
	private static void jumpFlight() {
		// 滞空時移動速度を上書き。
		player.jumpMovementFactor = 0.04F;
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
	private static void quietFlight() {
		// 滞空時移動速度を上書き。
		player.jumpMovementFactor = 0.08F;
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
	private static void horizontalFlight() {
		// 滞空時移動速度を上書き。
		player.jumpMovementFactor = 0.02F;
		// 上下移動をキャンセル。
		player.motionY = 0.0D;
	}

}
