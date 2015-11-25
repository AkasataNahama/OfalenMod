package nahama.ofalenmod.block;

import java.util.List;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OfalenBlock extends Block {

	private IIcon[] iicon = new IIcon[4];

	public OfalenBlock() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHardness(7.5F);
		this.setResistance(15.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setLightLevel(1.0F);
		this.setHarvestLevel("pickaxe", 3);
	}

	/**メタデータ違いのテクスチャを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for (int i = 0; i < 4; i ++) {
			this.iicon[i] = register.registerIcon(this.getTextureName() + i);
		}
	}

	/**メタデータにより返すIIconを変える*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[meta & 3];
	}

	/**メタデータ違いのブロックを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 4; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	/**メタデータによりドロップ品を変える*/
	@Override
	public int damageDropped(int meta) {
		return meta & 3;
	}

}
