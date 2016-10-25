package nahama.ofalenmod.util;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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
}
