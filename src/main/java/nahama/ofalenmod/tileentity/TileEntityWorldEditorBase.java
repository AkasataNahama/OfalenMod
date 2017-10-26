package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.block.BlockSurveyor;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.util.BlockPos;
import nahama.ofalenmod.util.BlockRange;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
import nahama.ofalenmod.util.OfalenParticleUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

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
	/** 燃料の残り燃焼時間。 */
	private short remainingEnergy;

	/** 更新時の処理。 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (range == null || coordWorking == null)
			this.initRangeAndCoord();
		// クライアント側ならパーティクルを表示して終了。
		if (worldObj.isRemote) {
			if (interval > 0)
				interval--;
			if (isSurveying && interval < 1) {
				// 測量器が隣接していて、残り時間が0になったらパーティクルを発生させる。
				OfalenParticleUtil.spawnParticleWithBlockRange(worldObj, range.copy(), this.getColor());
				interval = 20;
			}
			return;
		}
		// 作業していないなら終了。
		if (!isWorking)
			return;
		if (interval > 0) {
			interval--;
			return;
		}
		interval = (short) (intervalProcessing - 1);
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
			interval = (short) (intervalRestarting - 1);
			return;
		}
		// 作業を実行し、成功したら燃料を消費する。
		if (this.work(coordWorking.x, coordWorking.y, coordWorking.z))
			this.consumeEnergy(1);
	}

	private void initRangeAndCoord() {
		this.setRange(new BlockRange(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord));
		this.resetCoordWorking();
	}

	private void resetCoordWorking() {
		coordWorking = range.posMin.copy();
		coordWorking.y = range.posMax.y;
		coordWorking.x--;
	}

	/** 作業可能な状態かどうか。 */
	protected abstract boolean canWork();

	/** 範囲内のブロックを指定座標から調べ始め、最初に見つかった作業対象の座標を返す。 */
	protected BlockPos searchNextBlock(BlockRange range, BlockPos start) {
		BlockPos checking = start.copy();
		if (!range.isInRange(checking)) {
			checking = range.posMin.copy();
			checking.y = range.posMax.y;
			checking.x--;
		}
		while (true) {
			checking.x++;
			if (!range.isInRange(checking)) {
				checking.x = range.posMin.x;
				checking.z++;
				if (!range.isInRange(checking)) {
					checking.z = range.posMin.z;
					checking.y--;
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

	/** 停止していたら作業を始め、作業中なら停止する。 */
	public void changeIsWorking() {
		this.setIsWorking(!isWorking);
	}

	/** 作業中かどうかを設定する。変更があった場合、メタデータも更新する。 */
	private void setIsWorking(boolean isWorking) {
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
		return this.getAmountFuel() * OfalenModConfigCore.energyDarkFuel + remainingEnergy;
	}

	/** エネルギーを消費する。 */
	protected void consumeEnergy(int amount) {
		if (remainingEnergy >= amount) {
			remainingEnergy -= amount;
		} else {
			amount -= remainingEnergy;
			int energy = OfalenModConfigCore.energyDarkFuel;
			this.addAmountFuel(-(amount / energy + 1));
			remainingEnergy = (byte) (energy - amount % energy);
		}
	}

	public short getRemainingEnergy() {
		return remainingEnergy;
	}

	public void setRemainingEnergy(short remainingEnergy) {
		this.remainingEnergy = remainingEnergy;
	}

	public int getRemainingEnergyScaled(int scale) {
		if (OfalenModConfigCore.energyDarkFuel < 1)
			return 0;
		if (remainingEnergy == 0)
			return this.getAmountFuel() > 0 ? scale : 0;
		return remainingEnergy * scale / OfalenModConfigCore.energyDarkFuel;
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
		if (isSurveying)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	/** 設定IDに応じて表示値を返す。 */
	public Object getWithID(int id) {
		switch (id) {
		case 0:
			return isAbsoluteRangeSaving;
		case 1:
			return range.posMin.x;
		case 2:
			return range.posMax.x;
		case 3:
			return range.posMin.y;
		case 4:
			return range.posMax.y;
		case 5:
			return range.posMin.z;
		case 6:
			return range.posMax.z;
		case 7:
			return intervalProcessing;
		case 8:
			return intervalRestarting;
		case 9:
			return canRestart;
		}
		return 0;
	}

	/** 設定IDに応じて値を変更する。 */
	public void setWithID(int id, byte changeType) {
		int value = 0;
		if (changeType != 0) {
			value = (int) Math.pow(10, Math.abs(changeType) - 1);
			if (changeType < 0)
				value *= -1;
		}
		switch (id) {
		case 0:
			isAbsoluteRangeSaving = !isAbsoluteRangeSaving;
			break;
		case 1:
			value += range.posMin.x;
			if (value > range.posMax.x)
				value = range.posMax.x;
			range.posMin.x = value;
			break;
		case 2:
			value += range.posMax.x;
			if (value < range.posMin.x)
				value = range.posMin.x;
			range.posMax.x = value;
			break;
		case 3:
			value += range.posMin.y;
			if (value > range.posMax.y)
				value = range.posMax.y;
			range.posMin.y = value;
			break;
		case 4:
			value += range.posMax.y;
			if (value < range.posMin.y)
				value = range.posMin.y;
			range.posMax.y = value;
			break;
		case 5:
			value += range.posMin.z;
			if (value > range.posMax.z)
				value = range.posMax.z;
			range.posMin.z = value;
			break;
		case 6:
			value += range.posMax.z;
			if (value < range.posMin.z)
				value = range.posMin.z;
			range.posMax.z = value;
			break;
		case 7:
			value += intervalProcessing;
			if (value < 1)
				value = 1;
			if (value > Short.MAX_VALUE)
				value = Short.MAX_VALUE;
			intervalProcessing = (short) value;
			break;
		case 8:
			value += intervalRestarting;
			if (value < 1)
				value = 1;
			if (value > Short.MAX_VALUE)
				value = Short.MAX_VALUE;
			intervalRestarting = (short) value;
			break;
		case 9:
			canRestart = !canRestart;
			break;
		}
		// 座標が正常な値かどうか確認し、不正なら修正する。
		range.posMin.checkAndFixCoord();
		range.posMax.checkAndFixCoord();
		// サーバー側で設定変更されたら、作業を停止する。
		if (!worldObj.isRemote) {
			this.setIsWorking(false);
			if (0 < id && id < 7)
				this.resetCoordWorking();
		}
		// クライアントに同期する。
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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

	public List<String> getFilterMessage() {
		return FilterUtil.getFilterMessage(tagItemFilter);
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
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	/** 送信するパケットを返す。 */
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeSettingToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
	}

	/** 同期用パケットのNBTを書き込む。 */
	protected void writeSettingToNBT(NBTTagCompound nbt) {
		nbt.setBoolean(OfalenNBTUtil.IS_RANGE_SAVING_ABSOLUTE, isAbsoluteRangeSaving);
		if (range != null) {
			BlockRange tmp = range.copy();
			if (!isAbsoluteRangeSaving)
				tmp.applyOffset(xCoord, yCoord, zCoord, true);
			nbt.setTag(OfalenNBTUtil.RANGE, tmp.getNBT());
		}
		nbt.setShort(OfalenNBTUtil.PROCESSING_INTERVAL, intervalProcessing);
		nbt.setShort(OfalenNBTUtil.RESTARTING_INTERVAL, intervalRestarting);
		nbt.setBoolean(OfalenNBTUtil.CAN_RESTART, canRestart);
		nbt.setBoolean(OfalenNBTUtil.IS_SURVEYING, isSurveying);
	}

	/** パケットを処理する。 */
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readSettingFromNBT(pkt.func_148857_g());
		if (range != null)
			this.resetCoordWorking();
	}

	/** 同期用パケットのNBTを読み込む。 */
	protected void readSettingFromNBT(NBTTagCompound nbt) {
		isAbsoluteRangeSaving = nbt.getBoolean(OfalenNBTUtil.IS_RANGE_SAVING_ABSOLUTE);
		range = BlockRange.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.RANGE));
		if (range != null) {
			if (!isAbsoluteRangeSaving)
				range.applyOffset(xCoord, yCoord, zCoord);
			range.posMin.checkAndFixCoord();
			range.posMax.checkAndFixCoord();
		}
		intervalProcessing = nbt.getShort(OfalenNBTUtil.PROCESSING_INTERVAL);
		intervalRestarting = nbt.getShort(OfalenNBTUtil.RESTARTING_INTERVAL);
		canRestart = nbt.getBoolean(OfalenNBTUtil.CAN_RESTART);
		isSurveying = nbt.getBoolean(OfalenNBTUtil.IS_SURVEYING);
	}

	/** 色の番号を返す。4 : 橙, 5 : 翠, 6 : 紫, 7 : 黒。 */
	protected abstract byte getColor();

	/** TileEntityの情報をNBTに書き込む。 */
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		this.writeSettingToNBT(nbt);
		nbt.setTag(OfalenNBTUtil.WORKING_COORD, coordWorking.getNBT());
		nbt.setShort(OfalenNBTUtil.INTERVAL, interval);
		nbt.setShort(OfalenNBTUtil.FUEL_AMOUNT, this.getAmountFuel());
		nbt.setBoolean(OfalenNBTUtil.IS_WORKING, isWorking);
		nbt.setTag(FilterUtil.ITEM_FILTER, tagItemFilter);
		nbt.setShort(OfalenNBTUtil.REMAINING_ENERGY, remainingEnergy);
	}

	/** TileEntityの情報をNBTから読み込む。 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.readSettingFromNBT(nbt);
		coordWorking = BlockPos.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.WORKING_COORD));
		interval = nbt.getShort(OfalenNBTUtil.INTERVAL);
		this.setAmountFuel(nbt.getShort(OfalenNBTUtil.FUEL_AMOUNT));
		isWorking = nbt.getBoolean(OfalenNBTUtil.IS_WORKING);
		tagItemFilter = nbt.getCompoundTag(FilterUtil.ITEM_FILTER);
		remainingEnergy = nbt.getShort(OfalenNBTUtil.REMAINING_ENERGY);
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

	/** ドロップアイテムを返す。 */
	public ItemStack getDrop() {
		ItemStack stack = new ItemStack(this.getBlockType());
		FilterUtil.initFilterTag(stack);
		NBTTagCompound nbtItem = stack.stackTagCompound;
		// TileEntityの情報をNBTに保存する。
		NBTTagCompound nbtTileEntity = new NBTTagCompound();
		this.writeSettingToNBT(nbtTileEntity);
		nbtTileEntity.setTag(OfalenNBTUtil.WORKING_COORD, coordWorking.getNBT());
		nbtTileEntity.setShort(OfalenNBTUtil.INTERVAL, interval);
		nbtTileEntity.setBoolean(OfalenNBTUtil.IS_WORKING, isWorking);
		nbtTileEntity.setShort(OfalenNBTUtil.REMAINING_ENERGY, remainingEnergy);
		this.writeAdditionalDataToItemNBT(nbtTileEntity);
		nbtItem.setTag(OfalenNBTUtil.TILE_ENTITY_WORLD_EDITOR_BASE, nbtTileEntity);
		// フィルターの情報はアイテムの直下に保存する。
		FilterUtil.copyFilterTag(FilterUtil.getFilterTag(stack), tagItemFilter);
		stack.setTagCompound(nbtItem);
		return stack;
	}

	/** ドロップアイテムのNBTに追加で情報を書き込む。 */
	protected void writeAdditionalDataToItemNBT(NBTTagCompound nbt) {
	}

	/** 設置時の処理。 */
	public void onPlaced(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			return;
		// TileEntityの情報を読み込む。
		if (nbt.hasKey(OfalenNBTUtil.TILE_ENTITY_WORLD_EDITOR_BASE)) {
			NBTTagCompound nbtTileEntity = nbt.getCompoundTag(OfalenNBTUtil.TILE_ENTITY_WORLD_EDITOR_BASE);
			this.readSettingFromNBT(nbtTileEntity);
			coordWorking = BlockPos.loadFromNBT(nbtTileEntity.getCompoundTag(OfalenNBTUtil.WORKING_COORD));
			interval = nbtTileEntity.getShort(OfalenNBTUtil.INTERVAL);
			this.setIsWorking(nbtTileEntity.getBoolean(OfalenNBTUtil.IS_WORKING));
			remainingEnergy = nbtTileEntity.getShort(OfalenNBTUtil.REMAINING_ENERGY);
			this.readAdditionalDataFromItemNBT(nbtTileEntity);
		}
		// フィルターの情報を読み込む。
		if (FilterUtil.isAvailableFilterTag(stack))
			tagItemFilter = FilterUtil.getFilterTag(stack);
		// 測量器が隣接しているか判定する。
		this.searchSurveyor();
		// クライアントに同期する。
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	/** 設置時にアイテムのNBTから追加の情報を読み込む。 */
	protected void readAdditionalDataFromItemNBT(NBTTagCompound nbt) {
	}
}
