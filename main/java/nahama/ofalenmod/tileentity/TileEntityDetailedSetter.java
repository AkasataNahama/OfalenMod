package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.handler.OfalenDetailedSettingHandler;
import nahama.ofalenmod.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDetailedSetter extends TileEntity implements IInventory {
	protected ItemStack[] itemStacks = new ItemStack[this.getSizeInventory()];
	protected byte state = -1;

	public void apply() {
		OfalenTimer.start("TileEntityDetailedSetter.apply");
		if (itemStacks[0] == null || !(itemStacks[0].getItem() instanceof IItemOfalenSettable)) {
			OfalenLog.debuggingInfo("Failed to apply detailed setting!", "TileEntityDetailedSetter");
			return;
		}
		OfalenSetting setting = ((IItemOfalenSettable) itemStacks[0].getItem()).getSetting();
		// 設定のパス。
		String s = "";
		int i = 0;
		while (setting.hasChildSetting()) {
			i++;
			setting = setting.getChildSetting(itemStacks[i]);
			s += setting.getSettingName() + "/";
		}
		i++;
		NBTTagCompound nbtSetting = OfalenDetailedSettingHandler.getSettingTag(itemStacks[0]);
		OfalenDetailedSettingHandler.applySettingToNBT(nbtSetting, s, setting, itemStacks[i]);
		OfalenTimer.watchAndLog("TileEntityDetailedSetter.apply");
	}

	@Override
	public void updateEntity() {
		if (state != -1)
			state = -1;
	}

	protected byte getState() {
		if (state < 0)
			state = (byte) this.updateState();
		return state;
	}

	protected int updateState() {
		if (itemStacks[0] == null || !(itemStacks[0].getItem() instanceof IItemOfalenSettable))
			return 0;
		OfalenSetting setting = ((IItemOfalenSettable) itemStacks[0].getItem()).getSetting();
		for (int i = 1; i < itemStacks.length; i++) {
			if (itemStacks[i] == null)
				return i + 32;
			if (setting.isValidItem(itemStacks[i])) {
				if (setting.hasChildSetting()) {
					setting = setting.getChildSetting(itemStacks[i]);
					continue;
				}
				return i + 64;
			}
			return i;
		}
		return 0;
	}

	public boolean isValidState() {
		return this.getState() > 31;
	}

	public boolean isApplicableState() {
		return this.getState() > 63;
	}

	public int getFirstInvalidSlot() {
		return this.getState() % 32;
	}

	/** NBTにTileEntityの情報を記録する。 */
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
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
	}

	/** NBTをTileEntityの情報に反映する。 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList(OfalenNBTUtil.ITEMS, 10);
		itemStacks = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbt1.getByte(OfalenNBTUtil.SLOT);
			if (0 <= b0 && b0 < itemStacks.length) {
				itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}
	}

	protected boolean isExistingSlot(int slot) {
		return 0 <= slot && slot < this.getSizeInventory();
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 6;
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (!this.isExistingSlot(slot))
			return null;
		return itemStacks[slot];
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (!this.isExistingSlot(slot) || itemStacks[slot] == null)
			return null;
		ItemStack itemStack;
		if (itemStacks[slot].stackSize <= amount) {
			itemStack = itemStacks[slot];
			itemStacks[slot] = null;
			return itemStack;
		}
		itemStack = itemStacks[slot].splitStack(amount);
		if (itemStacks[slot].stackSize < 1)
			itemStacks[slot] = null;
		return itemStack;
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
		if (!this.isExistingSlot(slot))
			return;
		itemStacks[slot] = itemStack;
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
	public String getInventoryName() {
		return "container.ofalen.setterDetailed";
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
		return this.isExistingSlot(slot);
	}
}
