package nahama.ofalenmod.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.block.BlockMachineProcessor;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDicCore;
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
	public int workTime;
	/** 燃焼が終わるまでの時間。 */
	public int burnTime;
	/** 燃焼中アイテムの燃焼時間。 */
	public int maxBurnTime;

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
			burnTime--;
		// 燃焼中でなく、材料・燃料のどちらかでも空なら終了する。
		if (!this.isBurning() && (itemStacks[1] == null || itemStacks[0] == null)) {
			workTime = 0;
			if (isBurning)
				this.updateIsBurning();
			return;
		}
		// 燃焼中でなく作業可能なら、新たに燃料を消費し、燃焼させる。
		if (!this.isBurning() && this.canWork()) {
			maxBurnTime = burnTime = this.getItemBurnTime(itemStacks[1]);
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
			++workTime;
			if (workTime >= this.getWorkTime()) {
				workTime = 0;
				this.onWorking();
			}
		} else {
			// 燃焼中でないか作業不可能ならば作業時間をリセットする。
			workTime = 0;
		}

		// 燃焼しているかが変わっていたら、更新する。
		if (isBurning != this.isBurning()) {
			this.updateIsBurning();
		}
	}

	/** 作業可能かどうか。 */
	protected abstract boolean canWork();

	/** アイテムの燃焼時間を返す。 */
	public int getItemBurnTime(ItemStack itemStack) {
		if (itemStack == null)
			return 0;
		int time = TileEntityFurnace.getItemBurnTime(itemStack) * (4 - grade) / 200;
		if (itemStack.getItem() != OfalenModItemCore.partsOfalen)
			return time;
		int meta = itemStack.getItemDamage();
		if (meta == 3) {
			return OfalenModConfigCore.timeBurnStone;
		} else if (meta == 4) {
			return OfalenModConfigCore.timeBurnOfalen;
		}
		if (OfalenModOreDicCore.isTakumiCraftLoaded) {
			for (int i = 0; i < OfalenModOreDicCore.listTDiamond.size(); i++) {
				if (itemStack.isItemEqual(OfalenModOreDicCore.listTDiamond.get(i)))
					return OfalenModConfigCore.timeBurnStone;
			}
		}
		return time;
	}

	/** 作業にかかる時間を返す。 */
	protected abstract int getWorkTime();

	/** 作業時の処理。 */
	protected abstract void onWorking();

	/** 燃焼しているかでメタデータを更新する。 */
	protected void updateIsBurning() {
		int direction = this.getBlockMetadata();
		if (burnTime > 0) {
			direction = direction | 8;
		} else {
			direction = direction & 7;
		}
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, direction, 2);
		this.markDirty();
		this.updateGrade();
	}

	/** グレードを更新する。 */
	public void updateGrade() {
		int getX = xCoord, getY = yCoord, getZ = zCoord;
		switch (this.getBlockMetadata() & 7) {
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
		if (!(block instanceof BlockMachineProcessor)) {
			grade = 0;
			return;
		}
		int meta = ((BlockMachineProcessor) block).updateMetadata(worldObj, getX, getY, getZ);
		if (meta < 4) {
			grade = 0;
			return;
		}
		grade = (byte) ((meta & 3) + 1);
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
		nbt.setByte("Grade", grade);
		nbt.setInteger("WorkTime", workTime);
		nbt.setInteger("BurnTime", burnTime);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < itemStacks.length; i++) {
			if (itemStacks[i] == null)
				continue;
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setByte("Slot", (byte) i);
			itemStacks[i].writeToNBT(nbt1);
			nbttaglist.appendTag(nbt1);
		}
		nbt.setTag("Items", nbttaglist);
		if (this.hasCustomInventoryName())
			nbt.setString("CustomName", customName);
	}

	/** NBTから機械の情報を反映する。 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		grade = nbt.getByte("Grade");
		workTime = nbt.getInteger("WorkTime");
		burnTime = nbt.getInteger("BurnTime");
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		itemStacks = new ItemStack[3];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbt1.getByte("Slot");
			if (0 <= b0 && b0 < itemStacks.length) {
				itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}
		maxBurnTime = this.getItemBurnTime(itemStacks[1]);
		if (nbt.hasKey("CustomName", 8))
			customName = nbt.getString("CustomName");
	}

	/** アイテムが燃料として使えるかどうか。 */
	public boolean isItemFuel(ItemStack itemStack) {
		return this.getItemBurnTime(itemStack) > 0;
	}

	@SideOnly(Side.CLIENT)
	public int getWorkProgressScaled(int par1) {
		return workTime * par1 / this.getWorkTime();
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int par1) {
		if (maxBurnTime == 0)
			maxBurnTime = 200;
		return burnTime * par1 / maxBurnTime;
	}

	/** 燃焼中かどうか。 */
	public boolean isBurning() {
		return burnTime > 0;
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
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 2 ? false : (slot == 1 ? this.isItemFuel(itemStack) : true);
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
