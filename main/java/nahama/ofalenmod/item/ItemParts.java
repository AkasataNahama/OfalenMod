package nahama.ofalenmod.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemParts extends Item {

	protected IIcon[] iicon;
	private final int type;

	public ItemParts(int type) {
		super();
		this.type = type;
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	/** アイテムのテクスチャを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		this.iicon = new IIcon[type];
		for (int i = 0; i < type; i++) {
			this.iicon[i] = register.registerIcon(this.getIconString() + "-" + i);
		}
	}

	/** アイテムのテクスチャを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta];
	}

	/** アイテムをクリエイティブタブに登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < type; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	/** メタデータを返す。 */
	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	/** 内部名を返す。 */
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

}
