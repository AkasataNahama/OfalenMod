package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.handler.OfalenTeleportHandler;
import nahama.ofalenmod.item.ItemTeleporter;
import net.minecraft.item.ItemStack;

public class MTeleporterMeta implements IMessage {

	public int channel;

	public MTeleporterMeta() {}

	public MTeleporterMeta(int channel) {
		this.channel = channel;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		channel = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(channel);
	}

	public static class Handler implements IMessageHandler<MTeleporterMeta, IMessage> {

		@Override
		public IMessage onMessage(MTeleporterMeta message, MessageContext ctx) {
			ItemStack teleporter = ctx.getServerHandler().playerEntity.getHeldItem();
			if (teleporter == null || !(teleporter.getItem() instanceof ItemTeleporter))
				return null;
			teleporter.setItemDamage(message.channel);
			boolean isValid = OfalenTeleportHandler.getInstance(ctx.getServerHandler().playerEntity.worldObj).isChannelValid(message.channel);
			teleporter.getTagCompound().setBoolean("IsValid", isValid);
			return null;
		}

	}

}
