package alfheim.api.event

import cpw.mods.fml.common.eventhandler.Cancelable
import cpw.mods.fml.common.eventhandler.Event
import net.minecraft.entity.Entity

@Cancelable
class EntityUpdateEvent(val entity: Entity): Event() {
	
	val isRiding: Boolean
	
	init {
		isRiding = entity.ridingEntity != null
	}
	
	companion object {
		
		/** Used in ASM  */
		fun instantiate(e: Entity): EntityUpdateEvent {
			return EntityUpdateEvent(e)
		}
		
		/** Used in ASM  */
		fun stub() {}
	}
}
