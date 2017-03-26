package nahama.ofalenmod.block;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.tileentity.TileEntityDetailedSetter;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDetailedSetter extends BlockContainer {
	public BlockDetailedSetter() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDetailedSetter();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		player.openGui(OfalenModCore.instance, 1, world, x, y, z);
		return true;
	}

	/** 破壊された時の処理。 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (world.isRemote) {
			super.breakBlock(world, x, y, z, block, meta);
			return;
		}
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || !(tileEntity instanceof TileEntityDetailedSetter)) {
			super.breakBlock(world, x, y, z, block, meta);
			return;
		}
		// TileEntityの内部にあるアイテムをドロップさせる。
		IInventory inventory = (IInventory) tileEntity;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlotOnClosing(i);
			if (itemStack != null)
				OfalenUtil.dropItemStackNearBlock(itemStack, world, x, y, z);
		}
		// TileEntityの更新を通知する。
		world.func_147453_f(x, y, z, block);
		super.breakBlock(world, x, y, z, block, meta);
	}
}
