package alfheim.common.spell.nature

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect

class SpellSharedHealthPool: SpellBase("sharedhp", EnumRace.CAITSITH, 256000, 72000, 100, true) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster))
				 ?: return SpellBase.SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellBase.SpellCastResult.OK) return result
		
		for (i in 0 until pt.count) {
			val mr = pt.get(i)
			if (mr != null && mr!!.isPotionActive(AlfheimRegistry.sharedHP.id)) {
				mr!!.removePotionEffect(AlfheimRegistry.sharedHP.id)
				AlfheimCore.network.sendToAll(MessageEffect(mr!!.getEntityId(), AlfheimRegistry.sharedHP.id, 0, 0))
			}
		}
		
		var total = 0f
		var max = 0f
		for (i in 0 until pt.count) {
			val mr = pt.get(i)
			if (mr != null && Vector3.entityDistance(mr!!, caster) < 32) {
				total += mr!!.getHealth()
				max += mr!!.getMaxHealth()
			}
		}
		
		for (i in 0 until pt.count) {
			val mr = pt.get(i)
			if (mr != null && Vector3.entityDistance(mr!!, caster) < 32) {
				val pe = PotionEffect(AlfheimRegistry.sharedHP.id, 36000, max.toInt(), true)
				pe.curativeItems.clear()
				mr!!.addPotionEffect(pe)
				AlfheimCore.network.sendToAll(MessageEffect(mr!!.getEntityId(), AlfheimRegistry.sharedHP.id, 36000, max.toInt()))
				SpellEffectHandler.sendPacket(Spells.UPHEAL, mr!!)
			}
		}
		
		caster.health = total
		return result
	}
}