package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import static nahama.ofalenmod.util.OfalenUtil.random;

public class MSpawnParticle implements IMessage {
	public byte dimensionId;
	public double x, y, z;
	public byte type;

	public MSpawnParticle() {
	}

	public MSpawnParticle(byte dimensionId, double x, double y, double z, byte type) {
		this.dimensionId = dimensionId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}

	/** dimensionIdはbyteにキャストされます。 */
	public MSpawnParticle(int dimensionId, double x, double y, double z, byte type) {
		this((byte) dimensionId, x, y, z, type);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimensionId = buf.readByte();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		type = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(dimensionId);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeByte(type);
	}

	public static class Handler implements IMessageHandler<MSpawnParticle, IMessage> {
		@Override
		public IMessage onMessage(MSpawnParticle message, MessageContext ctx) {
			if (Minecraft.getMinecraft().theWorld.provider.dimensionId != message.dimensionId)
				return null;
			if (message.type == 0) {
				for (int i = 0; i < 80; i++) {
					double d0 = message.x + ((random.nextDouble() - 0.5) * 2);
					double d1 = message.y + (random.nextDouble() * 2);
					double d2 = message.z + ((random.nextDouble() - 0.5) * 2);
					Minecraft.getMinecraft().theWorld.spawnParticle("reddust", d0, d1, d2, 1.0D, 0.4D, 0.8D);
				}
			}
			if (message.type == 1) {
				for (int i = 0; i < 80; i++) {
					double d0 = message.x + ((random.nextDouble() - 0.5) * 2);
					double d1 = message.y + (random.nextDouble() * 2);
					double d2 = message.z + ((random.nextDouble() - 0.5) * 2);
					Minecraft.getMinecraft().theWorld.spawnParticle("reddust", d0, d1, d2, 0.8D, 1.0D, 0.4D);
				}
			}
			if (message.type == 2) {
				for (int i = 0; i < 40; i++) {
					double d0 = message.x + ((random.nextDouble() - 0.5) * 2);
					double d1 = message.y + (random.nextDouble() * 2);
					double d2 = message.z + ((random.nextDouble() - 0.5) * 2);
					Minecraft.getMinecraft().theWorld.spawnParticle("reddust", d0, d1, d2, 0.4D, 0.8D, 1.0D);
				}
			}
			return null;
		}
	}
}
