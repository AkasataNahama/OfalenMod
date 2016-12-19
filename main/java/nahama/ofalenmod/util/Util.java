package nahama.ofalenmod.util;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Util {
	public static Random random = new Random();
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

	public static boolean canStack(ItemStack stack0, ItemStack stack1) {
		// 同じアイテムで、メタデータで区別するなら一致していて、NBTが一致しているなら、スタック可能。
		return stack0.getItem() == stack1.getItem() && (!stack0.getHasSubtypes() || stack0.getItemDamage() == stack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack0, stack1);
	}

	public static EntityItem getEntityItemNearEntity(ItemStack itemStack, Entity entity) {
		return new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, itemStack);
	}

	public static void dropItemStackNearEntity(ItemStack itemStack, Entity entity) {
		entity.worldObj.spawnEntityInWorld(getEntityItemNearEntity(itemStack, entity));
	}

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
			Util.error("Error on reading string.", "Util");
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

	/** (base)の(index)乗を返す。0未満のindexには未対応。 */
	public static int power(int base, int index) {
		int ret = 1;
		for (int i = 0; i < index; i++) {
			ret *= base;
		}
		return ret;
	}

	public static void addChatMessage(EntityPlayer player, String message) {
		player.addChatMessage(new ChatComponentText(message));
	}
}
