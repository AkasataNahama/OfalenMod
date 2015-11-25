package nahama.ofalenmod.tileentity;

import nahama.ofalenmod.block.ConversionMachine;
import nahama.ofalenmod.core.OfalenModBlockCore;
import nahama.ofalenmod.core.OfalenModConfigCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.core.OfalenModOreDicCore;
import nahama.ofalenmod.item.Ofalen;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityConversionMachine extends TileEntity implements ISidedInventory {

	private static final int[] slotsTop = new int[] {0};
	private static final int[] slotsBottom = new int[] {2, 1};
	private static final int[] slotsSides = new int[] {1};
	/**0・・・変換前スロット 1・・・燃料スロット 2・・・完成品スロット 3・・・変換色スロット*/
	protected ItemStack[] itemStacks = new ItemStack[4];
	/**燃え尽きるまでの残り時間*/
	public int burnTime;
	/**現在の燃焼時間*/
	public int currentItemBurnTime;
	/**変換にかかる残り時間*/
	public int conversionTime;
	/**金床で設定された名前*/
	private String customName;
	/**この修繕機のGrade*/
	private int grade = 0;

	@Override
	public int getSizeInventory() {
		return this.itemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.itemStacks[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int number) {
		if (this.itemStacks[slot] != null) {
			ItemStack itemstack;

			if (this.itemStacks[slot].stackSize <= number) {
				itemstack = this.itemStacks[slot];
				this.itemStacks[slot] = null;
				return itemstack;
			} else {
				itemstack = this.itemStacks[slot].splitStack(number);

				if (this.itemStacks[slot].stackSize == 0) {
					this.itemStacks[slot] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.itemStacks[slot] != null) {
			ItemStack itemstack = this.itemStacks[slot];
			this.itemStacks[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.itemStacks[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**インベントリ名の設定*/
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.conversionmachine-" + grade;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && this.customName.length() > 0;
	}

	/**NBTタグの読み取り*/
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		this.itemStacks = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.itemStacks.length) {
				this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.burnTime = nbt.getShort("BurnTime");
		this.conversionTime = nbt.getShort("ConversionTime");
		this.currentItemBurnTime = getItemBurnTime(this.itemStacks[1]);

		if (nbt.hasKey("CustomName", 8)) {
			this.customName = nbt.getString("CustomName");
		}
	}

	/**NBTタグの書き込み*/
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("BurnTime", (short)this.burnTime);
		nbt.setShort("ConversionTime", (short)this.conversionTime);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.itemStacks.length; ++i) {
			if (this.itemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.itemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);

		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.customName);
		}
	}

	/**インベントリのスタックリミット*/
	@Override
	public int getInventoryStackLimit() {
		return 65536;
	}

	@SideOnly(Side.CLIENT)
	public int getConversionProgressScaled(int par1) {
		return this.conversionTime * par1 / this.getConversionTime();
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int par1) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = 200;
		}

		return this.burnTime * par1 / this.currentItemBurnTime;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}

	/**アップデート時の処理*/
	@Override
	public void updateEntity() {
		boolean flag = this.burnTime > 0;
		boolean flag1 = false;

		//燃えているなら、burnTimeを減らす
		if (this.burnTime > 0) {
			--this.burnTime;
		}

		//サーバー側のみでの処理
		if (!this.worldObj.isRemote) {
			//燃えているもしくは燃料スロットに何か入っているうえで、変換前スロットが空でないとき
			if (this.burnTime != 0 || this.itemStacks[1] != null && this.itemStacks[0] != null) {

				//燃焼時間がなく、変換可能ならば
				if (this.burnTime == 0 && this.canConversion()) {
					//燃料スロットのアイテムの燃焼時間をburnTimeとcurrentItemBurnTimeに代入する
					this.currentItemBurnTime = this.burnTime = getItemBurnTime(this.itemStacks[1]);
					//その結果burnTimeが0以上のとき
					if (this.burnTime > 0) {
						//flag1をtrueにする
						flag1 = true;
						//また、燃料スロットにアイテムが入っていれば
						if (this.itemStacks[1] != null) {
							//そのアイテムの数を減らす
							--this.itemStacks[1].stackSize;
							//その結果アイテムの数が0になったら
							if (this.itemStacks[1].stackSize == 0) {
								//更新する?
								this.itemStacks[1] = itemStacks[1].getItem().getContainerItem(itemStacks[1]);
							}
						}
					}
				}

				//燃焼中で、変換可能ならば
				if (this.isBurning() && this.canConversion()) {
					//変換時間を増やし
					++this.conversionTime;
					//変換時間が完成時間に達したら
					if (this.conversionTime >= this.getConversionTime()) {
						//変換時間を初期化し、
						this.conversionTime = 0;
						//変換処理を行い
						this.conversionItem();
						//flag1をtrueにする
						flag1 = true;
					}
				//違うなら、
				} else {
					//変換時間を0にする
					this.conversionTime = 0;
				}

			} else {
				this.conversionTime = 0;
			}
			if (flag != this.burnTime > 0) {
				flag1 = true;
				ConversionMachine.updateBlockState(this.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}

		if (flag1) {
			//保存処理?
			this.markDirty();
		}
	}

	/**変換が完成するまでの時間*/
	protected int getConversionTime() {
		return OfalenModConfigCore.timeConversion * (4 - grade) / 4;
	}

	/**変換可能かどうかの判定*/
	protected boolean canConversion() {
		//変換前スロットが空ならば
		if (this.itemStacks[0] == null || this.itemStacks[3] == null) {
			//変換不可
			return false;
		//変換前スロットと変換色スロットのアイテムが同じで、ダメージが違うなら
		} else if (this.itemStacks[0].getItem() == this.itemStacks[3].getItem() && this.itemStacks[0].getItemDamage() != this.itemStacks[3].getItemDamage()) {
			Item item = this.itemStacks[0].getItem();
			if ((item instanceof Ofalen) || (Block.getBlockFromItem(item) == OfalenModBlockCore.blockOfalen)) {
				if (item != OfalenModItemCore.coreOfalen || this.itemStacks[3].getItemDamage() != 3) {
					return true;
				}
			}
		} else if (OfalenModOreDicCore.isTakumiCraftLoaded) {
			if (this.itemStacks[0].isItemEqual(OfalenModOreDicCore.ofalenCreeper)) {
				if (Block.getBlockFromItem(this.itemStacks[3].getItem()) == OfalenModBlockCore.blockOfalen) {
					return true;
				}
			}
		}
		//アイテムは入っているが修繕できないときはスロットを移動させる
		this.moveItemStack();
		return false;
	}

	/**変換処理*/
	public void conversionItem() {
		//修繕可能ならば
		if (this.canConversion()) {
			//完成品スロットが空ならば
			if (this.itemStacks[2] == null) {
				//完成品スロットに変換結果を代入する
				this.itemStacks[2] = new ItemStack(itemStacks[0].getItem(), 1, itemStacks[3].getItemDamage());
			//完成品スロットと変換結果が一致したら
			} else if (this.itemStacks[2].isItemEqual(itemStacks[3])) {
				//アイテム数を増やす
				this.itemStacks[2].stackSize ++;
			}

			//変換前スロットのアイテム数を減らす
			--this.itemStacks[0].stackSize;

			//変換前アイテムがなくなったら
			if (this.itemStacks[0].stackSize <= 0) {
				//変換前スロットを空にする
				this.itemStacks[0] = null;
			}
		} else {
			this.moveItemStack();
		}
		//Gradeを更新する
		this.setGrade(ConversionMachine.getGrade(this.worldObj, this.xCoord, this.yCoord, this.zCoord));
	}

	/**変換前スロットから完成品スロットへ移動させる*/
	private void moveItemStack() {
		//完成品スロットが空ならば
		if (this.itemStacks[2] == null) {
			//完成品スロットに変換前スロットのアイテムを代入する
			this.itemStacks[2] = itemStacks[0].copy();
			//変換前スロットを空にする
			this.itemStacks[0] = null;
		//完成品スロットと変換前スロットが一致したら
		} else if (this.itemStacks[2].getItem() == itemStacks[0].getItem()) {
			//完成品スロットのアイテム数と変換前スロットのアイテム数を足した数
			int result = itemStacks[2].stackSize + 1;
			//resultがインベントリ・アイテムのスタックリミット以下なら
			if (result <= getInventoryStackLimit() && result <= this.itemStacks[2].getMaxStackSize()) {
				//アイテム数を増やす
				this.itemStacks[2].stackSize += 1;
				//変換前スロットのアイテム数を減らす
				--this.itemStacks[0].stackSize;
			}
			//変換前アイテムがなくなったら
			if (this.itemStacks[0].stackSize <= 0) {
				//変換前スロットを空にする
				this.itemStacks[0] = null;
			}
		}
	}

	/**そのアイテムの燃焼時間を返す*/
	public static int getItemBurnTime(ItemStack itemStack) {
		//受け取ったItemStackが空ならば
		if (itemStack == null) {
			//0を返す
			return 0;
		} else {
			//アイテムを取得する
			Item item = itemStack.getItem();
			//メタデータを取得する
			int damage = itemStack.getItemDamage();
			if (item == OfalenModItemCore.partsOfalen) {
				if (damage == 3) {
					return OfalenModConfigCore.timeBurnStone;
				} else if (damage == 4) {
					return OfalenModConfigCore.timeBurnOfalen;
				}
			}
			return 0;
		}
	}

	/**そのアイテムが燃料かどうかの判定*/
	public static boolean isItemFuel(ItemStack itemStack) {
		//燃焼時間があるならばtrueを返す
		return getItemBurnTime(itemStack) > 0;
	}

	/**Gradeを設定する*/
	public void setGrade(int grade) {
		this.grade = grade;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 2 ? false : (slot == 1 ? isItemFuel(itemStack) : true);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return side != 0 || slot != 1 || itemStack.getItem() == Items.bucket;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public void setCustomName(String displayName) {
		this.customName = displayName;
	}
}