package nahama.ofalenmod.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.tileentity.TileEntityGradedMachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockMachineBase extends BlockContainer {
	private IIcon[] icons = new IIcon[4];
	private Random random = new Random();

	protected BlockMachineBase() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	/** プレイヤーに右クリックされた時の処理。 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		((TileEntityGradedMachineBase) world.getTileEntity(x, y, z)).updateGrade();
		// GUIを開く
		player.openGui(OfalenModCore.instance, 1, world, x, y, z);
		return true;
	}

	/** ブロックが設置された時の処理。 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		// 設置したEntityの向きによってメタデータを設定する。
		int meta = 0;
		switch (MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) {
		case 0:
			meta = 2;
			break;
		case 1:
			meta = 5;
			break;
		case 2:
			meta = 3;
			break;
		case 3:
			meta = 4;
		}
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		// 名づけがされていたら反映する。
		if (itemStack.hasDisplayName()) {
			((TileEntityGradedMachineBase) world.getTileEntity(x, y, z)).setCustomName(itemStack.getDisplayName());
		}
	}

	/** ブロックが破壊された時の処理。 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		// TileEntityの内部にあるアイテムをドロップさせる。
		TileEntityGradedMachineBase tileEntity = (TileEntityGradedMachineBase) world.getTileEntity(x, y, z);
		if (tileEntity != null) {
			for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
				ItemStack itemStack = tileEntity.getStackInSlot(i);
				if (itemStack != null) {
					float f = random.nextFloat() * 0.6F + 0.1F;
					float f1 = random.nextFloat() * 0.6F + 0.1F;
					float f2 = random.nextFloat() * 0.6F + 0.1F;
					while (itemStack.stackSize > 0) {
						int j = random.nextInt(21) + 10;
						if (j > itemStack.stackSize) {
							j = itemStack.stackSize;
						}
						itemStack.stackSize -= j;
						EntityItem entityItem = new EntityItem(world, x + f, y + f1, z + f2, new ItemStack(itemStack.getItem(), j, itemStack.getItemDamage()));
						if (itemStack.hasTagCompound()) {
							entityItem.getEntityItem().setTagCompound(((NBTTagCompound) itemStack.getTagCompound().copy()));
						}
						float f3 = 0.025F;
						entityItem.motionX = (float) random.nextGaussian() * f3;
						entityItem.motionY = (float) random.nextGaussian() * f3 + 0.1F;
						entityItem.motionZ = (float) random.nextGaussian() * f3;
						world.spawnEntityInWorld(entityItem);
					}
				}
			}
			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	/** ブロックのテクスチャを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for (int i = 0; i < 4; i++) {
			icons[i] = register.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** ブロックのテクスチャを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta < 8) {
			return side == meta ? icons[2] : icons[0];
		} else {
			return side == (meta & 7) ? icons[3] : icons[1];
		}
	}

	/** 光源レベルを返す。 */
	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		if (iBlockAccess.getBlockMetadata(x, y, z) >= 8) {
			return 13;
		} else {
			return 0;
		}
	}
}
