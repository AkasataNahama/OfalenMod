package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemEmptyBall extends Item {
	private IIcon[] icons;

	public ItemEmptyBall() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public void registerIcons(IIconRegister iicon) {
		icons = new IIcon[3];
		for (int i = 0; i < 3; i++) {
			icons[i] = iicon.registerIcon(this.getIconString() + "-" + (i + 1) + "-1");
		}
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}

	@Override
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 3; i++) {
			OfalenUtil.add(list, new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}
}
