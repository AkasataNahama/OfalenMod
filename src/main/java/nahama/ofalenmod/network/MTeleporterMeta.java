package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.handler.OfalenTeleportHandler;
import nahama.ofalenmod.item.ItemTeleporter;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.item.ItemStack;

public class MTeleporterMeta implements IMessage {
	public short channel;

	public MTeleporterMeta() {
	}

	public MTeleporterMeta(short channel) {
		this.channel = channel;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		channel = buf.readShort();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeShort(channel);
	}

	public static class Handler implements IMessageHandler<MTeleporterMeta, IMessage> {
		@Override
		public IMessage onMessage(MTeleporterMeta message, MessageContext ctx) {
			ItemStack teleporter = ctx.getServerHandler().playerEntity.getHeldItem();
			if (teleporter == null || !(teleporter.getItem() instanceof ItemTeleporter))
				return null;
			teleporter.setItemDamage(message.channel);
			boolean isValid = OfalenTeleportHandler.isChannelValid(message.channel);
			teleporter.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, isValid);
			return null;
		}
	}
}
