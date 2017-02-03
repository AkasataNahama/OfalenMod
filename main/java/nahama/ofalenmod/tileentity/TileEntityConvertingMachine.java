package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDictCore;
import nahama.ofalenmod.item.ItemOfalen;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityConvertingMachine extends TileEntityGradedMachineBase {
	/** 変換色指定用スロット。(No.3) */
	protected ItemStack sample;

	/** インベントリ名を返す。 */
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? customName : "container.ofalen.machine.converting-" + grade;
	}

	@Override
	protected int getMaxWorkingTimeBase() {
		return OfalenModConfigCore.timeConverting;
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
		if (isProperPair(sample, itemStacks[0]))
			return true;
		this.moveItemStack();
		return false;
	}

	public static boolean isProperPair(ItemStack sample, ItemStack converting) {
		// どちらかが対象外のアイテムならfalse。
		if (!isProperItemStack(sample) || !isProperItemStack(converting))
			return false;
		// sampleがオファレンブロックでconvertingが匠式硬質オファレンならtrue。
		if (Block.getBlockFromItem(sample.getItem()) == OfalenModBlockCore.blockOfalen && OfalenModOreDictCore.isMatchedItemStack(OfalenModOreDictCore.listCreeperOfalenBlock, converting))
			return true;
		// アイテムが一致していないか、メタデータが同じならfalse。
		if (sample.getItem() != converting.getItem() || sample.getItemDamage() == converting.getItemDamage())
			return false;
		// コア以外ならtrue。
		if (sample.getItem() != OfalenModItemCore.coreOfalen)
			return true;
		// コアの時、
		int damageSample = sample.getItemDamage();
		int damageConverting = converting.getItemDamage();
		if (damageSample < 3) {
			// sampleとconvertingの双方が基本色ならtrue。
			return damageConverting < 3;
		} else if (3 < damageSample && damageSample < 7) {
			// sampleとconvertingの双方が基本融合色ならtrue。
			return 3 < damageConverting && damageConverting < 7;
		}
		// sampleとconvertingのどちらかが白または黒ならfalse。
		return false;
	}

	public static boolean isProperItemStack(ItemStack itemStack) {
		Item item = itemStack.getItem();
		if (item instanceof ItemOfalen) {
			if (item == OfalenModItemCore.coreOfalen) {
				if (itemStack.getItemDamage() == 3 || itemStack.getItemDamage() == 7)
					return false;
			}
			return true;
		}
		return Block.getBlockFromItem(item) == OfalenModBlockCore.blockOfalen || OfalenModOreDictCore.isMatchedItemStack(OfalenModOreDictCore.listCreeperOfalenBlock, itemStack);
	}

	@Override
	public void work() {
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
		nbt.setTag(OfalenNBTUtil.SAMPLE, nbt1);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		sample = null;
		if (nbt.hasKey(OfalenNBTUtil.SAMPLE))
			sample = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(OfalenNBTUtil.SAMPLE));
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
		if (slot != 3) {
			super.setInventorySlotContents(slot, itemStack);
			return;
		}
		sample = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}
}
