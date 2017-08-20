package nahama.ofalenmod.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class BlockRange {
	public BlockPos posMin;
	public BlockPos posMax;

	public BlockRange(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ));
	}

	public BlockRange(BlockPos min, BlockPos max) {
		posMin = min;
		posMax = max;
	}

	public boolean isInRange(BlockPos pos) {
		return posMin.x <= pos.x && posMin.y <= pos.y && posMin.z <= pos.z && pos.x <= posMax.x && pos.y <= posMax.y && pos.z <= posMax.z;
	}

	public boolean isInRange(Entity entity) {
		return posMin.x <= entity.posX && posMin.y <= entity.posY && posMin.z <= entity.posZ && entity.posX <= posMax.x + 1 && entity.posY <= posMax.y + 1 && entity.posZ <= posMax.z + 1;
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
		posMin.x += x;
		posMin.y += y;
		posMin.z += z;
		posMax.x += x;
		posMax.y += y;
		posMax.z += z;
	}

	public NBTTagCompound getNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag(OfalenNBTUtil.MIN_POS, posMin.getNBT());
		nbt.setTag(OfalenNBTUtil.MAX_POS, posMax.getNBT());
		return nbt;
	}

	public static BlockRange loadFromNBT(NBTTagCompound nbt) {
		if (nbt == null || !nbt.hasKey(OfalenNBTUtil.MIN_POS) || !nbt.hasKey(OfalenNBTUtil.MAX_POS))
			return null;
		return new BlockRange(BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.MIN_POS)), BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.MAX_POS)));
	}

	public BlockRange copy() {
		return new BlockRange(posMin.copy(), posMax.copy());
	}

	public BlockRange copyWithOffset(int x, int y, int z) {
		return this.copyWithOffset(x, y, z, false);
	}

	public BlockRange copyWithOffset(int x, int y, int z, boolean isMinus) {
		BlockRange ret = this.copy();
		ret.applyOffset(x, y, z, isMinus);
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (!(obj instanceof BlockRange))
			return false;
		BlockRange range = (BlockRange) obj;
		return posMin.equals(range.posMin) && posMax.equals(range.posMax);
	}

	/** "Min(x, y, z), Max(x, y, z)" */
	public String toStringRange() {
		return "Min(" + posMin.toStringCoord() + "), Max(" + posMax.toStringCoord() + ")";
	}
}
