package nahama.ofalenmod.inventory;

import nahama.ofalenmod.tileentity.TileEntityWorldEditorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWorldEditorBase extends Container {
	/** 設置機・収集機のインベントリの第一スロットの番号。通常は{@link #INDEX_2}と同値。 */
	protected byte INDEX_1;
	/** プレイヤーのインベントリの第一スロットの番号。 */
	protected byte INDEX_2;
	/** クイックスロットの第一スロットの番号。 */
	@SuppressWarnings("WeakerAccess")
	protected byte INDEX_3;
	/** このコンテナの全体のスロット数。 */
	@SuppressWarnings("WeakerAccess")
	protected byte INDEX_4;
	private TileEntityWorldEditorBase tileEntity;
	private short lastRemainingEnergy;

	public ContainerWorldEditorBase(EntityPlayer player, TileEntityWorldEditorBase tileEntity) {
		this.tileEntity = tileEntity;
		INDEX_1 = (byte) tileEntity.getSizeInventory();
		INDEX_2 = INDEX_1;
		INDEX_3 = (byte) (INDEX_2 + 27);
		INDEX_4 = (byte) (INDEX_3 + 9);
		this.addInventory(tileEntity);
		this.addPlayerInventory(player);
	}

	protected void addInventory(IInventory inventory) {
		for (int iy = 0; iy < 3; ++iy) {
			for (int ix = 0; ix < 9; ++ix) {
				this.addSlotToContainer(new Slot(inventory, ix + iy * 9, 8 + ix * 18, 18 + iy * 18));
			}
		}
	}

	protected void addPlayerInventory(EntityPlayer player) {
		// プレイヤーのインベントリ。
		for (int iy = 0; iy < 3; ++iy) {
			for (int ix = 0; ix < 9; ++ix) {
				this.addSlotToContainer(new Slot(player.inventory, ix + iy * 9 + 9, 8 + ix * 18, 86 + iy * 18));
			}
		}
		// クイックスロット。
		for (int ix = 0; ix < 9; ++ix) {
			this.addSlotToContainer(new Slot(player.inventory, ix, 8 + ix * 18, 144));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	@Override
	public void addCraftingToCrafters(ICrafting iCrafting) {
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, tileEntity.getRemainingEnergy());
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		short energy = tileEntity.getRemainingEnergy();
		if (lastRemainingEnergy != energy) {
			for (Object crafter : crafters) {
				((ICrafting) crafter).sendProgressBarUpdate(this, 0, energy);
			}
			lastRemainingEnergy = energy;
		}
	}

	@Override
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0)
			tileEntity.setRemainingEnergy((short) par2);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		// スロットが空なら終了。
		if (slot == null || !slot.getHasStack())
			return null;
		ItemStack slotStack = slot.getStack();
		ItemStack copyStack = slotStack.copy();
		if (slotNumber < INDEX_2) {
			// TileEntityのスロットなら、プレイヤーのインベントリへ移動。
			if (!this.mergeItemStack(slotStack, INDEX_2, INDEX_4, true))
				return null;
		} else {
			// 設置機用。通常は燃料ならTileEntityのスロットへ移動。設置機の場合は設置可能ブロックも移動。
			int type = this.getTypeStackTransfer(slotStack);
			if (type != 0) {
				if (!this.mergeStackWithType(type, slotStack))
					return null;
			} else {
				if (INDEX_2 <= slotNumber && slotNumber < INDEX_3) {
					// プレイヤーのインベントリなら、クイックスロットへ移動。
					if (!this.mergeItemStack(slotStack, INDEX_3, INDEX_4, false))
						return null;
				} else if (INDEX_3 <= slotNumber && slotNumber < INDEX_4) {
					// クイックスロットなら、プレイヤーのインベントリへ移動。
					if (!this.mergeItemStack(slotStack, INDEX_2, INDEX_3, false))
						return null;
				}
			}
		}
		// スロットを更新。
		if (slotStack.stackSize == 0) {
			slot.putStack(null);
		} else {
			slot.onSlotChanged();
		}
		if (slotStack.stackSize == copyStack.stackSize)
			return null;
		slot.onPickupFromSlot(player, slotStack);
		return copyStack;
	}

	/** プレイヤーのインベントリがスニークキー+左クリックされた時、どう移動するか。 */
	protected int getTypeStackTransfer(ItemStack itemStack) {
		if (tileEntity.isItemFuel(itemStack))
			return 1;
		return 0;
	}

	/** プレイヤーのインベントリがスニークキー+左クリックされた時の移動処理。 */
	protected boolean mergeStackWithType(int type, ItemStack itemStack) {
		return this.mergeItemStack(itemStack, 0, INDEX_1, false);
	}
}
