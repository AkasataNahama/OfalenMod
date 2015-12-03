package nahama.ofalenmod.tileentity;

import java.util.ArrayList;
import java.util.Iterator;

import nahama.ofalenmod.handler.OfalenTeleportHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
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
		OfalenTeleportHandler manager = OfalenTeleportHandler.getInstance(worldObj);
		if (isValid && manager.isChannelValid(channel)) {
			manager.removeMarker(channel);
		}
		channel = newChannel;
		if (newChannel < 1 || manager.isChannelValid(newChannel)) {
			isValid = false;
			return;
		}
		manager.registerMarker(newChannel, new int[] { xCoord, yCoord, zCoord });
		isValid = true;
	}

	public boolean canTeleport() {
		if (!isValid)
			return false;
		for (int i = 1; i < 3; i++) {
			Block block = worldObj.getBlock(xCoord, yCoord + i, zCoord);
			if (block == null || block.getBlockHardness(worldObj, xCoord, yCoord + i, zCoord) < 0)
				return false;
			if (!block.isNormalCube())
				continue;
			int meta = worldObj.getBlockMetadata(xCoord, yCoord + i, zCoord);
			ArrayList<ItemStack> drops = block.getDrops(worldObj, xCoord, yCoord + i, zCoord, meta, 0);
			Iterator<ItemStack> iterator = drops.iterator();
			while (iterator.hasNext()) {
				ItemStack itemStack = iterator.next();
				EntityItem entity = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, itemStack);
				if (itemStack.hasTagCompound()) {
					entity.getEntityItem().setTagCompound(((NBTTagCompound) itemStack.getTagCompound().copy()));
				}
				worldObj.spawnEntityInWorld(entity);
			}
			block.breakBlock(worldObj, xCoord, yCoord + i, zCoord, block, meta);
			worldObj.setBlockToAir(xCoord, yCoord + i, zCoord);
		}
		return true;
	}

	public void onBreaking() {
		if (channel < 1 || !isValid)
			return;
		OfalenTeleportHandler.getInstance(worldObj).removeMarker(channel);
	}

	public int getChannel() {
		return channel;
	}

}
