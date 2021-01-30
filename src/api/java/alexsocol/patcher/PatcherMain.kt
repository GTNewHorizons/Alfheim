package alexsocol.patcher

import alexsocol.asjlib.command.*
import alexsocol.patcher.asm.ASJHookLoader
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLServerStartingEvent

@Mod(modid = "asjpatcher")
class PatcherMain {
	
	@Mod.EventHandler
	fun onServerStart(e: FMLServerStartingEvent) {
		e.registerServerCommand(CommandDimTP)
		e.registerServerCommand(CommandSchema)
		
		if (!ASJHookLoader.OBF)
			e.registerServerCommand(CommandResources)
	}
}