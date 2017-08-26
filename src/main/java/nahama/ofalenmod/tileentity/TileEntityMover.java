package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.util.BlockPos;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class TileEntityMover extends TileEntityWorldEditorBase {
	/** 撤去が無効か。 */
	private boolean isRemovingDisabled;
	/** 設置が無効か。 */
	private boolean isPlacingDisabled;
	/** TileEntityを動かせるかどうか。 */
	private boolean canMoveTileEntity;
	/** 液体を対象とするか。 */
	private boolean canRemoveLiquid;
	private Map<BlockPos, BlockData> listMovingBlock = new HashMap<BlockPos, BlockData>();

	@Override
	protected boolean canWork() {
		return true;
	}

	@Override
	protected boolean canWorkWithCoord(int x, int y, int z) {
		BlockData data = BlockData.loadFromCoord(worldObj, x, y, z, false);
		boolean isAir = data.block.isAir(worldObj, x, y, z);
		// 対応する座標にデータが保存されていないか。
		BlockData dataStored = listMovingBlock.get(new BlockPos(x - range.posMin.x, y - range.posMin.y, z - range.posMin.z));
		boolean isPlacingAir = dataStored == null || dataStored.block.isAir(worldObj, x, y, z);
		if (isRemovingDisabled) {
			// 撤去が無効の時、設置ができないならfalse。
			if (!isAir || isPlacingAir)
				return false;
		}
		if (isPlacingDisabled) {
			// 設置が無効の時、撤去ができないならfalse。
			if (isAir || !isPlacingAir)
				return false;
		}
		// 空気から空気への置き換えならfalse。
		if (isAir && isPlacingAir)
			return false;
		// 破壊不可ブロックならfalse
		if (data.block.getBlockHardness(worldObj, x, y, z) < 0.0F)
			return false;
		// 液体の撤去が無効の時、液体ならfalse。
		if (!canRemoveLiquid && data.block instanceof BlockLiquid)
			return false;
		// フィルターで許可されていて、TileEntityを移動できるならtrue、移動できないなら持っていなければtrue。
		return OfalenNBTUtil.FilterUtil.canItemFilterThrough(tagItemFilter, new ItemStack(data.block, 1, data.meta)) && (canMoveTileEntity || !data.block.hasTileEntity(data.meta));
	}

	@Override
	protected boolean work(int x, int y, int z) {
		BlockPos pos = new BlockPos(x - range.posMin.x, y - range.posMin.y, z - range.posMin.z);
		BlockData dataPlacing = listMovingBlock.get(pos);
		if (!worldObj.isAirBlock(x, y, z)) {
			// 空気ブロックでないなら、保存し、音とパーティクルを出す。
			BlockData dataMoving = BlockData.loadFromCoord(worldObj, x, y, z, canMoveTileEntity);
			listMovingBlock.put(pos, dataMoving);
			worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(dataMoving.block) + (dataMoving.meta << 12));
		} else {
			// 空気ならマップから対応座標のデータを削除する。
			listMovingBlock.remove(pos);
		}
		if (dataPlacing != null) {
			// 対応座標のデータがあったら、設置し、音を出す。
			dataPlacing.putInWorld(worldObj, x, y, z);
			Block.SoundType stepSound = dataPlacing.block.stepSound;
			worldObj.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, stepSound.func_150496_b(), (stepSound.getVolume() + 1.0F) / 2.0F, stepSound.getPitch() * 0.8F);
		} else {
			// ないなら、空気に置き換える。
			worldObj.setBlockToAir(x, y, z);
		}
		return true;
	}

	@Override
	public byte getAmountSettingID() {
		return (byte) (super.getAmountSettingID() + 4);
	}

	@Override
	public short getWithID(int id) {
		switch (id - super.getAmountSettingID()) {
		case 0:
			return (short) (isRemovingDisabled ? 1 : 0);
		case 1:
			return (short) (isPlacingDisabled ? 1 : 0);
		case 2:
			return (short) (canRemoveLiquid ? 1 : 0);
		case 3:
			return (short) (canMoveTileEntity ? 1 : 0);
		}
		return super.getWithID(id);
	}

	@Override
	public void setWithID(int id, int value) {
		super.setWithID(id, value);
		switch (id - super.getAmountSettingID()) {
		case 0:
			isRemovingDisabled = (value != 0);
			break;
		case 1:
			isPlacingDisabled = (value != 0);
			break;
		case 2:
			canRemoveLiquid = (value != 0);
			break;
		case 3:
			canMoveTileEntity = (value != 0);
			break;
		}
	}

	@Override
	public String getSettingNameWithID(int id) {
		switch (id - super.getAmountSettingID()) {
		case 0:
			return "info.ofalen.setting.mover.isRemovingDisabled";
		case 1:
			return "info.ofalen.setting.mover.isPlacingDisabled";
		case 2:
			return "info.ofalen.setting.mover.canRemoveLiquid";
		case 3:
			return "info.ofalen.setting.mover.canMoveTileEntity";
		}
		return super.getSettingNameWithID(id);
	}

	@Override
	protected byte getColor() {
		return 5;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean(OfalenNBTUtil.IS_REMOVING_DISABLED, isRemovingDisabled);
		nbt.setBoolean(OfalenNBTUtil.IS_PLACING_DISABLED, isPlacingDisabled);
		nbt.setBoolean(OfalenNBTUtil.CAN_REMOVE_LIQUID, canRemoveLiquid);
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
		isRemovingDisabled = nbt.getBoolean(OfalenNBTUtil.IS_REMOVING_DISABLED);
		isPlacingDisabled = nbt.getBoolean(OfalenNBTUtil.IS_PLACING_DISABLED);
		canRemoveLiquid = nbt.getBoolean(OfalenNBTUtil.CAN_REMOVE_LIQUID);
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
