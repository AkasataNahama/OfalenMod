package nahama.ofalenmod.core;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.network.*;

public class OfalenModPacketCore {
	/** パケット通信用。 */
	public static final SimpleNetworkWrapper WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(OfalenModCore.MODID);
	private static byte discriminator;

	public static void registerPacket() {
		discriminator = 0;
		registerMessage(MTeleporterChannel.class, MTeleporterChannel.Handler.class, Side.SERVER);
		registerMessage(MTeleporterMeta.class, MTeleporterMeta.Handler.class, Side.SERVER);
		registerMessage(MFloaterMode.class, MFloaterMode.Handler.class, Side.SERVER);
		registerMessage(MSpawnParticle.class, MSpawnParticle.Handler.class, Side.CLIENT);
		registerMessage(MFilterInstaller.class, MFilterInstaller.Handler.class, Side.SERVER);
		registerMessage(MWorldEditorSetting.class, MWorldEditorSetting.Handler.class, Side.SERVER);
		registerMessage(MSpawnParticleWithRange.class, MSpawnParticleWithRange.Handler.class, Side.CLIENT);
		registerMessage(MApplyDetailedSetter.class, MApplyDetailedSetter.Handler.class, Side.SERVER);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<REQ> requestMessageType, Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Side side) {
		WRAPPER.registerMessage(messageHandler, requestMessageType, discriminator, side);
		discriminator++;
	}
}