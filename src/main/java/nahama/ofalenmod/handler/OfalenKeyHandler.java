package nahama.ofalenmod.handler;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.network.MKeyStateChange;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static nahama.ofalenmod.handler.OfalenKeyHandler.MonitoringKey.SETTING;
import static nahama.ofalenmod.handler.OfalenKeyHandler.MonitoringKey.SPRINT;

public class OfalenKeyHandler {
	/** 設定キーのキーバインディング。 */
	private static KeyBinding keySetting;
	/** プレイヤーと押しているキーのマップ。 */
	private static HashMap<String, Set<MonitoringKey>> map = new HashMap<String, Set<MonitoringKey>>();

	/** 初期化する。 */
	@SideOnly(Side.CLIENT)
	public static void init() {
		// キーバインディングを登録する。
		keySetting = new KeyBinding("key.ofalen.description.keySetting", Keyboard.KEY_F, "key.ofalen.category");
		ClientRegistry.registerKeyBinding(OfalenKeyHandler.keySetting);
	}

	public static void onServerStarting() {
		map.clear();
	}

	/** キーの状態を更新する。 */
	@SideOnly(Side.CLIENT)
	public static void update() {
		String name = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
		Set<MonitoringKey> set = map.get(name);
		if (set == null) {
			set = new HashSet<MonitoringKey>();
			map.put(name, set);
		}
		// 設定キーの状態を更新する。
		updateKey(set, keySetting, SETTING);
		// ダッシュキーの状態を更新する。
		updateKey(set, Minecraft.getMinecraft().gameSettings.keyBindSprint, SPRINT);
	}

	@SideOnly(Side.CLIENT)
	private static void updateKey(Set<MonitoringKey> set, KeyBinding keyBinding, MonitoringKey monitoringKey) {
		boolean lastIsPressed = set.contains(monitoringKey);
		boolean isPressed = OfalenUtil.isKeyPressed(keyBinding);
		// 状態が変わったらメッセージを送信。
		if (lastIsPressed != isPressed) {
			updateToSet(set, monitoringKey.ordinal(), isPressed);
			OfalenModPacketCore.WRAPPER.sendToServer(new MKeyStateChange(monitoringKey.ordinal(), isPressed));
		}
	}

	/** 設定キーの状態を設定する。 */
	@SideOnly(Side.SERVER)
	public static void setIsKeyPressed(String name, int numKey, boolean isPressed) {
		// サーバー側のメソッド。
		Set<MonitoringKey> set = map.get(name);
		if (set == null) {
			set = new HashSet<MonitoringKey>();
			map.put(name, set);
		}
		updateToSet(set, numKey, isPressed);
	}

	private static void updateToSet(Set<MonitoringKey> set, int num, boolean isPressed) {
		if (isPressed) {
			set.add(MonitoringKey.values()[num]);
		} else {
			set.remove(MonitoringKey.values()[num]);
		}
	}

	/** 設定キーが押されているか。 */
	public static boolean isSettingKeyPressed(EntityPlayer player) {
		return isKeyPressed(player, SETTING);
	}

	/** ダッシュキーが押されているか。 */
	public static boolean isSprintKeyPressed(EntityPlayer player) {
		return isKeyPressed(player, SPRINT);
	}

	private static boolean isKeyPressed(EntityPlayer player, MonitoringKey key) {
		Set<MonitoringKey> set = map.get(player.getCommandSenderName());
		// set == nullはキーの更新がない時。
		return set != null && set.contains(key);
	}

	public enum MonitoringKey {
		SETTING, SPRINT
	}
}
