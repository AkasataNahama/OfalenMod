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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

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
				// スプリントキーが押されていなかったらインベントリのGUIを開く。
				player.openGui(OfalenModCore.instance, 1, world, x, y, z);
			} else {
				// スプリントキーが押されていたら設定のGUIを開く。
				player.openGui(OfalenModCore.instance, 0, world, x, y, z);
			}
		} else {
			if (!OfalenKeyHandler.isSprintKeyPressed(player)) {
				// 設定キーが押されていたら起動/停止。
				TileEntity tileEntity = world.getTileEntity(x, y, z);
				if (tileEntity != null && tileEntity instanceof TileEntityWorldEditorBase)
					((TileEntityWorldEditorBase) tileEntity).changeIsWorking();
			} else {
				// 設定キーとスプリントキーが押されていたら手持ちアイテムの適用。
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
						if (world.isRemote) {
							List<String> list = ((TileEntityWorldEditorBase) tileEntity).getFilterMessage();
							for (String s : list) {
								player.addChatMessage(new ChatComponentText(s));
							}
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
		// 世界系のTileEntityがないか、ItemStackがNBTを持っていないなら終了。
		if (tileEntity == null || !(tileEntity instanceof TileEntityWorldEditorBase) || !itemStack.hasTagCompound())
			return;
		TileEntityWorldEditorBase editor = (TileEntityWorldEditorBase) tileEntity;
		// TileEntityのデータが保存されていないなら、アイテムフィルターの情報だけ読み込む。
		if (!itemStack.getTagCompound().hasKey(OfalenNBTUtil.TILE_ENTITY_WORLD_EDITOR_BASE, new NBTTagCompound().getId())) {
			editor.setTagItemFilter(FilterUtil.getFilterTag(itemStack));
			return;
		}
		// データが保存されていたら、座標を書き換えてから読み込む。
		NBTTagCompound nbt = itemStack.getTagCompound().getCompoundTag(OfalenNBTUtil.TILE_ENTITY_WORLD_EDITOR_BASE);
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		editor.readFromNBT(nbt);
		// フィルターの情報をItemStackのものに更新する。
		editor.setTagItemFilter(FilterUtil.getFilterTag(itemStack));
		// 測量器が隣接しているか判定する。
		editor.searchSurveyor();
	}

	/** 破壊された時の処理。 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (world.isRemote) {
			super.breakBlock(world, x, y, z, block, meta);
			return;
		}
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || !(tileEntity instanceof TileEntityWorldEditorBase)) {
			super.breakBlock(world, x, y, z, block, meta);
			return;
		}
		// TileEntityの内部にあるアイテムをドロップさせる。
		TileEntityWorldEditorBase editor = (TileEntityWorldEditorBase) tileEntity;
		for (int i = 0; i < editor.getSizeInventory(); i++) {
			ItemStack itemStack = editor.getStackInSlotOnClosing(i);
			if (itemStack != null)
				OfalenUtil.dropItemStackNearBlock(itemStack, world, x, y, z);
		}
		// このブロック自身をドロップさせる。NBT保存用。
		Item item = getItemDropped(meta, world.rand, 0);
		NBTTagCompound nbtItem = new NBTTagCompound();
		NBTTagCompound nbtTileEntity = new NBTTagCompound();
		tileEntity.writeToNBT(nbtTileEntity);
		nbtItem.setTag(OfalenNBTUtil.TILE_ENTITY_WORLD_EDITOR_BASE, nbtTileEntity);
		// アイテムフィルターのデータをItemStackのNBTの直下に複製する。
		nbtItem.setTag(FilterUtil.ITEM_FILTER, nbtTileEntity.getTag(FilterUtil.ITEM_FILTER));
		ItemStack itemStack = new ItemStack(item, 1, damageDropped(meta));
		itemStack.setTagCompound(nbtItem);
		OfalenUtil.dropItemStackNearBlock(itemStack, world, x, y, z);
		// TileEntityの更新を通知する。
		world.func_147453_f(x, y, z, block);
		super.breakBlock(world, x, y, z, block, meta);
	}

	/** ドロップ数を返す。 */
	@Override
	public int quantityDropped(Random random) {
		// breakBlockでNBTを保存してドロップさせるため、通常のドロップ数は0にする。
		return 0;
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
		if (iBlockAccess.getBlockMetadata(x, y, z) > 1)
			return 13;
		return 0;
	}
}
