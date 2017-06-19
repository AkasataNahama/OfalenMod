package nahama.ofalenmod.handler;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.entity.EntityLaserBase;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
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

	/** EntityLivingBaseがダメージを受けた時の処理。 */
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		if (event.entityLiving.worldObj.isRemote || event.isCanceled() || !event.isCancelable() || !(event.entityLiving instanceof EntityPlayer))
			return;
		if (event.source == DamageSource.outOfWorld)
			return;
		// サーバー側で、キャンセル可能で、プレイヤーが奈落ダメージ以外のダメージを受けた時。
		// シールドが有効なプレイヤーならダメージを無効化。
		if (OfalenShieldHandler.onProtect((EntityPlayer) event.entityLiving))
			event.setCanceled(true);
	}

	/** EntityLivingBaseのアップデート時の処理。 */
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (!event.entityLiving.worldObj.isRemote || !OfalenFlightHandlerClient.isPlayer(event.entityLiving))
			return;
		// クライアント側で、本人プレイヤーの時。（マルチプレイでの別プレイヤーでない時。）
		// 浮遊が許可されていないなら終了。
		if (!OfalenFlightHandlerClient.canFloat())
			return;
		OfalenFlightHandlerClient.floatPlayer();
	}

	/** EntityLivingBaseが落下した時の処理。 */
	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event) {
		// キャンセル不可能か、プレイヤー以外なら終了。
		if (event.isCanceled() || !event.isCancelable() || !(event.entityLiving instanceof EntityPlayer))
			return;
		if (event.entityLiving.worldObj.isRemote) {
			// クライアント側では、フローターが有効なら落下（音とか）をキャンセル。
			if (OfalenFlightHandlerClient.isPlayer(event.entityLiving) && OfalenFlightHandlerClient.canFloat())
				event.setCanceled(true);
			return;
		}
		// サーバー側では、フローターが有効なら落下（ダメージとか）をキャンセル。
		if (OfalenFlightHandlerServer.canFlightPlayer((EntityPlayer) event.entityLiving))
			event.setCanceled(true);
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
