package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.tileentity.TileEntityWorldEditorBase;
import net.minecraft.tileentity.TileEntity;

public class MWorldEditorSetting implements IMessage {
	public short x, y, z;
	public byte idSetting;
	public short value;

	public MWorldEditorSetting() {
	}

	public MWorldEditorSetting(TileEntityWorldEditorBase tileEntity, int idSetting, int value) {
		this.x = (short) tileEntity.xCoord;
		this.y = (short) tileEntity.yCoord;
		this.z = (short) tileEntity.zCoord;
		this.idSetting = (byte) idSetting;
		this.value = (short) value;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeShort(x);
		buf.writeShort(y);
		buf.writeShort(z);
		buf.writeByte(idSetting);
		buf.writeShort(value);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readShort();
		y = buf.readShort();
		z = buf.readShort();
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
