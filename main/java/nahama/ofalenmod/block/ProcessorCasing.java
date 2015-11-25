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

public class ProcessorCasing extends Block {

	private IIcon[] iicon = new IIcon[7];

	public ProcessorCasing() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	/**メタデータにより返すIIconを変える*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[meta];
	}

	/**メタデータによりドロップ品を変える*/
	@Override
	public int damageDropped(int meta) {
		if ((meta & 7) == 3) return 0;
		if ((meta & 7) == 7) return 4;
		return meta & 7;
	}

	/**メタデータ違いのブロックを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 7; i ++) {
			if (i != 3)
				list.add(new ItemStack(item, 1, i));
		}
	}

	/**メタデータ違いのテクスチャを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		for (int i = 0; i < 7; i ++) {
			if (i != 3)
				this.iicon[i] = iicon.registerIcon(this.getTextureName() + i);
		}
	}

}
