package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeSword extends Item {

	public CreativeSword() {
		super();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setMaxStackSize(1);
		this.setFull3D();
	}

	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player) {
		target.setHealth(0);
		return true;
	}

}
