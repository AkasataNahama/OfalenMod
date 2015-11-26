package nahama.ofalenmod.tileentity;

import java.util.ArrayList;
import java.util.Iterator;

import nahama.ofalenmod.core.OfalenTeleportManager;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTeleportMarker extends TileEntity {

	protected int channel = -1;

	@Override
	public void updateEntity() {
		super.updateEntity();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Channel", channel);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		channel = nbt.getInteger("Channel");
	}

	public boolean updateChannel(int channel) {
		OfalenTeleportManager manager = OfalenTeleportManager.getInstance(worldObj);
		if (channel < 0 || manager.isChannelValid(channel))
			return false;
		manager.registerMarker(channel, new int[] { xCoord, yCoord, zCoord });
		return true;
	}

	public boolean onTeleporting() {
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
		if (channel < 0)
			return;
		OfalenTeleportManager.getInstance(worldObj).removeMarker(channel);
	}

}
