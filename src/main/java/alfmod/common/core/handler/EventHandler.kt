package alfmod.common.core.handler

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import net.minecraftforge.common.MinecraftForge

object EventHandler {
	
	init {
		FMLCommonHandler.instance().bus().register(this)
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	var nextRain = 0
	
	@SubscribeEvent
	fun onWorldTick(e: TickEvent.WorldTickEvent) {
		if (WRATH_OF_THE_WINTER && !e.world.worldInfo.isRaining) {
			if (--nextRain <= 0) {
				e.world.prevRainingStrength = 1f
				e.world.rainingStrength = 1f
				e.world.worldInfo.isRaining = true
				e.world.worldInfo.rainTime = e.world.rand.nextInt(12000) + 24000
				
				nextRain = e.world.rand.nextInt(12000) + 18000
			}
		}
	}
}