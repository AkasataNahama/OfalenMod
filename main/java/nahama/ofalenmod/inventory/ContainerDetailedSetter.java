package nahama.ofalenmod.inventory;

import nahama.ofalenmod.tileentity.TileEntityDetailedSetter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDetailedSetter extends Container {
	private TileEntityDetailedSetter tileEntity;
	/** プレゼントボックスのインベントリの第一スロットの番号。 */
	private static final byte INDEX_0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号。 */
	private static final byte INDEX_1 = 6;
	/** クイックスロットの第一スロットの番号。 */
	private static final byte INDEX_2 = INDEX_1 + 27;
	/** このコンテナの全体のスロット数。 */
	private static final byte INDEX_3 = INDEX_2 + 9;

	public ContainerDetailedSetter(EntityPlayer player, TileEntityDetailedSetter tileEntity) {
		this.tileEntity = tileEntity;
		for (int iy = 0; iy < INDEX_1; iy++) {
			this.addSlotToContainer(new Slot(tileEntity, iy, 8, 18 + (iy * 18)));
		}
		for (int iy = 0; iy < 3; iy++) {
			for (int ix = 0; ix < 9; ix++) {
				this.addSlotToContainer(new Slot(player.inventory, ix + (iy * 9) + 9, 8 + (ix * 18), 140 + (iy * 18)));
			}
		}
		for (int ix = 0; ix < 9; ix++) {
			this.addSlotToContainer(new Slot(player.inventory, ix, 8 + (ix * 18), 198));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (INDEX_0 <= slotNumber && slotNumber < INDEX_1) {
				// TileEntityのインベントリならプレイヤーのインベントリに移動。
				if (!this.mergeItemStack(itemStack1, INDEX_1, INDEX_3, true))
					return null;
			} else {
				// TileEntityのインベントリに移動。
				if (!this.mergeItemStack(itemStack1, INDEX_0, INDEX_1, false))
					return null;
				//				if (INDEX_1 <= slotNumber && slotNumber < INDEX_2) {
				//					// プレイヤーのインベントリならクイックスロットに移動。
				//					if (!this.mergeItemStack(itemStack1, INDEX_2, INDEX_3, false))
				//						return null;
				//				} else if (INDEX_2 <= slotNumber && slotNumber < INDEX_3 && !this.mergeItemStack(itemStack1, INDEX_1, INDEX_2, false)) {
				//					// クイックスロットからプレイヤーのインベントリに移動できなかったら終了。
				//					return null;
				//				}
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
}
