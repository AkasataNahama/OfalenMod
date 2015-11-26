package nahama.ofalenmod.item;

import nahama.ofalenmod.Log;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenTeleportManager;
import nahama.ofalenmod.tileentity.TileEntityTeleportMarker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemTeleporter extends Item {

	private int duration;

	public ItemTeleporter() {
		super();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setMaxDamage(0);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		if (duration > 0)
			duration--;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 違うアイテムなら終了。
		if (itemStack.getItem() != this)
			return itemStack;
		if (player.isSneaking()) {
			// スニークしていたらGUIを開く。
			player.openGui(OfalenModCore.instance, 2, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			return itemStack;
		}
		// クライアントなら終了。
		if (world.isRemote)
			return itemStack;
		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}
		if (!itemStack.getTagCompound().hasKey("Material")) {
			// 材料がないならチャットに出力して終了。
			player.addChatMessage(new ChatComponentText("info.OfalenMod.ItemTeleporter.MaterialLacking"));
			return itemStack;
		}
		ItemStack material = ItemStack.loadItemStackFromNBT(itemStack.getTagCompound().getCompoundTag("Material"));
		if (material == null || !this.isMaterialValid(material)) {
			// 材料が足りないならチャットに出力して終了。
			player.addChatMessage(new ChatComponentText("info.OfalenMod.ItemTeleporter.MaterialLacking"));
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
		OfalenTeleportManager manager = OfalenTeleportManager.getInstance(world);
		int channel = itemStack.getItemDamage();
		if (channel < 0 || !manager.isChannelValid(channel)) {
			// チャンネルが無効ならチャットに出力して終了。
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemTeleporter.ChannelInvalid")));
			return itemStack;
		}
		int[] coord = manager.getCoord(channel);
		if (coord == null) {
			// 座標が取得できなかったらログに出力して終了。
			Log.error("Error on getting marker corrd! channel : " + channel);
			return itemStack;
		}
		TileEntity tileEntity = world.getTileEntity(coord[0], coord[1], coord[2]);
		if (tileEntity == null || !(tileEntity instanceof TileEntityTeleportMarker)) {
			// テレポートマーカーが取得できなかったらログに出力して終了。
			Log.error("Error on getting TileEntityTeleportMarker! channel : " + channel + ", coord : (" + coord[0] + ", " + coord[1] + ", " + coord[2] + ")");
			return itemStack;
		}
		if (!((TileEntityTeleportMarker) tileEntity).onTeleporting()) {
			// マーカーがテレポートを許可しなかったらチャットに出力して終了。
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemTeleporter.MarkerInvalid")));
			return itemStack;
		}
		// 問題なければテレポート。
		duration = 10;
		player.mountEntity(null);
		player.setPositionAndUpdate(coord[0] + 0.5, coord[1] + 1.1, coord[2] + 0.5);
		return itemStack;
	}

	public boolean isMaterialValid(ItemStack material) {
		if (material.getItem() == Items.ender_pearl)
			return true;
		return false;
	}

}
