package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenTeleportHandler;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenNBTUtil;
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
		this.setMaxDamage(0);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		ItemStack itemStack = new ItemStack(item);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte(OfalenNBTUtil.MATERIAL, (byte) 64);
		itemStack.setTagCompound(nbt);
		list.add(itemStack);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 違うアイテムなら終了。
		if (!(itemStack.getItem() instanceof ItemTeleporter))
			return itemStack;
		// スニークしていなかったらGUIを開く。
		if (!player.isSneaking()) {
			player.openGui(OfalenModCore.instance, 3, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			return itemStack;
		}
		// クライアントか、時間がたっていないなら終了。
		if (world.isRemote || itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL) > 0)
			return itemStack;
		// 材料がないならチャットに出力して終了。
		if (itemStack.getTagCompound().getByte(OfalenNBTUtil.MATERIAL) < OfalenModConfigCore.amountTeleporterDamage) {
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.ofalen.teleporter.lackingMaterial")));
			return itemStack;
		}
		ItemStack material = new ItemStack(OfalenModItemCore.partsOfalen, itemStack.getTagCompound().getByte(OfalenNBTUtil.MATERIAL), 7);
		short channel = (short) itemStack.getItemDamage();
		// チャンネルが無効ならチャットに出力して終了。
		if (channel < 1 || !OfalenTeleportHandler.isChannelValid(channel)) {
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.ofalen.teleporter.channelInvalid")));
			return itemStack;
		}
		OfalenTeleportHandler.MarkerPos pos = OfalenTeleportHandler.getCoord(channel);
		// 座標が取得できなかったらログに出力して終了。
		if (pos == null) {
			OfalenLog.error("Error on getting marker coord. channel : " + channel, "ItemTeleporter.onItemRightClick");
			return itemStack;
		}
		player.mountEntity(null);
		if (!(player instanceof EntityPlayerMP))
			return itemStack;
		if (player.ridingEntity != null || player.riddenByEntity != null)
			return itemStack;
		// 材料を消費し、保存。
		if (!player.capabilities.isCreativeMode) {
			material.stackSize -= OfalenModConfigCore.amountTeleporterDamage;
			if (material.stackSize < 1)
				material = null;
			if (material != null) {
				itemStack.getTagCompound().setByte(OfalenNBTUtil.MATERIAL, (byte) material.stackSize);
			} else {
				itemStack.getTagCompound().setByte(OfalenNBTUtil.MATERIAL, (byte) 0);
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
		itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) 10);
		OfalenModCore.WRAPPER.sendToAll(new MSpawnParticle(toId, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, (byte) 1));
		return itemStack;
	}

	public static boolean isItemMaterial(ItemStack material) {
		return material != null && material.isItemEqual(new ItemStack(OfalenModItemCore.partsOfalen, 1, 7));
	}
}
