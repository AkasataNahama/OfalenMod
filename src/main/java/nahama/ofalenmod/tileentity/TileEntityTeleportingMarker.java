package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.handler.OfalenTeleportHandler;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTeleportingMarker extends TileEntity {
	public boolean isValid;
	private boolean wasInited;
	protected short channel;

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote || wasInited)
			return;
		// サーバー側で、初期化されていなかったらする。
		this.setChannel(channel);
		wasInited = true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort(OfalenNBTUtil.CHANNEL, channel);
		nbt.setBoolean(OfalenNBTUtil.IS_VALID, isValid);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		channel = nbt.getShort(OfalenNBTUtil.CHANNEL);
		wasInited = !nbt.getBoolean(OfalenNBTUtil.IS_VALID);
	}

	public void setChannel(short newChannel) {
		if (worldObj.isRemote) {
			channel = newChannel;
			return;
		}
		if (isValid && OfalenTeleportHandler.isChannelValid(channel)) {
			OfalenTeleportHandler.removeMarker(channel);
		}
		channel = newChannel;
		isValid = OfalenTeleportHandler.registerMarker(newChannel, (byte) worldObj.provider.dimensionId, (short) xCoord, (short) yCoord, (short) zCoord);
	}

	public void onBreaking() {
		if (channel < 1 || !isValid)
			return;
		OfalenTeleportHandler.removeMarker(channel);
	}

	public short getChannel() {
		return channel;
	}
}
