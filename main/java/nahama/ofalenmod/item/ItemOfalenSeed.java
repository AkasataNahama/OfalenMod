package nahama.ofalenmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ItemOfalenSeed extends Item implements IPlantable {
	private Block plant;
	protected IIcon[] icons;

	public ItemOfalenSeed(Block plant) {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.plant = plant;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (side != 1)
			return false;
		if (!player.canPlayerEdit(x, y, z, side, itemStack) || !player.canPlayerEdit(x, y + 1, z, side, itemStack))
			return false;
		if (!world.getBlock(x, y, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, this) || !world.isAirBlock(x, y + 1, z))
			return false;
		world.setBlock(x, y + 1, z, this.plant);
		world.setBlockMetadataWithNotify(x, y + 1, z, itemStack.getItemDamage() & 3, 2);
		--itemStack.stackSize;
		return true;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) {
		return plant;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
		return 0;
	}

	/** アイテムのテクスチャを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		this.icons = new IIcon[4];
		for (int i = 0; i < 4; i++) {
			this.icons[i] = register.registerIcon(this.getIconString() + "-" + i);
		}
	}

	/** アイテムのテクスチャを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[meta & 3];
	}

	/** アイテムをクリエイティブタブに登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 4; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	/** メタデータを返す。 */
	@Override
	public int getMetadata(int meta) {
		return meta & 3;
	}

	/** 内部名を返す。 */
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}
}
