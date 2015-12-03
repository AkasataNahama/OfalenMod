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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class OfalenFlightHandlerClient {

	private static byte flightMode;
	private static EntityPlayerSP player;
	private static int duration;
	private static Random random = new Random();

	public static void init() {
		player = Minecraft.getMinecraft().thePlayer;
		flightMode = -1;
	}

	public static void checkPlayer() {
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null || !(itemStack.getItem() instanceof ItemFloater) || !itemStack.hasTagCompound())
				continue;
			allowPlayerToFloat(itemStack.getTagCompound().getByte("Mode"));
		}
	}

	public static void allowPlayerToFloat(byte mode) {
		if (mode < 0) {
			forbidPlayerToFloat();
			return;
		}
		flightMode = mode;
		OfalenModCore.wrapper.sendToServer(new MFloaterMode(flightMode, false));
	}

	public static void forbidPlayerToFloat() {
		flightMode = 0;
		// 滞空時移動速度を上書き。
		player.jumpMovementFactor = 0.02f;
		OfalenModCore.wrapper.sendToServer(new MFloaterMode(flightMode, false));
	}

	public static boolean canFloat() {
		if (flightMode == -1)
			checkPlayer();
		return flightMode > 0;
	}

	public static void setFlightMode(byte mode, EntityPlayer player) {
		flightMode = mode;
	}

	public static boolean isPlayer(Entity entity) {
		return player.getCommandSenderName() == entity.getCommandSenderName();
	}

	public static void floatPlayer() {
		switch (flightMode) {
		case 0:
			return;
		case 1:
			jetFlight();
			break;
		case 2:
			jumpFlight();
			break;
		case 3:
			quietFlight();
			break;
		case 4:
			horizontalFlight();
			break;
		}
		if (!player.onGround)
			Minecraft.getMinecraft().theWorld.spawnParticle("reddust", player.posX, player.posY - 1.6D - (random.nextDouble() / 2), player.posZ, 0.4D, 0.8D, 1.0D);
	}

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

	private static void horizontalFlight() {
		// 滞空時移動速度を上書き。
		player.jumpMovementFactor = 0.02F;
		// 上下移動をキャンセル。
		player.motionY = 0.0D;
	}

}
