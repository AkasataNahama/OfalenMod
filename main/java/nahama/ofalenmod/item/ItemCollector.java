package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.inventory.ContainerItemCollector;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
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

import java.util.ArrayList;
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
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			// プレイヤーが持っていて、コレクターのGUIを開いているなら終了。
			if (player.openContainer != null && player.openContainer instanceof ContainerItemCollector)
				return;
		}
		// 無効時間の残りを取得。
		byte interval = thisStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (interval > 0) {
			// 無効時間が残っていたら減らして終了。
			thisStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, --interval);
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
		ArrayList<Entity> listWaitingEntity = new ArrayList<>();
		// EntityItemとEntityXPOrbがあれば移動する。
		for (Object o : world.loadedEntityList) {
			if (!isItemDisabled && o instanceof EntityItem) {
				// EntityItemにキャスト。
				EntityItem entityItem = (EntityItem) o;
				// 範囲外か、拾えない状態（ドロップされてすぐ）なら次のEntityへ。
				if (entity.getDistanceToEntity(entityItem) > rangeItem || entityItem.delayBeforeCanPickup > 0)
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
					thisStack.setItemDamage(thisStack.getItemDamage() + (eItemStack.stackSize * OfalenModConfigCore.amountCollectorDamageItem));
					continue;
				}
				// 耐久値か空きスロットが足りなかったら足りる分だけ移動して終了。
				ItemStack itemStack1 = eItemStack.copy();
				itemStack1.stackSize = remaining / OfalenModConfigCore.amountCollectorDamageItem;
				listWaitingEntity.add(OfalenUtil.getEntityItemNearEntity(itemStack1, entity));
				eItemStack.stackSize -= remaining / OfalenModConfigCore.amountCollectorDamageItem;
				thisStack.setItemDamage(thisStack.getItemDamage() + (remaining / OfalenModConfigCore.amountCollectorDamageItem * OfalenModConfigCore.amountCollectorDamageItem));
			} else if (!isExpDisabled && (o instanceof EntityXPOrb)) {
				// EntityXPOrbにキャスト。
				EntityXPOrb e = (EntityXPOrb) o;
				// 範囲外なら次のEntityへ。
				if (entity.getDistanceToEntity(e) > rangeExp)
					continue;
				// 耐久値の残りを取得。
				int remaining = OfalenUtil.getRemainingDamage(thisStack);
				// 耐久値が尽きていたら終了。
				if (remaining < 1)
					return;
				if (remaining >= e.xpValue * OfalenModConfigCore.amountCollectorDamageExp) {
					e.setPosition(entity.posX, entity.posY, entity.posZ);
					thisStack.setItemDamage(thisStack.getItemDamage() + (e.xpValue * OfalenModConfigCore.amountCollectorDamageExp));
					continue;
				}
				// 耐久値が足りなかったら足りる分だけ移動して終了。
				listWaitingEntity.add(new EntityXPOrb(world, entity.posX, entity.posY, entity.posZ, remaining / OfalenModConfigCore.amountCollectorDamageExp));
				e.xpValue -= remaining / OfalenModConfigCore.amountCollectorDamageExp;
				thisStack.setItemDamage(thisStack.getItemDamage() + (remaining / OfalenModConfigCore.amountCollectorDamageExp * OfalenModConfigCore.amountCollectorDamageExp));
			}
		}
		// listWaitingEntityに入っているentityをworldにspawnさせる。ConcurrentModificationException回避。
		for (Entity e : listWaitingEntity) {
			world.spawnEntityInWorld(e);
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!OfalenUtil.isKeyDown(OfalenModCore.KEY_OSS.getKeyCode())) {
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
		list.add(StatCollector.translateToLocal("info.ofalen.collector.item") + " : " + (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_ITEM_DISABLED) ? "Off" : "On"));
		list.add(StatCollector.translateToLocal("info.ofalen.collector.exp") + " : " + (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_EXP_DISABLED) ? "Off" : "On"));
		list.addAll(FilterUtil.getFilterInformation(itemStack));
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
}
