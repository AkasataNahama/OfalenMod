package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenFlightHandlerClient;
import nahama.ofalenmod.network.MSpawnParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemFloater extends ItemFuture {

	private IIcon invalid;

	public ItemFloater() {
		super();
		this.setMaxDamage(64 * 9);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		if (entity.onGround || !(entity instanceof EntityPlayer) || ((EntityPlayer) entity).capabilities.isCreativeMode)
			return;
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (nbt.getByte("Mode") > 0 && nbt.getByte("Duration") < 1) {
			if (!world.isRemote)
				OfalenModCore.wrapper.sendToAll(new MSpawnParticle(entity.posX, entity.posY - 1.6D, entity.posZ, (byte) 2));
			itemStack.setItemDamage(itemStack.getItemDamage() + 1);
			if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
				nbt.setByte("Mode", (byte) 0);
				((EntityPlayer) entity).addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemFloater.MaterialLacking")));
				if (world.isRemote && entity == Minecraft.getMinecraft().thePlayer) {
					OfalenFlightHandlerClient.forbidPlayerToFloat();
				}
				return;
			}
			nbt.setByte("Duration", (byte) 20);
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 違うアイテムなら終了。
		if (itemStack == null || !(itemStack.getItem() instanceof ItemFloater))
			return itemStack;
		// GUIを開く。
		player.openGui(OfalenModCore.instance, 4, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return itemStack;
	}

	public static boolean isItemMaterial(ItemStack material) {
		if (material == null)
			return false;
		if (material.isItemEqual(OfalenModItemCore.dust))
			return true;
		return false;
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
		invalid = register.registerIcon(this.getIconString() + "0");
	}

	@Override
	public IIcon getIconIndex(ItemStack itemStack) {
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("IsValid"))
			return super.getIconIndex(itemStack);
		return invalid;
	}

}
