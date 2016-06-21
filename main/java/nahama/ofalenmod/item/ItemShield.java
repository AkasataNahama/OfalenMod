package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenShieldHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemShield extends ItemFuture {

	private IIcon invalid;

	public ItemShield() {
		super();
		this.setMaxDamage(64 * 9);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 違うアイテムなら終了。
		if (itemStack == null || !(itemStack.getItem() instanceof ItemShield))
			return itemStack;
		if (!player.isSneaking()) {
			// スニークしていなかったらGUIを開く。
			player.openGui(OfalenModCore.instance, 2, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			return itemStack;
		}
		// クライアントか、時間がたっていないなら終了。
		if (world.isRemote || itemStack.getTagCompound().getByte("Duration") > 0)
			return itemStack;
		if (itemStack.getTagCompound().getBoolean("IsValid")) {
			OfalenShieldHandler.unprotectPlayer(player);
			itemStack.getTagCompound().setBoolean("IsValid", false);
			itemStack.getTagCompound().setByte("Duration", (byte) 10);
			itemStack.setItemDamage(itemStack.getItemDamage() - OfalenModConfigCore.amountDamageShield);
			return itemStack;
		}
		if (itemStack.getItemDamage() + OfalenModConfigCore.amountDamageShield > itemStack.getMaxDamage()) {
			// 材料がないならチャットに出力して終了。
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemShield.MaterialLacking")));
			return itemStack;
		}
		OfalenShieldHandler.protectPlayer(player);
		itemStack.setItemDamage(itemStack.getItemDamage() + OfalenModConfigCore.amountDamageShield);
		itemStack.getTagCompound().setBoolean("IsValid", true);
		itemStack.getTagCompound().setByte("Duration", (byte) 10);
		return itemStack;
	}

	public static boolean isItemMaterial(ItemStack material) {
		if (material == null)
			return false;
		if (material.isItemEqual(OfalenModItemCore.ingot))
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
