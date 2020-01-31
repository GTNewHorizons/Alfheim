package alfheim.api.block

import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 * Have a block implement this class to make it do something when an adjacent
 * Hovering Hourglass turns.
 */
interface IHourglassTrigger {
	
	fun onTriggeredByHourglass(world: World, x: Int, y: Int, z: Int, hourglass: TileEntity)
	
}