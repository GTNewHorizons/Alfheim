package alexsocol.patcher.event

import cpw.mods.fml.common.event.*
import cpw.mods.fml.common.eventhandler.Event
import net.minecraft.server.MinecraftServer

abstract class ServerStateEvent: Event()

class ServerStartingEvent(val event: FMLServerStartingEvent): ServerStateEvent()
class ServerStartedEvent(val event: FMLServerStartedEvent): ServerStateEvent()
class ServerStoppingEvent(val event: FMLServerStoppingEvent): ServerStateEvent()
class ServerStoppedEvent(val event: FMLServerStoppedEvent): ServerStateEvent()

val ServerStateEvent.save: String get() = MinecraftServer.getServer().entityWorld.saveHandler.worldDirectory.absolutePath
val ServerStartingEvent.save: String get() = event.server.entityWorld.saveHandler.worldDirectory.absolutePath