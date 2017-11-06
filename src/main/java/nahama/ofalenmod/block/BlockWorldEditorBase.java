package nahama.ofalenmod.block;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.tileentity.TileEntityWorldEditorBase;
import nahama.ofalenmod.util.BlockRangeWithStandard;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenNBTUtil.FilterUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockWorldEditorBase extends BlockContainer {
	private IIcon[] icons;

	protected BlockWorldEditorBase() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	/** TileEntityを生成して返す。 */
	@Override
	public abstract TileEntity createNewTileEntity(World world, int meta);

	/** プレイヤーに右クリックされた時の処理。 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!OfalenKeyHandler.isSettingKeyPressed(player)) {
			// 設定キーが押されていなかったらGUIを開く。
			if (!OfalenKeyHandler.isSprintKeyPressed(player)) {
				// ダッシュキーが押されていなかったらインベントリのGUIを開く。
				player.openGui(OfalenModCore.instance, 1, world, x, y, z);
			} else {
				// ダッシュキーが押されていたら設定のGUIを開く。
				player.openGui(OfalenModCore.instance, 0, world, x, y, z);
			}
		} else {
			if (!OfalenKeyHandler.isSprintKeyPressed(player)) {
				// 設定キーが押されていたら起動/停止。
				TileEntity tileEntity = world.getTileEntity(x, y, z);
				if (tileEntity != null && tileEntity instanceof TileEntityWorldEditorBase)
					((TileEntityWorldEditorBase) tileEntity).changeIsWorking();
			} else if (!world.isRemote) {
				// 設定キーとダッシュキーが押されていたら手持ちアイテムの適用。
				ItemStack itemStack = player.getHeldItem();
				if (itemStack == null)
					return false;
				if (itemStack.getItem() == OfalenModItemCore.filterItem && FilterUtil.isAvailableFilterTag(itemStack)) {
					// アイテムフィルターを持っていたら、フィルターをインストールする。
					FilterUtil.installFilterToTileEntity(world, x, y, z, FilterUtil.getFilterTag(itemStack));
					OfalenUtil.addChatTranslationMessage(player, "info.ofalen.editor.installedFilter");
					return true;
				} else if (itemStack.getItem() == OfalenModItemCore.installerFilter) {
					TileEntity tileEntity = world.getTileEntity(x, y, z);
					if (tileEntity != null && tileEntity instanceof TileEntityWorldEditorBase) {
						List<String> list = ((TileEntityWorldEditorBase) tileEntity).getFilterMessage();
						for (String s : list) {
							player.addChatMessage(new ChatComponentText(s));
						}
						return true;
					}
				} else if (itemStack.getItem() == OfalenModItemCore.wandSurveying) {
					// 測量杖を持っていたら、作業範囲を設定する。
					BlockRangeWithStandard range = BlockRangeWithStandard.loadFromNBT(itemStack.getTagCompound().getCompoundTag(OfalenNBTUtil.RANGE));
					if (range != null) {
						TileEntity tileEntity = world.getTileEntity(x, y, z);
						if (tileEntity != null && tileEntity instanceof TileEntityWorldEditorBase) {
							((TileEntityWorldEditorBase) tileEntity).setRange(range.convertToNormal());
							OfalenUtil.addChatTranslationMessage(player, "info.ofalen.editor.setRange");
							return true;
						}
					}
				}
				return false;
			}
		}
		return true;
	}

	/** 隣接するブロックが更新された時の処理。 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || !(tileEntity instanceof TileEntityWorldEditorBase))
			return;
		TileEntityWorldEditorBase editor = (TileEntityWorldEditorBase) tileEntity;
		editor.searchSurveyor();
	}

	/** 設置された時の処理。 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		// TileEntityが存在したら、設置時の処理を行う。
		if (tileEntity != null && tileEntity instanceof TileEntityWorldEditorBase)
			((TileEntityWorldEditorBase) tileEntity).onPlaced(itemStack);
	}

	/** 破壊された時の処理。 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (world.isRemote) {
			super.breakBlock(world, x, y, z, block, meta);
			return;
		}
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityWorldEditorBase) {
			// TileEntityの内部にあるアイテムをドロップさせる。
			TileEntityWorldEditorBase editor = (TileEntityWorldEditorBase) tileEntity;
			for (int i = 0; i < editor.getSizeInventory(); i++) {
				ItemStack itemStack = editor.getStackInSlotOnClosing(i);
				if (itemStack != null)
					OfalenUtil.dropItemStackNearBlock(itemStack, world, x, y, z);
			}
			// TileEntityの更新を通知する。
			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	/** ブロックを破壊する処理。 */
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		// 回収予定（後でharvestBlockが呼ばれる）なら何もせずに、そうでないなら通常の処理（setBlockToAir）を行う。
		return willHarvest || super.removedByPlayer(world, player, x, y, z, false);
	}

	/** ブロックを回収する処理。 */
	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
		// 通常の処理を行う。getDropsが呼ばれる。
		super.harvestBlock(world, player, x, y, z, meta);
		// removedByPlayerでキャンセルした破壊処理（setBlockToAir）を行う。
		super.removedByPlayer(world, player, x, y, z, false);
	}

	/** ドロップアイテムのリストを返す。 */
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityWorldEditorBase) {
			// TileEntityが存在したら、データを保存する。
			ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
			drops.add(((TileEntityWorldEditorBase) tileEntity).getDrop());
			return drops;
		} else {
			// TileEntityが存在しなかったら、通常の処理を行う。
			return super.getDrops(world, x, y, z, metadata, fortune);
		}
	}

	/** 対応するItemStackを返す。 */
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityWorldEditorBase) {
			// TileEntityが存在したら、データを保存して返す。
			return ((TileEntityWorldEditorBase) tileEntity).getDrop();
		} else {
			// TileEntityが存在しなかったら、通常の処理を行う。
			return super.getPickBlock(target, world, x, y, z, player);
		}
	}

	/** テクスチャを登録する処理。 */
	@Override
	public void registerBlockIcons(IIconRegister register) {
		icons = new IIcon[2];
		for (int i = 0; i < 2; i++) {
			icons[i] = register.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** テクスチャを返す。 */
	@Override
	public IIcon getIcon(int side, int meta) {
		return icons[meta % 2];
	}

	/** 光源レベルを返す。 */
	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		if (iBlockAccess.getBlockMetadata(x, y, z) % 2 == 1)
			return 13;
		return 0;
	}
}
