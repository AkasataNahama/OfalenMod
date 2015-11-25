package nahama.ofalenmod.block;

import java.util.Random;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.tileentity.TileEntityRepairMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RepairMachine extends BlockContainer {

	protected final Random random = new Random();
	private IIcon[] iicon = new IIcon[4];
	private static boolean isBurning;

	public RepairMachine() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	/**プレイヤーに右クリックされたときの動作*/
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		((TileEntityRepairMachine)world.getTileEntity(x, y, z)).setGrade(getGrade(world, x, y, z));
		//GUIを開く
		player.openGui(OfalenModCore.instance, 2, world, x, y, z);
		return true;
	}

	/**ブロックが設置された時の処理*/
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}

		//TileEntityを生成する
		if (itemStack.hasDisplayName()) {
			((TileEntityRepairMachine) world.getTileEntity(x, y, z)).setCustomName(itemStack.getDisplayName());
		}
	}

	/**アップデート時の処理*/
	public static void updateBlockState(boolean burning, World world, int x, int y, int z) {
		int direction = world.getBlockMetadata(x, y, z);
		TileEntity tileentity = world.getTileEntity(x, y, z);
		isBurning = true;

		//燃えているかでブロックを置き換える
		if (burning) {
			direction = direction | 8;
		} else {
			direction = direction & 7;
		}

		isBurning = false;
		world.setBlockMetadataWithNotify(x, y, z, direction, 2);

		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(x, y, z, tileentity);
			((TileEntityRepairMachine)tileentity).setGrade(getGrade(world, x, y, z));
		}
	}

	/**ブロックが壊された時の処理*/
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (!isBurning) {
			TileEntityRepairMachine tileentity = (TileEntityRepairMachine) world.getTileEntity(x, y, z);

			//アイテムを周囲にばらまく
			if (tileentity != null) {
				for (int i = 0; i < tileentity.getSizeInventory(); ++i) {
					ItemStack itemstack = tileentity.getStackInSlot(i);

					if (itemstack != null) {
						float f = this.random.nextFloat() * 0.6F + 0.1F;
						float f1 = this.random.nextFloat() * 0.6F + 0.1F;
						float f2 = this.random.nextFloat() * 0.6F + 0.1F;

						while (itemstack.stackSize > 0) {
							int j = this.random.nextInt(21) + 10;

							if (j > itemstack.stackSize) {
								j = itemstack.stackSize;
							}

							itemstack.stackSize -= j;
							EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

							if (itemstack.hasTagCompound()) {
								entityitem.getEntityItem().setTagCompound(((NBTTagCompound) itemstack.getTagCompound().copy()));
							}

							float f3 = 0.025F;
							entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
							entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.1F);
							entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
							world.spawnEntityInWorld(entityitem);
						}
					}
				}
				world.func_147453_f(x, y, z, block);
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	/**TileEntityの生成*/
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityRepairMachine();
	}

	/**処理装置を検出しGradeを計算する*/
	public static int getGrade(World world, int x, int y, int z) {
		int side = world.getBlockMetadata(x, y, z) & 7;
		int getX = x, getY = y, getZ = z;
		switch (side) {
		case 2:
			getZ += 2;
			break;
		case 3:
			getZ -= 2;
			break;
		case 4:
			getX += 2;
			break;
		case 5:
			getX -= 2;
		}
		Block block = world.getBlock(getX, getY, getZ);
		if (block instanceof MachineProcessor) {
			int grade = ((MachineProcessor)block).setMetaFromGrade(world, getX, getY, getZ);
			if (grade >= 4) {
				return (grade & 3) + 1;
			} else {
				return 0;
			}
		}
		return 0;
	}

	/**メタデータにより返すIIconを変える*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta < 8) {
			return side == meta ? this.iicon[2] : this.iicon[0];
		} else {
			return side == (meta & 7) ? this.iicon[3] : this.iicon[1];
		}
	}

	/**メタデータ違いのテクスチャを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		for (int i = 0; i  < 4; i ++) {
			this.iicon[i] = iicon.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/**作業中は光源になるようにする*/
	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		if (iBlockAccess.getBlockMetadata(x, y, z) >= 8) {
			return 13;
		} else {
			return 0;
		}
	}

}
