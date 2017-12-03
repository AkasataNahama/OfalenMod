package nahama.ofalenmod.handler;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.entity.EntityLaserBase;
import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.Iterator;

public class OfalenModEventHandler {
	/** Entityがワールドに追加された時の処理。 */
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!(event.entity instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) event.entity;
		// プレイヤーの時。
		if (!event.world.isRemote) {
			// サーバー側
			// プレゼントの調査。
			OfalenModAnniversaryHandler.checkPlayer(player);
			if (OfalenModUpdateCheckHandler.isNewVersionAvailable && !OfalenModUpdateCheckHandler.namesNotified.contains(player.getCommandSenderName())) {
				// 最新バージョンの通知をする。
				OfalenUtil.addChatTranslationMessage(player, "info.ofalen.notificationVersion");
				player.addChatMessage(new ChatComponentText(OfalenModUpdateCheckHandler.getMessage()));
				OfalenModUpdateCheckHandler.namesNotified.add(player.getCommandSenderName());
			}
		}
	}

	/** プレイヤーがワールドに入った時の処理 */
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		OfalenFlightHandlerServer.onPlayerLoggedIn(event.player);
	}

	/** プレイヤーがワールドから出た時の処理。 */
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		OfalenLog.debuggingInfo("onPlayerLoggedOut");
		OfalenFlightHandlerServer.onPlayerLoggedOut(event.player);
	}

	/** EntityLivingBaseがダメージを受けた時の処理。 */
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		// キャンセル済みか、プレイヤー以外なら終了。サーバー側のみで呼ばれる。
		if (event.isCanceled() || !(event.entityLiving instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) event.entityLiving;
		// プロテクターがダメージを軽減する。
		event.ammount = OfalenProtectHandler.onProtect(player, event.ammount);
	}

	/** EntityLivingBaseのアップデート時の処理。 */
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.entityLiving.worldObj.isRemote) {
			// クライアントでは、プレイヤーで、有効なら飛行させる。
			if (OfalenFlightHandlerClient.isPlayer(event.entityLiving) && OfalenFlightHandlerClient.canFloat())
				OfalenFlightHandlerClient.floatPlayer();
		} else {
			// サーバーでは、プレイヤーなら更新する。
			if (event.entityLiving instanceof EntityPlayer)
				OfalenFlightHandlerServer.onUpdatePlayer((EntityPlayer) event.entityLiving);
		}
	}

	/** EntityLivingBaseが落下した時の処理。 */
	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event) {
		// キャンセル済みか、プレイヤー以外なら終了。
		if (event.isCanceled() || !(event.entityLiving instanceof EntityPlayer))
			return;
		if (event.entityLiving.worldObj.isRemote) {
			// クライアント側では、フローターが有効なら落下（音とか）をキャンセル。
			if (OfalenFlightHandlerClient.isPlayer(event.entityLiving) && OfalenFlightHandlerClient.canFloat())
				event.setCanceled(true);
		} else {
			// サーバー側では、フローターが有効なら落下（ダメージとか）をキャンセル。
			if (OfalenFlightHandlerServer.canFlightPlayer((EntityPlayer) event.entityLiving))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(OfalenModCore.MOD_ID))
			OfalenModConfigCore.syncConfig();
	}

	/** クライアントのキー入力更新時の処理。 */
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		OfalenKeyHandler.update();
	}

	/** 爆発時の処理。 */
	@SubscribeEvent
	public void onExplosionDetonate(ExplosionEvent.Detonate event) {
		// 爆発に当たったEntityのリストを取得する。
		Iterator<Entity> itr = event.getAffectedEntities().iterator();
		while (itr.hasNext()) {
			Entity e = itr.next();
			if (e instanceof EntityLaserBase) {
				// レーザーだったら除外する。（白レーザーが爆発の影響を受けずに進めるように。）
				itr.remove();
			}
		}
	}
}
