package nahama.ofalenmod.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.block.BlockProcessor;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDictCore;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

public abstract class TileEntityGradedMachineBase extends TileEntity implements ISidedInventory {
	/** 機械の中にあるアイテムの配列。 */
	protected ItemStack[] itemStacks = new ItemStack[3];
	/** 金床で設定された名前。 */
	protected String customName;
	/** 機械のグレード。 */
	protected byte grade;
	/** 作業した時間。 */
	public short timeWorking;
	/** 燃焼が終わるまでの時間。 */
	public short timeBurning;
	/** 燃焼中アイテムの燃焼時間。 */
	public short timeMaxBurning;

	/** アップデート時の処理。 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		// クライアントは終了。
		if (worldObj.isRemote)
			return;
		boolean isBurning = this.isBurning();
		// 燃焼中なら時間を減らす。
		if (isBurning)
			timeBurning--;
		// 燃焼中でなく、材料・燃料のどちらかでも空なら終了する。
		if (!this.isBurning() && (itemStacks[1] == null || itemStacks[0] == null)) {
			timeWorking = 0;
			if (isBurning)
				this.updateIsBurning();
			return;
		}
		// 燃焼中でなく作業可能なら、新たに燃料を消費し、燃焼させる。
		if (!this.isBurning() && this.canWork()) {
			timeMaxBurning = timeBurning = this.getItemBurnTime(itemStacks[1]);
			if (this.isBurning()) {
				if (itemStacks[1] != null) {
					--itemStacks[1].stackSize;
					if (itemStacks[1].stackSize == 0) {
						itemStacks[1] = itemStacks[1].getItem().getContainerItem(itemStacks[1]);
					}
				}
			}
		}
		// 燃焼中で作業可能なら、作業する。
		if (this.isBurning() && this.canWork()) {
			++timeWorking;
			if (timeWorking >= this.getMaxWorkingTimeWithGrade()) {
				timeWorking = 0;
				this.work();
			}
		} else {
			// 燃焼中でないか作業不可能ならば作業時間をリセットする。
			timeWorking = 0;
		}
		// 燃焼しているかが変わっていたら、更新する。
		if (isBurning != this.isBurning()) {
			this.updateIsBurning();
		}
	}

	/** 作業可能かどうか。 */
	protected abstract boolean canWork();

	/** アイテムの燃焼時間を返す。 */
	public short getItemBurnTime(ItemStack itemStack) {
		if (itemStack == null)
			return 0;
		if (OfalenModOreDictCore.isMatchedItemStack(OfalenModOreDictCore.listTDiamond, itemStack))
			return (short) (this.getTimeWithGrade(OfalenModConfigCore.timeTDiamondBurning));
		short time = (short) (this.getTimeWithGrade(TileEntityFurnace.getItemBurnTime(itemStack)) / OfalenModConfigCore.factorFurnaceFuelBurningTime);
		if (itemStack.getItem() != OfalenModItemCore.partsOfalen)
			return time;
		int meta = itemStack.getItemDamage();
		if (meta == 3)
			return (short) (this.getTimeWithGrade(OfalenModConfigCore.timeWhiteFuelBurning));
		return time;
	}

	/** 作業時間などにGradeを反映させる。 */
	protected int getTimeWithGrade(int time) {
		return time / OfalenUtil.power(2, grade);
	}

	/** 作業にかかる時間を返す。 */
	protected int getMaxWorkingTimeWithGrade() {
		return this.getTimeWithGrade(this.getMaxWorkingTimeBase());
	}

	/** 作業にかかる時間の基準値を返す。 */
	protected abstract int getMaxWorkingTimeBase();

	/** 作業時の処理。 */
	protected abstract void work();

	/** 燃焼しているかでメタデータを更新する。 */
	protected void updateIsBurning() {
		int direction = this.getBlockMetadata();
		if (timeBurning > 0) {
			direction = direction % 8 + 8;
		} else {
			direction = direction % 8;
		}
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, direction, 2);
		this.markDirty();
		this.updateGrade();
	}

	/** グレードを更新する。 */
	public void updateGrade() {
		int getX = xCoord, getY = yCoord, getZ = zCoord;
		switch (this.getBlockMetadata() % 8) {
		case 2:
			getZ += 2;
			break;
		case 3:
			getZ -= 2;
			break;
		case 4:
			getX += 2;
			break;
		case 5:
			getX -= 2;
			break;
		default:
			grade = 0;
			return;
		}
		Block block = worldObj.getBlock(getX, getY, getZ);
		if (!(block instanceof BlockProcessor)) {
			grade = 0;
			return;
		}
		int meta = ((BlockProcessor) block).updateMetadata(worldObj, getX, getY, getZ);
		if (meta < 4) {
			grade = 0;
			return;
		}
		grade = (byte) (meta % 4 + 1);
	}

	/** 材料スロットから完成品スロットにアイテムを移動する。 */
	protected void moveItemStack() {
		if (itemStacks[0] == null)
			return;
		if (itemStacks[2] == null) {
			itemStacks[2] = itemStacks[0].copy();
			itemStacks[0] = null;
			return;
		}
		int limit = Math.min(itemStacks[2].getMaxStackSize(), this.getInventoryStackLimit());
		if (itemStacks[2].stackSize >= limit || !itemStacks[0].isItemEqual(itemStacks[2]))
			return;
		int result = itemStacks[2].stackSize + itemStacks[0].stackSize;
		if (result <= limit) {
			itemStacks[2].stackSize = result;
			itemStacks[0] = null;
			return;
		}
		itemStacks[0].stackSize = result - limit;
		itemStacks[2].stackSize = limit;
	}

	/** NBTに機械の情報を記録する。 */
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setByte(OfalenNBTUtil.GRADE, grade);
		nbt.setShort(OfalenNBTUtil.WORKING_TIME, timeWorking);
		nbt.setShort(OfalenNBTUtil.BURNING_TIME, timeBurning);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < itemStacks.length; i++) {
			if (itemStacks[i] == null)
				continue;
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setByte(OfalenNBTUtil.SLOT, (byte) i);
			itemStacks[i].writeToNBT(nbt1);
			nbttaglist.appendTag(nbt1);
		}
		nbt.setTag(OfalenNBTUtil.ITEMS, nbttaglist);
		if (this.hasCustomInventoryName())
			nbt.setString(OfalenNBTUtil.CUSTOM_NAME, customName);
	}

	/** NBTから機械の情報を反映する。 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		grade = nbt.getByte(OfalenNBTUtil.GRADE);
		timeWorking = nbt.getShort(OfalenNBTUtil.WORKING_TIME);
		timeBurning = nbt.getShort(OfalenNBTUtil.BURNING_TIME);
		NBTTagList nbttaglist = nbt.getTagList(OfalenNBTUtil.ITEMS, 10);
		itemStacks = new ItemStack[3];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbt1.getByte(OfalenNBTUtil.SLOT);
			if (0 <= b0 && b0 < itemStacks.length) {
				itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}
		timeMaxBurning = this.getItemBurnTime(itemStacks[1]);
		if (nbt.hasKey(OfalenNBTUtil.CUSTOM_NAME, 8))
			customName = nbt.getString(OfalenNBTUtil.CUSTOM_NAME);
	}

	/** アイテムが燃料として使えるかどうか。 */
	public boolean isItemFuel(ItemStack itemStack) {
		return this.getItemBurnTime(itemStack) > 0;
	}

	/** 作業の完了率を0~scaleで返す。Gui表示用。 */
	@SideOnly(Side.CLIENT)
	public int getWorkProgressScaled(int scale) {
		return timeWorking * scale / this.getMaxWorkingTimeWithGrade();
	}

	/** 燃料の残り燃焼時間の割合を0~scaleで返す。Gui表示用。 */
	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int scale) {
		if (timeMaxBurning == 0)
			timeMaxBurning = 200;
		return timeBurning * scale / timeMaxBurning;
	}

	/**
	 * 燃焼中かどうか。
	 * @return timeBurning > 0
	 */
	public boolean isBurning() {
		return timeBurning > 0;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 3;
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemStacks[slot];
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (itemStacks[slot] == null)
			return null;
		ItemStack itemstack;
		if (itemStacks[slot].stackSize <= amount) {
			itemstack = itemStacks[slot];
			itemStacks[slot] = null;
			return itemstack;
		}
		itemstack = itemStacks[slot].splitStack(amount);
		if (itemStacks[slot].stackSize < 1) {
			itemStacks[slot] = null;
		}
		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	/** スロットの中身を設定する。 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot < 0 || this.getSizeInventory() < slot)
			return;
		itemStacks[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/** 金床で設定された名前を持つかどうか。 */
	@Override
	public boolean hasCustomInventoryName() {
		return customName != null && customName.length() > 0;
	}

	/** このインベントリの最大スタック数を返す。 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/** プレイヤーが使用できるかどうか。 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot != 2 && (slot != 1 || this.isItemFuel(itemStack));
	}

	/** 方向からアクセスできるスロットの配列を返す。 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? new int[] { 2, 1 } : (side == 1 ? new int[] { 0 } : new int[] { 1 });
	}

	/** スロットに搬入できるかどうか。 */
	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(slot, itemStack);
	}

	/** スロットから搬出できるかどうか。 */
	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return side != 0 || slot != 1 || itemStack.getItem() == Items.bucket;
	}
}
