package nahama.ofalenmod.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemOfalenBlock extends ItemBlockWithMetadata {

	public ItemOfalenBlock(Block block) {
		super(block, block);
	}

	/**メタデータにより内部名を変える*/
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

}
