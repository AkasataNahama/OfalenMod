package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemCreativeSword extends ItemSword {
	public ItemCreativeSword(ToolMaterial material) {
		super(material);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player) {
		target.setHealth(0);
		return true;
	}
}
