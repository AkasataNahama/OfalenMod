package nahama.ofalenmod.item.tool;

import nahama.ofalenmod.OfalenModCore;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class OfalenPerfectTool extends ItemTool {

	public OfalenPerfectTool(ToolMaterial material) {
		super(0.0F, material, null);
		this.setCreativeTab(OfalenModCore.tabOfalen);
	}

	@Override
	public boolean func_150897_b(Block block) {
		return true;
	 }

	/**採掘速度の設定*/
	@Override
	public float func_150893_a(ItemStack itemStack, Block block) {
		//他のツールでは適正ブロックの判定をするが、ここではすべてに適正採掘速度を適用する
		return this.efficiencyOnProperMaterial;
	}

	//クワの処理
	/**アイテムが使われた(右クリック)時の処理*/
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		//プレイヤーが編集不可ならば
		if (!entityPlayer.canPlayerEdit(x, y, z, side, itemStack)) {
			//falseを返す
			return false;
		} else {
			//eventの登録
			UseHoeEvent event = new UseHoeEvent(entityPlayer, itemStack, world, x, y, z);
			if (MinecraftForge.EVENT_BUS.post(event)) {
				return false;
			}

			if (event.getResult() == Result.ALLOW) {
				//ダメージを与える
				itemStack.damageItem(1, entityPlayer);
				return true;
			}

			//右クリックされたブロックを取得する
			Block block = world.getBlock(x, y, z);

			//右クリックされたブロックの上が空気ブロックで、右クリックされたブロックが草ブロックか土ブロックならば
			if (side != 0 && world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z) && (block == Blocks.grass || block == Blocks.dirt)) {
				Block block1 = Blocks.farmland;
				//音を鳴らす
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);

				//クライアント側では何もせず
				if (world.isRemote) {
					return true;
				//サーバー側では
				} else {
					//ブロックを置き換えて
					world.setBlock(x, y, z, block1);
					//ダメージを与える
					itemStack.damageItem(1, entityPlayer);
					return true;
				}
			} else {
				return false;
			}
		}
	}

	//剣の処理
	/**Entityを叩いたときの処理。ItemToolでは2のダメージをアイテムに与えるが、剣と同じように1与えるようにする。*/
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player) {
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

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		return itemStack;
	}

}