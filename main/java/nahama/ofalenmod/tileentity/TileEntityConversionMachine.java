package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDicCore;
import nahama.ofalenmod.item.Ofalen;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityConversionMachine extends TileEntityGradedMachineBase {

	/** 変換色指定用スロット。(No.3) */
	protected ItemStack sample;

	/** インベントリ名を返す。 */
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? customName : "container.conversionmachine-" + grade;
	}

	@Override
	protected int getWorkTime() {
		return OfalenModConfigCore.timeConversion * (4 - grade) / 4;
	}

	@Override
	protected boolean canWork() {
		// 変換前スロットかサンプルが空ならば不可。
		if (itemStacks[0] == null) {
			return false;
		} else if (sample == null) {
			this.moveItemStack();
			return false;
		}
		if (itemStacks[2] != null) {
			// 完成品スロットに空きがないなら不可。
			if (!itemStacks[2].isItemEqual(sample))
				return false;
			if (itemStacks[2].stackSize >= itemStacks[2].getMaxStackSize() || itemStacks[2].stackSize >= this.getInventoryStackLimit())
				return false;
		}
		// 変換前スロットとサンプルのアイテムが同じでメタデータが違い、オファレンなら可。
		if (itemStacks[0].getItem() == sample.getItem() && itemStacks[0].getItemDamage() != sample.getItemDamage()) {
			Item item = itemStacks[0].getItem();
			if ((item instanceof Ofalen) || (Block.getBlockFromItem(item) == OfalenModBlockCore.blockOfalen)) {
				if (item != OfalenModItemCore.coreOfalen || sample.getItemDamage() != 3) {
					return true;
				}
			}
		}
		// 匠Craftが読み込まれているなら、匠のオファレンでも可。
		if (OfalenModOreDicCore.isTakumiCraftLoaded) {
			if (itemStacks[0].isItemEqual(OfalenModOreDicCore.ofalenCreeper)) {
				if (Block.getBlockFromItem(sample.getItem()) == OfalenModBlockCore.blockOfalen) {
					return true;
				}
			}
		}
		this.moveItemStack();
		return false;
	}

	@Override
	public void onWorking() {
		// 完成品スロットに変換結果を代入/追加する。
		if (itemStacks[2] == null) {
			itemStacks[2] = new ItemStack(itemStacks[0].getItem(), 1, sample.getItemDamage());
		} else if (itemStacks[2].isItemEqual(sample)) {
			itemStacks[2].stackSize++;
		}
		// 変換前スロットのスタック数を減らす。
		--itemStacks[0].stackSize;
		if (itemStacks[0].stackSize < 1) {
			itemStacks[0] = null;
		}
		this.updateGrade();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (sample == null)
			return;
		NBTTagCompound nbt1 = new NBTTagCompound();
		sample.writeToNBT(nbt1);
		nbt.setTag("Sample", nbt1);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		sample = null;
		if (nbt.hasKey("Sample"))
			sample = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Sample"));
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot != 3)
			return super.getStackInSlot(slot);
		return sample;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot != 3)
			return super.decrStackSize(slot, amount);

		if (sample == null)
			return null;
		ItemStack itemstack;
		if (sample.stackSize <= amount) {
			itemstack = sample;
			sample = null;
			return itemstack;
		}
		itemstack = sample.splitStack(amount);
		if (sample.stackSize < 1) {
			sample = null;
		}
		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot != 3)
			super.setInventorySlotContents(slot, itemStack);
		sample = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

}
