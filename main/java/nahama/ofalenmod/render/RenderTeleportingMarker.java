package nahama.ofalenmod.render;

import nahama.ofalenmod.model.ModelTeleportingMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTeleportingMarker extends TileEntitySpecialRenderer {
	ResourceLocation texture = new ResourceLocation("ofalenmod:textures/models/teleporting_marker.png");
	private final ModelTeleportingMarker model = new ModelTeleportingMarker();

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
	}
}
