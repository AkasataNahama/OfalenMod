package nahama.ofalenmod.block;

import nahama.ofalenmod.tileentity.TileEntityRepairMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRepairMachine extends BlockGradedMachineBase {

	/** TileEntityを生成して返す。 */
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityRepairMachine();
	}

}
