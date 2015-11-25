package nahama.ofalenmod.item;

import java.util.List;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.entity.EntityBlueLaser;
import nahama.ofalenmod.entity.EntityGreenLaser;
import nahama.ofalenmod.entity.EntityRedLaser;
import nahama.ofalenmod.entity.EntityWhiteLaser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LaserPistol extends Item {

	/**次のレーザーが撃てるようになるまでのカウント。*/
	private int duration = 0;

	public LaserPistol() {
		super();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setMaxStackSize(1);
		this.setFull3D();
		this.setHasSubtypes(false);
		this.setMaxDamage(1024);
		this.setNoRepair();
	}

	/**クリエイティブタブにアイテムを登録する処理。リロード前で、修繕機での修繕を不可にしたアイテムを登録する。*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		ItemStack itemStack = new ItemStack(this, 1, this.getMaxDamage());
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

	/**更新時の処理。装填時間を減少させる。*/
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		if (world.isRemote && duration > 0) {
			duration --;
		}
	}

	/**右クリック時の処理。レーザーを発射したりマガジンをリロードしたりする。*/
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		//一発も弾が残っていなければ、
		if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
			//プレイヤーがマガジンを所持しているならリロード動作に移る。
			if (this.hasMagazinePlayer(player, itemStack)) player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
			//クライアントの処理なら装填時間を設定する。
			if (world.isRemote)
				duration = 10;
		//次のレーザーを発射できるならば、
		} else if (!world.isRemote || duration <= 0) {
			//LaserColorのNBTTagを持っているならば、
			if (itemStack.hasTagCompound() && itemStack.getTagCompound().getString("LaserColor").length() > 0) {
				String color = itemStack.getTagCompound().getString("LaserColor");
				//クリエイティブモードでないならダメージを与える。
				if (!player.capabilities.isCreativeMode)
					itemStack.setItemDamage(itemStack.getItemDamage() + 32);
				//クライアントの処理なら装填時間を設定する。
				if (world.isRemote)
					duration = 10;
				if (color.equals("Red")) {
					for (int i = -2; i < 3; i ++) {
						world.spawnEntityInWorld(new EntityRedLaser(world, player, i));
					}
					return itemStack;
				} else if (color.equals("Green")) {
					world.spawnEntityInWorld(new EntityGreenLaser(world, player));
					return itemStack;
				} else if (color.equals("Blue")) {
					world.spawnEntityInWorld(new EntityBlueLaser(world, player));
					return itemStack;
				} else if (color.equals("White")) {
					for (int i = -2; i < 3; i ++) {
						world.spawnEntityInWorld(new EntityWhiteLaser(world, player, i));
					}
					return itemStack;
				}
			}
			itemStack.setItemDamage(itemStack.getMaxDamage());
		}
		return itemStack;
	}

	/**プレイヤーがマガジンを持っているかどうか。*/
	private boolean hasMagazinePlayer(EntityPlayer player, ItemStack itemStack) {
		//プレイヤーのインベントリを取得。
		ItemStack[] itemStacks = player.inventory.mainInventory;
		//インベントリの全アイテムを調査し、
		for (int i = 0; i < itemStacks.length; i ++) {
			//マガジンで、充填済みなら、
			if (itemStacks[i] != null && itemStacks[i].getItem() instanceof LaserMagazine && itemStacks[i].getItemDamage() == 0) {
				//trueを返す。
				return true;
			}
		}

		//もしマガジンを持っていなくても、ピストル側にLaserColorが設定されていれば、trueを返す。
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().getString("LaserColor").length() > 0) return true;
		return false;
	}

	/**リロードにかかる時間*/
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 20;
	}

	/**リロード時の動作*/
	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		//剣のガードと同じ動作を返す。
		return EnumAction.block;
	}

	/**アイテムを使った時の処理。ここではリロード完了時の処理を行う。*/
	@Override
	public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
		//NBTTagを作成する。
		NBTTagCompound nbt = new NBTTagCompound();

		//すでに引数のItemStackがNBTTagを保持しているなら、
		if (itemStack.hasTagCompound()) {
			//nbtに代入する。
			nbt = itemStack.getTagCompound();
			//LaserColorに何かの文字が登録されていたら、
			if (nbt.getString("LaserColor").length() > 0 && !world.isRemote) {
				//マガジンをドロップさせ、
				world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(OfalenModItemCore.partsOfalen, 1, 5)));
				//LaserColorをリセットする。
				nbt.setString("LaserColor", "");
				itemStack.setTagCompound(nbt);
			}
		}

		//プレイヤーが所持しているマガジンの色をNBTTagに保存する。
		ItemStack[] inventory = player.inventory.mainInventory;
		for (int i = 0; i < inventory.length; i ++) {
			if (inventory[i] != null && inventory[i].getItem() instanceof LaserMagazine && inventory[i].getItemDamage() == 0) {
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
				nbt.setString("LaserColor", color);
				if (!player.capabilities.isCreativeMode) inventory[i].stackSize --;
				if (inventory[i].stackSize < 1) inventory[i] = null;
				//ダメージ値を全回復させる。
				itemStack.setItemDamage(0);
				itemStack.setTagCompound(nbt);
				return itemStack;
			}
		}

		return itemStack;
	}

	/**説明欄の内容を追加する。*/
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (nbt == null) return;
		String color = nbt.getString("LaserColor");
		if (color.length() < 1) return;
		list.add(StatCollector.translateToLocal("info.color:" + color.toLowerCase()) + " " + StatCollector.translateToLocal("info.loaded") + (32 - (itemStack.getItemDamage() / 32)));
	}

}
