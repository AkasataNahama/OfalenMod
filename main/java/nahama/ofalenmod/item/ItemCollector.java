package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.inventory.ContainerItemCollector;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemCollector extends Item {
	private IIcon[] icons;

	public ItemCollector() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setMaxStackSize(1);
		this.setMaxDamage(64 * 9 * 3);
	}

	@Override
	public void onUpdate(ItemStack thisStack, World world, Entity entity, int slot, boolean isHeld) {
		// クライアント側なら終了。
		if (world.isRemote)
			return;
		// NBTを持っていなかったら新しく持たせる。
		if (!thisStack.hasTagCompound())
			OfalenNBTUtil.FilterUtil.initFilterTag(thisStack);
		boolean isItemDisabled = thisStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED);
		boolean isExpDisabled = thisStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_EXP_DISABLED);
		if (isItemDisabled && isExpDisabled)
			return;
		if (Util.getRemainingDamage(thisStack) < 1)
			return;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.openContainer != null && player.openContainer instanceof ContainerItemCollector)
				return;
		}
		// 無効時間の残りを取得。
		byte interval2 = thisStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (interval2 > 0) {
			// 無効時間が残っていたら減らして終了。
			thisStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, --interval2);
			return;
		}
		// 無効時間をリセット。TODO 詳細設定
		thisStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) 10);
		// 範囲の設定。TODO 詳細設定
		int rangeItem = 10;
		int rangeExp = 10;
		if (thisStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_SET_IN_DETAIL)) {
			rangeItem = thisStack.getTagCompound().getShort(OfalenNBTUtil.ITEM_RANGE);
			rangeExp = thisStack.getTagCompound().getShort(OfalenNBTUtil.EXP_RANGE);
		}
		// EntityItemとEntityXPOrbがあれば移動する。
		for (Object o : world.loadedEntityList) {
			if (!isItemDisabled && o instanceof EntityItem) {
				// EntityItemにキャスト。
				EntityItem entityItem = (EntityItem) o;
				if (entity.getDistanceToEntity(entityItem) > rangeItem || entityItem.delayBeforeCanPickup > 0)
					continue;
				// 範囲以内かつ拾える状態なら移動。
				int remaining = Util.getRemainingDamage(thisStack);
				ItemStack eItemStack = entityItem.getEntityItem();
				if (remaining >= eItemStack.stackSize) {
					entityItem.setPosition(entity.posX, entity.posY, entity.posZ);
					thisStack.setItemDamage(thisStack.getItemDamage() + entityItem.getEntityItem().stackSize);
					continue;
				}
				// 耐久値が足りなかったら足りる分だけ移動して終了。
				ItemStack itemStack1 = eItemStack.copy();
				itemStack1.stackSize = remaining;
				Util.dropItemStackCopyNearEntity(itemStack1, entity);
				entityItem.getEntityItem().stackSize -= remaining;
				thisStack.setItemDamage(thisStack.getMaxDamage());
				return;
			} else if (!isExpDisabled && (o instanceof EntityXPOrb)) {
				// EntityXPOrbにキャスト。
				EntityXPOrb e = (EntityXPOrb) o;
				if (entity.getDistanceToEntity(e) > rangeExp)
					continue;
				// 範囲以内なら移動。
				int remaining = Util.getRemainingDamage(thisStack);
				if (remaining >= e.xpValue) {
					e.setPosition(entity.posX, entity.posY, entity.posZ);
					thisStack.setItemDamage(thisStack.getItemDamage() + e.xpValue);
					continue;
				}
				// 耐久値が足りなかったら足りる分だけ移動して終了。
				Entity entity1 = new EntityXPOrb(world, entity.posX, entity.posY, entity.posZ, remaining);
				world.spawnEntityInWorld(entity1);
				e.xpValue -= remaining;
				thisStack.setItemDamage(thisStack.getMaxDamage());
				return;
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!Util.isKeyDown(OfalenModCore.KEY_OSS.getKeyCode())) {
			Util.debuggingInfo("openGui", "ItemCollector.onItemRightClick");
			player.openGui(OfalenModCore.instance, 7, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			return itemStack;
		}
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (!player.isSneaking()) {
			// OSS + 右クリックでアイテムのOn/Off。
			nbt.setBoolean(OfalenNBTUtil.IS_ITEM_DISABLED, !nbt.getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED));
		} else {
			// OSS + Shift + 右クリックで経験値のOn/Off。
			nbt.setBoolean(OfalenNBTUtil.IS_EXP_DISABLED, !nbt.getBoolean(OfalenNBTUtil.IS_EXP_DISABLED));
		}
		return itemStack;
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	/** メタデータを返す。 */
	@Override
	public int getMetadata(int meta) {
		return meta & 1;
	}

	/** 説明欄の内容を追加する。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		if (!itemStack.hasTagCompound())
			return;
		list.add(StatCollector.translateToLocal("info.ofalen.collector.item") + " : " + (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED)?"Off":"On"));
		list.add(StatCollector.translateToLocal("info.ofalen.collector.exp") + " : " +  (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_EXP_DISABLED)?"Off":"On"));
		OfalenNBTUtil.FilterUtil.addFilterInformation(itemStack, list);
	}
}
