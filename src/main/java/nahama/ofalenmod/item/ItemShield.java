package nahama.ofalenmod.item;

import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemShield extends ItemFuture {
	private IIcon invalid;

	/** クリエイティブタブにアイテムを登録する。 */
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack itemStack = new ItemStack(item);
		// TODO 標準量を使用
		this.setMaterialAmount(itemStack, 64);
		OfalenUtil.add(list, itemStack);
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
			// ダッシュキーが押されていなければ、シールドの有効化か無効化。
			if (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID)) {
				// シールドが有効だったら無効にする。
				itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, false);
			} else {
				// シールドが無効だったら、
				if (this.getMaterialAmount(itemStack) < OfalenModConfigCore.amountShieldDamage) {
					// 材料がないならチャットに出力する。
					OfalenUtil.addChatTranslationMessage(player, "info.ofalen.future.lackingMaterial", new ItemStack(OfalenModItemCore.shieldOfalen).getDisplayName(), new ItemStack(OfalenModItemCore.partsOfalen, 1, 6).getDisplayName());
				} else {
					// シールドを有効にする。
					itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, true);
				}
			}
		} else {
			// ダッシュキーが押されていれば、インゴットの補充・取り出し。
			if (!player.isSneaking()) {
				// しゃがんでいなければ、補充。
				this.chargeMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 6), player);
			} else {
				// しゃがんでいれば、取り出し。
				this.dropMaterial(itemStack, new ItemStack(OfalenModItemCore.partsOfalen, 1, 6), player);
				// シールドが有効だったら無効化。
				if (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID))
					itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, false);
			}
		}
		return itemStack;
	}

	/** テクスチャを登録する。 */
	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
		invalid = register.registerIcon(this.getIconString() + "-0");
		itemIcon = register.registerIcon(this.getIconString() + "-1");
	}

	/** テクスチャを返す。 */
	@Override
	public IIcon getIconIndex(ItemStack itemStack) {
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID))
			return super.getIconIndex(itemStack);
		return invalid;
	}

	/** テクスチャを返す。 */
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		// TODO 標準量の設定
		if (pass == 1 && this.getMaterialAmount(stack) <= 64)
			return iconOverlayWeak;
		return this.getIconIndex(stack);
	}

	/** Tooltipに情報を追加する。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		List<String> stringList = OfalenUtil.getAs(list);
		// TODO 標準量を表示
		int amount = this.getMaterialAmount(itemStack);
		stringList.add(OfalenUtil.getStackAmountString(amount, 64) + " (" + amount + " / 64)");
		if (itemStack.hasTagCompound()) {
			stringList.add(StatCollector.translateToLocal("info.ofalen.future.isValid." + itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID)));
		}
	}
}
