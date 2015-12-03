package nahama.ofalenmod.handler;

import java.util.ArrayList;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.inventory.ContainerItemShield;
import nahama.ofalenmod.item.ItemShield;
import nahama.ofalenmod.network.MSpawnParticle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class OfalenShieldHandler {

	private static OfalenShieldHandler instance = new OfalenShieldHandler();
	private ArrayList<String> protectedPlayers = new ArrayList<String>();

	public static void init() {
		instance = new OfalenShieldHandler();
	}

	public static OfalenShieldHandler getInstance() {
		return instance;
	}

	public static void checkPlayer(EntityPlayer player) {
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null || !(itemStack.getItem() instanceof ItemShield) || !itemStack.hasTagCompound())
				continue;
			if (itemStack.getTagCompound().getBoolean("IsValid"))
				instance.protectPlayer(player);
		}
	}

	public void protectPlayer(EntityPlayer player) {
		protectedPlayers.add(player.getCommandSenderName());
	}

	public void unprotectPlayer(EntityPlayer player) {
		protectedPlayers.remove(player.getCommandSenderName());
	}

	public boolean isProtecting(EntityPlayer player) {
		return protectedPlayers.contains(player.getCommandSenderName());
	}

	public void onProtect(EntityPlayer player) {
		if (player.openContainer != null && player.openContainer instanceof ContainerItemShield) {
			player.closeScreen();
		}
		boolean flag = false;
		IInventory inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack == null || !(itemStack.getItem() instanceof ItemShield) || !itemStack.hasTagCompound())
				continue;
			if (!itemStack.getTagCompound().getBoolean("IsValid"))
				continue;
			if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
				itemStack.getTagCompound().setBoolean("IsValid", false);
				continue;
			}
			itemStack.setItemDamage(itemStack.getItemDamage() + 1);
			flag = true;
			if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
				itemStack.getTagCompound().setBoolean("IsValid", false);
			}
			break;
		}
		if (!flag) {
			this.unprotectPlayer(player);
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemShield.ShieldBreaked")));
		}
		OfalenModCore.wrapper.sendToAll(new MSpawnParticle(player.posX, player.posY, player.posZ, (byte) 0));
	}

}
