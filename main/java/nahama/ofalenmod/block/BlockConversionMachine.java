package nahama.ofalenmod.block;

import nahama.ofalenmod.tileentity.TileEntityConversionMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockConversionMachine extends BlockGradedMachineBase {

	/** TileEntityを生成して返す。 */
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityConversionMachine();
	}

}
