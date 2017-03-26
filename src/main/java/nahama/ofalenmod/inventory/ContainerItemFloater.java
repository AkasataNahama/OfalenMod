package nahama.ofalenmod.inventory;

import nahama.ofalenmod.item.ItemFloater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemFloater extends Container {
	private InventoryItemFloater inventory;
	/** フローターのインベントリの第一スロットの番号。 */
	private static final byte INDEX_0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号。 */
	private static final byte INDEX_1 = 9;
	/** クイックスロットの第一スロットの番号。 */
	private static final byte INDEX_2 = 36;
	/** このコンテナの全体のスロット数。 */
	private static final byte INDEX_3 = 45;

	public ContainerItemFloater(EntityPlayer player) {
		inventory = new InventoryItemFloater(player.inventory);
		inventory.openInventory();
		int i;
		for (i = 0; i < 9; i++) {
			this.addSlotToContainer(new SlotFloaterMaterial(inventory, i, 8 + (i * 18), 36));
		}
		for (i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
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
		if (slotNumber - INDEX_1 == player.inventory.currentItem + 27) {
			return null;
		}
		return super.slotClick(slotNumber, par2, par3, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (INDEX_0 <= slotNumber && slotNumber < INDEX_1) {
				// フローターのインベントリならプレイヤーのインベントリに移動。
				if (!this.mergeItemStack(itemStack1, INDEX_1, INDEX_3, true)) {
					return null;
				}
			} else {
				// このアイテムがあるスロットなら移動しない。
				if (slotNumber - INDEX_1 == player.inventory.currentItem + 27)
					return null;
				if (ItemFloater.isItemMaterial(itemStack1)) {
					// フローターの材料ならフローターのインベントリに移動。
					if (!this.mergeItemStack(itemStack1, INDEX_0, INDEX_1, false)) {
						return null;
					}
				} else if (INDEX_1 <= slotNumber && slotNumber < INDEX_2) {
					// プレイヤーのインベントリならクイックスロットに移動。
					if (!this.mergeItemStack(itemStack1, INDEX_2, INDEX_3, false)) {
						return null;
					}
				} else if (INDEX_2 <= slotNumber && slotNumber < INDEX_3 && !this.mergeItemStack(itemStack1, INDEX_1, INDEX_2, false)) {
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

	private static class SlotFloaterMaterial extends Slot {
		public SlotFloaterMaterial(IInventory iinventory, int index, int x, int y) {
			super(iinventory, index, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack itemStack) {
			return ItemFloater.isItemMaterial(itemStack);
		}
	}
}
