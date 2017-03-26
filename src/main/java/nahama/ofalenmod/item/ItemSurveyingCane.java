package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemSurveyingCane extends Item {
	public ItemSurveyingCane() {
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
		OfalenParticleUtil.spawnParticleWithBlockRange(world, range, 3);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		NBTTagCompound nbt = itemStack.getTagCompound();
		BlockRangeWithStandard range = BlockRangeWithStandard.loadFromNBT(nbt.getCompoundTag(OfalenNBTUtil.RANGE));
		if (range == null) {
			range = new BlockRangeWithStandard(x, y, z, x, y, z);
		} else {
			if (!OfalenUtil.isKeyDown(OfalenModCore.KEY_OSS.getKeyCode())) {
				range.posA = new BlockPos(x, y, z);
			} else {
				range.posB = new BlockPos(x, y, z);
			}
		}
		nbt.setTag(OfalenNBTUtil.RANGE, range.getNBT());
		return true;
	}
}
