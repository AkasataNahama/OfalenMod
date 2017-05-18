package nahama.ofalenmod.block;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockSurveyor extends Block {
	private IIcon[] icons;

	public BlockSurveyor() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	/** ブロックのテクスチャを登録する処理。 */
	@Override
	public void registerBlockIcons(IIconRegister register) {
		icons = new IIcon[6];
		for (int i = 0; i < 6; i++) {
			icons[i] = register.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** ブロックのテクスチャを返す。 */
	@Override
	public IIcon getIcon(int side, int meta) {
		switch (side) {
		// 0, DOWN, Y-, 翠
		case 0:
			return icons[4];
		// 1, UP, Y+, 緑
		case 1:
			return icons[1];
		// 2, NORTH, Z-, 紫
		case 2:
			return icons[5];
		// 3, SOUTH, Z+, 青
		case 3:
			return icons[2];
		// 4, WEST, X-, 橙
		case 4:
			return icons[3];
		// 5, EAST, X+, 赤
		case 5:
			return icons[0];
		}
		return icons[0];
	}
}
