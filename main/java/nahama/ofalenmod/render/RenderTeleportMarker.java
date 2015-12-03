package nahama.ofalenmod.render;

import org.lwjgl.opengl.GL11;

import nahama.ofalenmod.model.ModelTeleportMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTeleportMarker extends TileEntitySpecialRenderer {

	ResourceLocation texture = new ResourceLocation("ofalenmod:textures/models/TeleportMarker.png");
	private final ModelTeleportMarker model = new ModelTeleportMarker();

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		this.model.render((Entity) null, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
	}

}
