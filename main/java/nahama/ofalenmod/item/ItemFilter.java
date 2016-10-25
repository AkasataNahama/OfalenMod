package nahama.ofalenmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.Util;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

import static nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;

public class ItemFilter extends Item {

	private IIcon[] iicon;

	public ItemFilter() {
		super();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		if (FilterUtil.isAvailableFilterTag(itemStack))
			return;
		NBTTagCompound filter = new NBTTagCompound();
		filter.setTag(FilterUtil.SELECTING, new NBTTagList());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag(FilterUtil.ITEM_FILTER, filter);
		itemStack.setTagCompound(nbt);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!Util.isKeyDown(OfalenModCore.keyOSS.getKeyCode())) {
			if (itemStack.getItemDamage() < 1)
				player.openGui(OfalenModCore.instance, 5, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			return itemStack;
		}
		if (player.isSneaking()) {
			itemStack.setItemDamage(itemStack.getItemDamage() == 0 ? 1 : 0);
			return itemStack;
		}
		NBTTagCompound nbtFilter = FilterUtil.getFilterTag(itemStack);
		nbtFilter.setBoolean(FilterUtil.IS_WHITE, !FilterUtil.isWhiteList(nbtFilter));
		return itemStack;
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (itemStack.getItemDamage() < 1)
			return false;
		NBTTagCompound nbtFilter = FilterUtil.getFilterTag(itemStack);
		if (FilterUtil.getSelectingItemList(nbtFilter).tagCount() > 27)
			return false;
		ItemStack selecting = new ItemStack(world.getBlock(x, y, z), 1, world.getBlockMetadata(x, y, z));
		if (FilterUtil.isEnabledItem(nbtFilter, selecting))
			return true;
		NBTTagCompound nbt = new NBTTagCompound();
		selecting.writeToNBT(nbt);
		FilterUtil.getSelectingItemList(nbtFilter).appendTag(nbt);
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
		return iicon[meta & 1];
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

	/** 説明欄の内容を追加する。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		FilterUtil.addFilterInformation(itemStack, list);
	}
}

