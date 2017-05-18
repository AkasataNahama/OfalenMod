package nahama.ofalenmod.block;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockProcessorCasing extends Block {
	private IIcon[] icons;

	public BlockProcessorCasing() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	/** クリエイティブタブにブロックを登録する処理。 */
	@Override
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 7; i++) {
			if (i != 3)
				OfalenUtil.add(list, new ItemStack(item, 1, i));
		}
	}

	/** ドロップアイテムのメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		if ((meta % 8) == 3)
			return 0;
		if ((meta % 8) == 7)
			return 4;
		return meta % 8;
	}

	/** ブロックのテクスチャを登録する処理。 */
	@Override
	public void registerBlockIcons(IIconRegister iicon) {
		icons = new IIcon[7];
		for (int i = 0; i < 7; i++) {
			if (i != 3)
				icons[i] = iicon.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** ブロックのテクスチャを返す。 */
	@Override
	public IIcon getIcon(int side, int meta) {
		return icons[meta];
	}
}
