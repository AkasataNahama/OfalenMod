package nahama.ofalenmod.block;

import nahama.ofalenmod.core.OfalenModItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Random;

public class BlockOfalenGrass extends Block implements IPlantable, IGrowable {
	private IIcon[] icons;

	public BlockOfalenGrass() {
		super(Material.plants);
		// CreativeTabに登録しない。
		this.setCreativeTab(null);
		// updateTickがランダムで呼ばれるよう設定。
		this.setTickRandomly(true);
		// 枠の大きさを設定する。
		float f = 0.5F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		// 統計に数えられないようにする(?)。
		this.disableStats();
	}

	protected Item getSeeds() {
		return OfalenModItemCore.seedsOfalen;
	}

	protected Item getCrop() {
		return OfalenModItemCore.fragmentOfalen;
	}

	/** 更新時の処理。 */
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		// this.setTickRandomly(true)をしてあるため、ランダムなタイミングで呼ばれる。
		super.updateTick(world, x, y, z, random);
		// 明るさが足りないなら終了。
		if (world.getBlockLightValue(x, y + 1, z) < 9)
			return;
		int meta = world.getBlockMetadata(x, y, z);
		// 成長しきっているなら終了。
		if (meta > 11)
			return;
		// 1/10の確率で一段階成長させる。
		if (random.nextInt(10) == 0) {
			meta += 4;
			world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		}
	}

	/** ドロップアイテムを返す。 */
	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		// 成長しきっていたら作物（オファレンの欠片）を返す。
		return meta >= 12 ? this.getCrop() : this.getSeeds();
	}

	/** ドロップ数を返す。 */
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	/** ドロップアイテムのメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		return meta % 4;
	}

	/** 「幸運」を適用してアイテムをドロップする。 */
	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float f, int fortune) {
		// 幸運を無効化。
		super.dropBlockAsItemWithChance(world, x, y, z, meta, f, 0);
	}

	/** ドロップするItemStackのリストを返す。 */
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, meta, fortune);
		// 成長途中なら、種が既に追加されているので返す。
		if (meta < 12)
			return ret;
		// 成長しきっているなら、作物（オファレンの欠片）が既に登録されているので、種を追加して返す。
		ret.add(new ItemStack(this.getSeeds(), 1, meta % 4));
		return ret;
	}

	/** 留まれる場所かどうかを判定し、留まれないならドロップする。 */
	protected void checkAndDropBlock(World world, int x, int y, int z) {
		if (!this.canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	/** 留まっていられる場所かどうか。 */
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}

	/** 設置できる場所かどうか。 */
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
	}

	/** 隣接ブロックの更新時の処理。 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		this.checkAndDropBlock(world, x, y, z);
	}

	/** 当たり判定を返す。 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	/** 第三ボタンが押された時の対応アイテムを返す。 */
	@Override
	public Item getItem(World world, int x, int y, int z) {
		return this.getSeeds();
	}

	/** 不透明なブロックかどうか。 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/** 通常と同じように描画するかどうか。 */
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/** 描画方式の番号を返す。 */
	@Override
	public int getRenderType() {
		// 草や花と同じ。小麦や人参は6。
		return 1;
	}

	/** アイコンを返す。 */
	@Override
	public IIcon getIcon(int side, int meta) {
		return icons[meta % 16];
	}

	/** アイコンを登録する。 */
	@Override
	public void registerBlockIcons(IIconRegister register) {
		icons = new IIcon[16];
		for (int i = 0; i < icons.length; ++i) {
			icons[i] = register.registerIcon(this.getTextureName() + "-" + (i / 4) + "-" + (i % 4));
		}
	}

	/** 骨粉を使用できるかどうか。 */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean isRemote) {
		return world.getBlockMetadata(x, y, z) < 12;
	}

	/** 骨粉を適用するかどうか。 */
	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z) {
		return true;
	}

	/** 骨粉を適用した時の処理。 */
	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z) {
		// meta + 0, 4, 4, 8
		int meta = world.getBlockMetadata(x, y, z) + ((random.nextInt(4) + 1) / 2 * 4);
		// metaを正常な範囲にまで減らす。
		while (meta > 15) {
			meta -= 4;
		}
		// 結果的には、1/2の確率で一段階成長、それぞれ1/4の確率で無成長か二段階成長。
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}

	/** 作物の栽培方式を返す。 */
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Crop;
	}

	/** 作物のインスタンスを返す。 */
	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) {
		return this;
	}

	/** 作物のメタデータを返す。 */
	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}
}
