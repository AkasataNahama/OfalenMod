package nahama.ofalenmod.item;

import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.setting.IItemOfalenSettable;
import nahama.ofalenmod.setting.OfalenSettingBoolean;
import nahama.ofalenmod.setting.OfalenSettingByte;
import nahama.ofalenmod.setting.OfalenSettingCategory;
import nahama.ofalenmod.setting.OfalenSettingCategoryOrigin;
import nahama.ofalenmod.setting.OfalenSettingContent;
import nahama.ofalenmod.setting.OfalenSettingFloat;
import nahama.ofalenmod.util.EntityRange;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ItemCollector extends ItemFuture implements IItemOfalenSettable {
	private IIcon[] icons;
	private OfalenSettingCategoryOrigin settingCategoryOrigin;
	private OfalenSettingBoolean settingAutoStopping;
	private OfalenSettingByte settingInterval;
	private OfalenSettingCategory categoryItemRange;
	private OfalenSettingCategory categoryExpRange;

	public ItemCollector() {
		super();
		OfalenTimer.start("ItemCollector.init");
		// 詳細設定の初期化を行う。
		settingCategoryOrigin = new OfalenSettingCategoryOrigin("OfalenCollector", new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE));
		// 自動停止機能が有効か。
		settingAutoStopping = new OfalenSettingBoolean("AutoStopping", new ItemStack(Blocks.redstone_torch), true);
		settingCategoryOrigin.registerChildSetting(settingAutoStopping);
		// インターバル。
		settingInterval = new OfalenSettingByte("Interval", new ItemStack(Items.quartz), 10, 0);
		settingCategoryOrigin.registerChildSetting(settingInterval);
		// アイテム。
		OfalenSettingCategory categoryItem = new OfalenSettingCategory("Item", new ItemStack(OfalenModItemCore.partsOfalen, 1, 9));
		settingCategoryOrigin.registerChildSetting(categoryItem);
		// 範囲。
		categoryItemRange = new OfalenSettingCategory("Range", new ItemStack(OfalenModBlockCore.surveyorOfalen));
		categoryItem.registerChildSetting(categoryItemRange);
		categoryItemRange.registerChildSetting(new OfalenSettingDesignationMode("DesignationMode", new ItemStack(Items.stick), 0));
		categoryItemRange.registerChildSetting(new OfalenSettingFloat("Length-0", new ItemStack(OfalenModItemCore.gemOfalen, 1, 0), 10, 0));
		categoryItemRange.registerChildSetting(new OfalenSettingFloat("Length-1", new ItemStack(OfalenModItemCore.gemOfalen, 1, 1), 10, 0));
		categoryItemRange.registerChildSetting(new OfalenSettingFloat("Length-2", new ItemStack(OfalenModItemCore.gemOfalen, 1, 2), 10, 0));
		// 経験値。
		OfalenSettingCategory categoryExp = new OfalenSettingCategory("Exp", new ItemStack(Items.book));
		settingCategoryOrigin.registerChildSetting(categoryExp);
		// 範囲。
		categoryExpRange = new OfalenSettingCategory("Range", new ItemStack(OfalenModBlockCore.surveyorOfalen));
		categoryExp.registerChildSetting(categoryExpRange);
		categoryExpRange.registerChildSetting(new OfalenSettingDesignationMode("DesignationMode", new ItemStack(Items.stick), 0));
		categoryExpRange.registerChildSetting(new OfalenSettingFloat("Length-0", new ItemStack(OfalenModItemCore.gemOfalen, 1, 0), 10, 0));
		categoryExpRange.registerChildSetting(new OfalenSettingFloat("Length-1", new ItemStack(OfalenModItemCore.gemOfalen, 1, 1), 10, 0));
		categoryExpRange.registerChildSetting(new OfalenSettingFloat("Length-2", new ItemStack(OfalenModItemCore.gemOfalen, 1, 2), 10, 0));
		OfalenTimer.watchAndLog("ItemCollector.init");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack itemStack = new ItemStack(item);
		// TODO 標準量を使用
		this.setMaterialAmount(itemStack, 64 * 9 * 6);
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
		FilterUtil.onUpdateFilter(itemStack);
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
		// 無効時間をリセット。
		byte intervalMax = settingInterval.getValueByStack(itemStack);
		itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, intervalMax);
		// アイテムの範囲を設定する。
		EntityRange rangeItemMin = EntityRange.getRangeByType(player, 0, 2);
		double[] lengths = new double[] { this.getLengthByNumber(itemStack, categoryItemRange, 0), this.getLengthByNumber(itemStack, categoryItemRange, 1), this.getLengthByNumber(itemStack, categoryItemRange, 2) };
		EntityRange rangeItem = EntityRange.getRangeByType(player, ((OfalenSettingDesignationMode) categoryItemRange.getChildSetting("DesignationMode")).getValueByStack(itemStack), lengths);
		// 経験値の範囲を設定する。
		EntityRange rangeExpMin = EntityRange.getRangeByType(player, 0, 4);
		lengths = new double[] { this.getLengthByNumber(itemStack, categoryExpRange, 0), this.getLengthByNumber(itemStack, categoryExpRange, 1), this.getLengthByNumber(itemStack, categoryExpRange, 2) };
		EntityRange rangeExp = EntityRange.getRangeByType(player, ((OfalenSettingDesignationMode) categoryExpRange.getChildSetting("DesignationMode")).getValueByStack(itemStack), lengths);
		// 後でスポーンさせるEntityのリスト。
		ArrayList<Entity> listWaitingEntity = new ArrayList<Entity>();
		// EntityItemとEntityXPOrbがあれば移動する。
		for (Object o : world.loadedEntityList) {
			// 材料が残っていなかったら終了。
			if (this.getMaterialAmount(itemStack) < amountMin)
				break;
			if (!isItemDisabled && o instanceof EntityItem) {
				// EntityItemにキャスト。
				EntityItem entityItem = (EntityItem) o;
				// 拾えない状態（ドロップされてすぐ）か、範囲外なら次のEntityへ。
				if (entityItem.delayBeforeCanPickup > 0 || rangeItemMin.isInRange(entityItem) || !rangeItem.isInRange(entityItem))
					continue;
				ItemStack eItemStack = entityItem.getEntityItem();
				// アイテムフィルターで許可されていなかったら次のEntityへ。
				if (!FilterUtil.canItemFilterThrough(FilterUtil.getFilterTag(itemStack), eItemStack))
					continue;
				// 材料数を取得。
				int amount = this.getMaterialAmount(itemStack);
				// 回収量の上限値。
				int limit = Integer.MAX_VALUE;
				// 詳細設定で有効ならインベントリの空きスロット数を取得。
				if (settingAutoStopping.getValueByStack(itemStack))
					limit = OfalenUtil.getRemainingItemAmountInInventory(player.inventory.mainInventory, eItemStack, player.inventory.getInventoryStackLimit());
				// 材料の限界も考慮。
				if (OfalenModConfigCore.amountCollectorDamageItem > 0)
					limit = Math.min(limit, amount / OfalenModConfigCore.amountCollectorDamageItem);
				// 一個も移動できないなら次のEntityへ。
				if (limit < 1)
					continue;
				// スタック数が限界以下ならそのまま移動。
				if (eItemStack.stackSize <= limit) {
					entityItem.setPosition(player.posX, player.posY, player.posZ);
					entityItem.motionX = 0.0;
					entityItem.motionY = 0.0;
					entityItem.motionZ = 0.0;
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
				EntityXPOrb entityXPOrb = (EntityXPOrb) o;
				// 範囲外なら次のEntityへ。
				if (rangeExpMin.isInRange(entityXPOrb) || !rangeExp.isInRange(entityXPOrb))
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
				if (entityXPOrb.xpValue <= limit) {
					entityXPOrb.setPosition(player.posX, player.posY, player.posZ);
					entityXPOrb.motionX = 0.0;
					entityXPOrb.motionY = 0.0;
					entityXPOrb.motionZ = 0.0;
					if (canDamage)
						this.consumeMaterial(itemStack, entityXPOrb.xpValue * OfalenModConfigCore.amountCollectorDamageExp);
					continue;
				}
				// 材料が足りなかったら足りる分だけ移動して終了。
				listWaitingEntity.add(new EntityXPOrb(world, player.posX, player.posY, player.posZ, limit));
				entityXPOrb.xpValue -= limit;
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

	private float getLengthByNumber(ItemStack itemStack, OfalenSettingCategory category, int num) {
		return ((OfalenSettingFloat) category.getChildSetting("Length-" + num)).getValueByStack(itemStack);
	}

	/** 右クリック時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 時間がたっていないなら終了。
		if (itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK) > 0)
			return itemStack;
		itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK, (byte) 10);
		if (!OfalenKeyHandler.isSettingKeyPressed(player)) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			if (!player.isSneaking()) {
				// 右クリックでアイテムのOn/Off。
				nbt.setBoolean(OfalenNBTUtil.IS_ITEM_DISABLED, !nbt.getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED));
			} else {
				// Shift + 右クリックで経験値のOn/Off。
				nbt.setBoolean(OfalenNBTUtil.IS_EXP_DISABLED, !nbt.getBoolean(OfalenNBTUtil.IS_EXP_DISABLED));
			}
		} else {
			// 設定キーが押されていれば、ランプの補充・取り出し。
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
		int amount = this.getMaterialAmount(itemStack);
		stringList.add(OfalenUtil.getStackAmountString(amount, 64) + " (" + amount + " / " + OfalenModConfigCore.amountCollectingLumpReference + ")");
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
			if (material <= OfalenModConfigCore.amountCollectingLumpReference)
				return iconOverlayWeak;
		}
		return this.getIconIndex(stack);
	}

	@Override
	public OfalenSettingCategoryOrigin getSetting() {
		return settingCategoryOrigin;
	}

	private static class OfalenSettingDesignationMode extends OfalenSettingContent<Byte> {
		public OfalenSettingDesignationMode(String name, ItemStack stack, int valueDefault) {
			super(name, stack, (byte) valueDefault);
		}

		@Override
		public NBTBase getTagFromValue(Byte value) {
			return new NBTTagByte(value);
		}

		@Override
		public Byte getValueFromTag(NBTBase nbt) {
			byte value = valueDefault;
			if (nbt instanceof NBTBase.NBTPrimitive)
				value = ((NBTBase.NBTPrimitive) nbt).func_150290_f();
			return getValidValue(value);
		}

		@Override
		public Byte getChangedValue(Byte current, ItemStack stackSpecifier) {
			int value = valueDefault;
			if (stackSpecifier.getItem() == OfalenModItemCore.gemOfalen)
				value = stackSpecifier.getItemDamage();
			return getValidValue(value);
		}

		@Override
		public List<ItemStack> getSelectableItemList() {
			List<ItemStack> list = new ArrayList<ItemStack>();
			list.add(new ItemStack(OfalenModItemCore.gemOfalen, 1, 0));
			list.add(new ItemStack(OfalenModItemCore.gemOfalen, 1, 1));
			list.add(new ItemStack(OfalenModItemCore.gemOfalen, 1, 2));
			list.add(new ItemStack(Blocks.dirt));
			return list;
		}

		private byte getValidValue(int value) {
			value = Math.max(0, value);
			value = Math.min(2, value);
			return (byte) value;
		}

		@Override
		protected String getMessageFromValue(Byte value) {
			if (value != null) {
				switch (value) {
				case 0:
					return StatCollector.translateToLocal(LANGUAGE_PREFIX + "collector.sphere");
				case 1:
					return StatCollector.translateToLocal(LANGUAGE_PREFIX + "collector.cylinder");
				case 2:
					return StatCollector.translateToLocal(LANGUAGE_PREFIX + "collector.cuboid");
				}
			}
			return super.getMessageFromValue(value);
		}
	}
}
