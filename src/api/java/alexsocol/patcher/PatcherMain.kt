package alexsocol.patcher

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.command.*
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.*

@Mod(modid = "ASJPatcher")
class PatcherMain {
	
	@Mod.EventHandler
	fun construct(e: FMLConstructionEvent) {
		ASJUtilities.debug("I like trains")
	}
	
	@Mod.EventHandler
	fun onServerStart(e: FMLServerStartingEvent) {
		e.registerServerCommand(CommandDimTP)
		e.registerServerCommand(CommandSchema)
	}
}