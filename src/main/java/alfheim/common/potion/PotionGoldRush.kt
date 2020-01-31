package alfheim.common.potion

import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.spell.earth.SpellGoldRush
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed

class PotionGoldRush: PotionAlfheim(AlfheimConfigHandler.potionIDGoldRush, "goldRush", false, 0x55FF00) {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun onBreakSpeed(e: BreakSpeed) {
		if (AlfheimCore.enableMMO && e.entityLiving.isPotionActive(this.id)) e.newSpeed *= SpellGoldRush.efficiency.toFloat()
	}
}
