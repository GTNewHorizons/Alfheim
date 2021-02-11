package alfheim.common.block.magtrees.circuit

import net.minecraft.world.IBlockAccess

/**
 * @author WireSegal
 * Created at 10:00 PM on 5/29/16.
 */
interface ICircuitBlock {
	
	companion object {
		
		fun getPower(blockAccess: IBlockAccess, x: Int, y: Int, z: Int): Int {
			for (i in 1..15)
				if (blockAccess.getBlock(x, y + i, z) !is ICircuitBlock)
					return i - 1
			return 15
		}
	}
}