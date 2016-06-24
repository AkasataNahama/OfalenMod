package nahama.ofalenmod.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Random;

public class BlockGrassOfalen extends Block implements IPlantable, IGrowable {

	private IIcon[] icons;

	public BlockGrassOfalen() {
		super(Material.plants);
		this.setCreativeTab(null);
		this.setTickRandomly(true);
		float f = 0.5F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.disableStats();
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		this.checkAndDropBlock(world, x, y, z);
	}

	protected void checkAndDropBlock(World world, int x, int y, int z) {
		if (!this.canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		if (world.getBlockLightValue(x, y + 1, z) >= 9) {
			int meta = world.getBlockMetadata(x, y, z);
			if (meta < 12) {
				if (random.nextInt(10) == 0) {
					meta += 4;
					world.setBlockMetadataWithNotify(x, y, z, meta, 2);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.icons[meta & 15];
	}

	@Override
	public int getRenderType() {
		return 6;
	}

	protected Item getSeed() {
		return OfalenModItemCore.seedOfalen;
	}

	protected Item getCrop() {
		return OfalenModItemCore.fragmentOfalen;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float f, int fortune) {
		super.dropBlockAsItemWithChance(world, x, y, z, meta, f, 0);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return meta >= 12 ? this.getCrop() : this.getSeed();
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public int damageDropped(int meta) {
		return meta & 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return this.getSeed();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.icons = new IIcon[16];
		for (int i = 0; i < this.icons.length; ++i) {
			this.icons[i] = register.registerIcon(this.getTextureName() + "_" + (i / 4) + "-" + (i & 3));
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, meta, fortune);
		if (meta >= 12) {
			ret.add(new ItemStack(this.getSeed(), 1, meta & 3));
			for (int i = 0; i < fortune; i++) {
				if (world.rand.nextInt(10) == 0)
					ret.add(new ItemStack(this.getSeed(), 1, meta & 3));
			}
		}
		return ret;
	}

	/** 骨粉を使用できるかどうか。 */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean isRemote) {
		return world.getBlockMetadata(x, y, z) < 12;
	}

	/** 骨粉を適用するかどうか。 */
	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z) {
		return true;
	}

	/** 骨粉を適用した時の処理。 */
	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z) {
		// meta + 0, 4, 4, 8
		int meta = world.getBlockMetadata(x, y, z) + ((random.nextInt(4) + 1) / 2 * 4);
		while (meta > 15) {
			meta -= 4;
		}
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) {
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}

}
