package nahama.ofalenmod.item;

import java.util.List;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Parts extends Item {

	protected IIcon[] iicon;
	private final int type;

	public Parts (int type) {
		super ();
		this.type = type;
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	/**メタデータ違いのテクスチャを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		this.iicon = new IIcon[type];
		for (int i = 0; i < type; i ++) {
			this.iicon[i] =  register.registerIcon(this.getIconString() + "-" + i);
		}
	}

	/**メタデータにより返すIIconを変える*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta];
	}

	/**メタデータ違いのアイテムを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < type; i ++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	/**メタデータを返す*/
	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	/**メタデータにより内部名を変える*/
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

}
