package nahama.ofalenmod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemFilter extends ContainerItemBase {
	public ContainerItemFilter(EntityPlayer player) {
		super(player);
	}

	@Override
	protected IInventory createInventory(EntityPlayer player) {
		return new InventoryItemFilter(player.inventory);
	}

	@Override
	protected void initItemSlot() {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new SlotFilterItemSelected(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
			}
		}
	}

	@Override
	public ItemStack slotClick(int slotNumber, int par2, int par3, EntityPlayer player) {
		if (slotNumber - index1 == player.inventory.currentItem + 27) {
			return null;
		}
		ItemStack itemStack = super.slotClick(slotNumber, par2, par3, player);
		if (slotNumber < index1) {
			// フィルターのスロットをクリックしたなら、スロットを書き換える。
			ItemStack itemStack1 = player.inventory.getItemStack();
			if (itemStack1 == null) {
				inventory.setInventorySlotContents(slotNumber, null);
			} else {
				ItemStack itemStack2 = new ItemStack(itemStack1.getItem(), 1, itemStack1.getItemDamage());
				inventory.setInventorySlotContents(slotNumber, itemStack2);
			}
			return null;
		}
		return itemStack;
	}

	@Override
	protected boolean isItemStackEnabled(ItemStack itemStack) {
		return true;
	}

	@Override
	protected boolean transferStackInItemSlot(ItemStack itemStack, int slotNumber) {
		for (int i = 0; i < index1; i++) {
			ItemStack stackInInventory = inventory.getStackInSlot(i);
			if (stackInInventory == null) {
				stackInInventory = itemStack.copy();
				stackInInventory.stackSize = 1;
				inventory.setInventorySlotContents(i, stackInInventory);
				return true;
			} else if (stackInInventory.isItemEqual(itemStack)) {
				return false;
			}
		}
		return false;
	}

	private static class SlotFilterItemSelected extends Slot {
		public SlotFilterItemSelected(IInventory iinventory, int index, int x, int y) {
			super(iinventory, index, x, y);
		}

		/** スロットにアイテムを入れられるか。 */
		@Override
		public boolean isItemValid(ItemStack itemStack) {
			return false;
		}

		/** スロットからアイテムを取り出せるか。 */
		@Override
		public boolean canTakeStack(EntityPlayer player) {
			return false;
		}

		/** スロットのスタック限界を返す。 */
		@Override
		public int getSlotStackLimit() {
			return 1;
		}
	}
}
