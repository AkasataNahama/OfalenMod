package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.Util;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityPlacer extends TileEntityWorldEditorBase implements IInventory {
	protected ItemStack[] itemStacks = new ItemStack[this.getSizeOwnInventory()];

	@Override
	protected boolean canWork() {
		return this.getFirstValidIndex() >= 0;
	}

	@Override
	protected boolean canWorkWithCoord(int x, int y, int z) {
		return worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z);
	}

	@Override
	protected boolean work(int x, int y, int z) {
		int index = this.getFirstValidIndex();
		ItemStack itemStack = itemStacks[index];
		Item item = itemStack.getItem();
		Block block = Block.getBlockFromItem(item);
		int meta = item.getMetadata(itemStack.getItemDamage());
		meta = block.onBlockPlaced(worldObj, x, y, z, 1, 0.5F, 0.5F, 0.5F, meta);
		if (!worldObj.setBlock(x, y, z, block, meta, 3))
			return false;
		try {
			block.onBlockPlacedBy(worldObj, x, y, z, null, itemStack);
		} catch (NullPointerException e) {
			Util.error("NullPointerException on placing block. (name=" + block.getLocalizedName() + ", id=" + Block.getIdFromBlock(block) + ", meta=" + meta + ")", "TileEntityPlacer");
			Util.error("This error was anticipated. Probably Placer failed to place the block.", "TileEntityPlacer");
			e.printStackTrace();
		}
		block.onPostBlockPlaced(worldObj, x, y, z, meta);
		worldObj.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
		--itemStack.stackSize;
		if (itemStack.stackSize < 1)
			itemStacks[index] = null;
		return true;
	}

	/** itemStacks内でブロックとして設置できるアイテムの最初の番号を返す。 */
	protected int getFirstValidIndex() {
		for (int i = 0; i < itemStacks.length; i++) {
			if (itemStacks[i] != null && Block.getBlockFromItem(itemStacks[i].getItem()) != Blocks.air && OfalenNBTUtil.FilterUtil.canItemFilterThrough(tagItemFilter, itemStacks[i]))
				return i;
		}
		return -1;
	}

	@Override
	protected byte getColor() {
		return 4;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setTag(OfalenNBTUtil.ITEMS, OfalenNBTUtil.writeItemStacksToNBTTagList(itemStacks));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		itemStacks = OfalenNBTUtil.loadItemStacksFromNBTTagList(nbt.getTagList(OfalenNBTUtil.ITEMS, 10), this.getSizeOwnInventory());
	}

	/** 燃料スロットを含まないスロット数を返す。 */
	public int getSizeOwnInventory() {
		return 27;
	}

	@Override
	public int getSizeInventory() {
		return super.getSizeInventory() + this.getSizeOwnInventory();
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < super.getSizeInventory())
			return super.getStackInSlot(slot);
		return itemStacks[slot - super.getSizeInventory()];
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot < super.getSizeInventory())
			return super.decrStackSize(slot, amount);
		slot -= super.getSizeInventory();
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

	/** スロットの中身を設定する。 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot < super.getSizeInventory() || this.getSizeInventory() < slot) {
			super.setInventorySlotContents(slot, itemStack);
			return;
		}
		slot -= super.getSizeInventory();
		itemStacks[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
			itemStack.stackSize = this.getInventoryStackLimit();
	}

	/** インベントリの名前を返す。 */
	@Override
	public String getInventoryName() {
		return "container.ofalen.placer";
	}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return super.getSizeInventory() <= slot || super.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] ret = new int[this.getSizeInventory()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = i;
		}
		return ret;
	}
}
