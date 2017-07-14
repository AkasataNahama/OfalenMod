package nahama.ofalenmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemFuture extends Item {
	@SideOnly(Side.CLIENT)
	protected IIcon iconOverlayWeak;

	public ItemFuture() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setMaxStackSize(1);
	}

	/** アップデート時の処理。 */
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean flag) {
		super.onUpdate(itemStack, world, entity, slot, flag);
		// NBTを持っていなければ空のものを持たせ、Intervalがあれば減らす。
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		if (!itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_IRREPARABLE))
			itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_IRREPARABLE, true);
		byte interval = itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (interval > 0)
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) (interval - 1));
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public void registerIcons(IIconRegister register) {
		iconOverlayWeak = register.registerIcon("ofalenmod:future-overlay");
	}

	public void consumeMaterial(ItemStack itemStack, int amount) {
		this.setMaterialAmount(itemStack, this.getMaterialAmount(itemStack) - amount);
	}

	public void setMaterialAmount(ItemStack itemStack, int amount) {
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		itemStack.getTagCompound().setShort(OfalenNBTUtil.MATERIAL, (short) Math.max(0, amount));
	}

	public short getMaterialAmount(ItemStack itemStack) {
		if (!itemStack.hasTagCompound())
			return 0;
		return itemStack.getTagCompound().getShort(OfalenNBTUtil.MATERIAL);
	}
}
