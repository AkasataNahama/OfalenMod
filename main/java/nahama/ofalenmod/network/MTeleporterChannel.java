package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.tileentity.TileEntityTeleportingMarker;
import net.minecraft.tileentity.TileEntity;

public class MTeleporterChannel implements IMessage {
	public short channel;
	public short x, y, z;

	public MTeleporterChannel() {
	}

	public MTeleporterChannel(short channel, int x, int y, int z) {
		this.channel = channel;
		this.x = (short) x;
		this.y = (short) y;
		this.z = (short) z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		channel = buf.readShort();
		x = buf.readShort();
		y = buf.readShort();
		z = buf.readShort();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeShort(channel);
		buf.writeShort(x);
		buf.writeShort(y);
		buf.writeShort(z);
	}

	public static class Handler implements IMessageHandler<MTeleporterChannel, IMessage> {
		@Override
		public IMessage onMessage(MTeleporterChannel message, MessageContext ctx) {
			TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
			if (tileEntity == null || !(tileEntity instanceof TileEntityTeleportingMarker))
				return null;
			((TileEntityTeleportingMarker) tileEntity).setChannel(message.channel);
			return null;
		}
	}
}
