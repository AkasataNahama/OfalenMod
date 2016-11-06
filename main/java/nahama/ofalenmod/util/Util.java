package nahama.ofalenmod.util;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.io.InputStream;

public class Util {
	private static Logger logger = LogManager.getLogger(OfalenModCore.MODID);

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void info(String msg, String name) {
		info(msg + " [" + name + "]");
	}

	public static void info(String msg, String name, boolean flag) {
		if (flag)
			info(msg, name);
	}

	public static void error(String msg) {
		logger.error(msg);
	}

	public static void error(String msg, String name) {
		error(msg + " [" + name + "]");
	}

	public static void error(String msg, String name, boolean flag) {
		if (flag)
			error(msg, name);
	}

	public static void debuggingInfo(String msg) {
		if (OfalenModCore.IS_DEBUGGING)
			logger.info(msg + " [DEBUG]");
	}

	public static void debuggingInfo(String msg, String name) {
		debuggingInfo(msg + " [" + name + "]");
	}

	public static void debuggingInfo(String msg, String name, boolean flag) {
		if (flag)
			debuggingInfo(msg, name);
	}

	public static boolean isKeyDown(int key) {
		if (key > 0)
			return Keyboard.isKeyDown(key);
		else
			return Mouse.isButtonDown(100 + key);
	}

	public static int getRemainingDamage(ItemStack itemStack) {
		return itemStack.getMaxDamage() - itemStack.getItemDamage();
	}

	public static int getRemainingItemAmountInInventory(EntityPlayer player, ItemStack sampleStack) {
		int ret = 0;
		int limit = Math.min(sampleStack.getMaxStackSize(), player.inventory.getInventoryStackLimit());
		for (ItemStack invStack : player.inventory.mainInventory) {
			// 空のスロットがあるなら、最大スタック数を足す。
			if (invStack == null) {
				ret += limit;
				continue;
			}
			// 同じアイテムで、メタデータで区別するなら一致していて、NBTが一致しているなら、限界までの量を足す。
			if (invStack.getItem() == sampleStack.getItem() && (!invStack.getHasSubtypes() || invStack.getItemDamage() == sampleStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(invStack, sampleStack)) {
				ret += limit - invStack.stackSize;
			}
		}
		return ret;
	}

	public static EntityItem getEntityItemNearEntity(ItemStack itemStack, Entity entity) {
		return new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, itemStack.copy());
	}

	public static void dropItemStackCopyNearEntity(ItemStack itemStack, Entity entity) {
		entity.worldObj.spawnEntityInWorld(getEntityItemNearEntity(itemStack, entity));
	}

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
			Util.error("Error on reading string.", "OfalenModUpdateCheckCore");
			return null;
		}
	}

	public static String getTripleFiguresNum(int i) {
		i %= 1000;
		String ret = String.valueOf(i);
		while (ret.length() < 3) {
			ret = '0' + ret;
		}
		return ret;
	}
}
