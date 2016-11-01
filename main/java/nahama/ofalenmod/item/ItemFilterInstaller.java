package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFilterInstaller extends Item {
	public ItemFilterInstaller() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		player.openGui(OfalenModCore.instance, 6, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return itemStack;
	}
}
