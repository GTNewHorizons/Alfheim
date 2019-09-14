package alfheim.common.potion

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.EntityLivingBase

class PotionShowMana: PotionAlfheim(AlfheimConfigHandler.potionIDShowMana, "showMana", false, 0x0000DD) {
	
	override fun isReady(time: Int, ampl: Int): Boolean {
		return true
	}
	
	override fun performEffect(living: EntityLivingBase, ampl: Int) {
		if (!AlfheimCore.enableMMO) return
		val pe = living.getActivePotionEffect(this) ?: return
		
		if (ASJUtilities.isServer || pe.amplifier <= 0) {
			pe.duration = 1
			return
		} else {
			if (pe.duration < Integer.MAX_VALUE) ++pe.duration
			--pe.amplifier
		}
		
		if (!ASJUtilities.isServer) {
			var i = 0
			while (i < Math.sqrt(Math.sqrt(Math.sqrt(pe.duration.toDouble())))) {
				// looks like this "i < VALUE" is fine
				VisualEffectHandlerClient.spawnMana(living, i.toDouble())
				i++
			}
		}
	}
}
