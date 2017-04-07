package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemExpCrystal extends Item {
	public ItemExpCrystal() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		int amount = 1;
		if (player.isSneaking())
			amount *= 10;
		if (amount > itemStack.stackSize)
			amount = itemStack.stackSize;
		if (!world.isRemote) {
			// サーバー側なら、経験値オーブをスポーンさせる。
			EntityXPOrb entity = new EntityXPOrb(world, player.posX, player.posY, player.posZ, amount);
			world.spawnEntityInWorld(entity);
		}
		if (!player.capabilities.isCreativeMode) {
			itemStack.stackSize -= amount;
		}
		return itemStack;
	}
}
