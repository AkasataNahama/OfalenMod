package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemParts extends Item {
	private final byte type;
	protected IIcon[] icons;

	public ItemParts(byte type) {
		this.type = type;
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	/** アイテムのテクスチャを登録する処理。 */
	@Override
	public void registerIcons(IIconRegister register) {
		icons = new IIcon[type];
		for (int i = 0; i < type; i++) {
			icons[i] = register.registerIcon(this.getIconString() + "-" + i);
		}
	}

	/** アイテムのテクスチャを返す。 */
	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}

	/** アイテムをクリエイティブタブに登録する処理。 */
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < type; i++) {
			OfalenUtil.add(list, new ItemStack(this, 1, i));
		}
	}

	/** 内部名を返す。 */
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}
}
