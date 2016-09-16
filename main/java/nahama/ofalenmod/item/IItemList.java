package nahama.ofalenmod.item;

import net.minecraft.item.ItemStack;

public interface IItemList {

	boolean isBlackList(ItemStack list);

	boolean isEnabledItem(ItemStack list, ItemStack checking);

}
