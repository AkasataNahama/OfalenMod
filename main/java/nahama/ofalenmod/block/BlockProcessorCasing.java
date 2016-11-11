package nahama.ofalenmod.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
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
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 7; i++) {
			if (i != 3)
				list.add(new ItemStack(item, 1, i));
		}
	}

	/** ドロップアイテムのメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		if ((meta & 7) == 3)
			return 0;
		if ((meta & 7) == 7)
			return 4;
		return meta & 7;
	}

	/** ブロックのテクスチャを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		icons = new IIcon[7];
		for (int i = 0; i < 7; i++) {
			if (i != 3)
				icons[i] = iicon.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** ブロックのテクスチャを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons[meta];
	}
}
