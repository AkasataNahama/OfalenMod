package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityBreaker extends TileEntityWorldEditorBase {
	/** ブロックを削除するかどうか。 */
	private boolean canDeleteBrokenBlock;
	/** 液体を破壊するか。 */
	private boolean canDeleteLiquid;

	@Override
	protected boolean canWork() {
		return true;
	}

	@Override
	protected boolean canWorkWithCoord(int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		// 破壊不可ブロックならfalse。
		if (block.getBlockHardness(worldObj, x, y, z) < 0.0F)
			return false;
		// 空気ブロックならfalse。
		if (block.isAir(worldObj, x, y, z))
			return false;
		// 設定により液体ブロックを無視する。
		if (!canDeleteLiquid && block instanceof BlockLiquid)
			return false;
		// フィルターで許可されていればtrue。
		return OfalenNBTUtil.FilterUtil.canItemFilterThrough(tagItemFilter, new ItemStack(block, 1, worldObj.getBlockMetadata(x, y, z)));
	}

	@Override
	protected boolean work(int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		int meta = worldObj.getBlockMetadata(x, y, z);
		boolean flag;
		try {
			block.onBlockHarvested(worldObj, x, y, z, meta, null);
			flag = block.removedByPlayer(worldObj, null, x, y, z, true);
		} catch (NullPointerException e) {
			OfalenLog.error("Failed to break block. (name=" + block.getLocalizedName() + ", id=" + Block.getIdFromBlock(block) + ", meta=" + meta + ")", "TileEntityBreaker");
			OfalenLog.error("This error was anticipated. Probably Breaker failed to break the block.", "TileEntityBreaker");
			e.printStackTrace();
			return false;
		}
		worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
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
		return (byte) (super.getAmountSettingID() + 2);
	}

	@Override
	public short getWithID(int id) {
		switch (id - super.getAmountSettingID()) {
		case 0:
			return (short) (canDeleteBrokenBlock ? 1 : 0);
		case 1:
			return (short) (canDeleteLiquid ? 1 : 0);
		}
		return super.getWithID(id);
	}

	@Override
	public void setWithID(int id, int value) {
		super.setWithID(id, value);
		switch (id - super.getAmountSettingID()) {
		case 0:
			canDeleteBrokenBlock = (value != 0);
			break;
		case 1:
			canDeleteLiquid = (value != 0);
			break;
		}
	}

	@Override
	public String getSettingNameWithID(int id) {
		switch (id - super.getAmountSettingID()) {
		case 0:
			return "info.ofalen.setting.breaker.canDeleteBrokenBlock";
		case 1:
			return "info.ofalen.setting.breaker.canDeleteLiquid";
		}
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
		nbt.setBoolean(OfalenNBTUtil.CAN_DELETE_LIQUID, canDeleteLiquid);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		canDeleteBrokenBlock = nbt.getBoolean(OfalenNBTUtil.CAN_DELETE_BROKEN_BLOCK);
		canDeleteLiquid = nbt.getBoolean(OfalenNBTUtil.CAN_DELETE_LIQUID);
	}

	@Override
	public String getInventoryName() {
		return "container.ofalen.breaker";
	}
}
