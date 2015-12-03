package nahama.ofalenmod.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import nahama.ofalenmod.handler.OfalenFlightHandlerClient;
import nahama.ofalenmod.handler.OfalenFlightHandlerServer;
import nahama.ofalenmod.handler.OfalenShieldHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class OfalenModEventCore {

	/** Entityがワールドにスポーン/ログインした時の処理。 */
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!(event.entity instanceof EntityPlayer))
			return;
		// プレイヤーの時。
		if (!event.world.isRemote) {
			// サーバー側
			OfalenShieldHandler.checkPlayer((EntityPlayer) event.entity);
		} else {
			// クライアント側
			if (event.entity == Minecraft.getMinecraft().thePlayer) {
				OfalenFlightHandlerClient.init();
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
		EntityPlayer player = (EntityPlayer) event.entityLiving;
		if (!OfalenShieldHandler.getInstance().isProtecting(player))
			return;
		event.setCanceled(true);
		OfalenShieldHandler.getInstance().onProtect(player);
	}

	/** EntityLivingBaseのアップデート時の処理。 */
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (!event.entityLiving.worldObj.isRemote || !OfalenFlightHandlerClient.isPlayer(event.entityLiving))
			return;
		// クライアント側で、本人プレイヤーの時。（マルチプレイでの別プレイヤーでない時。）
		if (!OfalenFlightHandlerClient.canFloat())
			return;
		OfalenFlightHandlerClient.floatPlayer();
	}

	/** EntityLivingBaseが落下した時の処理。 */
	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event) {
		if (event.entityLiving.worldObj.isRemote || event.isCanceled() || !event.isCancelable() || !(event.entityLiving instanceof EntityPlayer))
			return;
		// サーバー側で、キャンセル可能で、本人プレイヤーの時。
		if (!OfalenFlightHandlerServer.canFlightPlayer((EntityPlayer) event.entityLiving))
			return;
		event.setCanceled(true);
	}

}
