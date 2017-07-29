package nahama.ofalenmod.item.block;

import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemBlockWorldEditorBase extends ItemBlock {
	public ItemBlockWorldEditorBase(Block block) {
		super(block);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		OfalenNBTUtil.FilterUtil.onUpdateFilter(itemStack);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		List<String> stringList = OfalenUtil.getAs(list);
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(OfalenNBTUtil.TILE_ENTITY_WORLD_EDITOR_BASE, new NBTTagCompound().getId())) {
			stringList.add(StatCollector.translateToLocal("info.ofalen.editor.hasNBT"));
		}
		stringList.addAll(OfalenNBTUtil.FilterUtil.getFilterInformation(itemStack));
	}
}
