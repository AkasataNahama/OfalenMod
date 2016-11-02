package nahama.ofalenmod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerItemBase extends Container {
	protected IInventory inventory;
	/** アイテムのインベントリの第一スロットの番号。 */
	protected static byte index0;
	/** プレイヤーのインベントリの第一スロットの番号。 */
	protected static byte index1;
	/** クイックスロットの第一スロットの番号。 */
	protected static byte index2;
	/** このコンテナの全体のスロット数。 */
	protected static byte index3;

	public ContainerItemBase(EntityPlayer player) {
		inventory = this.createInventory(player);
		inventory.openInventory();
		this.initIndexes();
		this.initItemSlot();
		this.initPlayerSlot(player);
	}

	protected abstract IInventory createInventory(EntityPlayer player);

	protected void initIndexes() {
		index0 = 0;
		index1 = 27;
		index2 = (byte) (index1 + 27);
		index3 = (byte) (index2 + 9);
	}

	protected void initItemSlot() {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new SlotFiltered(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
			}
		}
	}

	protected void initPlayerSlot(EntityPlayer player) {
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
		return super.slotClick(slotNumber, par2, par3, player);
	}

	/** Shift+左クリック時の処理。 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (index0 <= slotNumber && slotNumber < index1) {
				// アイテムのインベントリならプレイヤーのインベントリに移動。
				if (!this.mergeItemStack(itemStack1, index1, index3, true)) {
					return null;
				}
			} else {
				// このアイテムがあるスロットなら移動しない。
				if (slotNumber - index1 == player.inventory.currentItem + 27)
					return null;
				if (this.isItemStackEnabled(itemStack1)) {
					if (!this.transferStackInItemSlot(itemStack1, slotNumber)) {
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

	/** 「スロットに入れられるか」と「Shift+左クリック時の処理」に使う。 */
	protected abstract boolean isItemStackEnabled(ItemStack itemStack);

	/**
	 * プレイヤーのインベントリかクイックスロットでShift+左クリックされた時に移動する処理。
	 * @return 更新が必要か。
	 */
	protected boolean transferStackInItemSlot(ItemStack itemStack, int slotNumber) {
		// 指定されたアイテムならアイテムのインベントリへ移動。
		return this.mergeItemStack(itemStack, index0, index1, false);
	}

	private class SlotFiltered extends Slot {
		public SlotFiltered(IInventory iinventory, int index, int x, int y) {
			super(iinventory, index, x, y);
		}

		/** スロットにアイテムを入れられるか。 */
		@Override
		public boolean isItemValid(ItemStack itemStack) {
			return isItemStackEnabled(itemStack);
		}
	}
}
