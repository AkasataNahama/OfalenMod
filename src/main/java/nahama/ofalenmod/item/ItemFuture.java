package nahama.ofalenmod.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class ItemFuture extends Item {
	@SideOnly(Side.CLIENT)
	protected IIcon iconOverlayWeak;
	@SideOnly(Side.CLIENT)
	protected IIcon iconOverlayLacking;

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
		byte interval = itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL);
		if (interval > 0)
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) (interval - 1));
		interval = itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK);
		if (interval > 0)
			itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL_RIGHT_CLICK, (byte) (interval - 1));
	}

	/** 2層に分けてレンダリングを行うか。 */
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	/** テクスチャを登録する。 */
	@Override
	public void registerIcons(IIconRegister register) {
		iconOverlayWeak = register.registerIcon("ofalenmod:future-overlay-weak");
		iconOverlayLacking = register.registerIcon("ofalenmod:future-overlay-lacking");
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return OfalenModConfigCore.sizeDurabilityBar != 0.0 && this.getMaterialAmount(stack) < this.getReferenceAmount() * OfalenModConfigCore.sizeDurabilityBar;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - ((double) this.getMaterialAmount(stack)) / ((double) this.getReferenceAmount() * OfalenModConfigCore.sizeDurabilityBar);
	}

	protected abstract int getReferenceAmount();

	/**
	 * 素材数を減らす。
	 * @param amount 1未満の場合動作無し。
	 */
	public void consumeMaterial(ItemStack itemStack, int amount) {
		if (amount < 1)
			return;
		this.setMaterialAmount(itemStack, this.getMaterialAmount(itemStack) - amount);
	}

	/** 素材数を上書きする。 */
	protected void setMaterialAmount(ItemStack itemStack, int amount) {
		if (!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		itemStack.getTagCompound().setShort(OfalenNBTUtil.MATERIAL, (short) Math.max(0, Math.min(Short.MAX_VALUE, amount)));
	}

	/** 素材数を返す。 */
	public short getMaterialAmount(ItemStack itemStack) {
		if (!itemStack.hasTagCompound())
			return 0;
		return itemStack.getTagCompound().getShort(OfalenNBTUtil.MATERIAL);
	}

	/** プレイヤーのインベントリから素材を補充する。 */
	protected void chargeMaterial(ItemStack stackFuture, ItemStack stackMaterial, EntityPlayer player) {
		ItemStack[] stacks = player.inventory.mainInventory;
		int amount = 0;
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i] != null && stacks[i].isItemEqual(stackMaterial)) {
				amount += stacks[i].stackSize;
				stacks[i] = null;
			}
		}
		this.setMaterialAmount(stackFuture, this.getMaterialAmount(stackFuture) + amount);
	}

	/** 素材を全てドロップする。 */
	protected void dropMaterial(ItemStack stackFuture, ItemStack stackMaterial, EntityPlayer player) {
		int amount = this.getMaterialAmount(stackFuture);
		int limit = stackMaterial.getMaxStackSize();
		while (amount > 0) {
			ItemStack stackDrop = stackMaterial.copy();
			if (amount <= limit) {
				stackDrop.stackSize = amount;
				amount = 0;
			} else {
				stackDrop.stackSize = limit;
				amount -= limit;
			}
			OfalenUtil.dropItemStackNearEntity(stackDrop, player);
		}
		this.setMaterialAmount(stackFuture, 0);
	}
}
