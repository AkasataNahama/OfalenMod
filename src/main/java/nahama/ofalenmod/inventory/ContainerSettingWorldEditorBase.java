package nahama.ofalenmod.inventory;

import nahama.ofalenmod.tileentity.TileEntityWorldEditorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSettingWorldEditorBase extends Container {
	protected TileEntityWorldEditorBase tileEntity;

	public ContainerSettingWorldEditorBase(EntityPlayer player, TileEntityWorldEditorBase tileEntity) {
		this.tileEntity = tileEntity;
		for (int iy = 0; iy < 3; iy++) {
			for (int ix = 0; ix < 9; ix++) {
				this.addSlotToContainer(new Slot(player.inventory, ix + (iy * 9) + 9, 8 + (ix * 18), 140 + (iy * 18) + 18));
			}
		}
		for (int ix = 0; ix < 9; ix++) {
			this.addSlotToContainer(new Slot(player.inventory, ix, 8 + (ix * 18), 198 + 18));
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
			if (slotNumber < 27) {
				// プレイヤーのインベントリならクイックスロットに移動。
				if (!this.mergeItemStack(itemStack1, 27, 36, false))
					return null;
			} else if (slotNumber < 36 && !this.mergeItemStack(itemStack1, 0, 27, false)) {
				// クイックスロットからプレイヤーのインベントリに移動できなかったら終了。
				return null;
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
