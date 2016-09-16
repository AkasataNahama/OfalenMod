package nahama.ofalenmod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemList extends Container {

	private InventoryItemList inventory;
	/** リストのインベントリの第一スロットの番号 */
	private static final int index0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号 */
	private static final int index1 = 27;
	/** クイックスロットの第一スロットの番号 */
	private static final int index2 = index1 + 27;
	/** このコンテナの全体のスロット数 */
	private static final int index3 = index2 + 9;

	public ContainerItemList(EntityPlayer player) {
		inventory = new InventoryItemList(player.inventory);
		inventory.openInventory();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new SlotListItemSelected(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
			}
		}

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 144));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		inventory.closeInventory();
	}

	@Override
	public ItemStack slotClick(int slotNumber, int par2, int par3, EntityPlayer player) {
		if (slotNumber - index1 == player.inventory.currentItem + 27) {
			return null;
		}
		ItemStack itemStack = super.slotClick(slotNumber, par2, par3, player);
		if (slotNumber < index1) {
			// リストのスロットをクリックしたなら、スロットを書き換える。
			ItemStack itemStack1 = player.inventory.getItemStack();
			if (itemStack1 == null) {
				inventory.setInventorySlotContents(slotNumber, null);
			} else {
				ItemStack itemStack2 = new ItemStack(itemStack1.getItem(), 1, itemStack1.getItemDamage());
				inventory.setInventorySlotContents(slotNumber, itemStack2);
			}
		}
		return null;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (index0 <= slotNumber && slotNumber < index1) {
				// リストのインベントリならプレイヤーのインベントリに移動。
				if (!this.mergeItemStack(itemStack1, index1, index3, true)) {
					return null;
				}
			} else {
				// このアイテムがあるスロットなら移動しない。
				if (slotNumber - index1 == player.inventory.currentItem + 27)
					return null;
				if (index1 <= slotNumber && slotNumber < index2) {
					// プレイヤーのインベントリならクイックスロットに移動。
					if (!this.mergeItemStack(itemStack1, index2, index3, false)) {
						return null;
					}
				} else if (index2 <= slotNumber && slotNumber < index3 && !this.mergeItemStack(itemStack1, index1, index2, false)) {
					// クイックスロットからプレイヤーのインベントリに移動できなかったら終了。
					return null;
				}
			}

			if (itemStack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if (itemStack1.stackSize == itemStack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, itemStack1);
		}
		return itemStack;
	}

	private static class SlotListItemSelected extends Slot {
		public SlotListItemSelected(IInventory iinventory, int index, int x, int y) {
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
