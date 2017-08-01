package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.block.BlockSurveyor;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.network.MSpawnParticleWithRange;
import nahama.ofalenmod.util.BlockPos;
import nahama.ofalenmod.util.BlockRange;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityWorldEditorBase extends TileEntity implements ISidedInventory, FilterUtil.IFilterable {
	/** 作業範囲。 */
	protected BlockRange range;
	/** 次に作業するまでの残り時間。 */
	protected short interval;
	/** アイテムフィルターのタグ。 */
	protected NBTTagCompound tagItemFilter = FilterUtil.getInitializedFilterTag();
	/** 作業範囲の保存が相対的か。 */
	private boolean isAbsoluteRangeSaving;
	/** 作業する座標。 */
	private BlockPos coordWorking;
	/** 燃料のインベントリ。 */
	private ItemStack[] fuels = new ItemStack[this.getSizeFuelInventory()];
	/** 次の作業までの間隔。 */
	private short intervalProcessing = 10;
	/** 範囲内の作業が終わった時の再起動までの間隔。 */
	private short intervalRestarting = 40;
	/** 再起動が可能かどうか。 */
	private boolean canRestart;
	/** 作業しているかどうか。 */
	private boolean isWorking;
	/** 測量器が隣接しているかどうか。 */
	private boolean isSurveying;
	/** 次にパーティクルを発生させるまでの残り時間。 */
	private byte intervalParticle;
	/** 燃料の残り燃焼時間。 */
	private byte remainingEnergy;

	/** 更新時の処理。 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (range == null || coordWorking == null)
			this.initRangeAndCoord();
		// クライアント側なら終了。
		if (worldObj.isRemote)
			return;
		if (intervalParticle > 0) {
			intervalParticle--;
		} else {
			if (isSurveying) {
				// 測量器が隣接していて、残り時間が0になったらパーティクルを発生させる。
				this.survey();
				intervalParticle = 20;
			}
		}
		// 作業していないなら終了。
		if (!isWorking)
			return;
		if (interval > 0) {
			interval--;
			return;
		}
		interval = intervalProcessing;
		// 作業が不可能な状態か、燃料が足りなかったら作業を停止。
		if (!this.canWork() || !this.hasEnoughEnergy()) {
			this.setIsWorking(false);
			return;
		}
		// 次に作業するブロックの座標を取得する。
		coordWorking = this.searchNextBlock(range, coordWorking);
		// 前回作業した座標から範囲内に作業対象がなかったら、
		if (coordWorking == null) {
			// 作業中の座標をリセットし、
			this.resetCoordWorking();
			// 再起動が無効なら作業を終了する。
			if (!canRestart) {
				this.setIsWorking(false);
				return;
			}
			// 再起動が有効なら次の作業までの間隔を設定する。
			interval = (short) (intervalRestarting - intervalProcessing);
			return;
		}
		// 作業を実行し、成功したら燃料を消費する。
		if (this.work(coordWorking.x, coordWorking.y, coordWorking.z))
			this.consumeEnergy(1);
	}

	private void initRangeAndCoord() {
		range = new BlockRange(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
		this.resetCoordWorking();
	}

	private void resetCoordWorking() {
		coordWorking = range.posMin.copy();
		coordWorking.x--;
	}

	/** 作業可能な状態かどうか。 */
	protected abstract boolean canWork();

	/** 範囲内のブロックを指定座標から調べ始め、最初に見つかった作業対象の座標を返す。 */
	protected BlockPos searchNextBlock(BlockRange range, BlockPos start) {
		BlockPos checking = start.copy();
		if (!range.isInRange(checking)) {
			checking = range.posMin.copy();
			checking.x--;
		}
		while (true) {
			checking.x++;
			if (!range.isInRange(checking)) {
				checking.x = range.posMin.x;
				checking.z++;
				if (!range.isInRange(checking)) {
					checking.z = range.posMin.z;
					checking.y++;
					if (!range.isInRange(checking)) {
						return null;
					}
				}
			}
			if (!this.isCoordThis(checking.x, checking.y, checking.z) && this.canWorkWithCoord(checking.x, checking.y, checking.z))
				return new BlockPos(checking.x, checking.y, checking.z);
		}
	}

	/** このTileEntityがある座標かどうか。 */
	private boolean isCoordThis(int x, int y, int z) {
		return x == xCoord && y == yCoord && z == zCoord;
	}

	/** その座標のブロックが作業対象かどうか。 */
	protected abstract boolean canWorkWithCoord(int x, int y, int z);

	/** その座標に対して作業する。 */
	protected abstract boolean work(int x, int y, int z);

	/** 燃料として使えるItemStackかどうか。 */
	public boolean isItemFuel(ItemStack itemStack) {
		return itemStack.getItem() == OfalenModItemCore.partsOfalen && itemStack.getItemDamage() == 4;
	}

	/** 作業中かどうかを設定する。変更があった場合、メタデータも更新する。 */
	public void setIsWorking(boolean isWorking) {
		if (this.isWorking == isWorking)
			return;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, isWorking ? 1 : 0, 3);
		this.isWorking = isWorking;
	}

	/** 燃料の数を返す。 */
	private short getAmountFuel() {
		int amount = 0;
		for (ItemStack itemStack : fuels) {
			if (itemStack != null && isItemFuel(itemStack))
				amount += itemStack.stackSize;
		}
		return (short) amount;
	}

	/** 燃料の数を設定する。 */
	private void setAmountFuel(int amount) {
		fuels = new ItemStack[this.getSizeFuelInventory()];
		if (amount < 1)
			return;
		for (int i = 0; i < fuels.length; i++) {
			if (amount <= 64) {
				fuels[i] = this.getFuelStack(amount);
				return;
			}
			fuels[i] = this.getFuelStack(64);
			amount -= 64;
		}
	}

	/** 十分なエネルギーがあるか。 */
	private boolean hasEnoughEnergy() {
		return remainingEnergy > 0 || this.getAmountFuel() > 0;
	}

	protected int getAmountEnergy() {
		// TODO Config設定
		return this.getAmountFuel() * 20 + remainingEnergy;
	}

	/** エネルギーを消費する。 */
	protected void consumeEnergy(int amount) {
		if (remainingEnergy >= amount) {
			remainingEnergy -= amount;
		} else {
			amount -= remainingEnergy;
			this.addAmountFuel(-(amount / 20 + 1));
			// TODO Config設定
			remainingEnergy = (byte) (20 - amount % 20);
		}
	}

	/** 燃料を追加する。（負の値も可。） */
	private void addAmountFuel(int amount) {
		for (int i = 0; i < fuels.length; i++) {
			ItemStack fuel = fuels[i];
			if (fuel == null)
				fuel = this.getFuelStack(0);
			if (!this.isItemFuel(fuel))
				continue;
			int tmpAmount = amount + fuel.stackSize;
			if (tmpAmount < 1) {
				fuels[i] = null;
				amount = tmpAmount;
				continue;
			}
			if (tmpAmount < 64) {
				fuels[i] = this.getFuelStack(tmpAmount);
				return;
			}
			fuels[i] = this.getFuelStack(64);
			amount = tmpAmount - 64;
		}
	}

	/** 指定されたスタック数で燃料のItemStackを生成し返す。 */
	private ItemStack getFuelStack(int amount) {
		return new ItemStack(OfalenModItemCore.partsOfalen, amount, 4);
	}

	/** 燃料インベントリのスロット数を返す。 */
	private int getSizeFuelInventory() {
		return 27;
	}

	/** 設定IDの数を返す。 */
	public byte getAmountSettingID() {
		return 10;
	}

	public void setRange(BlockRange range) {
		this.range = range;
	}

	/** 設定IDに応じて表示値を返す。 */
	public short getWithID(int id) {
		switch (id) {
		case 0:
			return (short) (isAbsoluteRangeSaving ? 1 : 0);
		case 1:
			return (short) range.posMin.x;
		case 2:
			return (short) range.posMax.x;
		case 3:
			return (short) range.posMin.y;
		case 4:
			return (short) range.posMax.y;
		case 5:
			return (short) range.posMin.z;
		case 6:
			return (short) range.posMax.z;
		case 7:
			return intervalProcessing;
		case 8:
			return intervalRestarting;
		case 9:
			return (short) (canRestart ? 1 : 0);
		}
		return 0;
	}

	/** 設定IDに応じて値を変更する。 */
	public void setWithID(int id, int value) {
		switch (id) {
		case 0:
			isAbsoluteRangeSaving = (value != 0);
			break;
		case 1:
			if (value > range.posMax.x)
				value = range.posMax.x;
			range.posMin.x = (short) value;
			break;
		case 2:
			if (value < range.posMin.x)
				value = range.posMin.x;
			range.posMax.x = (short) value;
			break;
		case 3:
			if (value > range.posMax.y)
				value = range.posMax.y;
			range.posMin.y = (short) value;
			break;
		case 4:
			if (value < range.posMin.y)
				value = range.posMin.y;
			range.posMax.y = (short) value;
			break;
		case 5:
			if (value > range.posMax.z)
				value = range.posMax.z;
			range.posMin.z = (short) value;
			break;
		case 6:
			if (value < range.posMin.z)
				value = range.posMin.z;
			range.posMax.z = (short) value;
			break;
		case 7:
			if (value < 0)
				value = 0;
			if (value > intervalRestarting)
				value = intervalRestarting;
			intervalProcessing = (short) value;
			break;
		case 8:
			if (value < intervalProcessing)
				value = intervalProcessing;
			intervalRestarting = (short) value;
			break;
		case 9:
			canRestart = (value != 0);
			break;
		}
		// 座標が正常な値かどうか確認し、不正なら修正する。
		range.posMin.checkAndFixCoord(7);
		range.posMax.checkAndFixCoord(7);
		// サーバー側で設定変更されたら、作業を停止する。
		if (!worldObj.isRemote) {
			this.setIsWorking(false);
			if (0 < id && id < 7)
				this.resetCoordWorking();
		}
	}

	/** 設定の種類を返す。0 : boolean, 1 : short */
	public byte getSettingTypeWithID(int id) {
		if (0 < id && id < 9)
			return 1;
		return 0;
	}

	/** 設定の言語指定名を返す。 */
	public String getSettingNameWithID(int id) {
		switch (id) {
		case 0:
			return "info.ofalen.setting.editor.range.isAbsolute";
		case 1:
			return "info.ofalen.setting.editor.range.x.min";
		case 2:
			return "info.ofalen.setting.editor.range.x.max";
		case 3:
			return "info.ofalen.setting.editor.range.y.min";
		case 4:
			return "info.ofalen.setting.editor.range.y.max";
		case 5:
			return "info.ofalen.setting.editor.range.z.min";
		case 6:
			return "info.ofalen.setting.editor.range.z.max";
		case 7:
			return "info.ofalen.setting.editor.interval.processing";
		case 8:
			return "info.ofalen.setting.editor.interval.restarting";
		case 9:
			return "info.ofalen.setting.editor.canRestart";
		}
		return null;
	}

	@Override
	public void setTagItemFilter(NBTTagCompound filter) {
		tagItemFilter = filter;
	}

	/** 測量器が隣接しているかにより{@link #isSurveying}を更新する。 */
	public void searchSurveyor() {
		isSurveying = false;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int x = xCoord + dir.offsetX;
			int y = yCoord + dir.offsetY;
			int z = zCoord + dir.offsetZ;
			Block block = worldObj.getBlock(x, y, z);
			if (block instanceof BlockSurveyor) {
				isSurveying = true;
				break;
			}
		}
	}

	private void survey() {
		OfalenModPacketCore.WRAPPER.sendToAll(new MSpawnParticleWithRange(this.getColor(), (byte) worldObj.provider.dimensionId, range.copy()));
	}

	/** 色の番号を返す。4 : 橙, 5 : 翠, 6 : 紫, 7 : 黒。 */
	protected abstract byte getColor();

	/** TileEntityの情報をNBTに書き込む。 */
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean(OfalenNBTUtil.IS_RANGE_SAVING_ABSOLUTE, isAbsoluteRangeSaving);
		BlockRange tmp = range.copy();
		if (!isAbsoluteRangeSaving)
			tmp.applyOffset(xCoord, yCoord, zCoord, true);
		nbt.setTag(OfalenNBTUtil.RANGE, tmp.getNBT());
		nbt.setTag(OfalenNBTUtil.WORKING_COORD, coordWorking.getNBT());
		nbt.setShort(OfalenNBTUtil.INTERVAL, interval);
		nbt.setShort(OfalenNBTUtil.PROCESSING_INTERVAL, intervalProcessing);
		nbt.setShort(OfalenNBTUtil.RESTARTING_INTERVAL, intervalRestarting);
		nbt.setShort(OfalenNBTUtil.FUEL_AMOUNT, this.getAmountFuel());
		nbt.setBoolean(OfalenNBTUtil.CAN_RESTART, canRestart);
		nbt.setBoolean(OfalenNBTUtil.IS_WORKING, isWorking);
		nbt.setTag(FilterUtil.ITEM_FILTER, tagItemFilter);
		nbt.setByte(OfalenNBTUtil.REMAINING_ENERGY, remainingEnergy);
	}

	/** TileEntityの情報をNBTから読み込む。 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isAbsoluteRangeSaving = nbt.getBoolean(OfalenNBTUtil.IS_RANGE_SAVING_ABSOLUTE);
		range = BlockRange.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.RANGE));
		if (!isAbsoluteRangeSaving)
			range.applyOffset(xCoord, yCoord, zCoord);
		coordWorking = BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.WORKING_COORD));
		interval = nbt.getShort(OfalenNBTUtil.INTERVAL);
		intervalProcessing = nbt.getShort(OfalenNBTUtil.PROCESSING_INTERVAL);
		intervalRestarting = nbt.getShort(OfalenNBTUtil.RESTARTING_INTERVAL);
		this.setAmountFuel(nbt.getShort(OfalenNBTUtil.FUEL_AMOUNT));
		canRestart = nbt.getBoolean(OfalenNBTUtil.CAN_RESTART);
		isWorking = nbt.getBoolean(OfalenNBTUtil.IS_WORKING);
		tagItemFilter = nbt.getCompoundTag(FilterUtil.ITEM_FILTER);
		remainingEnergy = nbt.getByte(OfalenNBTUtil.REMAINING_ENERGY);
	}

	@Override
	public int getSizeInventory() {
		return this.getSizeFuelInventory();
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return fuels[slot];
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (fuels[slot] == null)
			return null;
		ItemStack itemstack;
		if (fuels[slot].stackSize <= amount) {
			itemstack = fuels[slot];
			fuels[slot] = null;
			return itemstack;
		}
		itemstack = fuels[slot].splitStack(amount);
		if (fuels[slot].stackSize < 1) {
			fuels[slot] = null;
		}
		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack itemStack = this.getStackInSlot(slot);
		this.setInventorySlotContents(slot, null);
		return itemStack;
	}

	/** スロットの中身を設定する。 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot < 0 || this.getSizeInventory() < slot)
			return;
		fuels[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
			itemStack.stackSize = this.getInventoryStackLimit();
	}

	/** 金床で設定された名前を持つかどうか。 */
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	/** インベントリの名前を返す。 */
	@Override
	public abstract String getInventoryName();

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
		return 0 <= slot && slot < this.getSizeInventory() && itemStack.isItemEqual(this.getFuelStack(1));
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] ret = new int[this.getSizeInventory()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = i;
		}
		return ret;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return false;
	}
}
