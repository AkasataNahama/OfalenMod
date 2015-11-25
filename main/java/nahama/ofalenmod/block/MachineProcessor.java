package nahama.ofalenmod.block;

import java.util.List;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MachineProcessor extends Block {

	private IIcon[] iicon = new IIcon[3];

	public MachineProcessor() {
		super(Material.rock);
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	/** メタデータにより返すIIconを変える */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[meta & 3];
	}

	/** メタデータによりドロップ品を変える */
	@Override
	public int damageDropped(int meta) {
		return meta & 3;
	}

	/** メタデータ違いのブロックを登録する */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 3; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	/** メタデータ違いのテクスチャを登録する */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		for (int i = 0; i < 3; i++) {
			this.iicon[i] = iicon.registerIcon(this.getTextureName() + i);
		}
	}

	/** プレイヤーに右クリックされたときの動作 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// 筐体を判定する
		this.setMetaFromGrade(world, x, y, z);
		return false;
	}

	/** 隣接するブロックが更新された時に呼ばれる */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		// 筐体を判定する
		this.setMetaFromGrade(world, x, y, z);
	}

	/** getMetaFromGradeを実行し、筐体が成立しているならtrueを返す */
	public int setMetaFromGrade(World world, int x, int y, int z) {
		// 筐体が成立しているか判定し、メタデータを受け取る
		int meta = this.getMetaFromGrade(world, x, y, z);
		// 受け取ったメタデータで上書きする
		world.setBlockMetadataWithNotify(x, y, z, meta, 4);
		// 受け取ったメタデータを返す。
		return meta;
	}

	/** 周囲のブロックから筐体の条件を満たしているか判定し、メタデータを返す */
	public int getMetaFromGrade(World world, int x, int y, int z) {
		// 処理装置のGradeを手に入れる
		int gradeProcessor = world.getBlockMetadata(x, y, z) & 3;
		// 筐体として成立しているか
		boolean flag = true;

		// x, y, zのループ
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					// ここまでのブロックが条件を満たしていれば
					if (flag == true) {

						// インスタンスが処理装置筐体だったら
						if (world.getBlock(x + i, y + j, z + k) instanceof ProcessorCasing) {
							// 判定するブロックのメタデータを入手
							int metaBlock = world.getBlockMetadata(x + i, y + j, z + k);
							// 絶対値がすべて1だったら(筐体の頂点部分だったら)
							if (Math.abs(i) == 1 && Math.abs(j) == 1 && Math.abs(k) == 1) {
								// メタデータが8未満で処理装置のGrade +
								// 4以上でなければ(処理装置のGrade以上の固定金具でなければ)判定失敗
								if (!(metaBlock < 8) || !(metaBlock >= gradeProcessor + 4)) {
									flag = false;
								}
								// 筐体の頂点部分以外(辺か面の部分)だったら
							} else {
								// メタデータが4未満で処理装置のGrade以上でなければ(処理装置のGrade以上の筐体でなければ)判定失敗
								if (!(metaBlock < 4) || !(metaBlock >= gradeProcessor)) {
									flag = false;
								}
							}
							// インスタンスが処理装置筐体以外で、中心のブロックでなければ判定失敗
						} else if (i != 0 || j != 0 || k != 0) {
							flag = false;
						}

						// 条件を満たしていなければ
					} else {

						// 判定を中断してGradeを返す
						return gradeProcessor;

					}
				}
			}
		}

		// 判定が終わって条件を満たしていれば
		if (flag) {
			// Grade + 4を返す
			return gradeProcessor + 4;
			// 条件を満たしていなければ
		} else {
			// Gradeを返す
			return gradeProcessor;
		}
	}
}
