package nahama.ofalenmod.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockOfalenOre extends Block {

	private IIcon[] iicon = new IIcon[4];
	private Random random = new Random();

	public BlockOfalenOre() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHardness(5.0F);
		this.setResistance(7.5F);
		this.setStepSound(Block.soundTypePiston);
		this.setLightLevel(0.6F);
		this.setHarvestLevel("pickaxe", 3);
	}

	/** ドロップするアイテムのインスタンスを返す。 */
	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return OfalenModItemCore.fragmentOfalen;
	}

	/** アイテムのドロップ数を返す。 */
	@Override
	public int quantityDropped(Random random) {
		return OfalenModConfigCore.amountDrop;
	}

	/** 幸運エンチャントを反映したドロップ数を返す。 */
	@Override
	public int quantityDroppedWithBonus(int level, Random random) {
		// BlockOre参照
		// 幸運のレベルが1以上で、ドロップアイテムがこのブロック自身でない場合に適用。
		if (level > 0 && Item.getItemFromBlock(this) != this.getItemDropped(0, random, level)) {
			int i = random.nextInt(level + 2) - 1;
			if (i < 0) {
				i = 0;
			}
			return this.quantityDropped(random) * (i + 1);
		}
		return this.quantityDropped(random);
	}

	/** 経験値のドロップ量を返す。 */
	@Override
	public int getExpDrop(IBlockAccess iBlockAccess, int meta, int fortune) {
		// ダイヤモンドと同様。
		return MathHelper.getRandomIntegerInRange(random, 3, 7);
	}

	/** クリエイティブタブにブロックを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 4; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	/** ドロップアイテムのメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		return meta & 3;
	}

	/** ブロックのテクスチャを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		for (int i = 0; i < 4; i++) {
			this.iicon[i] = iicon.registerIcon(this.getTextureName() + i);
		}
	}

	/** ブロックのテクスチャを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[meta & 3];
	}

}
