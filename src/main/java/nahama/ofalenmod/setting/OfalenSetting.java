package nahama.ofalenmod.setting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public abstract class OfalenSetting {
	protected static final String PATH_SEPARATOR = ".";
	protected static final String LANGUAGE_PREFIX = "info.ofalen.settingDetailed.";
	private final String name;
	private final ItemStack stack;
	private String path;

	public OfalenSetting(String name, ItemStack stack) {
		this.name = name;
		this.stack = stack;
	}

	/** この設定の名前を返す。 */
	public String getName() {
		return name;
	}

	/** この設定のパスを返す。 */
	public String getPath() {
		return path;
	}

	/** この設定のパスを決める。 */
	protected void setPath(String path) {
		// 上書き不可。
		if (this.path != null)
			throw new IllegalStateException("The setting path cannot be overwritten.");
		this.path = path;
	}

	/** 翻訳されたこの設定の名前を返す。 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal(LANGUAGE_PREFIX + this.getPath());
	}

	/** この設定を指定するためのItemStackを返す。 */
	protected ItemStack getSpecifierStack() {
		return stack;
	}

	/** この次の操作を指定するItemStackの一覧を返す。 */
	public abstract List<ItemStack> getSelectableItemList();

	/** この次の操作を指定するItemStackであるか。 */
	public boolean isSpecifierStack(ItemStack itemStack) {
		return OreDictionary.itemMatches(this.getSpecifierStack(), itemStack, false);
	}
}
