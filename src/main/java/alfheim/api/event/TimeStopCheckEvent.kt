package alfheim.api.event

import cpw.mods.fml.common.eventhandler.*
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity

/**
 *
 * Fired to change (tile)entity's behavior in time-stop area
 *
 * You should cancel event if you want it to take effect
 */
abstract class TimeStopCheckEvent: Event() {
	
	var result: Boolean = false
	
	@Cancelable
	class TimeStopEntityCheckEvent(val entity: Entity): TimeStopCheckEvent()
}