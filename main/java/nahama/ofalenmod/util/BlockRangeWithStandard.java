package nahama.ofalenmod.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class BlockRangeWithStandard {
	public BlockPos posA, posB;

	public BlockRangeWithStandard(int aX, int aY, int aZ, int bX, int bY, int bZ) {
		this(new BlockPos(aX, aY, aZ), new BlockPos(bX, bY, bZ));
	}

	public BlockRangeWithStandard(BlockPos posA, BlockPos posB) {
		this.posA = posA;
		this.posB = posB;
	}

	public BlockRange convertToNormal() {
		return new BlockRange(Math.min(posA.x, posB.x), Math.min(posA.y, posB.y), Math.min(posA.z, posB.z), Math.max(posA.x, posB.x), Math.max(posA.y, posB.y), Math.max(posA.z, posB.z));
	}

	public boolean isInRange(BlockPos pos) {
		BlockRange range = this.convertToNormal();
		return range.posMin.x <= pos.x && range.posMin.y <= pos.y && range.posMin.z <= pos.z && pos.x <= range.posMax.x && pos.y <= range.posMax.y && pos.z <= range.posMax.z;
	}

	public boolean isInRange(Entity entity) {
		BlockRange range = this.convertToNormal();
		return range.posMin.x <= entity.posX && range.posMin.y <= entity.posY && range.posMin.z <= entity.posZ && entity.posX <= range.posMax.x + 1 && entity.posY <= range.posMax.y + 1 && entity.posZ <= range.posMax.z + 1;
	}

	public void applyOffset(int x, int y, int z) {
		this.applyOffset(x, y, z, false);
	}

	public void applyOffset(int x, int y, int z, boolean isMinus) {
		if (isMinus) {
			x = -x;
			y = -y;
			z = -z;
		}
		posA.x += x;
		posA.y += y;
		posA.z += z;
		posB.x += x;
		posB.y += y;
		posB.z += z;
	}

	public NBTTagCompound getNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag(OfalenNBTUtil.POS_A, posA.getNBT());
		nbt.setTag(OfalenNBTUtil.POS_B, posB.getNBT());
		return nbt;
	}

	public static BlockRangeWithStandard loadFromNBT(NBTTagCompound nbt) {
		if (nbt == null || !nbt.hasKey(OfalenNBTUtil.POS_A) || !nbt.hasKey(OfalenNBTUtil.POS_B))
			return null;
		return new BlockRangeWithStandard(BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.POS_A)), BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.POS_B)));
	}

	public BlockRangeWithStandard copy() {
		return new BlockRangeWithStandard(posA.copy(), posB.copy());
	}

	public BlockRangeWithStandard copyWithOffset(int x, int y, int z) {
		return this.copyWithOffset(x, y, z, false);
	}

	public BlockRangeWithStandard copyWithOffset(int x, int y, int z, boolean isMinus) {
		BlockRangeWithStandard ret = this.copy();
		ret.applyOffset(x, y, z, isMinus);
		return ret;
	}

	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (!(obj instanceof BlockRangeWithStandard))
			return false;
		BlockRangeWithStandard range = (BlockRangeWithStandard) obj;
		return posA.equals(range.posA) && posB.equals(range.posB);
	}

	/** "A(x, y, z), B(x, y, z)" */
	public String toStringRange() {
		return "A(" + posA.toStringCoord() + "), B(" + posB.toStringCoord() + ")";
	}
}

