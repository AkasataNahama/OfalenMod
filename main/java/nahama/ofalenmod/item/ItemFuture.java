package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemFuture extends Item {
	public ItemFuture() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setMaxStackSize(1);
	}

	/** アップデート時の処理。 */
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		// NBTを持っていなければ空のものを持たせ、Durationがあれば減らす。
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		if (!itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_IRREPARABLE))
			itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_IRREPARABLE, true);
		byte interval = itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (interval > 0)
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) (interval - 1));
	}
}
