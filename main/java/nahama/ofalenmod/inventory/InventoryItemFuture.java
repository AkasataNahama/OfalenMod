package nahama.ofalenmod.inventory;

import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.util.Util;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class InventoryItemFuture extends InventoryItemBase {
	private byte meta;
	private short size;
	private String name;

	public InventoryItemFuture(InventoryPlayer inventory, byte meta, short size, String name) {
		super(inventory);
		this.meta = meta;
		this.size = size;
		this.name = name;
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return size;
	}

	@Override
	public String getInventoryName() {
		return "container.ofalen." + name;
	}

	/** インベントリが開かれた時の処理。 */
	@Override
	public void openInventory() {
		// アイテムを読み込む。
		int amount = Util.getRemainingDamage(inventoryPlayer.getCurrentItem());
		itemStacks = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < itemStacks.length; i++) {
			if (amount < 1)
				break;
			if (amount < 64) {
				itemStacks[i] = new ItemStack(OfalenModItemCore.partsOfalen, amount, meta);
				break;
			}
			itemStacks[i] = new ItemStack(OfalenModItemCore.partsOfalen, 64, meta);
			amount -= 64;
		}
	}

	/** インベントリが閉じられた時の処理。 */
	@Override
	public void closeInventory() {
		int amount = 0;
		for (ItemStack itemStack : itemStacks) {
			if (itemStack == null)
				continue;
			amount += itemStack.stackSize;
		}
		itemStacks = new ItemStack[this.getSizeInventory()];
		inventoryPlayer.getCurrentItem().setItemDamage(inventoryPlayer.getCurrentItem().getMaxDamage() - amount);
	}
}
