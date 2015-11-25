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

public class EmptyBall extends Item {

	private IIcon[] iicon;

	public EmptyBall () {
		super ();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	/**メタデータにより返すIIconを変える*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta];
	}

	/**メタデータを返す。*/
	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	/**メタデータにより内部名を変える*/
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

	/**クリエイティブタブにメタデータ違いのアイテムを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 3; i ++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	/**テクスチャを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iicon) {
		this.iicon = new IIcon[3];
		for (int i = 0; i < 3; i ++) {
			this.iicon[i] = iicon.registerIcon(this.getIconString() + i);
		}
	}

}
