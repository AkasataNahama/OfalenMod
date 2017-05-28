package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.util.BlockRange;
import nahama.ofalenmod.util.OfalenParticleUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class MSpawnParticleWithRange implements IMessage {
	/** 0 : 橙, 1 : 翠, 2 : 紫, 3 : 黒。 */
	public byte type;
	public byte dimensionId;
	public BlockRange range;

	public MSpawnParticleWithRange() {
	}

	public MSpawnParticleWithRange(byte type, byte dimensionId, BlockRange range) {
		this.type = type;
		this.dimensionId = dimensionId;
		this.range = range;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(type);
		buf.writeByte(dimensionId);
		buf.writeShort(range.posMin.x);
		buf.writeShort(range.posMin.y);
		buf.writeShort(range.posMin.z);
		buf.writeShort(range.posMax.x);
		buf.writeShort(range.posMax.y);
		buf.writeShort(range.posMax.z);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = buf.readByte();
		dimensionId = buf.readByte();
		range = new BlockRange(buf.readShort(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readShort());
	}

	public static class Handler implements IMessageHandler<MSpawnParticleWithRange, IMessage> {
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MSpawnParticleWithRange message, MessageContext ctx) {
			World world = Minecraft.getMinecraft().theWorld;
			if (world.provider.dimensionId != message.dimensionId)
				return null;
			// パーティクルを発生させる。
			OfalenParticleUtil.spawnParticleWithBlockRange(world, message.range, message.type);
			return null;
		}
	}
}
