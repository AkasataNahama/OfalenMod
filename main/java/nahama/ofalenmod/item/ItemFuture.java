package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemFuture extends Item {

	public ItemFuture() {
		super();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setMaxStackSize(1);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		byte duration = itemStack.getTagCompound().getByte("Duration");
		if (duration > 0)
			itemStack.getTagCompound().setByte("Duration", (byte) (duration - 1));
	}

}
