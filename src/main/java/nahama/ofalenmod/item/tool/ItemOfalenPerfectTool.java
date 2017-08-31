package nahama.ofalenmod.item.tool;

import cpw.mods.fml.common.eventhandler.Event.Result;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.setting.IItemOfalenSettable;
import nahama.ofalenmod.setting.OfalenSettingBoolean;
import nahama.ofalenmod.setting.OfalenSettingCategory;
import nahama.ofalenmod.setting.OfalenSettingCategoryOrigin;
import nahama.ofalenmod.setting.OfalenSettingInteger;
import nahama.ofalenmod.util.BlockPos;
import nahama.ofalenmod.util.BlockRange;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
import nahama.ofalenmod.util.OfalenParticleUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemOfalenPerfectTool extends ItemTool implements IItemOfalenSettable {
	private OfalenSettingCategoryOrigin origin;

	public ItemOfalenPerfectTool(ToolMaterial material) {
		super(0.0F, material, null);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
	}

	/** 回収可能なブロックかどうか。 */
	@Override
	public boolean func_150897_b(Block block) {
		return true;
	}

	/** 採掘速度の設定。 */
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta) {
		// 他のツールでは適正ブロックの判定をするが、ここではすべてに適正採掘速度を適用する。
		if (((OfalenSettingBoolean) this.getSetting().getChildCategory("NormalBreaking").getChildSetting("ItemFilter")).getValueByStack(stack) && !FilterUtil.canItemFilterThrough(FilterUtil.getFilterTag(stack), new ItemStack(block, 1, meta)))
			return 1;
		return ((OfalenSettingInteger) this.getSetting().getChildCategory("NormalBreaking").getChildSetting("BreakingEfficiency")).getValueByStack(stack);
	}

	/** アップデート時の処理。 */
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
		// クライアント側なら終了。
		if (world.isRemote)
			return;
		// フィルタータグが無効だったら初期化する。
		FilterUtil.onUpdateFilter(itemStack);
		byte interval = itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (interval > 0)
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) (interval - 1));
	}

	/** 右クリックされた時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		byte mode = itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE);
		if (!OfalenKeyHandler.isSettingKeyPressed(player)) {
			// 右クリック時効果がガードで、設定キーが押されていないなら、ガードを始める。
			if (mode == 3)
				player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
			// 設定キーが押されていなければ終了。
			return itemStack;
		}
		// インターバルが残っていたら終了。
		if (itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL) > 0)
			return itemStack;
		if (OfalenKeyHandler.isSprintKeyPressed(player)) {
			// スプリントキーが押されていたら、モード設定の変更を行う。
			if (mode == 1) {
				int addition = itemStack.getTagCompound().getByte(OfalenNBTUtil.RANGE_LENGTH_ADDITION);
				if (!player.isSneaking()) {
					addition = Math.min(addition + 1, Byte.MAX_VALUE);
				} else {
					addition = Math.max(addition - 1, Byte.MIN_VALUE);
				}
				itemStack.getTagCompound().setByte(OfalenNBTUtil.RANGE_LENGTH_ADDITION, (byte) addition);
				// 次に変更できるまでの間隔を設定する。
				itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) 10);
				if (world.isRemote) {
					MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
					OfalenParticleUtil.spawnParticleWithBlockRange(player.worldObj, this.getRange(player, itemStack, pos.blockX, pos.blockY, pos.blockZ), 2);
				}
			}
		} else {
			if (world.isRemote)
				return itemStack;
			// 設定キーが押されていたら、modeを変更する。
			mode = this.getNextMode(itemStack, mode, player.isSneaking());
			itemStack.getTagCompound().setByte(OfalenNBTUtil.MODE, mode);
			// 次に変更できるまでの間隔を設定する。
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) 10);
			// プレイヤーのチャット欄に通知する。
			if (((OfalenSettingBoolean) this.getSetting().getChildSetting("ModeChangeNotification")).getValueByStack(itemStack))
				OfalenUtil.addChatTranslationMessage(player, "info.ofalen.toolPerfect.mode.changed", StatCollector.translateToLocal("info.ofalen.toolPerfect.mode." + mode));
		}
		return itemStack;
	}

	/** 次のモード番号を返す。 */
	private byte getNextMode(ItemStack stack, int current, boolean isReverse) {
		if (current < 0)
			current = 0;
		int mode = current;
		do {
			if (!isReverse) {
				mode++;
			} else {
				mode--;
			}
			if (mode < 0)
				mode = 3;
			if (3 < mode)
				mode = 0;
			if (((OfalenSettingBoolean) this.getSetting().getChildCategory("ModeChange").getChildSetting("Mode-" + mode)).getValueByStack(stack))
				return (byte) mode;
		} while (mode != current);
		return -1;
	}

	/** アイテムが使われた(ブロックに右クリックされた)時の処理。 */
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (OfalenKeyHandler.isSettingKeyPressed(player))
			return false;
		byte mode = itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE);
		switch (mode) {
		case 0:
			// 右クリック時効果が単一破壊の時。
			this.breakAndDropBlockByPlayerWithFilter(itemStack, player, world, x, y, z, ((OfalenSettingBoolean) this.getSetting().getChildCategory("SingleBreaking").getChildSetting("ItemFilter")).getValueByStack(itemStack));
			return true;
		case 1:
			// 右クリック時効果が範囲破壊の時。
			boolean isFilterEnabled = ((OfalenSettingBoolean) this.getSetting().getChildCategory("RangeBreaking").getChildSetting("ItemFilter")).getValueByStack(itemStack);
			BlockRange range = this.getRange(player, itemStack, x, y, z);
			for (int ix = range.posMin.x; ix <= range.posMax.x; ix++) {
				for (int iy = range.posMax.y; iy >= range.posMin.y; iy--) {
					for (int iz = range.posMin.z; iz <= range.posMax.z; iz++) {
						this.breakAndDropBlockByPlayerWithFilter(itemStack, player, world, ix, iy, iz, isFilterEnabled);
					}
				}
			}
			return true;
		case 2:
			// 右クリック時効果がクワの時。
			// 右クリックしたブロックを取得する。
			Block block = world.getBlock(x, y, z);
			// プレイヤーが編集不可ならば使えない。（アドベンチャーモードかつ素手の時しかfalseが返ってきてない？）
			if (!player.canPlayerEdit(x, y, z, side, itemStack))
				return false;
			// eventを呼び出す。
			UseHoeEvent event = new UseHoeEvent(player, itemStack, world, x, y, z);
			// キャンセルされたら終了。
			if (MinecraftForge.EVENT_BUS.post(event))
				break;
			// 処理が代行されたらダメージを与えて終了。
			if (event.getResult() == Result.ALLOW) {
				itemStack.damageItem(1, player);
				return true;
			}
			// 右クリックされたブロックの上が空気ブロックで、右クリックされたブロックが草ブロックか土ブロックならば、
			if (side != 0 && world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z) && (block == Blocks.grass || block == Blocks.dirt)) {
				Block block1 = Blocks.farmland;
				// 音を鳴らす。
				world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
				// クライアント側は終了。
				if (world.isRemote)
					return true;
				// サーバー側では、ブロックを置き換えて、
				world.setBlock(x, y, z, block1);
				// ダメージを与える。
				itemStack.damageItem(1, player);
				return true;
			}
		}
		return false;
	}

	/** 詳細設定とNBTから範囲を算出し返す。 */
	private BlockRange getRange(ItemStack itemStack, int x, int y, int z) {
		OfalenSettingCategory categoryDefaultRange = this.getSetting().getChildCategory("RangeBreaking").getChildCategory("DefaultRange");
		OfalenSettingCategory categoryStrength = this.getSetting().getChildCategory("RangeBreaking").getChildCategory("Strength");
		int addition = itemStack.getTagCompound().getByte(OfalenNBTUtil.RANGE_LENGTH_ADDITION);
		BlockRange range = new BlockRange(x, y, z, x, y, z);
		range.posMax.x += this.getValidLength(this.getLengthSetting(itemStack, categoryDefaultRange, "X+") + this.getLengthSetting(itemStack, categoryStrength, "X+") * addition);
		range.posMax.y += this.getValidLength(this.getLengthSetting(itemStack, categoryDefaultRange, "Y+") + this.getLengthSetting(itemStack, categoryStrength, "Y+") * addition);
		range.posMax.z += this.getValidLength(this.getLengthSetting(itemStack, categoryDefaultRange, "Z+") + this.getLengthSetting(itemStack, categoryStrength, "Z+") * addition);
		range.posMin.x -= this.getValidLength(this.getLengthSetting(itemStack, categoryDefaultRange, "X-") + this.getLengthSetting(itemStack, categoryStrength, "X-") * addition);
		range.posMin.y -= this.getValidLength(this.getLengthSetting(itemStack, categoryDefaultRange, "Y-") + this.getLengthSetting(itemStack, categoryStrength, "Y-") * addition);
		range.posMin.z -= this.getValidLength(this.getLengthSetting(itemStack, categoryDefaultRange, "Z-") + this.getLengthSetting(itemStack, categoryStrength, "Z-") * addition);
		return range;
	}

	/** 詳細設定とNBTから範囲を算出し、プレイヤーの向きにより回転させて返す。 */
	private BlockRange getRange(Entity entity, ItemStack itemStack, int x, int y, int z) {
		BlockRange range = this.getRange(itemStack, x, y, z);
		// 詳細設定で視線応答が無効化されていたら終了。
		if (!((OfalenSettingBoolean) this.getSetting().getChildCategory("RangeBreaking").getChildSetting("EyeResponse")).getValueByStack(itemStack))
			return range;
		ForgeDirection direction = ForgeDirection.EAST;
		if (entity.rotationPitch > 45) {
			direction = ForgeDirection.DOWN;
		} else if (entity.rotationPitch < -45) {
			direction = ForgeDirection.UP;
		}
		range = range.rotate(new BlockPos(x, y, z), direction);
		direction = ForgeDirection.EAST;
		switch (MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) {
		case 0:
			direction = ForgeDirection.SOUTH;
			break;
		case 1:
			direction = ForgeDirection.WEST;
			break;
		case 2:
			direction = ForgeDirection.NORTH;
			break;
		}
		return range.rotate(new BlockPos(x, y, z), direction);
	}

	/** 詳細設定から長さを取得する。 */
	private int getLengthSetting(ItemStack itemStack, OfalenSettingCategory category, String name) {
		return ((OfalenSettingInteger) category.getChildSetting("Length-" + name)).getValueByStack(itemStack);
	}

	/** 有効な長さを返す。 */
	private int getValidLength(int length) {
		length = Math.max(length, 0);
		length = Math.min(length, OfalenModConfigCore.rangeMax);
		return length;
	}

	/** フィルターで指定されたブロックを破壊し、ドロップさせる。 */
	private void breakAndDropBlockByPlayerWithFilter(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, boolean isFilterEnabled) {
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		// 破壊不可（岩盤）なら終了。
		if (block.getBlockHardness(world, x, y, z) < 0.0F)
			return;
		// フィルターに引っかかったら不可。
		if (isFilterEnabled && !FilterUtil.canItemFilterThrough(FilterUtil.getFilterTag(itemStack), new ItemStack(block, 1, meta)))
			return;
		// ブロックを壊した時、道具の耐久値を減らす処理。
		itemStack.func_150999_a(world, block, x, y, z, player);
		// クライアント側なら終了。
		if (world.isRemote)
			return;
		// ブロックが破壊された時の音を鳴らす。
		world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
		// ブロックが回収される時の処理をする。
		block.onBlockHarvested(world, x, y, z, meta, player);
		// ブロックがプレイヤーに削除された時の処理をする。（基本的にここで空気ブロックに置き換わる。）
		boolean flag = block.removedByPlayer(world, player, x, y, z, true);
		if (flag) {
			// ブロックがプレイヤーに破壊された時の処理をする。
			block.onBlockDestroyedByPlayer(world, x, y, z, meta);
			// ブロックが回収された時の処理をする。（基本的にここでドロップする。）
			block.harvestBlock(world, player, x, y, z, meta);
			// シルクタッチがなければ、ブロックが破壊された時の経験値をドロップさせる。
			if (!EnchantmentHelper.getSilkTouchModifier(player))
				block.dropXpOnBlockBreak(world, x, y, z, block.getExpDrop(world, meta, EnchantmentHelper.getFortuneModifier(player)));
		}
	}

	/** Entityを叩いた時の処理。 */
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player) {
		// ItemToolでは耐久値を2減らすが、剣と同じように1だけ減らすようにする。
		itemStack.damageItem(1, player);
		return true;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		return EnumAction.block;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 72000;
	}

	/** 説明欄の内容を追加する。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		if (!itemStack.hasTagCompound())
			return;
		int mode = itemStack.getTagCompound().getByte(OfalenNBTUtil.MODE);
		List<String> stringList = OfalenUtil.getAs(list);
		stringList.add(StatCollector.translateToLocal("info.ofalen.toolPerfect.mode") + OfalenUtil.getColon() + StatCollector.translateToLocal("info.ofalen.toolPerfect.mode." + mode));
		if (mode == 1) {
			stringList.add(StatCollector.translateToLocal("info.ofalen.toolPerfect.mode.1.range") + OfalenUtil.getColon() + this.getRange(itemStack, 0, 0, 0).toStringRange() + " (" + itemStack.getTagCompound().getByte(OfalenNBTUtil.RANGE_LENGTH_ADDITION) + ")");
		}
		stringList.addAll(FilterUtil.getFilterInformation(itemStack));
	}

	@Override
	public OfalenSettingCategoryOrigin getSetting() {
		if (origin != null)
			return origin;
		// 詳細設定の初期化を行う。
		origin = new OfalenSettingCategoryOrigin("OfalenPerfectTool", new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE));
		// モード変更通知。
		origin.registerChildSetting(new OfalenSettingBoolean("ModeChangeNotification", new ItemStack(Blocks.redstone_torch), true));
		OfalenSettingCategory category = new OfalenSettingCategory("ModeChange", new ItemStack(Blocks.glass));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingBoolean("Mode-0", new ItemStack(Items.gold_ingot), true));
		category.registerChildSetting(new OfalenSettingBoolean("Mode-1", new ItemStack(Items.diamond), true));
		category.registerChildSetting(new OfalenSettingBoolean("Mode-2", new ItemStack(Items.wheat_seeds), true));
		category.registerChildSetting(new OfalenSettingBoolean("Mode-3", new ItemStack(Items.bone), true));
		// 通常破壊。
		category = new OfalenSettingCategory("NormalBreaking", new ItemStack(Items.iron_ingot));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingBoolean("ItemFilter", new ItemStack(Items.string), false));
		category.registerChildSetting(new OfalenSettingInteger("BreakingEfficiency", new ItemStack(Blocks.obsidian), 64, 0));
		// 単一破壊。
		category = new OfalenSettingCategory("SingleBreaking", new ItemStack(Items.gold_ingot));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingBoolean("ItemFilter", new ItemStack(Items.string), false));
		// 範囲破壊。
		category = new OfalenSettingCategory("RangeBreaking", new ItemStack(Items.diamond));
		origin.registerChildSetting(category);
		category.registerChildSetting(new OfalenSettingBoolean("ItemFilter", new ItemStack(Items.string), true));
		category.registerChildSetting(new OfalenSettingBoolean("EyeResponse", new ItemStack(Items.stick), true));
		OfalenSettingCategory category1 = new OfalenSettingCategory("DefaultRange", new ItemStack(OfalenModBlockCore.surveyorOfalen));
		category.registerChildSetting(category1);
		category1.registerChildSetting(new OfalenSettingInteger("Length-X+", new ItemStack(OfalenModItemCore.gemOfalen, 1, 0), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-Y+", new ItemStack(OfalenModItemCore.gemOfalen, 1, 1), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-Z+", new ItemStack(OfalenModItemCore.gemOfalen, 1, 2), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-X-", new ItemStack(OfalenModItemCore.gemOfalen, 1, 4), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-Y-", new ItemStack(OfalenModItemCore.gemOfalen, 1, 5), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-Z-", new ItemStack(OfalenModItemCore.gemOfalen, 1, 6), 1, 0));
		category1 = new OfalenSettingCategory("Strength", new ItemStack(OfalenModItemCore.wandSurveying));
		category.registerChildSetting(category1);
		category1.registerChildSetting(new OfalenSettingInteger("Length-X+", new ItemStack(OfalenModItemCore.gemOfalen, 1, 0), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-Y+", new ItemStack(OfalenModItemCore.gemOfalen, 1, 1), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-Z+", new ItemStack(OfalenModItemCore.gemOfalen, 1, 2), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-X-", new ItemStack(OfalenModItemCore.gemOfalen, 1, 4), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-Y-", new ItemStack(OfalenModItemCore.gemOfalen, 1, 5), 1, 0));
		category1.registerChildSetting(new OfalenSettingInteger("Length-Z-", new ItemStack(OfalenModItemCore.gemOfalen, 1, 6), 1, 0));
		return origin;
	}
}
