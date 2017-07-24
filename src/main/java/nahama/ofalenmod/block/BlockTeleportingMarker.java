package nahama.ofalenmod.block;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.tileentity.TileEntityTeleportingMarker;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTeleportingMarker extends Block implements ITileEntityProvider {
	public BlockTeleportingMarker() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
		this.setLightLevel(1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityTeleportingMarker();
	}

	/** プレイヤーに右クリックされた時の処理。 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// GUIを開く
		player.openGui(OfalenModCore.instance, 1, world, x, y, z);
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (world.isRemote) {
			super.breakBlock(world, x, y, z, block, meta);
			return;
		}
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || !(tileEntity instanceof TileEntityTeleportingMarker)) {
			super.breakBlock(world, x, y, z, block, meta);
			return;
		}
		((TileEntityTeleportingMarker) tileEntity).onBreaking();
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
