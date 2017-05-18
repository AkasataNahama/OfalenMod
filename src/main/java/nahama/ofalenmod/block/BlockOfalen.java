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

public class BlockOfalen extends Block {
	private IIcon[] icons;

	public BlockOfalen() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHardness(7.5F);
		this.setResistance(15.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setLightLevel(1.0F);
		this.setHarvestLevel("pickaxe", 3);
	}

	/** クリエイティブタブにブロックを登録する処理。 */
	@Override
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 8; i++) {
			OfalenUtil.add(list, new ItemStack(item, 1, i));
		}
	}

	/** ドロップアイテムのメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		return meta % 8;
	}

	/** ブロックのテクスチャを登録する処理。 */
	@Override
	public void registerBlockIcons(IIconRegister register) {
		icons = new IIcon[8];
		for (int i = 0; i < 8; i++) {
			icons[i] = register.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** ブロックのテクスチャを返す。 */
	@Override
	public IIcon getIcon(int side, int meta) {
		return icons[meta % 8];
	}
}
