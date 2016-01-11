package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.core.OfalenModConfigCore;

public class TileEntityRepairMachine extends TileEntityGradedMachineBase {

	/** インベントリ名を返す。 */
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? customName : "container.OfalenMod.repairmachine-" + grade;
	}

	@Override
	protected int getWorkTime() {
		return OfalenModConfigCore.timeRepair * (4 - grade) / 4;
	}

	@Override
	protected boolean canWork() {
		// 修繕前スロットが空なら不可。
		if (itemStacks[0] == null)
			return false;
		// 修繕前スロットのアイテムの耐久値が減っているなら、
		if (itemStacks[0].isItemDamaged()) {
			// NBTを持っていないか、NotRepairableがfalseなら可。
			if (!itemStacks[0].hasTagCompound() || !itemStacks[0].getTagCompound().getBoolean("NotRepairable"))
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
