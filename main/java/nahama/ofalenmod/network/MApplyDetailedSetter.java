package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.tileentity.TileEntityDetailedSetter;
import net.minecraft.tileentity.TileEntity;

public class MApplyDetailedSetter implements IMessage {
	public short x, y, z;

	public MApplyDetailedSetter() {
	}

	public MApplyDetailedSetter(int x, int y, int z) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (short) z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readShort();
		y = buf.readShort();
		z = buf.readShort();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeShort(x);
		buf.writeShort(y);
		buf.writeShort(z);
	}

	public static class Handler implements IMessageHandler<MApplyDetailedSetter, IMessage> {
		@Override
		public IMessage onMessage(MApplyDetailedSetter message, MessageContext ctx) {
			TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
			if (tileEntity != null && tileEntity instanceof TileEntityDetailedSetter)
				((TileEntityDetailedSetter) tileEntity).apply();
			return null;
		}
	}
}
