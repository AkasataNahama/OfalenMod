package nahama.ofalenmod.inventory;

import nahama.ofalenmod.tileentity.TileEntityCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerBlockCollector extends ContainerWorldEditorBase {
	public ContainerBlockCollector(EntityPlayer player, TileEntityCollector tileEntity) {
		super(player, tileEntity);
		// 収集機のインベントリの第一スロットの番号。
		INDEX_1 = (byte) (tileEntity.getSizeInventory() - tileEntity.getSizeOwnInventory());
	}

	@Override
	protected void addInventory(IInventory inventory) {
		for (int iy = 0; iy < 6; iy++) {
			for (int ix = 0; ix < 9; ix++) {
				this.addSlotToContainer(new SlotNotPuttable(inventory, ix + (iy * 9), 8 + (ix * 18), 18 + (iy * 18)));
			}
		}
	}

	@Override
	protected void addPlayerInventory(EntityPlayer player) {
		for (int iy = 0; iy < 3; iy++) {
			for (int ix = 0; ix < 9; ix++) {
				this.addSlotToContainer(new Slot(player.inventory, ix + (iy * 9) + 9, 8 + (ix * 18), 140 + (iy * 18)));
			}
		}
		for (int ix = 0; ix < 9; ix++) {
			this.addSlotToContainer(new Slot(player.inventory, ix, 8 + (ix * 18), 198));
		}
	}
}
