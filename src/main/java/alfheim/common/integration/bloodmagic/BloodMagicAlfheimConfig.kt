package alfheim.common.integration.bloodmagic

import alfheim.common.block.AlfheimBlocks
import net.minecraftforge.common.MinecraftForge

object BloodMagicAlfheimConfig  {
	
	val blacklist = arrayOf(AlfheimBlocks.alfheimPortal, AlfheimBlocks.anyavil, AlfheimBlocks.manaAccelerator)
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	// this is generated while class transformation
	
	/*@SubscribeEvent
	fun onTeleposing(e: TeleposeEvent) {
		if (e.finalBlock in blacklist || e.initialBlock in blacklist) e.isCanceled = true
	}*/
}