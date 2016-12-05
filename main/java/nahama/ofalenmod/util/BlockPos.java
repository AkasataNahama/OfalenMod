package nahama.ofalenmod.util;

import net.minecraft.nbt.NBTTagCompound;

/** x, y, zの座標をshortで保持する。equalsオーバーライド済み。 */
public class BlockPos {
	public short x, y, z;
	private static final short MAX_X_Z = 30000;
	private static final short MIN_X_Z = -30000;
	private static final short MAX_Y = 255;
	private static final short MIN_Y = 0;

	public BlockPos(int x, int y, int z) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (short) z;
	}

	public boolean isValid() {
		return MIN_X_Z <= x && x <= MAX_X_Z && MIN_Y <= y && y <= MAX_Y && MIN_X_Z <= z && z <= MAX_X_Z;
	}

	public void checkAndFixCoord(int type) {
		if ((type & 1) == 1) {
			if (x < MIN_X_Z)
				x = MIN_X_Z;
			if (x > MAX_X_Z)
				x = MAX_X_Z;
		}
		if ((type & 2) == 2) {
			if (y < MIN_Y)
				y = MIN_Y;
			if (y > MAX_Y)
				y = MAX_Y;
		}
		if ((type & 4) == 4) {
			if (z < MIN_X_Z)
				z = MIN_X_Z;
			if (z > MAX_X_Z)
				z = MAX_X_Z;
		}
	}

	public BlockPos copy() {
		return new BlockPos(x, y, z);
	}

	public NBTTagCompound getNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setShort(OfalenNBTUtil.X, x);
		nbt.setShort(OfalenNBTUtil.Y, y);
		nbt.setShort(OfalenNBTUtil.Z, z);
		return nbt;
	}

	public static BlockPos loadFromNBT(NBTTagCompound nbt) {
		return new BlockPos(nbt.getShort(OfalenNBTUtil.X), nbt.getShort(OfalenNBTUtil.Y), nbt.getShort(OfalenNBTUtil.Z));
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (!(obj instanceof BlockPos))
			return false;
		BlockPos pos = (BlockPos) obj;
		return x == pos.x && y == pos.y && z == pos.z;
	}

	/** "x, y, z" */
	public String toStringCoord() {
		return x + ", " + y + ", " + z;
	}
}
