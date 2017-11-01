package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.util.OfalenNBTUtil;

public class TileEntityRepairingMachine extends TileEntityGradedMachineBase {
	/** インベントリ名を返す。 */
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? customName : "container.ofalen.machine.repairing-" + grade;
	}

	@Override
	protected int getMaxWorkingTime() {
		return OfalenModConfigCore.timeRepairing;
	}

	@Override
	protected boolean canWork() {
		// 修繕前スロットが空なら不可。
		if (itemStacks[0] == null)
			return false;
		// 修繕前スロットのアイテムの耐久値が減っているなら、
		if (itemStacks[0].isItemDamaged()) {
			// NBTを持っていないか、NotRepairableがfalseなら可。
			if (!itemStacks[0].hasTagCompound() || !itemStacks[0].getTagCompound().getBoolean(OfalenNBTUtil.IS_IRREPARABLE))
				return true;
		}
		this.moveItemStack();
		return false;
	}

	@Override
	public void work() {
		// 修繕前スロットのアイテムの耐久値を回復。
		itemStacks[0].setItemDamage(itemStacks[0].getItemDamage() - 1);
		// ダメージがなくなったら移動。
		if (itemStacks[0].getItemDamage() == 0) {
			this.moveItemStack();
		}
		this.updateGrade();
	}
}
