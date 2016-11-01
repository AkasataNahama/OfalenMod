package nahama.ofalenmod.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.handler.OfalenModAnniversaryHandler;
import nahama.ofalenmod.tileentity.TileEntityPresentBox;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Calendar;
import java.util.Random;

public class BlockPresentBox extends Block implements ITileEntityProvider {
	private Random random = new Random();
	/** 0:下,1:上,2:横。 */
	private IIcon[] icons = new IIcon[3];

	public BlockPresentBox() {
		super(Material.sponge);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHardness(1.0F);
		this.setResistance(1.0F);
		this.setStepSound(Block.soundTypeCloth);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityPresentBox();
	}

	/** プレイヤーに右クリックされた時の処理。 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// GUIを開く
		((TileEntityPresentBox) world.getTileEntity(x, y, z)).openInventory(player);
		player.openGui(OfalenModCore.instance, 1, world, x, y, z);
		return true;
	}

	/** ブロックが破壊された時の処理。 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		// TileEntityの内部にあるアイテムをドロップさせる。
		TileEntityPresentBox tileEntity = (TileEntityPresentBox) world.getTileEntity(x, y, z);
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

	/** ブロックのアイコンを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		String s = "";
		if (OfalenModAnniversaryHandler.isTextureSpecial)
			s = "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
		for (int i = 0; i < 3; i++) {
			icons[i] = register.registerIcon(this.getTextureName() + "-" + i + s);
		}
	}

	/** ブロックのアイコンを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		int i = 2;
		if (side == 0)
			i = 0;
		if (side == 1)
			i = 1;
		return icons[i];
	}
}
