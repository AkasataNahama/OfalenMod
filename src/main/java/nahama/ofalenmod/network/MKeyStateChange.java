package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.util.OfalenUtil;

public class MKeyStateChange implements IMessage {
	/** キー番号。 */
	public byte ordinal;
	/** 設定キーが押されているか。 */
	public boolean isPressed;

	public MKeyStateChange() {
	}

	public MKeyStateChange(int ordinal, boolean isPressed) {
		this.ordinal = (byte) ordinal;
		this.isPressed = isPressed;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		ordinal = buf.readByte();
		isPressed = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(ordinal);
		buf.writeBoolean(isPressed);
	}

	public static class Handler implements IMessageHandler<MKeyStateChange, IMessage> {
		@Override
		public IMessage onMessage(MKeyStateChange message, MessageContext ctx) {
			// 設定キーの状態を更新する。
			if (!OfalenUtil.isClient())
				OfalenKeyHandler.setIsKeyPressed(ctx.getServerHandler().playerEntity.getCommandSenderName(), message.ordinal, message.isPressed);
			return null;
		}
	}
}
