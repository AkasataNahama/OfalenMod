package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.entity.EntityLaserBlue;
import nahama.ofalenmod.entity.EntityLaserGreen;
import nahama.ofalenmod.entity.EntityLaserRed;
import nahama.ofalenmod.entity.EntityWhiteLaser;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemLaserPistol extends Item {
	public ItemLaserPistol() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setMaxStackSize(1);
		this.setFull3D();
		this.setHasSubtypes(false);
		this.setMaxDamage(1024);
		this.setNoRepair();
	}

	/** クリエイティブタブにアイテムを登録する処理。 */
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		// リロード前で、修繕機での修繕を不可にしたアイテムを登録する。
		ItemStack itemStack = new ItemStack(this, 1, this.getMaxDamage());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean(OfalenNBTUtil.IS_IRREPARABLE, true);
		itemStack.setTagCompound(nbt);
		OfalenUtil.add(list, itemStack);
	}

	/** 更新時の処理。 */
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		// 時間を減少させる。
		if (itemStack.getItem() != this)
			return;
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		// 修繕機での耐久値回復を無効にする。
		if (!itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_IRREPARABLE))
			itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_IRREPARABLE, true);
		// 無効時間があれば減らす。
		byte interval = itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (interval > 0)
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, --interval);
	}

	/** 右クリック時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		// 一発も弾が残っていなければ、
		if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
			// プレイヤーがマガジンを所持しているならリロード動作に移る。
			if (this.hasMagazinePlayer(player, itemStack))
				player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		} else if (itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL) < 1) {
			// 次のレーザーを発射できるならば、
			if (itemStack.getTagCompound().getString(OfalenNBTUtil.LASER_COLOR).length() > 0) {
				// LaserColorのNBTTagを持っているならば、
				String color = itemStack.getTagCompound().getString(OfalenNBTUtil.LASER_COLOR);
				// サーバー側で、クリエイティブモードでないならダメージを与える。
				if (!world.isRemote && !player.capabilities.isCreativeMode)
					itemStack.setItemDamage(itemStack.getItemDamage() + 32);
				// 連続発射を防止するため、インターバルを設定する。
				itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) 10);
				// 発射音を鳴らす。
				if (!world.isRemote && OfalenModConfigCore.isLaserSoundEnabled) {
					//					world.playSoundAtEntity(player, "mob.irongolem.hit", 2.0F, 100000000000000000.0F);
					world.playSoundAtEntity(player, "ofalenmod:laser", 0.5F, 0.8F - 0.05F + world.rand.nextFloat() * 0.1F);
				}
				// 色に応じてレーザーのEntityをスポーンさせる。レーザーは描画のため両側で必要。
				if (color.equals("Red")) {
					for (int i = -2; i < 3; i++) {
						world.spawnEntityInWorld(new EntityLaserRed(world, player, i));
					}
					return itemStack;
				} else if (color.equals("Green")) {
					world.spawnEntityInWorld(new EntityLaserGreen(world, player));
					return itemStack;
				} else if (color.equals("Blue")) {
					world.spawnEntityInWorld(new EntityLaserBlue(world, player));
					return itemStack;
				} else if (color.equals("White")) {
					for (int i = -2; i < 3; i++) {
						world.spawnEntityInWorld(new EntityWhiteLaser(world, player, i));
					}
					return itemStack;
				}
			}
			// 色がタグに設定されていないか、不正なら耐久値を0にする。
			if (!world.isRemote)
				itemStack.setItemDamage(itemStack.getMaxDamage());
		}
		return itemStack;
	}

	/** プレイヤーがマガジンを持っているかどうか。 */
	private boolean hasMagazinePlayer(EntityPlayer player, ItemStack itemStack) {
		// プレイヤーのインベントリを取得。
		ItemStack[] itemStacks = player.inventory.mainInventory;
		// インベントリの全アイテムを調査し、
		for (ItemStack itemStack1 : itemStacks) {
			// マガジンで、充填済みなら、
			if (itemStack1 != null && itemStack1.getItem() instanceof ItemLaserMagazine && itemStack1.getItemDamage() == 0) {
				// trueを返す。
				return true;
			}
		}
		// もしマガジンを持っていなくても、ピストル側にLaserColorが設定されていれば、trueを返す。
		return itemStack.getTagCompound().getString(OfalenNBTUtil.LASER_COLOR).length() > 0;
	}

	/** リロードにかかる時間を返す。 */
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 20;
	}

	/** リロード時の動作の種類を返す。 */
	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		// 剣のガードと同じ動作を返す。
		return EnumAction.block;
	}

	/** アイテムを使った時の処理。 */
	@Override
	public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			world.playSoundAtEntity(player, "tile.piston.in", 2.0F, 10000000.0F);
		}
		// リロード完了時の処理を行う。
		NBTTagCompound nbt = itemStack.getTagCompound();
		// LaserColorに何かの文字が登録されていたら、
		if (nbt.getString(OfalenNBTUtil.LASER_COLOR).length() > 0) {
			// マガジンをドロップさせ、
			OfalenUtil.dropItemStackNearEntity(new ItemStack(OfalenModItemCore.partsOfalen, 1, 5), player);
			// LaserColorをリセットする。
			nbt.setString(OfalenNBTUtil.LASER_COLOR, "");
		}
		nbt.setByte(OfalenNBTUtil.INTERVAL, (byte) 10);
		// プレイヤーが所持しているマガジンの色をNBTに保存する。
		ItemStack[] inventory = player.inventory.mainInventory;
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null && inventory[i].getItem() instanceof ItemLaserMagazine && inventory[i].getItemDamage() == 0) {
				String color = "";
				if (inventory[i].getItem() == OfalenModItemCore.magazineLaserRed) {
					color = "Red";
				} else if (inventory[i].getItem() == OfalenModItemCore.magazineLaserGreen) {
					color = "Green";
				} else if (inventory[i].getItem() == OfalenModItemCore.magazineLaserBlue) {
					color = "Blue";
				} else if (inventory[i].getItem() == OfalenModItemCore.magazineLaserWhite) {
					color = "White";
				}
				nbt.setString(OfalenNBTUtil.LASER_COLOR, color);
				if (!player.capabilities.isCreativeMode)
					inventory[i].stackSize--;
				if (inventory[i].stackSize < 1)
					inventory[i] = null;
				// ダメージ値を全回復させる。
				if (!world.isRemote)
					itemStack.setItemDamage(0);
				return itemStack;
			}
		}
		return itemStack;
	}

	/** 説明欄の内容を登録する処理。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (nbt == null)
			return;
		String color = nbt.getString(OfalenNBTUtil.LASER_COLOR);
		if (color.length() < 1)
			return;
		OfalenUtil.add(list, StatCollector.translateToLocal("info.ofalen.color:" + color.toLowerCase()) + " " + StatCollector.translateToLocal("info.ofalen.crystal") + " : " + (32 - (itemStack.getItemDamage() / 32)));
	}

	/** ダメージを受けられるかどうか。 */
	@Override
	public boolean isDamageable() {
		return false;
	}
}
