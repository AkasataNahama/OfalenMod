package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.util.OfalenLog;

public class MSettingKeyChange implements IMessage {
	public boolean isPressed;

	public MSettingKeyChange() {
	}

	public MSettingKeyChange(boolean isPressed) {
		OfalenLog.debuggingInfo("Constructor called.", "MSettingKeyChange");
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
			OfalenLog.debuggingInfo("onMessage", "MSettingKeyChange");
			OfalenKeyHandler.setSettingKeyPressed(message.isPressed);
			return null;
		}
	}
}
