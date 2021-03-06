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
import net.minecraft.world.World;

import java.util.List;

public class BlockProcessor extends Block {
	private IIcon[] icons;

	public BlockProcessor() {
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
		for (int i = 0; i < 3; i++) {
			OfalenUtil.add(list, new ItemStack(item, 1, i));
		}
	}

	/** ドロップアイテムのメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		return meta % 4;
	}

	/** ブロックのテクスチャを登録する処理。 */
	@Override
	public void registerBlockIcons(IIconRegister iicon) {
		icons = new IIcon[3];
		for (int i = 0; i < 3; i++) {
			icons[i] = iicon.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** ブロックのテクスチャを返す。 */
	@Override
	public IIcon getIcon(int side, int meta) {
		return icons[meta % 4];
	}

	/** シルクタッチで回収できるか。 */
	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	/** メタデータを更新して返す。 */
	public int updateMetadata(World world, int x, int y, int z) {
		int meta = this.getUpdatedMeta(world, x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		return meta;
	}

	/** 周囲のブロックを調査し、メタデータを返す。 */
	private int getUpdatedMeta(World world, int x, int y, int z) {
		// 無効時のメタデータ。
		int grade = world.getBlockMetadata(x, y, z) % 4;
		for (int iy = -1; iy <= 1; iy++) {
			for (int iz = -1; iz <= 1; iz++) {
				for (int ix = -1; ix <= 1; ix++) {
					// 中央なら判定せず次へ。
					if (ix == 0 && iy == 0 && iz == 0)
						continue;
					// 処理装置筐体でないなら無効。
					if (!(world.getBlock(x + ix, y + iy, z + iz) instanceof BlockProcessorCasing))
						return grade;
					int meta = world.getBlockMetadata(x + ix, y + iy, z + iz);
					if (Math.abs(ix) != 1 || Math.abs(iy) != 1 || Math.abs(iz) != 1) {
						// 角以外で、Grade未満もしくは固定ブロックなら無効。
						if (meta < grade || 3 < meta)
							return grade;
					} else {
						// 角で、Grade未満もしくは固定ブロックでないなら無効。
						if (meta - 4 < grade || 7 < meta)
							return grade;
					}
				}
			}
		}
		// 有効ならGrade + 4を返す。
		return grade + 4;
	}
}
