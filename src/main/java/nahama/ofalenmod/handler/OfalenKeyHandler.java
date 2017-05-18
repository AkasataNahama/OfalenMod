package nahama.ofalenmod.handler;

import cpw.mods.fml.client.registry.ClientRegistry;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.network.MSettingKeyChange;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class OfalenKeyHandler {
	/** 設定キーのキーバインディング。 */
	private static KeyBinding keySetting;
	/** 設定キーが押されているか。 */
	private static boolean isSettingKeyPressed;

	/** 初期化する。 */
	public static void init() {
		// キーバインディングを登録する。
		keySetting = new KeyBinding("key.ofalen.description.keySetting", Keyboard.KEY_F, "key.ofalen.category");
		ClientRegistry.registerKeyBinding(OfalenKeyHandler.keySetting);
	}

	/** キーの状態を更新する。 */
	public static void update() {
		// 設定キーの状態を更新する。
		boolean lastIsPressed = isSettingKeyPressed;
		isSettingKeyPressed = OfalenUtil.isKeyPressed(keySetting);
		// 状態が変わったらメッセージを送信。
		if (lastIsPressed != isSettingKeyPressed)
			OfalenModPacketCore.WRAPPER.sendToServer(new MSettingKeyChange(isSettingKeyPressed));
	}

	/** 設定キーが押されているか。 */
	public static boolean isSettingKeyPressed() {
		return isSettingKeyPressed;
	}

	/** 設定キーの状態を設定する。 */
	public static void setSettingKeyPressed(boolean isSettingKeyPressed) {
		// サーバー側でのみ、反映する。
		if (!OfalenUtil.isClient())
			OfalenKeyHandler.isSettingKeyPressed = isSettingKeyPressed;
	}
}
