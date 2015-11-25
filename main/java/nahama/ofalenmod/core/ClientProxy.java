package nahama.ofalenmod.core;

import nahama.ofalenmod.entity.EntityBlueLaser;
import nahama.ofalenmod.entity.EntityExplosionBall;
import nahama.ofalenmod.entity.EntityFlameBall;
import nahama.ofalenmod.entity.EntityGreenLaser;
import nahama.ofalenmod.entity.EntityRedLaser;
import nahama.ofalenmod.entity.EntityWhiteLaser;
import nahama.ofalenmod.model.ModelLaser;
import nahama.ofalenmod.nei.OfalenModNEILoad;
import nahama.ofalenmod.renderer.ItemPistolRenderer;
import nahama.ofalenmod.renderer.RenderLaser;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public void registerRender() {
		//エンティティとレンダラーの紐付け
		RenderingRegistry.registerEntityRenderingHandler(EntityExplosionBall.class, new RenderSnowball(OfalenModItemCore.ballExplosion));
		RenderingRegistry.registerEntityRenderingHandler(EntityFlameBall.class, new RenderSnowball(OfalenModItemCore.ballFlame));
		RenderingRegistry.registerEntityRenderingHandler(EntityRedLaser.class, new RenderLaser(new ModelLaser(), "red"));
		RenderingRegistry.registerEntityRenderingHandler(EntityGreenLaser.class, new RenderLaser(new ModelLaser(), "green"));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlueLaser.class, new RenderLaser(new ModelLaser(), "blue"));
		RenderingRegistry.registerEntityRenderingHandler(EntityWhiteLaser.class, new RenderLaser(new ModelLaser(), "white"));
		//アイテムとモデルの紐付け
		MinecraftForgeClient.registerItemRenderer(OfalenModItemCore.pistolLaser, new ItemPistolRenderer());
	}

	@Override
	public void loadNEI() {
		if (Loader.isModLoaded("NotEnoughItems")) {
			try {
				OfalenModNEILoad.load();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

}