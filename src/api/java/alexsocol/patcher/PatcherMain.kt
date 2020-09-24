package alexsocol.patcher

import alexsocol.asjlib.command.CommandDimTP
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLServerStartingEvent

@Mod(modid = "ASJPatcher")
class PatcherMain {
	
	@Mod.EventHandler
	fun onServerStart(e: FMLServerStartingEvent) {
		e.registerServerCommand(CommandDimTP)
	}
}