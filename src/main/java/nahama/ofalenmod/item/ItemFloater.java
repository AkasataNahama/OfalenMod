package nahama.ofalenmod.item;

import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.handler.OfalenFlightHandlerServer;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemFloater extends ItemFuture {
	/** 無効時のアイコン。 */
	private IIcon invalid;

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack itemStack = new ItemStack(item);
		// TODO 標準量を使用
		this.setMaterialAmount(itemStack, 64);
		OfalenUtil.add(list, itemStack);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		// 持ち主がプレイヤー以外なら終了。
		if (!(entity instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) entity;
		NBTTagCompound nbt = itemStack.getTagCompound();
		// このフローターが有効で、ハンドラーが無効化されていたら、再調査させる。
		if (!world.isRemote && nbt.getBoolean(OfalenNBTUtil.IS_VALID) && this.getMaterialAmount(itemStack) >= OfalenModConfigCore.amountFloaterDamage && !OfalenFlightHandlerServer.canFlightPlayer(player))
			OfalenFlightHandlerServer.checkPlayer(player);
		// ダストの消費間隔を管理する。
		byte interval = nbt.getByte(OfalenNBTUtil.INTERVAL);
		if (entity.onGround || !nbt.getBoolean(OfalenNBTUtil.IS_VALID) || this.getMaterialAmount(itemStack) < OfalenModConfigCore.amountFloaterDamage) {
			// 持ち主が地上にいるか、無効か、材料不足なら間隔をリセットして終了。
			if (interval != OfalenModConfigCore.intervalFloaterDamage)
				nbt.setByte(OfalenNBTUtil.INTERVAL, OfalenModConfigCore.intervalFloaterDamage);
			return;
		}
		// 時間がたっていないなら終了。
		if (interval > 0)
			return;
		nbt.setByte(OfalenNBTUtil.INTERVAL, OfalenModConfigCore.intervalFloaterDamage);
		// 耐久値を減らす。
		if (!player.capabilities.isCreativeMode)
			this.consumeMaterial(itemStack, OfalenModConfigCore.amountFloaterDamage);
		// サーバー側なら全クライアントにパーティクルを生成するようパケットを送信。
		if (!world.isRemote)
			OfalenModPacketCore.WRAPPER.sendToAll(new MSpawnParticle(entity.worldObj.provider.dimensionId, entity.posX, entity.posY - 1.6D, entity.posZ, (byte) 2));
		if (this.getMaterialAmount(itemStack) < OfalenModConfigCore.amountFloaterDamage) {
			// 材料が尽きたならチャットに出力し、調査する。
			OfalenUtil.addChatTranslationMessage(player, "info.ofalen.future.lackingMaterial", new ItemStack(OfalenModItemCore.floaterOfalen).getDisplayName(), new ItemStack(OfalenModItemCore.partsOfalen, 1, 8).getDisplayName());
			if (!world.isRemote)
				OfalenFlightHandlerServer.checkPlayer(player);
		}
	}

	/** 右クリック時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		NBTTagCompound nbt = itemStack.getTagCompound();
		// 時間がたっていないなら終了。
		if (nbt.getByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK) > 0)
			return itemStack;
		nbt.setByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK, (byte) 10);
		if (!OfalenKeyHandler.isSprintKeyPressed(player)) {
			// ダッシュキーが押されていなければ、モード変更か無効化。
			if (!player.isSneaking()) {
				// しゃがんでいなかったらモードを切り替える。
				byte mode = nbt.getByte(OfalenNBTUtil.MODE);
				mode++;
				if (mode > 6)
					mode = 1;
				nbt.setByte(OfalenNBTUtil.MODE, mode);
				OfalenUtil.addChatTranslationMessage(player, "info.ofalen.floater.modeChanged", mode);
				if (!world.isRemote)
					OfalenFlightHandlerServer.checkPlayer(player);
			} else {
				// しゃがんでいたら、有効化・無効化する。
				nbt.setBoolean(OfalenNBTUtil.IS_VALID, !nbt.getBoolean(OfalenNBTUtil.IS_VALID));
				if (!world.isRemote)
					OfalenFlightHandlerServer.checkPlayer(player);
			}
		} else {
			// ダッシュキーが押されていれば、ダストの補充・取り出し。
			if (!player.isSneaking()) {
				// しゃがんでいなければ、補充。
				this.chargeMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), player);
			} else {
				// しゃがんでいれば、取り出し。
				this.dropMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), player);
				// フローターが有効だったら更新する。
				if (nbt.getBoolean(OfalenNBTUtil.IS_VALID)) {
					if (!world.isRemote)
						OfalenFlightHandlerServer.checkPlayer(player);
				}
			}
		}
		return itemStack;
	}

	/** アイテムのアイコンを登録する処理。 */
	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
		// 無効時のアイコンを登録。
		invalid = register.registerIcon(this.getIconString() + "-0");
		itemIcon = register.registerIcon(this.getIconString() + "-1");
	}

	/** アイテムのアイコンを返す。 */
	@Override
	public IIcon getIconIndex(ItemStack itemStack) {
		// 有効なら通常のアイコン、無効なら無効時のアイコン。
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID))
			return super.getIconIndex(itemStack);
		return invalid;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if (pass == 1) {
			int material = this.getMaterialAmount(stack);
			if (material < OfalenModConfigCore.amountFloaterDamage)
				return iconOverlayLacking;
			// TODO 標準量の設定
			if (material <= 64)
				return iconOverlayWeak;
		}
		return this.getIconIndex(stack);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		List<String> stringList = OfalenUtil.getAs(list);
		// TODO 標準量を表示
		int amount = this.getMaterialAmount(itemStack);
		stringList.add(OfalenUtil.getStackAmountString(amount, 64) + " (" + amount + " / 64)");
		if (itemStack.hasTagCompound()) {
			String message = StatCollector.translateToLocal("info.ofalen.future.isValid." + itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID));
			if (this.getMaterialAmount(itemStack) < OfalenModConfigCore.amountFloaterDamage)
				message += " " + StatCollector.translateToLocal("info.ofalen.future.lack");
			stringList.add(message);
			stringList.add(StatCollector.translateToLocal("info.ofalen.floater.mode") + " " + itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE));
		}
	}
}
