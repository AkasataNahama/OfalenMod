package nahama.ofalenmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemCollector extends Item {
	private IIcon[] icons;
	private static byte interval = 10;
	private static int range = 5;

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
		// クライアント側なら終了。
		if (world.isRemote)
			return;
		// NBTを持っていなかったら新しく持たせる。
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		// オフにされていたら終了。
		if (itemStack.getItemDamage() > 0)
			return;
		// 無効時間の残りを取得。
		byte interval2 = itemStack.getTagCompound().getByte("Interval");
		if (interval2 > 0) {
			// 無効時間が残っていたら減らして終了。
			itemStack.getTagCompound().setByte("Interval", --interval2);
			return;
		}
		// 無効時間をリセット。
		itemStack.getTagCompound().setByte("Interval", interval);
		// TODO 範囲のNBT読み込み? ダメージ？
		// ワールドのentityリストでループ。
		for (Object o : world.loadedEntityList) {
			if (!(o instanceof EntityItem))
				continue;
			// EntityItem以外を除外してからキャスト。
			EntityItem e = (EntityItem) o;
			if (entity.getDistanceToEntity(e) > range || e.delayBeforeCanPickup > 0)
				continue;
			// 範囲以内かつ拾える状態なら移動。
			e.setPosition(entity.posX, entity.posY, entity.posZ);
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (player.isSneaking())
			itemStack.setItemDamage(itemStack.getItemDamage() + 1 & 1);
		return itemStack;
	}

	/** アイテムのテクスチャを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		icons = new IIcon[2];
		for (int i = 0; i < 2; i++) {
			icons[i] = register.registerIcon(this.getIconString() + "-" + i);
		}
	}

	/** アイテムのテクスチャを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[meta & 1];
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
		return meta & 1;
	}

	/** 内部名を返す。 */
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}
}
