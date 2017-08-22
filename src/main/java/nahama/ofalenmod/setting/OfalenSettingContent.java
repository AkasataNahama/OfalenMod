package nahama.ofalenmod.setting;

import nahama.ofalenmod.util.OfalenNBTUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public abstract class OfalenSettingContent<T> extends OfalenSetting {
	protected final T valueDefault;

	public OfalenSettingContent(String name, ItemStack stack, T valueDefault) {
		super(name, stack);
		this.valueDefault = valueDefault;
	}

	/** 値をNBTにして返す。 */
	protected abstract NBTBase getTagFromValue(T value);

	/** NBTを値にして返す。 */
	protected abstract T getValueFromTag(NBTBase nbt);

	/**
	 * ItemStackにより値を変更して返す。
	 * @return nullは不可。
	 */
	protected abstract T getChangedValue(T current, ItemStack stackSpecifier);

	/** 設定が記録されているNBTを返す。 */
	private NBTTagCompound getSettingTag(ItemStack stackOrigin) {
		if (!stackOrigin.hasTagCompound() || !stackOrigin.getTagCompound().hasKey(OfalenNBTUtil.DETAILED_SETTING, 10)) {
			NBTTagCompound tagOrigin = new NBTTagCompound();
			if (stackOrigin.hasTagCompound())
				tagOrigin = stackOrigin.getTagCompound();
			NBTTagCompound tagSetting = new NBTTagCompound();
			tagOrigin.setTag(OfalenNBTUtil.DETAILED_SETTING, tagSetting);
			stackOrigin.setTagCompound(tagOrigin);
			return tagSetting;
		}
		return stackOrigin.getTagCompound().getCompoundTag(OfalenNBTUtil.DETAILED_SETTING);
	}

	/** ItemStackからNBTを取得して返す。 */
	private NBTBase getTagOfStack(ItemStack stackOrigin) {
		return this.getSettingTag(stackOrigin).getTag(this.getPath());
	}

	/** ItemStackのNBTを上書きする。 */
	private void setTagOfStack(ItemStack stackOrigin, NBTBase nbt) {
		this.getSettingTag(stackOrigin).setTag(this.getPath(), nbt);
	}

	/** ItemStackから現在値を取得して返す。 */
	public T getValueByStack(ItemStack stackOrigin) {
		NBTBase nbt = this.getTagOfStack(stackOrigin);
		if (nbt == null)
			return valueDefault;
		return this.getValueFromTag(nbt);
	}

	/** stackOriginの設定をstackSpecifierにより変更する。 */
	public void changeTagByStack(ItemStack stackOrigin, ItemStack stackSpecifier) {
		this.setTagOfStack(stackOrigin, this.getTagFromValue(this.getChangedValue(this.getValueByStack(stackOrigin), stackSpecifier)));
	}

	/** 値からメッセージの文字列を返す。 */
	protected String getMessageFromValue(T value) {
		if (value == null)
			return StatCollector.translateToLocal("info.ofalen.settingDetailed.message.invalid");
		return value.toString();
	}

	/** 現在値と変更値を文字列として返す。 */
	public String getSecondMessage(ItemStack stackOrigin, ItemStack stackSpecifier) {
		T current = this.getValueByStack(stackOrigin);
		T changed = this.getChangedValue(current, stackSpecifier);
		String string = this.getMessageFromValue(current);
		if ((current == null) != (changed == null) || (current != null && !current.equals(changed))) {
			string += " -> " + this.getMessageFromValue(changed);
		} else {
			string += " " + StatCollector.translateToLocal("info.ofalen.settingDetailed.message.noChange");
		}
		return string;
	}
}
