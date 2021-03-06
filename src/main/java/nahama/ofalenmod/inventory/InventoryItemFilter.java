package nahama.ofalenmod.inventory;

import nahama.ofalenmod.item.ItemFilter;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryItemFilter extends InventoryItemBase {
	public InventoryItemFilter(InventoryPlayer inventory) {
		super(inventory);
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 27;
	}

	@Override
	public String getInventoryName() {
		return "container.ofalen.filterItem";
	}

	/** インベントリが開かれた時の処理。 */
	@Override
	public void openInventory() {
		// アイテムを読み込む。
		itemStacks = new ItemStack[this.getSizeInventory()];
		NBTTagList nbtTagList = FilterUtil.getSelectingItemList(FilterUtil.getFilterTag(inventoryPlayer.getCurrentItem()));
		for (int i = 0; i < 27 && i < nbtTagList.tagCount(); i++) {
			NBTTagCompound nbt = nbtTagList.getCompoundTagAt(i);
			if (nbt == null)
				continue;
			itemStacks[i] = ItemStack.loadItemStackFromNBT(nbt);
		}
	}

	/** インベントリが閉じられた時の処理。 */
	@Override
	public void closeInventory() {
		ItemStack current = inventoryPlayer.getCurrentItem();
		if (!(current.getItem() instanceof ItemFilter))
			return;
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < 27; i++) {
			if (itemStacks[i] == null)
				continue;
			NBTTagCompound nbt = new NBTTagCompound();
			new ItemStack(itemStacks[i].getItem(), 1, itemStacks[i].getItemDamage()).writeToNBT(nbt);
			if (OfalenNBTUtil.containsNBT(nbtTagList, nbt))
				continue;
			nbtTagList.appendTag(nbt);
		}
		itemStacks = new ItemStack[this.getSizeInventory()];
		FilterUtil.getFilterTag(current).setTag(FilterUtil.SELECTING, nbtTagList);
	}
}
