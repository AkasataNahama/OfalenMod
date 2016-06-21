package nahama.ofalenmod.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterOfalen extends Teleporter {
	private final WorldServer world;

	public TeleporterOfalen(WorldServer world) {
		super(world);
		this.world = world;
	}

	/** Entityをポータルの中に移動する。 */
	@Override
	public void placeInPortal(Entity entity, double x, double y, double z, float par5) {
		this.placeInExistingPortal(entity, x, y, z, par5);
	}

	/** Entityを有効なポータルの中に移動する。 */
	@Override
	public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float par5) {
		// Entityの動きを消す。
		entity.motionX = entity.motionY = entity.motionZ = 0.0D;
		return true;
	}

}
