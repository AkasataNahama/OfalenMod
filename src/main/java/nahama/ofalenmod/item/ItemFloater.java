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
		if (!world.isRemote && nbt.getByte(OfalenNBTUtil.MODE) > 0 && !OfalenFlightHandlerServer.canFlightPlayer(player))
			OfalenFlightHandlerServer.checkPlayer(player);
		// ダストの消費間隔を管理する。
		byte interval = itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (entity.onGround && interval != OfalenModConfigCore.intervalFloaterDamage) {
			// 持ち主が地上にいるなら間隔をリセットして終了。
			nbt.setByte(OfalenNBTUtil.INTERVAL, OfalenModConfigCore.intervalFloaterDamage);
			return;
		}
		// 無効か、時間がたっていないなら終了。
		if (nbt.getByte(OfalenNBTUtil.MODE) < 1 || nbt.getByte(OfalenNBTUtil.INTERVAL) > 0)
			return;
		// 耐久値を減らす。
		if (!player.capabilities.isCreativeMode)
			this.consumeMaterial(itemStack, OfalenModConfigCore.amountFloaterDamage);
		// サーバー側なら全クライアントにパーティクルを生成するようパケットを送信。
		if (!world.isRemote)
			OfalenModPacketCore.WRAPPER.sendToAll(new MSpawnParticle(entity.worldObj.provider.dimensionId, entity.posX, entity.posY - 1.6D, entity.posZ, (byte) 2));
		if (this.getMaterialAmount(itemStack) >= OfalenModConfigCore.amountFloaterDamage) {
			// 耐久値が残っているなら間隔をリセットして終了。
			nbt.setByte(OfalenNBTUtil.INTERVAL, OfalenModConfigCore.intervalFloaterDamage);
			return;
		}
		// 耐久値が尽きたなら、無効にし、ログに出力する。
		nbt.setByte(OfalenNBTUtil.MODE, (byte) 0);
		if (!world.isRemote && OfalenFlightHandlerServer.canFlightPlayer(player))
			OfalenFlightHandlerServer.checkPlayer(player);
		OfalenUtil.addChatTranslationMessage(player, "info.ofalen.future.lackingMaterial", new ItemStack(OfalenModItemCore.floaterOfalen).getDisplayName(), new ItemStack(OfalenModItemCore.partsOfalen, 1, 8).getDisplayName());
	}

	/** 右クリック時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 時間がたっていないなら終了。
		if (itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK) > 0)
			return itemStack;
		itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK, (byte) 10);
		if (!OfalenKeyHandler.isSprintKeyPressed(player)) {
			// ダッシュキーが押されていなければ、モード変更か無効化。
			if (!player.isSneaking()) {
				// しゃがんでいなかったらモードを切り替える。
				byte mode = itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE);
				if (this.getMaterialAmount(itemStack) < OfalenModConfigCore.amountFloaterDamage) {
					// 材料がないならチャットに出力し、無効化して終了。
					OfalenUtil.addChatTranslationMessage(player, "info.ofalen.future.lackingMaterial", new ItemStack(OfalenModItemCore.floaterOfalen).getDisplayName(), new ItemStack(OfalenModItemCore.partsOfalen, 1, 8).getDisplayName());
					if (mode != 0) {
						itemStack.getTagCompound().setByte(OfalenNBTUtil.MODE, (byte) 0);
						if (!world.isRemote)
							OfalenFlightHandlerServer.checkPlayer(player);
					}
					return itemStack;
				}
				mode++;
				if (mode > 5)
					mode = 1;
				itemStack.getTagCompound().setByte(OfalenNBTUtil.MODE, mode);
				if (!world.isRemote)
					OfalenFlightHandlerServer.checkPlayer(player);
				OfalenUtil.addChatTranslationMessage(player, "info.ofalen.floater.modeChanged", mode);
			} else {
				// しゃがんでいたら、無効化する。
				if (itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE) != 0) {
					itemStack.getTagCompound().setByte(OfalenNBTUtil.MODE, (byte) 0);
					if (!world.isRemote)
						OfalenFlightHandlerServer.checkPlayer(player);
				}
			}
		} else {
			// ダッシュキーが押されていれば、ダストの補充・取り出し。
			if (!player.isSneaking()) {
				// しゃがんでいなければ、補充。
				this.chargeMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), player);
			} else {
				// しゃがんでいれば、取り出し。
				this.dropMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), player);
				// フローターが有効だったら無効化。
				if (itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE) != 0) {
					itemStack.getTagCompound().setByte(OfalenNBTUtil.MODE, (byte) 0);
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
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE) > 0)
			return super.getIconIndex(itemStack);
		return invalid;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		// TODO 標準量の設定
		if (pass == 1 && this.getMaterialAmount(stack) <= 64)
			return iconOverlayWeak;
		return this.getIconIndex(stack);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		List<String> stringList = OfalenUtil.getAs(list);
		// TODO 標準量を表示
		int amount = this.getMaterialAmount(itemStack);
		stringList.add(OfalenUtil.getStackAmountString(amount, 64) + " (" + amount + " / 64)");
		if (itemStack.hasTagCompound()) {
			stringList.add(StatCollector.translateToLocal("info.ofalen.floater.mode") + " " + itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE));
		}
	}
}
