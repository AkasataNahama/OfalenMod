package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenDetailedSettingHandler;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.inventory.ContainerItemCollector;
import nahama.ofalenmod.util.*;
import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
import nahama.ofalenmod.util.OfalenSetting.OfalenSettingByte;
import nahama.ofalenmod.util.OfalenSetting.OfalenSettingDouble;
import nahama.ofalenmod.util.OfalenSetting.OfalenSettingList;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemCollector extends Item implements IItemOfalenSettable {
	private IIcon[] icons;

	public ItemCollector() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setMaxStackSize(1);
		this.setMaxDamage(64 * 9 * 3);
	}

	@Override
	public void onUpdate(ItemStack thisStack, World world, Entity entity, int slot, boolean isHeld) {
		OfalenTimer.start("ItemCollector.onUpdate");
		// クライアント側なら終了。
		if (world.isRemote)
			return;
		// フィルタータグが無効だったら初期化する。
		if (!FilterUtil.isAvailableFilterTag(thisStack))
			FilterUtil.initFilterTag(thisStack);
		// 修繕不可に設定。
		if (!thisStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_IRREPARABLE))
			thisStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_IRREPARABLE, true);
		boolean isItemDisabled = thisStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED);
		boolean isExpDisabled = thisStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_EXP_DISABLED);
		// アイテムも経験値も無効化されていたら終了。
		if (isItemDisabled && isExpDisabled)
			return;
		// 耐久値が残っていなかったら終了。
		if (OfalenUtil.getRemainingDamage(thisStack) < 1)
			return;
		boolean canDamage = true;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			// プレイヤーが持っていて、コレクターのGUIを開いているなら終了。
			if (player.openContainer != null && player.openContainer instanceof ContainerItemCollector)
				return;
			if (player.capabilities.isCreativeMode)
				canDamage = false;
		}
		// 無効時間の残りを取得。
		byte interval = thisStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (interval > 0) {
			// 無効時間が残っていたら減らして終了。
			thisStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, --interval);
			return;
		}
		// 無効時間をリセット。TODO 詳細設定
		byte intervalMax = (Byte) OfalenDetailedSettingHandler.getCurrentValueFromNBT(OfalenDetailedSettingHandler.getSettingTag(thisStack), "Interval/", this.getSetting().getChildSetting(new ItemStack(Items.quartz)));
		thisStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, intervalMax);
		// 範囲の設定。TODO 詳細設定
		int rangeItem = 10;
		int rangeExp = 15;
		if (thisStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_SET_IN_DETAIL)) {
			rangeItem = thisStack.getTagCompound().getShort(OfalenNBTUtil.ITEM_RANGE);
			rangeExp = thisStack.getTagCompound().getShort(OfalenNBTUtil.EXP_RANGE);
		}
		ArrayList<Entity> listWaitingEntity = new ArrayList<Entity>();
		// EntityItemとEntityXPOrbがあれば移動する。
		for (Object o : world.loadedEntityList) {
			if (!isItemDisabled && o instanceof EntityItem) {
				// EntityItemにキャスト。
				EntityItem entityItem = (EntityItem) o;
				double distance = entity.getDistanceSqToEntity(entityItem);
				// 範囲外か、拾えない状態（ドロップされてすぐ）なら次のEntityへ。
				if (distance < 2 || distance > rangeItem * rangeItem || entityItem.delayBeforeCanPickup > 0)
					continue;
				ItemStack eItemStack = entityItem.getEntityItem();
				// アイテムフィルターで許可されていなかったら次のEntityへ。
				if (!FilterUtil.canItemFilterThrough(FilterUtil.getFilterTag(thisStack), eItemStack))
					continue;
				// 耐久値の残りを取得。
				int remaining = OfalenUtil.getRemainingDamage(thisStack);
				// 耐久値が尽きていたら終了。
				if (remaining < 1)
					return;
				// プレイヤーが持っているなら、インベントリの空きスロット数も考慮。 TODO 詳細設定
				if (entity instanceof EntityPlayer)
					remaining = Math.min(remaining, OfalenUtil.getRemainingItemAmountInInventory(((EntityPlayer) entity).inventory.mainInventory, eItemStack, ((EntityPlayer) entity).inventory.getInventoryStackLimit()));
				// 一個も移動できないなら次のEntityへ。
				if (remaining < OfalenModConfigCore.amountCollectorDamageItem)
					continue;
				// スタック数が限界以下ならそのまま移動。
				if (remaining >= eItemStack.stackSize * OfalenModConfigCore.amountCollectorDamageItem) {
					entityItem.setPosition(entity.posX, entity.posY, entity.posZ);
					if (canDamage)
						thisStack.setItemDamage(thisStack.getItemDamage() + (eItemStack.stackSize * OfalenModConfigCore.amountCollectorDamageItem));
					continue;
				}
				// 耐久値か空きスロットが足りなかったら足りる分だけ移動して終了。
				ItemStack itemStack1 = eItemStack.copy();
				itemStack1.stackSize = remaining / OfalenModConfigCore.amountCollectorDamageItem;
				listWaitingEntity.add(OfalenUtil.getEntityItemNearEntity(itemStack1, entity));
				eItemStack.stackSize -= remaining / OfalenModConfigCore.amountCollectorDamageItem;
				if (canDamage)
					thisStack.setItemDamage(thisStack.getItemDamage() + (remaining / OfalenModConfigCore.amountCollectorDamageItem * OfalenModConfigCore.amountCollectorDamageItem));
			} else if (!isExpDisabled && (o instanceof EntityXPOrb)) {
				// EntityXPOrbにキャスト。
				EntityXPOrb e = (EntityXPOrb) o;
				double distance = entity.getDistanceSqToEntity(e);
				// 範囲外なら次のEntityへ。
				if (distance < 16 || distance > rangeExp * rangeExp)
					continue;
				// 耐久値の残りを取得。
				int remaining = OfalenUtil.getRemainingDamage(thisStack);
				// 耐久値が尽きていたら終了。
				if (remaining < 1)
					return;
				if (remaining >= e.xpValue * OfalenModConfigCore.amountCollectorDamageExp) {
					e.setPosition(entity.posX, entity.posY, entity.posZ);
					if (canDamage)
						thisStack.setItemDamage(thisStack.getItemDamage() + (e.xpValue * OfalenModConfigCore.amountCollectorDamageExp));
					continue;
				}
				// 耐久値が足りなかったら足りる分だけ移動して終了。
				listWaitingEntity.add(new EntityXPOrb(world, entity.posX, entity.posY, entity.posZ, remaining / OfalenModConfigCore.amountCollectorDamageExp));
				e.xpValue -= remaining / OfalenModConfigCore.amountCollectorDamageExp;
				if (canDamage)
					thisStack.setItemDamage(thisStack.getItemDamage() + (remaining / OfalenModConfigCore.amountCollectorDamageExp * OfalenModConfigCore.amountCollectorDamageExp));
			}
		}
		// listWaitingEntityに入っているentityをworldにspawnさせる。ConcurrentModificationException回避。
		for (Entity e : listWaitingEntity) {
			world.spawnEntityInWorld(e);
		}
		OfalenTimer.watchAndLog("ItemCollector.onUpdate", 0.1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!OfalenKeyHandler.isSettingKeyPressed()) {
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

	/** 説明欄の内容を追加する。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		if (!itemStack.hasTagCompound())
			return;
		List<String> stringList = OfalenUtil.add(list, StatCollector.translateToLocal("info.ofalen.collector.item") + " : " + (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED) ? "Off" : "On"));
		stringList.add(StatCollector.translateToLocal("info.ofalen.collector.exp") + " : " + (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_EXP_DISABLED) ? "Off" : "On"));
		stringList.addAll(FilterUtil.getFilterInformation(itemStack));
	}

	@Override
	public void registerIcons(IIconRegister register) {
		icons = new IIcon[4];
		for (int i = 0; i < 4; i++) {
			icons[i] = register.registerIcon(this.getIconString() + "-" + (i / 2) + "-" + (i % 2));
		}
	}

	@Override
	public IIcon getIconIndex(ItemStack itemStack) {
		if (!itemStack.hasTagCompound())
			return icons[3];
		NBTTagCompound nbt = itemStack.getTagCompound();
		int index = 0;
		if (!nbt.getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED))
			index += 2;
		if (!nbt.getBoolean(OfalenNBTUtil.IS_EXP_DISABLED))
			index += 1;
		return icons[index];
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return this.getIconIndex(stack);
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public OfalenSettingList getSetting() {
		ArrayList<OfalenSetting> list1;
		ArrayList<OfalenSetting> list2;
		ArrayList<OfalenSetting> list3;
		// コレクター。
		list1 = new ArrayList<OfalenSetting>();
		// 自動停止機能が有効か。
		list1.add(new OfalenSetting.OfalenSettingBoolean("AutoStopping", new ItemStack(Blocks.redstone_torch), true));
		// インターバル。
		list1.add(new OfalenSettingByte("Interval", new ItemStack(Items.quartz), 10, true));
		// アイテム。
		list2 = new ArrayList<OfalenSetting>();
		// 範囲。
		list3 = new ArrayList<OfalenSetting>();
		list3.add(new OfalenSettingByte("DesignationMode", new ItemStack(Items.stick), 1, 1, 3));
		list3.add(new OfalenSettingDouble("Length.0", new ItemStack(OfalenModItemCore.gemOfalen, 1, 0), 10.0D));
		list3.add(new OfalenSettingDouble("Length.1", new ItemStack(OfalenModItemCore.gemOfalen, 1, 1), 10.0D));
		list3.add(new OfalenSettingDouble("Length.2", new ItemStack(OfalenModItemCore.gemOfalen, 1, 2), 10.0D));
		list2.add(new OfalenSettingList("Range", new ItemStack(OfalenModBlockCore.surveyorOfalen), list3));
		list1.add(new OfalenSettingList("Item", new ItemStack(OfalenModItemCore.partsOfalen, 1, 9), list2));
		// 経験値。
		list2 = new ArrayList<OfalenSetting>();
		// 範囲。
		list3 = new ArrayList<OfalenSetting>();
		list3.add(new OfalenSettingByte("DesignationMode", new ItemStack(Items.stick), 1, 1, 3));
		list3.add(new OfalenSettingDouble("Length.0", new ItemStack(OfalenModItemCore.gemOfalen, 1, 0), 10.0D));
		list3.add(new OfalenSettingDouble("Length.1", new ItemStack(OfalenModItemCore.gemOfalen, 1, 1), 10.0D));
		list3.add(new OfalenSettingDouble("Length.2", new ItemStack(OfalenModItemCore.gemOfalen, 1, 2), 10.0D));
		list2.add(new OfalenSettingList("Range", new ItemStack(OfalenModBlockCore.surveyorOfalen), list3));
		list1.add(new OfalenSettingList("Exp", new ItemStack(Items.book), list2));
		return new OfalenSettingList("OfalenCollector", null, list1);
	}
}
