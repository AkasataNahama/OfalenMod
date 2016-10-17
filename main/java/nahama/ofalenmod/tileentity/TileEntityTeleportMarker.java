package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.handler.OfalenTeleportHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTeleportMarker extends TileEntity {

	public boolean isValid;
	private boolean wasInited;
	protected int channel;

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
		nbt.setInteger("Channel", channel);
		nbt.setBoolean("IsValid", isValid);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		channel = nbt.getInteger("Channel");
		wasInited = !nbt.getBoolean("IsValid");
	}

	public void setChannel(int newChannel) {
		if (worldObj.isRemote) {
			channel = newChannel;
			return;
		}
		if (isValid && OfalenTeleportHandler.isChannelValid(channel)) {
			OfalenTeleportHandler.removeMarker(channel);
		}
		channel = newChannel;
		isValid = OfalenTeleportHandler.registerMarker(newChannel, (byte) worldObj.provider.dimensionId, xCoord, yCoord, zCoord);
	}

	public void onBreaking() {
		if (channel < 1 || !isValid)
			return;
		OfalenTeleportHandler.removeMarker(channel);
	}

	public int getChannel() {
		return channel;
	}

}
