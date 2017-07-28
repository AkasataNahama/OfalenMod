package nahama.ofalenmod.item;

import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenDetailedSettingHandler;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.util.IItemOfalenSettable;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
import nahama.ofalenmod.util.OfalenSetting;
import nahama.ofalenmod.util.OfalenSetting.OfalenSettingByte;
import nahama.ofalenmod.util.OfalenSetting.OfalenSettingDouble;
import nahama.ofalenmod.util.OfalenSetting.OfalenSettingList;
import nahama.ofalenmod.util.OfalenTimer;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
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

public class ItemCollector extends ItemFuture implements IItemOfalenSettable {
	private IIcon[] icons;

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack itemStack = new ItemStack(item);
		// TODO 標準量を使用
		this.setMaterialAmount(itemStack, 64);
		OfalenUtil.add(list, itemStack);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
		super.onUpdate(itemStack, world, entity, slot, isHeld);
		OfalenTimer.start("ItemCollector.onUpdate");
		// クライアント側か、プレイヤーでないなら終了。
		if (world.isRemote || !(entity instanceof EntityPlayer))
			return;
		// フィルタータグが無効だったら初期化する。
		if (!FilterUtil.isAvailableFilterTag(itemStack))
			FilterUtil.initFilterTag(itemStack);
		boolean isItemDisabled = itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED);
		boolean isExpDisabled = itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_EXP_DISABLED);
		// アイテムも経験値も無効化されていたら終了。
		if (isItemDisabled && isExpDisabled)
			return;
		// 材料が残っていなかったら終了。
		int amountMin = Math.min(OfalenModConfigCore.amountCollectorDamageItem, OfalenModConfigCore.amountCollectorDamageExp);
		if (this.getMaterialAmount(itemStack) < amountMin)
			return;
		EntityPlayer player = (EntityPlayer) entity;
		boolean canDamage = true;
		if (player.capabilities.isCreativeMode)
			canDamage = false;
		// 無効時間が残っていたら終了。
		if (itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL) > 0)
			return;
		// 無効時間をリセット。TODO 詳細設定
		byte intervalMax = (Byte) OfalenDetailedSettingHandler.getCurrentValueFromNBT(OfalenDetailedSettingHandler.getSettingTag(itemStack), "Interval/", this.getSetting().getChildSetting(new ItemStack(Items.quartz)));
		itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, intervalMax);
		// 範囲の設定。TODO 詳細設定
		int rangeItem = 10;
		int rangeExp = 15;
		if (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_SET_IN_DETAIL)) {
			rangeItem = itemStack.getTagCompound().getShort(OfalenNBTUtil.ITEM_RANGE);
			rangeExp = itemStack.getTagCompound().getShort(OfalenNBTUtil.EXP_RANGE);
		}
		ArrayList<Entity> listWaitingEntity = new ArrayList<Entity>();
		// EntityItemとEntityXPOrbがあれば移動する。
		for (Object o : world.loadedEntityList) {
			// 材料が残っていなかったら終了。
			if (this.getMaterialAmount(itemStack) < amountMin)
				break;
			if (!isItemDisabled && o instanceof EntityItem) {
				// EntityItemにキャスト。
				EntityItem entityItem = (EntityItem) o;
				double distance = player.getDistanceSqToEntity(entityItem);
				// 範囲外か、拾えない状態（ドロップされてすぐ）なら次のEntityへ。
				if (distance < 2 || distance > rangeItem * rangeItem || entityItem.delayBeforeCanPickup > 0)
					continue;
				ItemStack eItemStack = entityItem.getEntityItem();
				// アイテムフィルターで許可されていなかったら次のEntityへ。
				if (!FilterUtil.canItemFilterThrough(FilterUtil.getFilterTag(itemStack), eItemStack))
					continue;
				// 材料数を取得。
				int amount = this.getMaterialAmount(itemStack);
				// インベントリの空きスロット数を取得。 TODO 詳細設定
				int limit = OfalenUtil.getRemainingItemAmountInInventory(player.inventory.mainInventory, eItemStack, player.inventory.getInventoryStackLimit());
				// 材料の限界も考慮。
				if (OfalenModConfigCore.amountCollectorDamageItem > 0)
					limit = Math.min(limit, amount / OfalenModConfigCore.amountCollectorDamageItem);
				// 一個も移動できないなら次のEntityへ。
				if (limit < 1)
					continue;
				// スタック数が限界以下ならそのまま移動。
				if (eItemStack.stackSize <= limit) {
					entityItem.setPosition(player.posX, player.posY, player.posZ);
					if (canDamage)
						this.consumeMaterial(itemStack, eItemStack.stackSize * OfalenModConfigCore.amountCollectorDamageItem);
					continue;
				}
				// 材料か空きスロットが足りなかったら足りる分だけ移動して終了。
				ItemStack itemStack1 = eItemStack.copy();
				itemStack1.stackSize = limit;
				listWaitingEntity.add(OfalenUtil.getEntityItemNearEntity(itemStack1, player));
				eItemStack.stackSize -= limit;
				if (canDamage)
					this.consumeMaterial(itemStack, limit * OfalenModConfigCore.amountCollectorDamageItem);
			} else if (!isExpDisabled && (o instanceof EntityXPOrb)) {
				// EntityXPOrbにキャスト。
				EntityXPOrb e = (EntityXPOrb) o;
				double distance = player.getDistanceSqToEntity(e);
				// 範囲外なら次のEntityへ。
				if (distance < 16 || distance > rangeExp * rangeExp)
					continue;
				// 材料の残りを取得。
				int amount = this.getMaterialAmount(itemStack);
				int limit = Integer.MAX_VALUE;
				if (OfalenModConfigCore.amountCollectorDamageExp > 0)
					limit = amount / OfalenModConfigCore.amountCollectorDamageExp;
				// 全く運べない（材料がない）なら次へ。
				if (limit < 1)
					continue;
				// 経験値量が限界以下ならそのまま移動。
				if (e.xpValue <= limit) {
					e.setPosition(player.posX, player.posY, player.posZ);
					if (canDamage)
						this.consumeMaterial(itemStack, e.xpValue * OfalenModConfigCore.amountCollectorDamageExp);
					continue;
				}
				// 材料が足りなかったら足りる分だけ移動して終了。
				listWaitingEntity.add(new EntityXPOrb(world, player.posX, player.posY, player.posZ, limit));
				e.xpValue -= limit;
				if (canDamage)
					this.consumeMaterial(itemStack, limit * OfalenModConfigCore.amountCollectorDamageExp);
			}
		}
		// listWaitingEntityに入っているentityをworldにspawnさせる。ConcurrentModificationException回避。
		for (Entity e : listWaitingEntity) {
			world.spawnEntityInWorld(e);
		}
		OfalenTimer.watchAndLog("ItemCollector.onUpdate", 0.1);
	}

	/** 右クリック時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 時間がたっていないなら終了。
		if (itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK) > 0)
			return itemStack;
		itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK, (byte) 10);
		if (!OfalenKeyHandler.isSprintKeyPressed(player)) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			if (!player.isSneaking()) {
				// 右クリックでアイテムのOn/Off。
				nbt.setBoolean(OfalenNBTUtil.IS_ITEM_DISABLED, !nbt.getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED));
			} else {
				// Shift + 右クリックで経験値のOn/Off。
				nbt.setBoolean(OfalenNBTUtil.IS_EXP_DISABLED, !nbt.getBoolean(OfalenNBTUtil.IS_EXP_DISABLED));
			}
		} else {
			// ダッシュキーが押されていれば、ランプの補充・取り出し。
			if (!player.isSneaking()) {
				// しゃがんでいなければ、補充。
				this.chargeMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 9), player);
			} else {
				// しゃがんでいれば、取り出し。
				this.dropMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 9), player);
			}
		}
		return itemStack;
	}

	/** 説明欄の内容を追加する。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		List<String> stringList = OfalenUtil.getAs(list);
		// TODO 標準量を表示
		int amount = this.getMaterialAmount(itemStack);
		stringList.add(OfalenUtil.getStackAmountString(amount, 64) + " (" + amount + " / 64)");
		if (itemStack.hasTagCompound()) {
			stringList.add(StatCollector.translateToLocal("info.ofalen.collector.item") + " " + StatCollector.translateToLocal("info.ofalen.future.isValid." + !itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED)));
			stringList.add(StatCollector.translateToLocal("info.ofalen.collector.exp") + " " + StatCollector.translateToLocal("info.ofalen.future.isValid." + !itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_EXP_DISABLED)));
		}
		stringList.addAll(FilterUtil.getFilterInformation(itemStack));
	}

	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
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
		if (pass == 1) {
			int material = this.getMaterialAmount(stack);
			if (material < OfalenModConfigCore.amountCollectorDamageItem && material < OfalenModConfigCore.amountCollectorDamageExp)
				return iconOverlayLacking;
			// TODO 標準量の設定
			if (material <= 64)
				return iconOverlayWeak;
		}
		return this.getIconIndex(stack);
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
