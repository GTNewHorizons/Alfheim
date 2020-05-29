package alfheim.common.block.container

import alexsocol.asjlib.math.Vector3
import alfheim.common.block.tile.TileRaceSelector
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container

class ContainerRaceSelector(val tile: TileRaceSelector): Container() {
	
	override fun canInteractWith(player: EntityPlayer) = Vector3.vecEntityDistance(Vector3(tile.xCoord, tile.yCoord, tile.zCoord), player) < 5
}
