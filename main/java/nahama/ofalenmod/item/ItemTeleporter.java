package nahama.ofalenmod.item;

import nahama.ofalenmod.Log;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenTeleportHandler;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.world.TeleporterOfalen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemTeleporter extends ItemFuture {

	public ItemTeleporter() {
		super();
		this.setMaxDamage(0);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		ItemStack itemStack = new ItemStack(item);
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound nbt1 = new NBTTagCompound();
		new ItemStack(OfalenModItemCore.partsOfalen, 64, 7).writeToNBT(nbt1);
		nbt.setTag("Material", nbt1);
		itemStack.setTagCompound(nbt);
		list.add(itemStack);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 違うアイテムなら終了。
		if (!(itemStack.getItem() instanceof ItemTeleporter))
			return itemStack;
		if (!player.isSneaking()) {
			// スニークしていなかったらGUIを開く。
			player.openGui(OfalenModCore.instance, 3, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			return itemStack;
		}
		// クライアントか、時間がたっていないなら終了。
		if (world.isRemote || itemStack.getTagCompound().getByte("Duration") > 0)
			return itemStack;
		if (!itemStack.getTagCompound().hasKey("Material")) {
			// 材料がないならチャットに出力して終了。
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemTeleporter.MaterialLacking")));
			return itemStack;
		}
		ItemStack material = ItemStack.loadItemStackFromNBT(itemStack.getTagCompound().getCompoundTag("Material"));
		if (material == null || !this.canUseItemStack(material)) {
			// 材料が足りないならチャットに出力して終了。
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemTeleporter.MaterialLacking")));
			return itemStack;
		}
		int channel = itemStack.getItemDamage();
		if (channel < 1 || !OfalenTeleportHandler.isChannelValid(channel)) {
			// チャンネルが無効ならチャットに出力して終了。
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemTeleporter.ChannelInvalid")));
			return itemStack;
		}
		OfalenTeleportHandler.MarkerPos pos = OfalenTeleportHandler.getCoord(channel);
		if (pos == null) {
			// 座標が取得できなかったらログに出力して終了。
			Log.error("Error on getting marker coord! channel:" + channel, "ItemTeleporter.onItemRightClick", true);
			return itemStack;
		}
		player.mountEntity(null);
		if (!(player instanceof EntityPlayerMP))
			return itemStack;
		if (player.ridingEntity != null || player.riddenByEntity != null)
			return itemStack;
		// 材料を消費し、保存。
		if (!player.capabilities.isCreativeMode) {
			material.stackSize -= OfalenModConfigCore.amountDamageTeleporter;
			if (material.stackSize < 1)
				material = null;
			if (material != null) {
				NBTTagCompound nbt1 = new NBTTagCompound();
				material.writeToNBT(nbt1);
				itemStack.getTagCompound().setTag("Material", nbt1);
			} else {
				itemStack.getTagCompound().removeTag("Material");
			}
		}
		// 問題なければテレポート。
		byte toId = pos.getId();
		if (player.worldObj.provider.dimensionId != toId) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, toId, new TeleporterOfalen(playerMP.mcServer.worldServerForDimension(toId)));
			playerMP.addExperienceLevel(0);
		}
		player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5);
		player.setSneaking(false);
		itemStack.getTagCompound().setByte("Duration", (byte) 10);
		OfalenModCore.wrapper.sendToAll(new MSpawnParticle(toId, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, (byte) 1));
		return itemStack;
	}

	private boolean canUseItemStack(ItemStack material) {
		if (material == null || !isItemMaterial(material))
			return false;
		if (material.stackSize < OfalenModConfigCore.amountDamageTeleporter)
			return false;
		return true;
	}

	public static boolean isItemMaterial(ItemStack material) {
		if (material == null)
			return false;
		if (material.isItemEqual(OfalenModItemCore.pearl))
			return true;
		return false;
	}

}
