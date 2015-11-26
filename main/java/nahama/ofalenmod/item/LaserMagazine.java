package nahama.ofalenmod.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class LaserMagazine extends Item {

	public LaserMagazine() {
		super();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHasSubtypes(false);
		this.setMaxDamage(1024);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}

	/** クリエイティブタブにアイテムを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		// 修繕機での修繕を不可にしたアイテムを登録する。
		ItemStack itemStack = new ItemStack(this, 1, 0);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("NotRepairable", true);
		itemStack.setTagCompound(nbt);
		list.add(itemStack);
	}

	/** クラフト時の処理。 */
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		// 修繕機での修繕を不可にする。
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("NotRepairable", true);
		itemStack.setTagCompound(nbt);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}
		if (!itemStack.getTagCompound().getBoolean("NotRepairable")) {
			itemStack.getTagCompound().setBoolean("NotRepairable", true);
		}
	}

	/** 説明欄の内容を追加する。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		list.add(StatCollector.translateToLocal("info.loaded") + (32 - (itemStack.getItemDamage() / 32)));
	}

	@Override
	public int getItemStackLimit(ItemStack itemStack) {
		if (itemStack.getItemDamage() == 0) {
			return 64;
		} else {
			return 1;
		}
	}

}
