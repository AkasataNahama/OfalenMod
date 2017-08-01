package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.util.BlockPos;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class TileEntityMover extends TileEntityWorldEditorBase {
	/** TileEntityを動かせるかどうか。 */
	private boolean canMoveTileEntity;
	private Map<BlockPos, BlockData> listMovingBlock = new HashMap<BlockPos, BlockData>();

	@Override
	protected boolean canWork() {
		return true;
	}

	@Override
	protected boolean canWorkWithCoord(int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		// 空気から空気への置き換えならfalse。
		if (worldObj.isAirBlock(x, y, z) && !listMovingBlock.containsKey(new BlockPos(x - range.posMin.x, y - range.posMin.y, z - range.posMin.z)))
			return false;
		// フィルターで許可されていて、TileEntityを移動できるならtrue、移動できないなら持っていなければtrue。
		return OfalenNBTUtil.FilterUtil.canItemFilterThrough(tagItemFilter, new ItemStack(block, 1, worldObj.getBlockMetadata(x, y, z))) && (canMoveTileEntity || !block.hasTileEntity(worldObj.getBlockMetadata(x, y, z)));
	}

	@Override
	protected boolean work(int x, int y, int z) {
		BlockPos pos = new BlockPos(x - range.posMin.x, y - range.posMin.y, z - range.posMin.z);
		BlockData data = listMovingBlock.get(pos);
		// 空気ブロックでないなら保存する。
		if (!worldObj.isAirBlock(x, y, z)) {
			listMovingBlock.put(pos, BlockData.loadFromCoord(worldObj, x, y, z, canMoveTileEntity));
		} else {
			listMovingBlock.remove(pos);
		}
		if (data != null) {
			data.putInWorld(worldObj, x, y, z);
		} else {
			worldObj.setBlockToAir(x, y, z);
		}
		return true;
	}

	@Override
	public byte getAmountSettingID() {
		return (byte) (super.getAmountSettingID() + 1);
	}

	@Override
	public short getWithID(int id) {
		if (id == super.getAmountSettingID())
			return (short) (canMoveTileEntity ? 1 : 0);
		return super.getWithID(id);
	}

	@Override
	public void setWithID(int id, int value) {
		super.setWithID(id, value);
		if (id == super.getAmountSettingID())
			canMoveTileEntity = (value != 0);
	}

	@Override
	public String getSettingNameWithID(int id) {
		if (id == super.getAmountSettingID())
			return "info.ofalen.setting.mover.canMoveTileEntity";
		return super.getSettingNameWithID(id);
	}

	@Override
	protected byte getColor() {
		return 5;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean(OfalenNBTUtil.CAN_MOVE_TILE_ENTITY, canMoveTileEntity);
		NBTTagList nbtTagList = new NBTTagList();
		for (Map.Entry<BlockPos, BlockData> entry : listMovingBlock.entrySet()) {
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setTag(OfalenNBTUtil.BLOCK_POS, entry.getKey().getNBT());
			nbt1.setTag(OfalenNBTUtil.BLOCK_DATA, entry.getValue().getNBT());
			nbtTagList.appendTag(nbt1);
		}
		nbt.setTag(OfalenNBTUtil.MOVING_BLOCKS, nbtTagList);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		canMoveTileEntity = nbt.getBoolean(OfalenNBTUtil.CAN_MOVE_TILE_ENTITY);
		listMovingBlock.clear();
		NBTTagList nbtTagList = nbt.getTagList(OfalenNBTUtil.MOVING_BLOCKS, 10);
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			NBTTagCompound nbt1 = nbtTagList.getCompoundTagAt(i);
			listMovingBlock.put(BlockPos.loadFromNBT(nbt1.getCompoundTag(OfalenNBTUtil.BLOCK_POS)), BlockData.loadFromNBT(nbt1.getCompoundTag(OfalenNBTUtil.BLOCK_DATA)));
		}
	}

	@Override
	public String getInventoryName() {
		return "container.ofalen.mover";
	}

	protected static class BlockData {
		protected Block block;
		protected byte meta;
		protected NBTTagCompound tagTileEntity;

		public BlockData(Block block, int meta) {
			this(block, meta, null);
		}

		public BlockData(Block block, int meta, TileEntity tileEntity) {
			this.block = block;
			this.meta = (byte) (meta % 16);
			if (tileEntity == null)
				return;
			this.tagTileEntity = new NBTTagCompound();
			tileEntity.writeToNBT(tagTileEntity);
		}

		public static BlockData loadFromNBT(NBTTagCompound nbt) {
			BlockData ret = new BlockData(Block.getBlockById(nbt.getShort(OfalenNBTUtil.TILE_ID)), nbt.getByte(OfalenNBTUtil.META));
			if (nbt.hasKey(OfalenNBTUtil.TILE_ENTITY, 10))
				ret.tagTileEntity = nbt.getCompoundTag(OfalenNBTUtil.TILE_ENTITY);
			return ret;
		}

		public static BlockData loadFromCoord(World world, int x, int y, int z, boolean canLoadTileEntity) {
			return new BlockData(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), canLoadTileEntity ? world.getTileEntity(x, y, z) : null);
		}

		public void putInWorld(World world, int x, int y, int z) {
			world.setBlock(x, y, z, block, meta, 3);
			if (tagTileEntity == null)
				return;
			tagTileEntity.setInteger("x", x);
			tagTileEntity.setInteger("y", y);
			tagTileEntity.setInteger("z", z);
			world.setTileEntity(x, y, z, TileEntity.createAndLoadEntity(tagTileEntity));
		}

		public NBTTagCompound getNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setShort(OfalenNBTUtil.TILE_ID, (short) Block.getIdFromBlock(block));
			nbt.setByte(OfalenNBTUtil.META, meta);
			if (tagTileEntity != null)
				nbt.setTag(OfalenNBTUtil.TILE_ENTITY, tagTileEntity);
			return nbt;
		}
	}
}
