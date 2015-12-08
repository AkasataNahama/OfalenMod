package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
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

	/** 無効時のアイコン。 */
	private IIcon invalid;

	public ItemFloater() {
		super();
		this.setMaxDamage(64 * 9);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		// 持ち主がプレイヤー以外か、クリエイティブなら終了。
		if (!(entity instanceof EntityPlayer) || ((EntityPlayer) entity).capabilities.isCreativeMode)
			return;
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (entity.onGround) {
			// 持ち主が地上にいるなら間隔をリセットして終了。
			nbt.setByte("Duration", OfalenModConfigCore.intervalDamageFloater);
			return;
		}
		// 無効か、時間がたっていないなら終了。
		if (nbt.getByte("Mode") < 1 || nbt.getByte("Duration") > 0)
			return;
		// 耐久値を減らす。
		itemStack.setItemDamage(itemStack.getItemDamage() + OfalenModConfigCore.amountDamageFloater);
		// サーバー側なら全クライアントにパーティクルを生成するようパケットを送信。
		if (!world.isRemote)
			OfalenModCore.wrapper.sendToAll(new MSpawnParticle(entity.posX, entity.posY - 1.6D, entity.posZ, (byte) 2));
		if (itemStack.getItemDamage() + OfalenModConfigCore.amountDamageFloater <= itemStack.getMaxDamage()) {
			// 耐久値が残っているなら間隔をリセットして終了。
			nbt.setByte("Duration", OfalenModConfigCore.intervalDamageFloater);
			return;
		}
		// 耐久値が尽きたなら、無効にし、ログに出力する。
		nbt.setByte("Mode", (byte) 0);
		if (world.isRemote && entity == Minecraft.getMinecraft().thePlayer)
			OfalenFlightHandlerClient.forbidPlayerToFloat();
		if (!world.isRemote)
			((EntityPlayer) entity).addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemFloater.MaterialLacking")));
	}

	/** 右クリック時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 違うアイテムなら終了。
		if (itemStack == null || !(itemStack.getItem() instanceof ItemFloater))
			return itemStack;
		if (player.isSneaking()) {
			// スニークしていたらGUIを開く。
			player.openGui(OfalenModCore.instance, 4, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			return itemStack;
		}
		byte mode = itemStack.getTagCompound().getByte("Mode");
		if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
			// 材料がないならチャットに出力して終了。
			if (!world.isRemote)
				player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemFloater.MaterialLacking")));
			if (mode != 0) {
				itemStack.getTagCompound().setByte("Mode", (byte) 0);
				if (world.isRemote)
					OfalenFlightHandlerClient.allowPlayerToFloat((byte) 0);
			}
			return itemStack;
		}
		// スニークしていなかったらモードを切り替える。
		mode++;
		if (mode > 5)
			mode = 1;
		itemStack.getTagCompound().setByte("Mode", mode);
		if (world.isRemote) {
			OfalenFlightHandlerClient.allowPlayerToFloat(mode);
		} else {
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("info.OfalenMod.ItemFloater.ChangeMode" + mode)));
		}
		return itemStack;
	}

	/** 材料として使用できるかの判定。 */
	public static boolean isItemMaterial(ItemStack material) {
		if (material == null)
			return false;
		// フロートパウダーのみ使用可能。
		if (material.isItemEqual(OfalenModItemCore.dust))
			return true;
		return false;
	}

	/** 道具など、耐久値を減らせるかどうか。 */
	@Override
	public boolean isDamageable() {
		// 耐久力エンチャントなどが付かないよう、false。
		return false;
	}

	/** アイテムのアイコンを登録する処理。 */
	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
		// 無効時のアイコンを登録。
		invalid = register.registerIcon(this.getIconString() + "0");
	}

	/** アイテムのアイコンを返す。 */
	@Override
	public IIcon getIconIndex(ItemStack itemStack) {
		// 有効なら通常のアイコン、無効なら無効時のアイコン。
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().getByte("Mode") > 0)
			return super.getIconIndex(itemStack);
		return invalid;
	}

}
