package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.handler.OfalenFlightHandlerClient;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemFloater extends ItemFuture {
	/** 無効時のアイコン。 */
	private IIcon invalid;

	public ItemFloater() {
		this.setMaxDamage(64 * 9);
	}

	/** 材料として使用できるかの判定。 */
	public static boolean isItemMaterial(ItemStack material) {
		// フロートパウダーのみ使用可能。
		return material != null && material.isItemEqual(new ItemStack(OfalenModItemCore.partsOfalen, 1, 8));
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
			nbt.setByte(OfalenNBTUtil.INTERVAL, OfalenModConfigCore.intervalFloaterDamage);
			return;
		}
		// 無効か、時間がたっていないなら終了。
		if (nbt.getByte(OfalenNBTUtil.MODE) < 1 || nbt.getByte(OfalenNBTUtil.INTERVAL) > 0)
			return;
		// 耐久値を減らす。
		itemStack.setItemDamage(itemStack.getItemDamage() + OfalenModConfigCore.amountFloaterDamage);
		// サーバー側なら全クライアントにパーティクルを生成するようパケットを送信。
		if (!world.isRemote)
			OfalenModPacketCore.WRAPPER.sendToAll(new MSpawnParticle(entity.worldObj.provider.dimensionId, entity.posX, entity.posY - 1.6D, entity.posZ, (byte) 2));
		if (itemStack.getItemDamage() + OfalenModConfigCore.amountFloaterDamage <= itemStack.getMaxDamage()) {
			// 耐久値が残っているなら間隔をリセットして終了。
			nbt.setByte(OfalenNBTUtil.INTERVAL, OfalenModConfigCore.intervalFloaterDamage);
			return;
		}
		// 耐久値が尽きたなら、無効にし、ログに出力する。
		nbt.setByte(OfalenNBTUtil.MODE, (byte) 0);
		if (world.isRemote && entity == Minecraft.getMinecraft().thePlayer)
			OfalenFlightHandlerClient.checkPlayer();
		if (!world.isRemote)
			OfalenUtil.addChatTranslationMessage((EntityPlayer) entity, "info.ofalen.future.lackingMaterial", new ItemStack(OfalenModItemCore.floaterOfalen).getDisplayName(), new ItemStack(OfalenModItemCore.partsOfalen, 1, 8).getDisplayName());
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
		byte mode = itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE);
		if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
			// 材料がないならチャットに出力して終了。
			if (!world.isRemote)
				OfalenUtil.addChatTranslationMessage(player, "info.ofalen.future.lackingMaterial", new ItemStack(OfalenModItemCore.floaterOfalen).getDisplayName(), new ItemStack(OfalenModItemCore.partsOfalen, 1, 8).getDisplayName());
			if (mode != 0) {
				itemStack.getTagCompound().setByte(OfalenNBTUtil.MODE, (byte) 0);
				if (world.isRemote)
					OfalenFlightHandlerClient.checkPlayer();
			}
			return itemStack;
		}
		// スニークしていなかったらモードを切り替える。
		mode++;
		if (mode > 5)
			mode = 1;
		itemStack.getTagCompound().setByte(OfalenNBTUtil.MODE, mode);
		if (world.isRemote) {
			OfalenFlightHandlerClient.checkPlayer();
		} else {
			OfalenUtil.addChatTranslationMessage(player, "info.ofalen.floater.modeChanged", mode);
		}
		return itemStack;
	}

	/** 道具などのように、耐久値を減らせるかどうか。 */
	@Override
	public boolean isDamageable() {
		return false;
	}

	/** アイテムのアイコンを登録する処理。 */
	@Override
	public void registerIcons(IIconRegister register) {
		// 無効時のアイコンを登録。
		invalid = register.registerIcon(this.getIconString() + "-0");
		itemIcon = register.registerIcon(this.getIconString() + "-1");
	}

	/** アイテムのアイコンを返す。 */
	@Override
	public IIcon getIconIndex(ItemStack itemStack) {
		// 有効なら通常のアイコン、無効なら無効時のアイコン。
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE) > 0)
			return super.getIconIndex(itemStack);
		return invalid;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return this.getIconIndex(stack);
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		List<String> stringList = OfalenUtil.getAs(list);
		stringList.add((itemStack.getMaxDamage() - itemStack.getItemDamage()) + " / " + itemStack.getMaxDamage());
		if (itemStack.hasTagCompound()) {
			stringList.add(StatCollector.translateToLocal("info.ofalen.floater.mode") + " " + itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE));
		}
	}
}
