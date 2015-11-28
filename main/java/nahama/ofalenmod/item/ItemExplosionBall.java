package nahama.ofalenmod.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.OfalenModCore;
import nahama.ofalenmod.core.OfalenModItemCore;
import nahama.ofalenmod.entity.EntityExplosionBall;
import nahama.ofalenmod.entity.EntityFlameBall;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemExplosionBall extends Item {

	private IIcon[] iicon;

	public ItemExplosionBall() {
		super();
		this.setCreativeTab(OfalenModCore.tabOfalen);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	/** 右クリックされた時の処理。 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		// クリエイティブモードでないならスタックサイズを減らす
		if (!player.capabilities.isCreativeMode) {
			--itemStack.stackSize;
		}

		// 音を出す
		world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		// Entityを生成する
		if (itemStack.getItem() == OfalenModItemCore.ballExplosion) {
			world.spawnEntityInWorld(new EntityExplosionBall(world, player, this.getDamage(itemStack)));
		} else if (itemStack.getItem() == OfalenModItemCore.ballFlame) {
			world.spawnEntityInWorld(new EntityFlameBall(world, player, this.getDamage(itemStack)));
		}

		return itemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta];
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 3; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iicon) {
		this.iicon = new IIcon[3];
		for (int i = 0; i < 3; i++) {
			this.iicon[i] = iicon.registerIcon(this.getIconString() + i);
		}
	}

}