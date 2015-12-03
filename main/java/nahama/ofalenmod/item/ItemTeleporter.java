package nahama.ofalenmod.item;

import nahama.ofalenmod.Log;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenTeleportHandler;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.tileentity.TileEntityTeleportMarker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemTeleporter extends ItemFuture {

	public ItemTeleporter() {
		super();
		this.setMaxDamage(0);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 違うアイテムなら終了。
		if (!(itemStack.getItem() instanceof ItemTeleporter))
			return itemStack;
		if (player.isSneaking()) {
			// スニークしていたらGUIを開く。
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
		// 材料を消費し、保存。
		material.stackSize--;
		if (material.stackSize < 1)
			material = null;
		if (material != null) {
			NBTTagCompound nbt1 = new NBTTagCompound();
			material.writeToNBT(nbt1);
			itemStack.getTagCompound().setTag("Material", nbt1);
		} else {
			itemStack.getTagCompound().removeTag("Material");;
		}
		OfalenTeleportHandler manager = OfalenTeleportHandler.getInstance(world);
		int channel = itemStack.getItemDamage();
		if (channel < 1 || !manager.isChannelValid(channel)) {
			// チャンネルが無効ならチャットに出力して終了。
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemTeleporter.ChannelInvalid")));
			return itemStack;
		}
		int[] coord = manager.getCoord(channel);
		if (coord == null) {
			// 座標が取得できなかったらログに出力して終了。
			Log.error("Error on getting marker corrd! channel:" + channel, "ItemTeleporter.onItemRightClick", true);
			return itemStack;
		}
		TileEntity tileEntity = world.getTileEntity(coord[0], coord[1], coord[2]);
		if (tileEntity == null || !(tileEntity instanceof TileEntityTeleportMarker)) {
			// テレポートマーカーが取得できなかったらログに出力して終了。
			Log.error("Error on getting TileEntityTeleportMarker! channel:" + channel + ", coord : (" + coord[0] + ", " + coord[1] + ", " + coord[2] + ")", "ItemTeleporter.onItemRightClick", true);
			return itemStack;
		}
		if (!((TileEntityTeleportMarker) tileEntity).canTeleport()) {
			// マーカーがテレポートを許可しなかったらチャットに出力して終了。
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemTeleporter.MarkerInvalid")));
			return itemStack;
		}
		// 問題なければテレポート。
		itemStack.getTagCompound().setByte("Duration", (byte) 10);
		player.mountEntity(null);
		player.setPositionAndUpdate(coord[0] + 0.5, coord[1] + 1.1, coord[2] + 0.5);
		OfalenModCore.wrapper.sendToAll(new MSpawnParticle(coord[0] + 0.5, coord[1] + 1.5, coord[2] + 0.5, (byte) 1));
		return itemStack;
	}

	public boolean canUseItemStack(ItemStack material) {
		if (material == null || !isItemMaterial(material))
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
