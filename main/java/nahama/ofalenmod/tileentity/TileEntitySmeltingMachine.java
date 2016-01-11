package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.recipe.OfalenSmeltingRecipes;
import net.minecraft.item.ItemStack;

public class TileEntitySmeltingMachine extends TileEntityGradedMachineBase {

	/** インベントリ名を返す。 */
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? customName : "container.OfalenMod.smeltingmachine-" + grade;
	}

	@Override
	protected int getWorkTime() {
		return OfalenModConfigCore.timeSmelting * (4 - grade) / 4;
	}

	@Override
	protected boolean canWork() {
		// 製錬前スロットが空なら不可。
		if (itemStacks[0] == null)
			return false;
		boolean flag = true;
		// 製錬レシピに登録されていて、完成品スロットに空きがあるなら可。
		ItemStack itemStack = OfalenSmeltingRecipes.getInstance().getSmeltingResultFromGrade(itemStacks[0], grade);
		if (itemStack == null)
			flag = false;
		if (flag && itemStacks[2] == null)
			return true;
		if (flag && !itemStacks[2].isItemEqual(itemStack))
			flag = false;
		if (flag) {
			int result = itemStacks[2].stackSize + itemStack.stackSize;
			if (result <= this.getInventoryStackLimit() && result <= itemStacks[2].getMaxStackSize()) {
				return true;
			}
		}
		this.moveItemStack();
		return false;
	}

	@Override
	public void work() {
		// 製錬結果を取得し、完成品スロットに代入/追加する。
		ItemStack itemStack = OfalenSmeltingRecipes.getInstance().getSmeltingResultFromGrade(itemStacks[0], grade);
		if (itemStacks[2] == null) {
			itemStacks[2] = itemStack;
		} else if (itemStacks[2].isItemEqual(itemStack)) {
			itemStacks[2].stackSize += itemStack.stackSize;
		}
		// 製錬前スロットのアイテム数を減らす
		--itemStacks[0].stackSize;
		if (itemStacks[0].stackSize <= 0) {
			itemStacks[0] = null;
		}
		this.updateGrade();
	}

}
