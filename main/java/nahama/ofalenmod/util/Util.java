package nahama.ofalenmod.util;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
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

	public static void dropItemStackCopyNearEntity(ItemStack itemStack, Entity entity) {
		EntityItem entityItem = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, itemStack.copy());
		entity.worldObj.spawnEntityInWorld(entityItem);
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
}
