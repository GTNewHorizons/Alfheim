package alexsocol.patcher.event

import cpw.mods.fml.common.eventhandler.Event
import net.minecraft.world.WorldServer

/**
 * Fired when all players are woken up by server in [net.minecraft.world.WorldServer.wakeAllPlayers]
 *
 * This event is fired on the [net.minecraftforge.common.MinecraftForge.EVENT_BUS]
 */
class ServerWakeUpEvent(val world: WorldServer): Event()