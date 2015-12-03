package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.handler.OfalenFlightHandlerServer;
import nahama.ofalenmod.item.ItemFloater;
import net.minecraft.item.ItemStack;

public class MFloaterMode implements IMessage {

	public byte mode;
	public boolean isItem;

	public MFloaterMode() {}

	public MFloaterMode(byte mode, boolean isItem) {
		this.mode = mode;
		this.isItem = isItem;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		mode = buf.readByte();
		isItem = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(mode);
		buf.writeBoolean(isItem);
	}

	public static class Handler implements IMessageHandler<MFloaterMode, IMessage> {

		@Override
		public IMessage onMessage(MFloaterMode message, MessageContext ctx) {
			if (message.isItem) {
				ItemStack floater = ctx.getServerHandler().playerEntity.getHeldItem();
				if (floater == null || !(floater.getItem() instanceof ItemFloater) || !floater.hasTagCompound())
					return null;
				floater.getTagCompound().setByte("Mode", message.mode);
			} else {
				OfalenFlightHandlerServer.setPlayerFlightMode(ctx.getServerHandler().playerEntity, message.mode);
			}
			return null;
		}

	}

}
