package nahama.ofalenmod.handler;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.network.MSettingKeyChange;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class OfalenKeyHandler {
	/** 設定キー。 */
	private static KeyBinding keySetting;
	/** 設定キーが押されているか。 */
	private static boolean isSettingKeyPressed;

	/** 初期化する。 */
	@SideOnly(Side.CLIENT)
	public static void init() {
		// キーバインディングを登録する。
		keySetting = new KeyBinding("key.ofalen.description.keySetting", Keyboard.KEY_F, "key.ofalen.category");
		ClientRegistry.registerKeyBinding(OfalenKeyHandler.keySetting);
	}

	@SideOnly(Side.CLIENT)
	public static void update() {
		boolean lastIsPressed = isSettingKeyPressed;
		isSettingKeyPressed = OfalenUtil.isKeyPressed(keySetting);
		if (lastIsPressed != isSettingKeyPressed)
			OfalenModPacketCore.WRAPPER.sendToServer(new MSettingKeyChange(isSettingKeyPressed));
	}

	public static boolean isSettingKeyPressed() {
		return isSettingKeyPressed;
	}

	public static void setSettingKeyPressed(boolean isSettingKeyPressed) {
		if (!OfalenUtil.isClient())
			OfalenKeyHandler.isSettingKeyPressed = isSettingKeyPressed;
	}
}
