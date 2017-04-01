package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.handler.OfalenKeyHandler;

public class MSettingKeyChange implements IMessage {
	/** 設定キーが押されているか。 */
	public boolean isPressed;

	public MSettingKeyChange() {
	}

	public MSettingKeyChange(boolean isPressed) {
		this.isPressed = isPressed;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		isPressed = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(isPressed);
	}

	public static class Handler implements IMessageHandler<MSettingKeyChange, IMessage> {
		@Override
		public IMessage onMessage(MSettingKeyChange message, MessageContext ctx) {
			// 設定キーの状態を更新する。
			OfalenKeyHandler.setSettingKeyPressed(message.isPressed);
			return null;
		}
	}
}
