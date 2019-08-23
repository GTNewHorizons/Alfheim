package alfheim.common.potion

import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.registry.AlfheimRegistry
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed

class PotionGoldRush: PotionAlfheim(AlfheimConfigHandler.potionIDGoldRush, "goldRush", false, 0x55FF00) {
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun onBreakSpeed(e: BreakSpeed) {
		if (AlfheimCore.enableMMO && e.entityLiving.isPotionActive(AlfheimRegistry.goldRush)) e.newSpeed *= 2f
	}
}
