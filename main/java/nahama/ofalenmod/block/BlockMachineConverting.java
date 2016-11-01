package nahama.ofalenmod.block;

import nahama.ofalenmod.tileentity.TileEntityConvertingMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachineConverting extends BlockMachineBase {
	/** TileEntityを生成して返す。 */
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityConvertingMachine();
	}
}
