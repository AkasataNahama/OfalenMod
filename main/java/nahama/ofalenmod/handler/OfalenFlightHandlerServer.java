package nahama.ofalenmod.handler;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

public class OfalenFlightHandlerServer {

	private static ArrayList<String> floatingPlayers = new ArrayList<String>();

	public static void init() {
		floatingPlayers.clear();
	}

	public static void setPlayerFlightMode(EntityPlayer player, byte mode) {
		if (mode < 1) {
			floatingPlayers.remove(player.getCommandSenderName());
		} else if (!floatingPlayers.contains(player.getCommandSenderName())) {
			floatingPlayers.add(player.getCommandSenderName());
		}
	}

	public static boolean canFlightPlayer(EntityPlayer player) {
		return floatingPlayers.contains(player.getCommandSenderName());
	}

}
