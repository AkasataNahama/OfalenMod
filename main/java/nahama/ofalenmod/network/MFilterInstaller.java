package nahama.ofalenmod.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.ofalenmod.inventory.ContainerItemFilterInstaller;
import net.minecraft.inventory.Container;

public class MFilterInstaller implements IMessage {

	public MFilterInstaller() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<MFilterInstaller, IMessage> {
		@Override
		public IMessage onMessage(MFilterInstaller message, MessageContext ctx) {
			Container container = ctx.getServerHandler().playerEntity.openContainer;
			if (container instanceof ContainerItemFilterInstaller)
				((ContainerItemFilterInstaller) container).installFilter();
			return null;
		}
	}
}
