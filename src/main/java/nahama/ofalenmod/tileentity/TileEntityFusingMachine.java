package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.block.BlockOfalen;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.item.ItemOfalen;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileEntityFusingMachine extends TileEntityGradedMachineBase {
	@Override
	protected ItemStack getFuelStack() {
		return itemStacks[3];
	}

	@Override
	protected void setFuelStack(ItemStack stack) {
		itemStacks[3] = stack;
	}

	@Override
	protected boolean hasMaterialOrFuel() {
		byte numEmptySlot = 0;
		for (int i = 0; i < 3; i++) {
			if (itemStacks[i] == null)
				numEmptySlot++;
		}
		return numEmptySlot < 2 && itemStacks[3] != null;
	}

	/** 作業可能かどうか。 */
	@Override
	protected boolean canWork() {
		Item item = null;
		for (int i = 0; i < 3; i++) {
			if (itemStacks[i] == null)
				continue;
			if (item == null)
				item = itemStacks[i].getItem();
			else if (item != itemStacks[i].getItem())
				return false;
		}
		int color = this.getResultColor();
		if (color < 0) {
			return false;
		}
		int amount = this.getResultAmount(color, item);
		if (amount < 0) {
			return false;
		}
		ItemStack result = new ItemStack(item, amount, color);
		if (itemStacks[4] != null) {
			// 完成品スロットに空きがないなら不可。
			if (!itemStacks[4].isItemEqual(result))
				return false;
			int size = itemStacks[4].stackSize + result.stackSize;
			if (size > this.getInventoryStackLimit() || size > itemStacks[4].getMaxStackSize())
				return false;
		}
		return true;
	}

	/** 完成品の色を返す。 */
	private int getResultColor() {
		int flag = 0;
		for (int i = 0; i < 3; i++) {
			if (itemStacks[i] == null) {
				continue;
			}
			if (this.isFusableOfalen(itemStacks[i].getItem())) {
				flag += 1 << itemStacks[i].getItemDamage();
			} else {
				return -1;
			}
		}
		switch (flag) {
		// 00000011 赤・緑→橙
		case 3:
			return 4;
		// 00000101 赤・青→紫
		case 5:
			return 6;
		// 0000,0110 緑・青→翠
		case 6:
			return 5;
		// 00000111 赤・緑・青→白
		case 7:
			return 3;
		// 01110000 橙・翠・紫→黒
		case 112:
			return 7;
		default:
			return -1;
		}
	}

	/** 完成品の量を返す。 */
	private int getResultAmount(int color, Item item) {
		if (item == OfalenModItemCore.coreOfalen)
			return 1;
		if (3 < color && color < 7)
			return 2;
		if (color == 3 || color == 7)
			return 3;
		return -1;
	}

	/** 融合可能な種類かどうか。 */
	public boolean isFusableOfalen(Item item) {
		return item instanceof ItemOfalen || Block.getBlockFromItem(item) instanceof BlockOfalen;
	}

	/** 作業にかかる時間を返す。 */
	@Override
	protected int getMaxWorkingTimeBase() {
		return OfalenModConfigCore.timeFusing;
	}

	/** 作業時の処理。 */
	@Override
	public void work() {
		Item item = null;
		for (int i = 0; i < 3; i++) {
			if (itemStacks[i] == null)
				continue;
			item = itemStacks[i].getItem();
			break;
		}
		int color = this.getResultColor();
		int amount = this.getResultAmount(color, item);
		ItemStack result = new ItemStack(item, amount, color);
		// 完成品スロットに融合結果を代入/追加する。
		if (itemStacks[4] == null) {
			itemStacks[4] = result;
		} else if (itemStacks[4].isItemEqual(result)) {
			itemStacks[4].stackSize += amount;
		}
		// 変換前スロットのスタック数を減らす。
		for (int i = 0; i < 3; i++) {
			if (itemStacks[i] == null)
				continue;
			--itemStacks[i].stackSize;
			if (itemStacks[i].stackSize < 1) {
				itemStacks[i] = null;
			}
		}
		this.updateGrade();
	}

	/** 材料スロットから完成品スロットにアイテムを移動する。 */
	@Override
	protected void moveItemStack() {
	}

	@Override
	public int getSizeInventory() {
		return 5;
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemStacks[slot];
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? customName : "container.ofalen.machine.fusing-" + grade;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		if (slot == 4)
			return false;
		if (slot == 3)
			return this.isItemFuel(itemStack);
		return this.isFusableOfalen(itemStack.getItem()) && slot == (itemStack.getItemDamage() % 4);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? new int[] { 4, 3 } : (side == 1 ? new int[] { 0, 1, 2 } : new int[] { 3 });
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return side != 0 || slot != 3 || itemStack.getItem() == Items.bucket;
	}
}
