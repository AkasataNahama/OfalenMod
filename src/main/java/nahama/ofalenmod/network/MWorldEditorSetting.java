package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.tileentity.TileEntityWorldEditorBase;
import net.minecraft.tileentity.TileEntity;

public class MWorldEditorSetting implements IMessage {
	public int x, y, z;
	public byte idSetting;
	public short value;

	public MWorldEditorSetting() {
	}

	public MWorldEditorSetting(TileEntityWorldEditorBase tileEntity, int idSetting, int value) {
		this.x = tileEntity.xCoord;
		this.y = tileEntity.yCoord;
		this.z = tileEntity.zCoord;
		this.idSetting = (byte) idSetting;
		this.value = (short) value;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeByte(idSetting);
		buf.writeShort(value);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		idSetting = buf.readByte();
		value = buf.readShort();
	}

	public static class Handler implements IMessageHandler<MWorldEditorSetting, IMessage> {
		@Override
		public IMessage onMessage(MWorldEditorSetting message, MessageContext ctx) {
			TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
			if (tileEntity == null || !(tileEntity instanceof TileEntityWorldEditorBase))
				return null;
			((TileEntityWorldEditorBase) tileEntity).setWithID(message.idSetting, message.value);
			return null;
		}
	}
}
