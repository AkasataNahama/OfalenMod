package nahama.ofalenmod.item;

import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModPacketCore;
import nahama.ofalenmod.handler.OfalenFlightHandlerServer;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.network.MSpawnParticle;
import nahama.ofalenmod.setting.IItemOfalenSettable;
import nahama.ofalenmod.setting.OfalenSettingCategory;
import nahama.ofalenmod.setting.OfalenSettingCategoryOrigin;
import nahama.ofalenmod.setting.OfalenSettingContent;
import nahama.ofalenmod.setting.OfalenSettingFloat;
import nahama.ofalenmod.setting.OfalenSettingString;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ItemFloater extends ItemFuture implements IItemOfalenSettable {
	/** 無効時のアイコン。 */
	private IIcon invalid;
	private OfalenSettingCategoryOrigin origin;

	/** クリエイティブタブに登録する処理。 */
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack itemStack = new ItemStack(item);
		this.setMaterialAmount(itemStack, OfalenModConfigCore.amountFloaterDamage * 64 * 9 * 6);
		OfalenUtil.add(list, itemStack);
	}

	/** 更新時の処理。 */
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		// 持ち主がプレイヤー以外なら終了。
		if (!(entity instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) entity;
		NBTTagCompound nbt = itemStack.getTagCompound();
		boolean isValid = this.isValidFloater(itemStack);
		// このフローターが有効で、ハンドラーが無効化されていたら、再調査させる。
		if (!world.isRemote && isValid && !OfalenFlightHandlerServer.canFlightPlayer(player))
			OfalenFlightHandlerServer.checkPlayer(player);
		// ダストの消費間隔を管理する。
		byte interval = nbt.getByte(OfalenNBTUtil.INTERVAL);
		if (entity.onGround || !isValid) {
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
		if (!OfalenKeyHandler.isSettingKeyPressed(player)) {
			// 設定キーが押されていなければ、モード変更か無効化。
			if (!player.isSneaking()) {
				// しゃがんでいなかったらモードを切り替える。
				byte mode = nbt.getByte(OfalenNBTUtil.MODE);
				byte modeLast = mode;
				mode = this.getNextMode(itemStack, mode);
				if (mode != modeLast && mode > 0) {
					// 次の飛行形態が有効なモードに変更する。
					nbt.setByte(OfalenNBTUtil.MODE, mode);
					// チャットに通知する。
					OfalenUtil.addChatTranslationMessage(player, "info.ofalen.floater.modeChanged", this.getModeName(itemStack, mode));
				} else if (mode < 1) {
					// 全てのモードの飛行形態が無効なら、チャットに通知する。
					OfalenUtil.addChatTranslationMessage(player, "info.ofalen.floater.mode.disabled.all");
				}
				// サーバーなら更新する。
				if (!world.isRemote)
					OfalenFlightHandlerServer.checkPlayer(player);
			} else {
				// しゃがんでいたら、有効化・無効化する。
				byte mode = nbt.getByte(OfalenNBTUtil.MODE);
				// フローターが無効で、選択中のモードの飛行形態が無効ならチャットに通知する。
				if (!nbt.getBoolean(OfalenNBTUtil.IS_VALID) && mode > 0 && !this.isValidFlightForm(itemStack, mode))
					OfalenUtil.addChatTranslationMessage(player, "info.ofalen.floater.mode.disabled", this.getModeName(itemStack, mode));
				// フローターの有効性を反転する。
				nbt.setBoolean(OfalenNBTUtil.IS_VALID, !nbt.getBoolean(OfalenNBTUtil.IS_VALID));
				// サーバーなら更新する。
				if (!world.isRemote)
					OfalenFlightHandlerServer.checkPlayer(player);
			}
		} else {
			// 設定キーが押されていれば、ダストの補充・取り出し。
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

	/**
	 * フローターが有効か。<br>
	 * 有効性タグ、ダスト数、選択中モードの飛行形態を考慮。
	 */
	public boolean isValidFloater(ItemStack itemStack) {
		NBTTagCompound nbt = itemStack.getTagCompound();
		byte mode = nbt.getByte(OfalenNBTUtil.MODE);
		return mode > 0 && nbt.getBoolean(OfalenNBTUtil.IS_VALID) && this.getMaterialAmount(itemStack) >= OfalenModConfigCore.amountFloaterDamage && this.isValidFlightForm(itemStack, mode);
	}

	/** 次に選択可能なモードの番号を返す。 */
	private byte getNextMode(ItemStack itemStack, byte mode) {
		byte modeStart = mode;
		do {
			mode++;
			if (mode > 6)
				mode = 1;
			if (this.isValidFlightForm(itemStack, mode))
				return mode;
		} while (mode != modeStart);
		return 0;
	}

	/** 有効な飛行形態か。 */
	private boolean isValidFlightForm(ItemStack itemStack, byte modeFloater) {
		return ((OfalenSettingFlightForm) this.getSetting().getChildCategory("Mode-" + modeFloater).getChildSetting("FlightForm")).getValueByStack(itemStack) > 0;
	}

	/** モード名を詳細設定から取得して返す。 */
	private String getModeName(ItemStack itemStack, byte modeFloater) {
		return ((OfalenSettingString) this.getSetting().getChildCategory("Mode-" + modeFloater).getChildSetting("Name")).getValueByStack(itemStack);
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

	/** アイテムのアイコンを返す。 */
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if (pass == 1) {
			int material = this.getMaterialAmount(stack);
			if (material < OfalenModConfigCore.amountFloaterDamage)
				return iconOverlayLacking;
			if (material <= OfalenModConfigCore.amountFloatingDustReference)
				return iconOverlayWeak;
		}
		return this.getIconIndex(stack);
	}

	/** Tooltipに情報を追加する。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		List<String> stringList = OfalenUtil.getAs(list);
		int amount = this.getMaterialAmount(itemStack);
		stringList.add(OfalenUtil.getStackAmountString(amount, 64) + " (" + amount + " / " + OfalenModConfigCore.amountFloatingDustReference + ")");
		if (itemStack.hasTagCompound()) {
			String message = StatCollector.translateToLocal("info.ofalen.future.isValid." + itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID));
			if (this.getMaterialAmount(itemStack) < OfalenModConfigCore.amountFloaterDamage)
				message += " " + StatCollector.translateToLocal("info.ofalen.future.lack");
			stringList.add(message);
			byte mode = itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE);
			if (mode > 0)
				stringList.add(StatCollector.translateToLocal("info.ofalen.floater.mode") + " " + this.getModeName(itemStack, mode));
		}
	}

	@Override
	public OfalenSettingCategoryOrigin getSetting() {
		if (origin != null)
			return origin;
		origin = new OfalenSettingCategoryOrigin("OfalenFloater", new ItemStack(OfalenModItemCore.floaterOfalen, 1, OreDictionary.WILDCARD_VALUE));
		OfalenSettingCategory category = new OfalenSettingCategory("Mode-1", new ItemStack(OfalenModItemCore.gemOfalen, 1, 0));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingString("Name", new ItemStack(Items.paper), "Jet Mode"));
		category.registerChildSetting(new OfalenSettingFlightForm("FlightForm", new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), 1));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-HorizontalSpeed", new ItemStack(Items.diamond), 2.0, 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-1", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 0), 0.2));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-2", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 1), 1.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-3", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 2), -0.1));
		category = new OfalenSettingCategory("Mode-2", new ItemStack(OfalenModItemCore.gemOfalen, 1, 1));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingString("Name", new ItemStack(Items.paper), "Glide Mode"));
		category.registerChildSetting(new OfalenSettingFlightForm("FlightForm", new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), 1));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-HorizontalSpeed", new ItemStack(Items.diamond), 4.0, 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-1", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 0), 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-2", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 1), 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-3", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 2), 0.1));
		category = new OfalenSettingCategory("Mode-3", new ItemStack(OfalenModItemCore.gemOfalen, 1, 2));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingString("Name", new ItemStack(Items.paper), "Jump Mode"));
		category.registerChildSetting(new OfalenSettingFlightForm("FlightForm", new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), 2));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-HorizontalSpeed", new ItemStack(Items.diamond), 2.0, 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-1", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 0), 2.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-2", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 1), 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-3", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 2), 0.0));
		category = new OfalenSettingCategory("Mode-4", new ItemStack(OfalenModItemCore.gemOfalen, 1, 4));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingString("Name", new ItemStack(Items.paper), "Float Mode"));
		category.registerChildSetting(new OfalenSettingFlightForm("FlightForm", new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), 3));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-HorizontalSpeed", new ItemStack(Items.diamond), 4.0, 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-1", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 0), 0.4));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-2", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 1), 0.4));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-3", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 2), 0.0));
		category = new OfalenSettingCategory("Mode-5", new ItemStack(OfalenModItemCore.gemOfalen, 1, 5));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingString("Name", new ItemStack(Items.paper), "Horizontal Mode"));
		category.registerChildSetting(new OfalenSettingFlightForm("FlightForm", new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), 3));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-HorizontalSpeed", new ItemStack(Items.diamond), 1.0, 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-1", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 0), 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-2", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 1), 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-3", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 2), 0.0));
		category = new OfalenSettingCategory("Mode-6", new ItemStack(OfalenModItemCore.gemOfalen, 1, 6));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingString("Name", new ItemStack(Items.paper), "Speed Mode"));
		category.registerChildSetting(new OfalenSettingFlightForm("FlightForm", new ItemStack(OfalenModItemCore.partsOfalen, 1, 8), 3));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-HorizontalSpeed", new ItemStack(Items.diamond), 8.0, 0.0));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-1", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 0), 0.8));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-2", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 1), 0.8));
		category.registerChildSetting(new OfalenSettingFloat("Parameter-3", new ItemStack(OfalenModItemCore.fragmentOfalen, 1, 2), 0.0));
		return origin;
	}

	public static class OfalenSettingFlightForm extends OfalenSettingContent<Byte> {
		public OfalenSettingFlightForm(String name, ItemStack stack, int valueDefault) {
			super(name, stack, (byte) valueDefault);
		}

		@Override
		public NBTBase getTagFromValue(Byte value) {
			return new NBTTagByte(value);
		}

		@Override
		public Byte getValueFromTag(NBTBase nbt) {
			byte value = valueDefault;
			if (nbt instanceof NBTBase.NBTPrimitive)
				value = ((NBTBase.NBTPrimitive) nbt).func_150290_f();
			return getValidValue(value);
		}

		@Override
		public Byte getChangedValue(Byte current, ItemStack stackSpecifier) {
			int value = valueDefault;
			Item item = stackSpecifier.getItem();
			if (item == Items.sugar) {
				value = 0;
			} else if (item == Items.gunpowder) {
				value = 1;
			} else if (item == Items.redstone) {
				value = 2;
			} else if (item == Items.glowstone_dust) {
				value = 3;
			}
			return getValidValue(value);
		}

		@Override
		public List<ItemStack> getSelectableItemList() {
			List<ItemStack> list = new ArrayList<ItemStack>();
			list.add(new ItemStack(Items.sugar));
			list.add(new ItemStack(Items.gunpowder));
			list.add(new ItemStack(Items.redstone));
			list.add(new ItemStack(Items.glowstone_dust));
			list.add(new ItemStack(Blocks.dirt));
			return list;
		}

		private byte getValidValue(int value) {
			value = Math.max(0, value);
			value = Math.min(3, value);
			return (byte) value;
		}

		@Override
		protected String getMessageFromValue(Byte value) {
			if (value != null) {
				switch (value) {
				case 0:
					return StatCollector.translateToLocal(LANGUAGE_PREFIX + "floater.disabled");
				case 1:
					return StatCollector.translateToLocal(LANGUAGE_PREFIX + "floater.jet");
				case 2:
					return StatCollector.translateToLocal(LANGUAGE_PREFIX + "floater.jump");
				case 3:
					return StatCollector.translateToLocal(LANGUAGE_PREFIX + "floater.float");
				}
			}
			return super.getMessageFromValue(value);
		}
	}
}
