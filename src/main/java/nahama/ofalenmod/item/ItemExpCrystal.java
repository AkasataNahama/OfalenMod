package nahama.ofalenmod.item;

import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.handler.OfalenKeyHandler;
import nahama.ofalenmod.util.OfalenUtil;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemExpCrystal extends Item {
	public ItemExpCrystal() {
		this.setCreativeTab(OfalenModCore.TAB_OFALEN);
		this.setHasSubtypes(true);
	}

	/** 右クリック時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (OfalenKeyHandler.isSettingKeyPressed(player)) {
			if (!player.isSneaking()) {
				// 設定キー + 右クリックで圧縮。
				if (itemStack.stackSize == 64) {
					OfalenUtil.dropItemStackNearEntity(new ItemStack(OfalenModItemCore.crystalExp, 1, itemStack.getItemDamage() + 1), player);
					itemStack.stackSize = 0;
				}
			} else {
				// 設定キー + Shift + 右クリックで分解。
				if (itemStack.getItemDamage() > 0) {
					for (int i = 0; i < itemStack.stackSize; i++) {
						OfalenUtil.dropItemStackNearEntity(new ItemStack(OfalenModItemCore.crystalExp, 64, itemStack.getItemDamage() - 1), player);
					}
					itemStack.stackSize = 0;
				}
			}
			return itemStack;
		}
		// プレイヤーの近くに経験値を出す。
		int amount = 1;
		if (player.isSneaking())
			amount *= 10;
		if (amount > itemStack.stackSize)
			amount = itemStack.stackSize;
		if (!world.isRemote) {
			// サーバー側なら、経験値オーブをスポーンさせる。
			EntityXPOrb entity = new EntityXPOrb(world, player.posX, player.posY, player.posZ, amount * OfalenUtil.power(64, itemStack.getItemDamage()));
			world.spawnEntityInWorld(entity);
		}
		if (!player.capabilities.isCreativeMode) {
			itemStack.stackSize -= amount;
		}
		return itemStack;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isAdvanced) {
		super.addInformation(itemStack, player, list, isAdvanced);
		List<String> stringList = OfalenUtil.getAs(list);
		stringList.add(StatCollector.translateToLocal("info.ofalen.crystalExp.size") + " " + OfalenUtil.power(64, itemStack.getItemDamage()));
	}
}
