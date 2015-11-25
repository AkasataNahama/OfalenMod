package nahama.ofalenmod.item;

import java.util.List;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LaserMagazine extends Item {

	public LaserMagazine() {
		super();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHasSubtypes(false);
		this.setMaxDamage(1024);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}

	/**クリエイティブタブにアイテムを登録する処理。修繕機での修繕を不可にしたアイテムを登録する。*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		ItemStack itemStack = new ItemStack(this, 1, 0);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("CanRepair", false);
		itemStack.setTagCompound(nbt);
		list.add(itemStack);
	}

	/**クラフト時の処理。修繕機での修繕を不可にする。*/
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("CanRepair", false);
		itemStack.setTagCompound(nbt);
	}

	/**説明欄の内容を追加する。*/
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
