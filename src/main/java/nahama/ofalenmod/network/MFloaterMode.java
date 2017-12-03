package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.handler.OfalenFlightHandlerClient;
import nahama.ofalenmod.util.FloaterMode;

public class MFloaterMode implements IMessage {
	public FloaterMode mode;

	@SuppressWarnings("unused")
	public MFloaterMode() {
	}

	public MFloaterMode(FloaterMode mode) {
		this.mode = mode;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		byte formFlight = buf.readByte();
		float factor = 0.0F;
		if (formFlight > 0)
			factor = buf.readFloat();
		float[] params = new float[FloaterMode.getRequiredParamAmount(formFlight)];
		for (int i = 0; i < params.length; i++) {
			params[i] = buf.readFloat();
		}
		mode = new FloaterMode(null, formFlight, factor, params);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(mode.getFlightForm());
		if (mode.getFlightForm() > 0)
			buf.writeFloat(mode.getFactor());
		for (int i = 0; i < FloaterMode.getRequiredParamAmount(mode.getFlightForm()); i++) {
			buf.writeFloat(mode.getParam(i));
		}
	}

	public static class Handler implements IMessageHandler<MFloaterMode, IMessage> {
		@Override
		public IMessage onMessage(MFloaterMode message, MessageContext ctx) {
			OfalenFlightHandlerClient.setMode(message.mode);
			return null;
		}
	}
}
