package alfheim.api.event

import cpw.mods.fml.common.eventhandler.*
import net.minecraft.tileentity.TileEntity

@Cancelable
class TileUpdateEvent(val tile: TileEntity): Event() {
	
	companion object {
		
		/** Used in ASM  */
		@JvmStatic
		fun instantiate(te: TileEntity): TileUpdateEvent {
			return TileUpdateEvent(te)
		}
		
		/** Used in ASM  */
		@JvmStatic
		fun stub() {}
	}
}
