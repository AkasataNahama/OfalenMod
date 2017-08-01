package nahama.ofalenmod.util;

import net.minecraft.nbt.NBTTagCompound;

public class BlockRangeWithStandard {
	public BlockPos posA, posB;

	public BlockRangeWithStandard(int aX, int aY, int aZ, int bX, int bY, int bZ) {
		this(new BlockPos(aX, aY, aZ), new BlockPos(bX, bY, bZ));
	}

	private BlockRangeWithStandard(BlockPos posA, BlockPos posB) {
		this.posA = posA;
		this.posB = posB;
	}

	public static BlockRangeWithStandard loadFromNBT(NBTTagCompound nbt) {
		if (nbt == null || !nbt.hasKey(OfalenNBTUtil.POS_A) || !nbt.hasKey(OfalenNBTUtil.POS_B))
			return null;
		return new BlockRangeWithStandard(BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.POS_A)), BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.POS_B)));
	}

	public BlockRange convertToNormal() {
		return new BlockRange(Math.min(posA.x, posB.x), Math.min(posA.y, posB.y), Math.min(posA.z, posB.z), Math.max(posA.x, posB.x), Math.max(posA.y, posB.y), Math.max(posA.z, posB.z));
	}

	public NBTTagCompound getNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag(OfalenNBTUtil.POS_A, posA.getNBT());
		nbt.setTag(OfalenNBTUtil.POS_B, posB.getNBT());
		return nbt;
	}

	public BlockRangeWithStandard copy() {
		return new BlockRangeWithStandard(posA.copy(), posB.copy());
	}

	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (!(obj instanceof BlockRangeWithStandard))
			return false;
		BlockRangeWithStandard range = (BlockRangeWithStandard) obj;
		return posA.equals(range.posA) && posB.equals(range.posB);
	}

	/** "x * y * z" */
	public String toStringRange() {
		return Math.abs(posA.x - posB.x) + " * " + Math.abs(posA.y - posB.y) + " * " + Math.abs(posA.z - posB.z);
	}
}

