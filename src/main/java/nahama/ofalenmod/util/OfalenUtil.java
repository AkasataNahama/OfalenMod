package nahama.ofalenmod.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class OfalenUtil {
	public static Random random = new Random();

	/** キーバインディングに登録されたキーが押されているか。 */
	public static boolean isKeyPressed(KeyBinding key) {
		// クライアント側なら、取得して返す。
		int code = key.getKeyCode();
		if (code > 0) {
			return Keyboard.isKeyDown(code);
		} else {
			return Mouse.isButtonDown(100 + code);
		}
	}

	/** sampleStackがstacksにあといくつ入るか返す。 */
	public static int getRemainingItemAmountInInventory(ItemStack[] stacks, ItemStack sampleStack, int limitStack) {
		int ret = 0;
		int limit = Math.min(sampleStack.getMaxStackSize(), limitStack);
		for (ItemStack invStack : stacks) {
			// 空のスロットがあるなら、最大スタック数を足す。
			if (invStack == null) {
				ret += limit;
				continue;
			}
			// スタックできるなら、限界までの量を足す。
			if (canStack(invStack, sampleStack)) {
				ret += limit - invStack.stackSize;
			}
		}
		return ret;
	}

	/** stack0とstack1がスタックできるかどうか。 */
	public static boolean canStack(ItemStack stack0, ItemStack stack1) {
		// 同じアイテムで、メタデータで区別するなら一致していて、NBTが一致しているなら、スタック可能。
		return stack0.getItem() == stack1.getItem() && (!stack0.getHasSubtypes() || stack0.getItemDamage() == stack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack0, stack1);
	}

	/** entityの座標にあるitemStackを持ったEntityItemを生成して返す。 */
	public static EntityItem getEntityItemNearEntity(ItemStack itemStack, Entity entity) {
		if (entity.worldObj.isRemote)
			OfalenLog.debuggingInfo("EntityItem mustn't be spawned on the client side.", "OfalenUtil");
		return new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, itemStack);
	}

	/** entityの座標にあるitemStackを持ったEntityItemを生成してワールドにスポーンさせる。 */
	public static void dropItemStackNearEntity(ItemStack itemStack, Entity entity) {
		if (!entity.worldObj.isRemote)
			entity.worldObj.spawnEntityInWorld(getEntityItemNearEntity(itemStack, entity));
	}

	/** 指定されたブロック座標の周囲にitemStackを持ったEntityItemをばらまく。 */
	public static void dropItemStackNearBlock(ItemStack itemStack, World world, int x, int y, int z) {
		itemStack = itemStack.copy();
		float fx = random.nextFloat() * 0.6F + 0.1F;
		float fy = random.nextFloat() * 0.6F + 0.1F;
		float fz = random.nextFloat() * 0.6F + 0.1F;
		while (itemStack.stackSize > 0) {
			int j = random.nextInt(21) + 10;
			if (j > itemStack.stackSize)
				j = itemStack.stackSize;
			itemStack.stackSize -= j;
			EntityItem entityItem = new EntityItem(world, x + fx, y + fy, z + fz, itemStack.copy());
			entityItem.getEntityItem().stackSize = j;
			float f3 = 0.025F;
			entityItem.motionX = (float) random.nextGaussian() * f3;
			entityItem.motionY = (float) random.nextGaussian() * f3 + 0.1F;
			entityItem.motionZ = (float) random.nextGaussian() * f3;
			world.spawnEntityInWorld(entityItem);
		}
	}

	/** 中身のItemStackをコピーした新しい配列を返す。 */
	public static ItemStack[] copyItemStacks(ItemStack[] itemStacks) {
		ItemStack[] ret = new ItemStack[itemStacks.length];
		for (int i = 0; i < itemStacks.length; i++) {
			if (itemStacks[i] != null)
				ret[i] = itemStacks[i].copy();
		}
		return ret;
	}

	/** 一行を読み込み、Stringとして返す。 */
	public static String readString(InputStream inputStream) {
		try {
			byte bytes[] = new byte[2048];
			int reading = inputStream.read();
			if (reading < 0)
				return null;
			int length = 0;
			while (reading > 10) {
				if (reading >= ' ') {
					bytes[length] = (byte) reading;
					length++;
				}
				reading = inputStream.read();
			}
			return new String(bytes, 0, length);
		} catch (IOException e) {
			OfalenLog.error("Error on reading string.", "OfalenUtil");
			return null;
		}
	}

	/** 3桁の文字列に変換して返す。 */
	public static String getTripleFiguresNum(int i) {
		i %= 1000;
		StringBuilder ret = new StringBuilder(String.valueOf(i));
		while (ret.length() < 3) {
			ret.insert(0, '0');
		}
		return ret.toString();
	}

	/** (base)の(index)乗を返す。0未満のindexには未対応。 */
	public static int power(int base, int index) {
		int ret = 1;
		for (int i = 0; i < index; i++) {
			ret *= base;
		}
		return ret;
	}

	/** 翻訳したメッセージをプレイヤーのチャット欄に出力する。 */
	public static void addChatTranslationMessage(EntityPlayer player, String message, Object... objects) {
		if (player.worldObj.isRemote) {
			// クライアントでは無視する。
			OfalenLog.debuggingInfo("addChatTranslationMessage is ignored on client world.", "OfalenUtil");
		} else {
			player.addChatMessage(new ChatComponentTranslation(message, objects));
		}
	}

	/** FMLCommonHandlerを利用したサイドの判定。Minecraft起動時に確定。 */
	public static boolean isClient() {
		return FMLCommonHandler.instance().getSide() == Side.CLIENT;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void add(List list, Object object) {
		try {
			list.add(object);
		} catch (ClassCastException e) {
			OfalenLog.error("Error on class casting.", "OfalenUtil");
			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getAs(List list) {
		return (List<T>) list;
	}

	/**
	 * 与えられたアイテム数を"x Stack y Item"の形で表示した文字列を返す。
	 * @param amount アイテム数
	 * @param limit スタック限界
	 */
	public static String getStackAmountString(int amount, int limit) {
		String string = "";
		int stack = amount / limit;
		if (stack > 0)
			string += stack + " " + StatCollector.translateToLocal("info.ofalen.util.stack") + " ";
		int item = amount % limit;
		if (stack < 1 || item > 0)
			string += item + " " + StatCollector.translateToLocal("info.ofalen.util.item");
		return string;
	}

	public static int getValidValue(int value, int min, int max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	public static double getValidValue(double value, double min, double max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	public static String getColon() {
		return StatCollector.translateToLocal("mark.ofalen.colon") + " ";
	}

	public static String translate(String arg, Object... objects) {
		return String.format(StatCollector.translateToLocal(arg), objects);
	}
}
