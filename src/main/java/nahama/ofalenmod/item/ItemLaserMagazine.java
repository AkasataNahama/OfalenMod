package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemLaserMagazine extends Item {
	public ItemLaserMagazine() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHasSubtypes(false);
		this.setMaxDamage(1024);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}

	/** クリエイティブタブにアイテムを登録する処理。 */
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		// 修繕機での修繕を不可にしたアイテムを登録する。
		ItemStack itemStack = new ItemStack(this, 1, 0);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean(OfalenNBTUtil.IS_IRREPARABLE, true);
		itemStack.setTagCompound(nbt);
		OfalenUtil.add(list, itemStack);
	}

	/** クラフト時の処理。 */
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		// 修繕機での修繕を不可にする。
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean(OfalenNBTUtil.IS_IRREPARABLE, true);
		itemStack.setTagCompound(nbt);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		if (!itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_IRREPARABLE))
			itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_IRREPARABLE, true);
	}

	/** 説明欄の内容を追加する。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		OfalenUtil.add(list, StatCollector.translateToLocal("info.ofalen.crystal") + OfalenUtil.getColon() + (32 - (itemStack.getItemDamage() / 32)));
	}

	/** アイテムスタックの最大スタック数を返す。 */
	@Override
	public int getItemStackLimit(ItemStack itemStack) {
		if (itemStack.getItemDamage() == 0) {
			// 最大装填されていればスタック可能にする。
			return 64;
		} else {
			return 1;
		}
	}

	/** ダメージを受けられるかどうか。 */
	@Override
	public boolean isDamageable() {
		return false;
	}
}
