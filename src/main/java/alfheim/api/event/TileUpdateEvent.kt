package alfheim.api.event

import cpw.mods.fml.common.eventhandler.Cancelable
import cpw.mods.fml.common.eventhandler.Event
import net.minecraft.tileentity.TileEntity

@Cancelable
class TileUpdateEvent(val tile: TileEntity): Event() {
	
	companion object {
		
		/** Used in ASM  */
		fun instantiate(te: TileEntity): TileUpdateEvent {
			return TileUpdateEvent(te)
		}
		
		/** Used in ASM  */
		fun stub() {}
	}
}
