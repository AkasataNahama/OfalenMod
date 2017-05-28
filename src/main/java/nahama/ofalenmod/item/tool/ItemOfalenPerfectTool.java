package nahama.ofalenmod.item.tool;

import cpw.mods.fml.common.eventhandler.Event.Result;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.handler.OfalenKeyHandler;
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
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

import java.util.List;

public class ItemOfalenPerfectTool extends ItemTool {
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
	public float func_150893_a(ItemStack itemStack, Block block) {
		// 他のツールでは適正ブロックの判定をするが、ここではすべてに適正採掘速度を適用する。
		return this.efficiencyOnProperMaterial;
	}

	/** アップデート時の処理。 */
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
		// クライアント側なら終了。
		if (world.isRemote)
			return;
		// フィルタータグが無効だったら初期化する。
		if (!FilterUtil.isAvailableFilterTag(itemStack))
			FilterUtil.initFilterTag(itemStack);
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
					int length = this.getLength(itemStack);
					int x = Minecraft.getMinecraft().objectMouseOver.blockX;
					int y = Minecraft.getMinecraft().objectMouseOver.blockY;
					int z = Minecraft.getMinecraft().objectMouseOver.blockZ;
					BlockRange range = new BlockRange(x - length, y - length, z - length, x + length, y + length, z + length);
					OfalenParticleUtil.spawnParticleWithBlockRange(player.worldObj, range, 2);
				}
			}
		} else {
			if (world.isRemote)
				return itemStack;
			// 設定キーが押されていたら、modeを変更する。
			if (!player.isSneaking()) {
				mode++;
			} else {
				mode--;
			}
			if (mode < 0)
				mode = 3;
			if (3 < mode)
				mode = 0;
			itemStack.getTagCompound().setByte(OfalenNBTUtil.MODE, mode);
			// 次に変更できるまでの間隔を設定する。
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) 10);
			// プレイヤーのチャット欄に通知する。TODO 文字列置換方式変更
			OfalenUtil.addChatMessage(player, StatCollector.translateToLocal("info.ofalen.toolPerfect.mode.changed").replaceAll("%s", StatCollector.translateToLocal("info.ofalen.toolPerfect.mode." + mode)));
		}
		return itemStack;
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
			this.breakAndDropBlockByPlayerWithFilter(itemStack, player, world, x, y, z);
			return true;
		case 1:
			// 右クリック時効果が範囲破壊の時。
			int length = this.getLength(itemStack);
			for (int ix = -length; ix <= length; ix++) {
				for (int iy = -length; iy <= length; iy++) {
					for (int iz = -length; iz <= length; iz++) {
						this.breakAndDropBlockByPlayerWithFilter(itemStack, player, world, x + ix, y + iy, z + iz);
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

	/** フィルターで指定されたブロックを破壊し、ドロップさせる。 */
	private void breakAndDropBlockByPlayerWithFilter(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		// 破壊不可（岩盤）なら終了。
		if (block.getBlockHardness(world, x, y, z) < 0.0F)
			return;
		// フィルターに引っかかったら不可。
		if (!FilterUtil.canItemFilterThrough(FilterUtil.getFilterTag(itemStack), new ItemStack(block, 1, meta)))
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

	private int getRawLength(ItemStack itemStack) {
		return 1 + itemStack.getTagCompound().getByte(OfalenNBTUtil.RANGE_LENGTH_ADDITION);
	}

	private int getLength(ItemStack itemStack) {
		int length = this.getRawLength(itemStack);
		length = Math.max(length, 0);
		length = Math.min(length, OfalenModConfigCore.rangeMax);
		return length;
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
		stringList.add(StatCollector.translateToLocal("info.ofalen.toolPerfect.mode") + " : " + StatCollector.translateToLocal("info.ofalen.toolPerfect.mode." + mode));
		if (mode == 1) {
			stringList.add(StatCollector.translateToLocal("info.ofalen.toolPerfect.mode.1.range") + " : " + this.getLength(itemStack) + " (" + this.getRawLength(itemStack) + ")");
		}
		stringList.addAll(FilterUtil.getFilterInformation(itemStack));
	}
}
