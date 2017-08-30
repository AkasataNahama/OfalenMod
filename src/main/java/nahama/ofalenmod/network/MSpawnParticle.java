package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.util.OfalenParticleUtil;
import net.minecraft.client.Minecraft;

import static nahama.ofalenmod.util.OfalenUtil.random;

public class MSpawnParticle implements IMessage {
	public byte dimensionId;
	public float x, y, z;
	public byte type;

	@SuppressWarnings("unused")
	public MSpawnParticle() {
	}

	public MSpawnParticle(byte dimensionId, double x, double y, double z, byte type) {
		this.dimensionId = dimensionId;
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
		this.type = type;
	}

	/** dimensionIdはbyteにキャストされる。 */
	public MSpawnParticle(int dimensionId, double x, double y, double z, byte type) {
		this((byte) dimensionId, x, y, z, type);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimensionId = buf.readByte();
		x = buf.readFloat();
		y = buf.readFloat();
		z = buf.readFloat();
		type = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(dimensionId);
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeFloat(z);
		buf.writeByte(type);
	}

	public static class Handler implements IMessageHandler<MSpawnParticle, IMessage> {
		@Override
		public IMessage onMessage(MSpawnParticle message, MessageContext ctx) {
			if (Minecraft.getMinecraft().theWorld.provider.dimensionId != message.dimensionId)
				return null;
			double[] color = OfalenParticleUtil.getColorWithTypeForParticle(message.type);
			byte typeParticle = 0;
			switch (message.type) {
			case 0:
				typeParticle = OfalenModConfigCore.typeProtectorParticle;
				break;
			case 1:
				typeParticle = OfalenModConfigCore.typeTeleporterParticle;
				break;
			case 2:
				typeParticle = OfalenModConfigCore.typeFloaterParticle;
				break;
			}
			if (typeParticle == 0) {
				Minecraft.getMinecraft().theWorld.spawnParticle("reddust", message.x, message.y, message.z, color[0], color[1], color[2]);
			} else if (typeParticle == 1) {
				// 円形でパーティクルを生成する。
				int i = 0;
				while (i < 40) {
					double dx = (random.nextDouble() - 0.5) * 2;
					double dz = (random.nextDouble() - 0.5) * 2;
					if (dx * dx + dz * dz < 1) {
						Minecraft.getMinecraft().theWorld.spawnParticle("reddust", message.x + dx, message.y, message.z + dz, color[0], color[1], color[2]);
						i++;
					}
				}
			} else if (typeParticle == 2) {
				// 円柱形でパーティクルを生成する。
				for (int i = 0; i < 20; i++) {
					double rad = random.nextDouble() * Math.PI * 2;
					Minecraft.getMinecraft().theWorld.spawnParticle("reddust", message.x + Math.cos(rad) / 2, message.y + random.nextDouble() * 2, message.z + Math.sin(rad) / 2, color[0], color[1], color[2]);
				}
			}
			return null;
		}
	}
}
