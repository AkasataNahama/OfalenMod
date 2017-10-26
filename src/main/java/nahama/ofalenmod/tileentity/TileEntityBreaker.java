package nahama.ofalenmod.tileentity;

import cpw.mods.fml.relauncher.ReflectionHelper;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenLog;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.lang.reflect.InvocationTargetException;

public class TileEntityBreaker extends TileEntityWorldEditorBase {
	/** シルクタッチを適用するか。 */
	private boolean isSilkTouchEnabled;
	/** ブロックを削除するかどうか。 */
	private boolean canDeleteBrokenBlock;
	/** 液体を破壊するか。 */
	private boolean canDeleteLiquid;

	@Override
	protected boolean canWork() {
		return true;
	}

	@Override
	protected boolean canWorkWithCoord(int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		// 破壊不可ブロックならfalse。
		if (block.getBlockHardness(worldObj, x, y, z) < 0.0F)
			return false;
		// 空気ブロックならfalse。
		if (block.isAir(worldObj, x, y, z))
			return false;
		// 設定により液体ブロックを無視する。
		if (!canDeleteLiquid && block instanceof BlockLiquid)
			return false;
		// フィルターで許可されていればtrue。
		return OfalenNBTUtil.FilterUtil.canItemFilterThrough(tagItemFilter, new ItemStack(block, 1, worldObj.getBlockMetadata(x, y, z)));
	}

	@Override
	protected boolean work(int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		int meta = worldObj.getBlockMetadata(x, y, z);
		FakePlayer fakePlayer = FakePlayerFactory.get((WorldServer) worldObj, OfalenModCore.profile);
		block.onBlockHarvested(worldObj, x, y, z, meta, fakePlayer);
		boolean flag = block.removedByPlayer(worldObj, fakePlayer, x, y, z, true);
		worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
		if (flag)
			block.onBlockDestroyedByPlayer(worldObj, x, y, z, meta);
		if (!canDeleteBrokenBlock) {
			if (isSilkTouchEnabled && block.canSilkHarvest(worldObj, fakePlayer, x, y, z, meta)) {
				ItemStack itemStack = null;
				try {
					itemStack = (ItemStack) ReflectionHelper.findMethod(Block.class, block, new String[] { "createStackedBlock", "func_149644_j" }, int.class).invoke(block, meta);
				} catch (IllegalAccessException e) {
					OfalenLog.error("Unable to get drop block by silk touch.", "TileEntityBreaker");
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					OfalenLog.error("Unable to get drop block by silk touch.", "TileEntityBreaker");
					e.printStackTrace();
				}
				if (itemStack != null) {
					// シルクタッチで破壊し、ドロップする。
					if (worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
						float f = 0.7F;
						double d0 = (double) (worldObj.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
						double d1 = (double) (worldObj.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
						double d2 = (double) (worldObj.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
						EntityItem entityitem = new EntityItem(worldObj, (double) x + d0, (double) y + d1, (double) z + d2, itemStack);
						entityitem.delayBeforeCanPickup = 10;
						worldObj.spawnEntityInWorld(entityitem);
					}
					return true;
				}
			}
			if (flag) {
				// 通常のドロップ処理を行う。
				block.harvestBlock(worldObj, fakePlayer, x, y, z, 0);
				block.dropXpOnBlockBreak(worldObj, x, y, z, block.getExpDrop(worldObj, meta, 0));
			}
		}
		return true;
	}

	@Override
	public byte getAmountSettingID() {
		return (byte) (super.getAmountSettingID() + 3);
	}

	@Override
	public Object getWithID(int id) {
		switch (id - super.getAmountSettingID()) {
		case 0:
			return isSilkTouchEnabled;
		case 1:
			return canDeleteBrokenBlock;
		case 2:
			return canDeleteLiquid;
		}
		return super.getWithID(id);
	}

	@Override
	public void setWithID(int id, byte changeType) {
		super.setWithID(id, changeType);
		switch (id - super.getAmountSettingID()) {
		case 0:
			isSilkTouchEnabled = !isSilkTouchEnabled;
			break;
		case 1:
			canDeleteBrokenBlock = !canDeleteBrokenBlock;
			break;
		case 2:
			canDeleteLiquid = !canDeleteLiquid;
			break;
		}
	}

	@Override
	public String getSettingNameWithID(int id) {
		switch (id - super.getAmountSettingID()) {
		case 0:
			return "info.ofalen.setting.breaker.isSilkTouchEnabled";
		case 1:
			return "info.ofalen.setting.breaker.canDeleteBrokenBlock";
		case 2:
			return "info.ofalen.setting.breaker.canDeleteLiquid";
		}
		return super.getSettingNameWithID(id);
	}

	@Override
	protected byte getColor() {
		return 6;
	}

	@Override
	protected void writeSettingToNBT(NBTTagCompound nbt) {
		super.writeSettingToNBT(nbt);
		nbt.setBoolean(OfalenNBTUtil.IS_SILK_TOUCH_ENABLED, isSilkTouchEnabled);
		nbt.setBoolean(OfalenNBTUtil.CAN_DELETE_BROKEN_BLOCK, canDeleteBrokenBlock);
		nbt.setBoolean(OfalenNBTUtil.CAN_DELETE_LIQUID, canDeleteLiquid);
	}

	@Override
	protected void readSettingFromNBT(NBTTagCompound nbt) {
		super.readSettingFromNBT(nbt);
		isSilkTouchEnabled = nbt.getBoolean(OfalenNBTUtil.IS_SILK_TOUCH_ENABLED);
		canDeleteBrokenBlock = nbt.getBoolean(OfalenNBTUtil.CAN_DELETE_BROKEN_BLOCK);
		canDeleteLiquid = nbt.getBoolean(OfalenNBTUtil.CAN_DELETE_LIQUID);
	}

	@Override
	public String getInventoryName() {
		return "container.ofalen.breaker";
	}
}
