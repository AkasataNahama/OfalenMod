package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.util.OfalenNBTUtil;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemShield extends ItemFuture {
	private IIcon invalid;

	public static boolean isItemMaterial(ItemStack material) {
		return material != null && material.isItemEqual(new ItemStack(OfalenModItemCore.partsOfalen, 1, 6));
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack itemStack = new ItemStack(item);
		// TODO 標準量を使用
		this.setMaterialAmount(itemStack, 64);
		OfalenUtil.add(list, itemStack);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		super.onItemRightClick(itemStack, world, player);
		// 違うアイテムなら終了。
		if (itemStack == null || !(itemStack.getItem() instanceof ItemShield))
			return itemStack;
		if (!player.isSneaking()) {
			// スニークしていなかったらGUIを開く。
			player.openGui(OfalenModCore.instance, 2, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			return itemStack;
		}
		// クライアントか、時間がたっていないなら終了。
		if (world.isRemote || itemStack.getTagCompound().getByte(OfalenNBTUtil.INTERVAL) > 0)
			return itemStack;
		itemStack.getTagCompound().setByte(OfalenNBTUtil.INTERVAL, (byte) 10);
		if (itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID)) {
			// シールドが有効だったら無効にして終了。
			itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, false);
			return itemStack;
		}
		if (this.getMaterialAmount(itemStack) < OfalenModConfigCore.amountShieldDamage) {
			// 材料がないならチャットに出力して終了。
			OfalenUtil.addChatTranslationMessage(player, "info.ofalen.future.lackingMaterial", new ItemStack(OfalenModItemCore.shieldOfalen).getDisplayName(), new ItemStack(OfalenModItemCore.partsOfalen, 1, 6).getDisplayName());
			return itemStack;
		}
		// シールドを有効にして終了。
		itemStack.getTagCompound().setBoolean(OfalenNBTUtil.IS_VALID, true);
		return itemStack;
	}

	@Override
	public void registerIcons(IIconRegister register) {
		invalid = register.registerIcon(this.getIconString() + "-0");
		itemIcon = register.registerIcon(this.getIconString() + "-1");
	}

	@Override
	public IIcon getIconIndex(ItemStack itemStack) {
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID))
			return super.getIconIndex(itemStack);
		return invalid;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return this.getIconIndex(stack);
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		List<String> stringList = OfalenUtil.getAs(list);
		// TODO 標準量を表示, スタック数表示
		stringList.add(this.getMaterialAmount(itemStack) + " / 64");
		if (itemStack.hasTagCompound()) {
			stringList.add(StatCollector.translateToLocal("info.ofalen.future.isValid." + itemStack.getTagCompound().getBoolean(OfalenNBTUtil.IS_VALID)));
		}
	}
}
