package nahama.ofalenmod.core;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.network.MApplyDetailedSetter;
import nahama.ofalenmod.network.MFloaterInit;
import nahama.ofalenmod.network.MFloaterMode;
import nahama.ofalenmod.network.MKeyStateChange;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.network.MTeleporterChannel;
import nahama.ofalenmod.network.MTeleporterMeta;
import nahama.ofalenmod.network.MWorldEditorSetting;

public class OfalenModPacketCore {
	/** パケット通信用。 */
	public static final SimpleNetworkWrapper WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(OfalenModCore.MOD_ID);
	private static byte discriminator;

	public static void registerPacket() {
		discriminator = 0;
		registerMessage(MTeleporterChannel.class, MTeleporterChannel.Handler.class, Side.SERVER);
		registerMessage(MTeleporterMeta.class, MTeleporterMeta.Handler.class, Side.SERVER);
		registerMessage(MFloaterInit.class, MFloaterInit.Handler.class, Side.CLIENT);
		registerMessage(MFloaterMode.class, MFloaterMode.Handler.class, Side.CLIENT);
		registerMessage(MSpawnParticle.class, MSpawnParticle.Handler.class, Side.CLIENT);
		registerMessage(MWorldEditorSetting.class, MWorldEditorSetting.Handler.class, Side.SERVER);
		registerMessage(MApplyDetailedSetter.class, MApplyDetailedSetter.Handler.class, Side.SERVER);
		registerMessage(MKeyStateChange.class, MKeyStateChange.Handler.class, Side.SERVER);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<REQ> requestMessageType, Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Side sideHandler) {
		WRAPPER.registerMessage(messageHandler, requestMessageType, discriminator, sideHandler);
		discriminator++;
	}
}
