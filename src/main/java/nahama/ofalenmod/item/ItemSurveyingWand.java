package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.util.BlockPos;
import nahama.ofalenmod.util.BlockRangeWithStandard;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenParticleUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemSurveyingWand extends Item {
	public ItemSurveyingWand() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setFull3D();
		this.setMaxStackSize(1);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = itemStack.getTagCompound();
		byte interval = itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (interval > 0)
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) (interval - 1));
		if (!isHeld || !world.isRemote || interval > 0 || entity != Minecraft.getMinecraft().thePlayer)
			return;
		itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) 20);
		BlockRangeWithStandard range = BlockRangeWithStandard.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.RANGE));
		if (range == null)
			return;
		OfalenParticleUtil.spawnParticleWithBlockRange(world, range.convertToNormal(), 0.498, 0.498, 0.498);
		OfalenParticleUtil.spawnParticleAroundBlock(world, range.posA, 1, 1, 1);
		OfalenParticleUtil.spawnParticleAroundBlock(world, range.posB, 0, 0, 0);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		NBTTagCompound nbt = itemStack.getTagCompound();
		BlockRangeWithStandard range = BlockRangeWithStandard.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.RANGE));
		if (range == null) {
			range = new BlockRangeWithStandard(x, y, z, x, y, z);
		} else {
			if (!OfalenKeyHandler.isSettingKeyPressed(player)) {
				range.posA = new BlockPos(x, y, z);
			} else {
				range.posB = new BlockPos(x, y, z);
			}
		}
		nbt.setTag(OfalenNBTUtil.RANGE, range.getNBT());
		return true;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		super.addInformation(itemStack, player, list, isAdvanced);
		List<String> stringList = OfalenUtil.getAs(list);
		if (itemStack.hasTagCompound()) {
			BlockRangeWithStandard range = BlockRangeWithStandard.loadFromNBT(itemStack.getTagCompound().getCompoundTag(OfalenNBTUtil.RANGE));
			stringList.add(StatCollector.translateToLocal("info.ofalen.wandSurveying.standard.a") + " (" + range.posA.toStringCoord() + ")");
			stringList.add(StatCollector.translateToLocal("info.ofalen.wandSurveying.standard.b") + " (" + range.posB.toStringCoord() + ")");
			stringList.add(StatCollector.translateToLocal("info.ofalen.wandSurveying.range") + " (" + range.toStringRange() + ")");
		}
	}
}