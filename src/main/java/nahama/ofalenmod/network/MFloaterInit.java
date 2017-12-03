package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.handler.OfalenFlightHandlerClient;

public class MFloaterInit implements IMessage {
	public boolean onGround;

	@SuppressWarnings("unused")
	public MFloaterInit() {
	}

	public MFloaterInit(boolean onGround) {
		this.onGround = onGround;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		onGround = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(onGround);
	}

	public static class Handler implements IMessageHandler<MFloaterInit, IMessage> {
		@Override
		public IMessage onMessage(MFloaterInit message, MessageContext ctx) {
			OfalenFlightHandlerClient.init(message.onGround);
			return null;
		}
	}
}
