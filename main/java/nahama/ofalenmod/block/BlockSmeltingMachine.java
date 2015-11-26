package nahama.ofalenmod.block;

import nahama.ofalenmod.tileentity.TileEntitySmeltingMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSmeltingMachine extends BlockGradedMachineBase {

	/** TileEntityを生成して返す。 */
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntitySmeltingMachine();
	}

}
