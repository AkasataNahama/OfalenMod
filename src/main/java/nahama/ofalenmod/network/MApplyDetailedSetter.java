package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.tileentity.TileEntityDetailedSetter;
import net.minecraft.tileentity.TileEntity;

public class MApplyDetailedSetter implements IMessage {
	public int x, y, z;

	public MApplyDetailedSetter() {
	}

	public MApplyDetailedSetter(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
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
