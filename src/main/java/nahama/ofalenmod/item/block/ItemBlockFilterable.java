package nahama.ofalenmod.item.block;

import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockFilterable extends ItemBlock {
	public ItemBlockFilterable(Block block) {
		super(block);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		OfalenNBTUtil.FilterUtil.onUpdateFilter(itemStack);
	}
}
