package nahama.ofalenmod.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRange {
	public BlockPos posMin;
	public BlockPos posMax;

	public BlockRange(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ));
	}

	@SuppressWarnings("WeakerAccess")
	public BlockRange(BlockPos min, BlockPos max) {
		posMin = min;
		posMax = max;
	}

	public static BlockRange loadFromNBT(NBTTagCompound nbt) {
		if (nbt == null || !nbt.hasKey(OfalenNBTUtil.MIN_POS) || !nbt.hasKey(OfalenNBTUtil.MAX_POS))
			return null;
		return new BlockRange(BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.MIN_POS)), BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.MAX_POS)));
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

	public BlockRange copy() {
		return new BlockRange(posMin.copy(), posMax.copy());
	}

	@SuppressWarnings("WeakerAccess")
	public BlockRange copyWithOffset(int x, int y, int z, boolean isMinus) {
		BlockRange ret = this.copy();
		ret.applyOffset(x, y, z, isMinus);
		return ret;
	}

	private BlockRange copyWithOffset(BlockPos pos, boolean isMinus) {
		return this.copyWithOffset(pos.x, pos.y, pos.z, isMinus);
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

	/**
	 * 範囲を回転させる。
	 * @param directionTo デフォルトをEASTとした時の回転方向。
	 */
	@SuppressWarnings("SuspiciousNameCombination")
	public BlockRange rotate(BlockPos standard, ForgeDirection directionTo) {
		BlockRange range = this.copyWithOffset(standard, true);
		BlockRange tmp = range.copy();
		switch (directionTo) {
		// 左を向く
		case NORTH:
			range.posMin.z = -tmp.posMax.x;
			range.posMax.z = -tmp.posMin.x;
			range.posMax.x = tmp.posMax.z;
			range.posMin.x = tmp.posMin.z;
			break;
		// 後を向く
		case WEST:
			range.posMin.x = -tmp.posMax.x;
			range.posMax.x = -tmp.posMin.x;
			range.posMin.z = -tmp.posMax.z;
			range.posMax.z = -tmp.posMin.z;
			break;
		// 右を向く
		case SOUTH:
			range.posMax.z = tmp.posMax.x;
			range.posMin.z = tmp.posMin.x;
			range.posMin.x = -tmp.posMax.z;
			range.posMax.x = -tmp.posMin.z;
			break;
		// 上を向く
		case UP:
			range.posMin.x = -tmp.posMax.y;
			range.posMax.x = -tmp.posMin.y;
			range.posMax.y = tmp.posMax.x;
			range.posMin.y = tmp.posMin.x;
			break;
		// 下を向く
		case DOWN:
			range.posMax.x = tmp.posMax.y;
			range.posMin.x = tmp.posMin.y;
			range.posMin.y = -tmp.posMax.x;
			range.posMax.y = -tmp.posMin.x;
			break;
		}
		return range.copyWithOffset(standard, false);
	}
}
