package nahama.ofalenmod.block;

import nahama.ofalenmod.tileentity.TileEntityRepairingMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineRepairing extends BlockMachineBase {
	/** TileEntityを生成して返す。 */
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityRepairingMachine();
	}
}
