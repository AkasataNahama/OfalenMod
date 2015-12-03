package nahama.ofalenmod.inventory;

import nahama.ofalenmod.item.ItemShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemShield extends Container {

	private InventoryItemShield inventory;
	/** シールドのインベントリの第一スロットの番号 */
	private static final int index0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号 */
	private static final int index1 = 9;
	/** クイックスロットの第一スロットの番号 */
	private static final int index2 = 36;
	/** このコンテナの全体のスロット数 */
	private static final int index3 = 45;

	public ContainerItemShield(EntityPlayer player) {
		inventory = new InventoryItemShield(player.inventory);
		inventory.openInventory();
		int i;
		for (i = 0; i < 9; i++) {
			this.addSlotToContainer(new SlotShieldMaterial(inventory, i, 8 + (i * 18), 36));
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
		if (slotNumber - index1 == player.inventory.currentItem + 27) {
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
			if (index0 <= slotNumber && slotNumber < index1) {
				// シールドのインベントリならプレイヤーのインベントリに移動。
				if (!this.mergeItemStack(itemStack1, index1, index3, true)) {
					return null;
				}
			} else {
				// このアイテムがあるスロットなら移動しない。
				if (slotNumber - index1 == player.inventory.currentItem + 27)
					return null;
				if (ItemShield.isItemMaterial(itemStack1)) {
					// シールドの材料ならシールドのインベントリに移動。
					if (!this.mergeItemStack(itemStack1, index0, index1, false)) {
						return null;
					}
				} else if (index1 <= slotNumber && slotNumber < index2) {
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
				slot.putStack((ItemStack) null);
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

	private static class SlotShieldMaterial extends Slot {

		public SlotShieldMaterial(IInventory iinventory, int index, int x, int y) {
			super(iinventory, index, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack itemStack) {
			return ItemShield.isItemMaterial(itemStack);
		}

	}

}
