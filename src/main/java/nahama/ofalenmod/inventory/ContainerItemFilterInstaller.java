package nahama.ofalenmod.inventory;

import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemFilterInstaller extends Container {
	/** 出力スロットのインベントリ。 */
	private IInventory outputSlot = new InventoryCraftResult();
	/** 入力スロットのインベントリ。 */
	private IInventory inputSlots = new InventoryBasic("ItemFilterInstaller", true, 2) {
		public void markDirty() {
			super.markDirty();
			ContainerItemFilterInstaller.this.onCraftMatrixChanged(this);
		}
	};

	public ContainerItemFilterInstaller(EntityPlayer player) {
		this.addSlotToContainer(new Slot(inputSlots, 0, 56, 35));
		this.addSlotToContainer(new Slot(inputSlots, 1, 83, 17));
		this.addSlotToContainer(new Slot(outputSlot, 2, 116, 35) {
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			public boolean canTakeStack(EntityPlayer player1) {
				return this.getHasStack();
			}

			public void onPickupFromSlot(EntityPlayer player1, ItemStack stack) {
				ContainerItemFilterInstaller.this.inputSlots.setInventorySlotContents(0, null);
			}
		});
		// プレイヤーインベントリのスロットを追加する。
		for (int iy = 0; iy < 3; iy++) {
			for (int ix = 0; ix < 9; ix++) {
				this.addSlotToContainer(new Slot(player.inventory, ix + iy * 9 + 9, 8 + ix * 18, 84 + iy * 18));
			}
		}
		for (int ix = 0; ix < 9; ix++) {
			this.addSlotToContainer(new Slot(player.inventory, ix, 8 + ix * 18, 142));
		}
	}

	/** スロットが更新された時の処理。 */
	public void onCraftMatrixChanged(IInventory inventory) {
		super.onCraftMatrixChanged(inventory);
		if (inventory == inputSlots)
			this.updateInstallerOutput();
	}

	/** インストーラーの出力を更新する。 */
	private void updateInstallerOutput() {
		ItemStack stackTarget = inputSlots.getStackInSlot(0);
		ItemStack stackModel = inputSlots.getStackInSlot(1);
		if (stackTarget == null || !OfalenNBTUtil.FilterUtil.isAvailableFilterTag(stackTarget)) {
			// 対象が無効なら出力は無し。
			outputSlot.setInventorySlotContents(0, null);
		} else {
			ItemStack stackOutput = stackTarget.copy();
			if (stackModel == null || !OfalenNBTUtil.FilterUtil.isAvailableFilterTag(stackModel)) {
				// 見本が無効なら適用対象を初期化する。
				OfalenNBTUtil.FilterUtil.initFilterTag(stackOutput);
			} else {
				// 見本のフィルターを適用対象にコピーする。
				OfalenNBTUtil.FilterUtil.copyFilterTag(OfalenNBTUtil.FilterUtil.getFilterTag(stackOutput), OfalenNBTUtil.FilterUtil.getFilterTag(stackModel));
			}
			outputSlot.setInventorySlotContents(0, stackOutput);
		}
	}

	/** GUIが閉じられた時の処理。 */
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (!player.worldObj.isRemote) {
			for (int i = 0; i < inputSlots.getSizeInventory(); i++) {
				ItemStack stack = inputSlots.getStackInSlotOnClosing(i);
				if (stack != null)
					player.dropPlayerItemWithRandomChoice(stack, false);
			}
		}
	}

	/** プレイヤーがGUIを開いていられるか。 */
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack slotClick(int slotNumber, int par2, int par3, EntityPlayer player) {
		if (slotNumber - 3 == player.inventory.currentItem + 27)
			return null;
		return super.slotClick(slotNumber, par2, par3, player);
	}

	/** プレイヤーがスニーククリックした時の処理。 */
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(slotNum);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			// このアイテムがあるスロットなら移動しない。
			if (slotNum - 3 == player.inventory.currentItem + 27)
				return null;
			if (slotNum == 2) {
				if (!this.mergeItemStack(stack1, 3, 39, true))
					return null;
				slot.onSlotChange(stack1, stack);
			} else if (slotNum != 0 && slotNum != 1) {
				if (slotNum < 39 && OfalenNBTUtil.FilterUtil.isAvailableFilterTag(stack1) && !this.mergeItemStack(stack1, 0, 2, false))
					return null;
			} else if (!this.mergeItemStack(stack1, 3, 39, false)) {
				return null;
			}
			if (stack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if (stack1.stackSize == stack.stackSize)
				return null;
			slot.onPickupFromSlot(player, stack1);
		}
		return stack;
	}
}
