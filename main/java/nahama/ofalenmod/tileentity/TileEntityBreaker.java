package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityBreaker extends TileEntityWorldEditorBase {
	/** ブロックを削除するかどうか。 */
	public boolean canDeleteBrokenBlock;

	@Override
	protected boolean canWork() {
		return true;
	}

	@Override
	protected boolean canWorkWithCoord(int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		return !block.isAir(worldObj, x, y, z) && OfalenNBTUtil.FilterUtil.canItemFilterThrough(tagItemFilter, new ItemStack(block, 1, worldObj.getBlockMetadata(x, y, z)));
	}

	@Override
	protected boolean work(int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		int meta = worldObj.getBlockMetadata(x, y, z);
		boolean flag = true;
		try {
			block.onBlockHarvested(worldObj, x, y, z, meta, null);
			flag = block.removedByPlayer(worldObj, null, x, y, z, true);
		} catch (NullPointerException e) {
			Util.error("NullPointerException on breaking block. (name=" + block.getLocalizedName() + ", id=" + Block.getIdFromBlock(block) + ", meta=" + meta + ")", "TileEntityBreaker");
			Util.error("This error was anticipated. Probably Breaker failed to break the block.", "TileEntityBreaker");
			e.printStackTrace();
		}
		if (flag)
			block.onBlockDestroyedByPlayer(worldObj, x, y, z, meta);
		if (!canDeleteBrokenBlock)
			block.dropBlockAsItem(worldObj, x, y, z, meta, 0);
		if (flag)
			block.dropXpOnBlockBreak(worldObj, x, y, z, block.getExpDrop(worldObj, meta, 0));
		return true;
	}

	@Override
	public byte getAmountSettingID() {
		return (byte) (super.getAmountSettingID() + 1);
	}

	@Override
	public short getWithID(int id) {
		if (id == super.getAmountSettingID())
			return (short) (canDeleteBrokenBlock ? 1 : 0);
		return super.getWithID(id);
	}

	@Override
	public void setWithID(int id, int value) {
		super.setWithID(id, value);
		if (id == super.getAmountSettingID())
			canDeleteBrokenBlock = (value != 0);
	}

	@Override
	public String getSettingNameWithID(int id) {
		if (id == super.getAmountSettingID())
			return "info.ofalen.setting.breaker.canDeleteBrokenBlock";
		return super.getSettingNameWithID(id);
	}

	@Override
	protected byte getColor() {
		return 6;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean(OfalenNBTUtil.CAN_DELETE_BROKEN_BLOCK, canDeleteBrokenBlock);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		canDeleteBrokenBlock = nbt.getBoolean(OfalenNBTUtil.CAN_DELETE_BROKEN_BLOCK);
	}

	@Override
	public String getInventoryName() {
		return "container.ofalen.breaker";
	}
}
