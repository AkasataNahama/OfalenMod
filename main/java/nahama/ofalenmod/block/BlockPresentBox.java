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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Calendar;

import static nahama.ofalenmod.util.Util.random;

public class BlockPresentBox extends Block implements ITileEntityProvider {
	/** 0:下, 1:上, 2:横 */
	private IIcon[] icons;

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
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || !(tileEntity instanceof IInventory)) {
			super.breakBlock(world, x, y, z, block, meta);
			return;
		}
		// TileEntityの内部にあるアイテムをドロップさせる。
		IInventory inventory = (IInventory) tileEntity;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack != null) {
				float fx = random.nextFloat() * 0.6F + 0.1F;
				float fy = random.nextFloat() * 0.6F + 0.1F;
				float fz = random.nextFloat() * 0.6F + 0.1F;
				while (itemStack.stackSize > 0) {
					int j = random.nextInt(21) + 10;
					if (j > itemStack.stackSize)
						j = itemStack.stackSize;
					itemStack.stackSize -= j;
					EntityItem entityItem = new EntityItem(world, x + fx, y + fy, z + fz, itemStack.copy());
					entityItem.getEntityItem().stackSize = j;
					float f3 = 0.025F;
					entityItem.motionX = (float) random.nextGaussian() * f3;
					entityItem.motionY = (float) random.nextGaussian() * f3 + 0.1F;
					entityItem.motionZ = (float) random.nextGaussian() * f3;
					world.spawnEntityInWorld(entityItem);
				}
			}
		}
		world.func_147453_f(x, y, z, block);
		super.breakBlock(world, x, y, z, block, meta);
	}

	/** ブロックのアイコンを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		icons = new IIcon[3];
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
