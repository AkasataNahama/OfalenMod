package nahama.ofalenmod.item;

import cpw.mods.fml.common.FMLCommonHandler;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.handler.OfalenTeleportHandler;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import nahama.ofalenmod.world.TeleporterOfalen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

public class ItemTeleporter extends ItemFuture {
	public ItemTeleporter() {
		this.setMaxDamage(0);
	}

	/** クリエイティブタブにアイテムを登録する。 */
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		ItemStack itemStack = new ItemStack(item);
		this.setMaterialAmount(itemStack, OfalenModConfigCore.amountTeleporterDamage * 64 * 9 * 6);
		OfalenUtil.add(list, itemStack);
	}

	/** 右クリック時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		if (!OfalenKeyHandler.isSettingKeyPressed(player)) {
			// 設定キーが押されていないとき。
			if (!player.isSneaking()) {
				// スニークしていなかったらGUIを開く。
				player.openGui(OfalenModCore.instance, 3, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			} else {
				// クライアントか、時間がたっていないなら終了。
				if (world.isRemote || itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK) > 0)
					return itemStack;
				itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK, (byte) 10);
				// スニークしていたらテレポートする。
				if (this.getMaterialAmount(itemStack) < OfalenModConfigCore.amountTeleporterDamage) {
					// 材料がないならチャットに出力して終了。
					OfalenUtil.addChatTranslationMessage(player, "info.ofalen.future.lackingMaterial", new ItemStack(OfalenModItemCore.teleporterOfalen).getDisplayName(), new ItemStack(OfalenModItemCore.partsOfalen, 1, 7).getDisplayName());
					return itemStack;
				}
				short channel = (short) itemStack.getItemDamage();
				if (!OfalenTeleportHandler.isChannelValid(channel)) {
					// チャンネルが無効ならチャットに出力して終了。
					OfalenUtil.addChatTranslationMessage(player, "info.ofalen.teleporter.channelInvalid");
					return itemStack;
				}
				OfalenTeleportHandler.MarkerPos pos = OfalenTeleportHandler.getCoord(channel);
				if (pos == null) {
					// 座標が取得できなかったらログに出力して終了。
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
					this.consumeMaterial(itemStack, OfalenModConfigCore.amountTeleporterDamage);
				}
				// 問題なければテレポート。
				byte toId = pos.getId();
				if (player.dimension != toId) {
					EntityPlayerMP playerMP = (EntityPlayerMP) player;
					if (player.dimension == 1) {
						this.teleportPlayerFromEndDangerously(playerMP, toId);
					} else {
						playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, toId, new TeleporterOfalen(playerMP.mcServer.worldServerForDimension(toId)));
					}
					playerMP.addExperienceLevel(0);
				}
				player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5);
				player.setSneaking(false);
				if (OfalenModConfigCore.isTeleporterParticleEnabled)
					OfalenModPacketCore.WRAPPER.sendToAll(new MSpawnParticle(toId, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, (byte) 1));
			}
		} else {
			// 時間がたっていないなら終了。
			if (itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK) > 0)
				return itemStack;
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK, (byte) 10);
			// 設定キーが押されていれば、パールの補充・取り出し。
			if (!player.isSneaking()) {
				// しゃがんでいなければ、補充。
				this.chargeMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 7), player);
			} else {
				// しゃがんでいれば、取り出し。
				this.dropMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 7), player);
			}
		}
		return itemStack;
	}

	/** プレイヤーをエンドから別のディメンションにテレポートさせる。 */
	private void teleportPlayerFromEndDangerously(EntityPlayerMP player, int toId) {
		// ServerConfigurationManager.transferPlayerToDimensionで行われているのと同様の処理。
		// 基本的には1。
		int fromId = player.dimension;
		player.dimension = toId;
		WorldServer worldServerEnd = player.mcServer.worldServerForDimension(fromId);
		WorldServer worldServerDestination = player.mcServer.worldServerForDimension(toId);
		player.playerNetServerHandler.sendPacket(new S07PacketRespawn(toId, worldServerDestination.difficultySetting, worldServerDestination.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
		worldServerEnd.removePlayerEntityDangerously(player);
		player.isDead = false;
		// ここからServerConfigurationManager.transferEntityToWorld。
		// この前後にあったネザー用座標変換処理は省略。
		// ここにあったエンド（及びコメントアウトされていた通常世界・ネザー）からの移動時処理は省略。
		// ここからエンドからの移動時に実行されていなかった部分。
		worldServerEnd.theProfiler.startSection("placing");
		if (player.isEntityAlive()) {
			// ここにあった座標変換適用処理は省略。
			new TeleporterOfalen(worldServerDestination).placeInPortal(player, player.posX, player.posY, player.posZ, player.rotationYaw);
			worldServerDestination.spawnEntityInWorld(player);
			worldServerDestination.updateEntityWithOptionalForce(player, false);
		}
		worldServerEnd.theProfiler.endSection();
		// ここまでエンドからの移動時に実行されていなかった部分。
		player.setWorld(worldServerDestination);
		// ここまでServerConfigurationManager.transferEntityToWorld。
		player.mcServer.getConfigurationManager().func_72375_a(player, worldServerEnd);
		player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		player.theItemInWorldManager.setWorld(worldServerDestination);
		player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, worldServerDestination);
		player.mcServer.getConfigurationManager().syncPlayerInventory(player);
		for (Object o : player.getActivePotionEffects()) {
			PotionEffect potioneffect = (PotionEffect) o;
			player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
		}
		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, fromId, toId);
	}

	/** テクスチャを登録する。 */
	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
		itemIcon = register.registerIcon(this.getIconString());
	}

	/** テクスチャを返す。 */
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if (pass == 1) {
			int material = this.getMaterialAmount(stack);
			if (material < OfalenModConfigCore.amountTeleporterDamage)
				return iconOverlayLacking;
			if (material <= OfalenModConfigCore.amountTeleportingPearlReference)
				return iconOverlayWeak;
		}
		return super.getIcon(stack, pass);
	}

	/** Tooltipに情報を追加する。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		List<String> stringList = OfalenUtil.getAs(list);
		int amount = this.getMaterialAmount(itemStack);
		stringList.add(OfalenUtil.getStackAmountString(amount, 64) + " (" + amount + " / " + OfalenModConfigCore.amountTeleportingPearlReference + ")");
		stringList.add(StatCollector.translateToLocal("info.ofalen.teleporter.channel") + " " + itemStack.getItemDamage());
	}
}
