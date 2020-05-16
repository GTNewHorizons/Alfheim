package alfheim.common.potion

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.EntityLivingBase
import kotlin.math.sqrt

class PotionShowMana: PotionAlfheim(AlfheimConfigHandler.potionIDShowMana, "showMana", false, 0x0000DD) {
	
	override fun isReady(time: Int, ampl: Int) = true
	
	override fun performEffect(living: EntityLivingBase, ampl: Int) {
		if (!AlfheimConfigHandler.enableMMO) return
		val pe = living.getActivePotionEffect(id) ?: return
		
		if (ASJUtilities.isServer || pe.amplifier <= 0) {
			pe.duration = 1
			return
		}
		
		if (pe.duration < Integer.MAX_VALUE) ++pe.duration
		--pe.amplifier
		
		if (!ASJUtilities.isServer) {
			var i = 0
			while (i < sqrt(sqrt(sqrt(pe.duration.D)))) {
				// looks like this "i < VALUE" is fine
				VisualEffectHandlerClient.spawnMana(living, i.D * 0.5)
				i++
			}
		}
	}
}
