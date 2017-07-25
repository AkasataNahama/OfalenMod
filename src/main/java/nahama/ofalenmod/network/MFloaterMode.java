package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.handler.OfalenFlightHandlerClient;

public class MFloaterMode implements IMessage {
	public byte mode;

	@SuppressWarnings("unused")
	public MFloaterMode() {
	}

	public MFloaterMode(byte mode) {
		this.mode = mode;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		mode = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(mode);
	}

	public static class Handler implements IMessageHandler<MFloaterMode, IMessage> {
		@Override
		public IMessage onMessage(MFloaterMode message, MessageContext ctx) {
			OfalenFlightHandlerClient.setMode(message.mode);
			return null;
		}
	}
}
