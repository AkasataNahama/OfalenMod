package nahama.ofalenmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemListPaper extends Item implements IItemList {

	private IIcon[] iicon;
	public static final String TAG_NAME = "ListItemSelected";

	public ItemListPaper() {
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHasSubtypes(true);
	}

	@Override
	public boolean isBlackList(ItemStack list) {
		return (list.getItemDamage() & 1) == 1;
	}

	@Override
	public boolean isEnabledItem(ItemStack list, ItemStack checking) {
		boolean isBlack = this.isBlackList(list);
		NBTTagList nbtTagList = list.getTagCompound().getTagList(TAG_NAME, 10);
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			NBTTagCompound nbt = nbtTagList.getCompoundTagAt(i);
			if (nbt == null)
				continue;
			ItemStack stack = ItemStack.loadItemStackFromNBT(nbt);
			if (stack == null)
				continue;
			if (stack.isItemEqual(checking))
				return !isBlack;
			if (!(stack.getItem() instanceof IItemList))
				continue;
			if (((IItemList) stack.getItem()).isEnabledItem(stack, checking))
				return !isBlack;
		}
		return false;
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		if (itemStack.hasTagCompound())
			return;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag(TAG_NAME, new NBTTagList());
		itemStack.setTagCompound(nbt);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		int meta = itemStack.getItemDamage();
		if (player.isSneaking()) {
			if (meta < 2)
				itemStack.setItemDamage(meta + 2);
			else
				itemStack.setItemDamage(meta - 2);
			return itemStack;
		}
		if (meta > 1)
			return itemStack;
		player.openGui(OfalenModCore.instance, 5, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return itemStack;
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		if (itemStack.getItemDamage() < 2)
			return false;
		if (itemStack.getTagCompound().getTagList(TAG_NAME, 10).tagCount() > 27)
			return false;
		NBTTagCompound nbt = new NBTTagCompound();
		new ItemStack(world.getBlock(x, y, z), 0, world.getBlockMetadata(x, y, z)).writeToNBT(nbt);
		itemStack.getTagCompound().getTagList(TAG_NAME, 10).appendTag(nbt);
		return true;
	}

	/** アイテムのテクスチャを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		iicon = new IIcon[4];
		for (int i = 0; i < 4; i++) {
			iicon[i] = register.registerIcon(this.getIconString() + "-" + i);
		}
	}

	/** アイテムのテクスチャを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta & 3];
	}

	/** アイテムをクリエイティブタブに登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 2; i++) {
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
