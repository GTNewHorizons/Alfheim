package alfheim.common.spell.nature

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect

class SpellSharedHealthPool: SpellBase("sharedhp", EnumRace.CAITSITH, 256000, 72000, 100, true) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster))
				 ?: return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (i in 0 until pt.count) {
			val mr = pt[i]
			if (mr != null && mr.isPotionActive(AlfheimConfigHandler.potionIDSharedHP)) {
				mr.removePotionEffect(AlfheimConfigHandler.potionIDSharedHP)
				AlfheimCore.network.sendToAll(MessageEffect(mr.entityId, AlfheimConfigHandler.potionIDSharedHP, 0, 0))
			}
		}
		
		var total = 0f
		var maxTotal = 0f
		for (i in 0 until pt.count) {
			val mr = pt[i]
			if (mr != null && Vector3.entityDistance(mr, caster) < 32) {
				total += mr.health
				maxTotal += mr.maxHealth
			}
		}
		
		for (i in 0 until pt.count) {
			val mr = pt[i]
			if (mr != null && Vector3.entityDistance(mr, caster) < 32) {
				val pe = PotionEffect(AlfheimConfigHandler.potionIDSharedHP, 36000, maxTotal.toInt(), true)
				pe.curativeItems.clear()
				mr.addPotionEffect(pe)
				AlfheimCore.network.sendToAll(MessageEffect(mr.entityId, AlfheimConfigHandler.potionIDSharedHP, 36000, maxTotal.toInt()))
				SpellEffectHandler.sendPacket(Spells.UPHEAL, mr)
			}
		}
		
		caster.health = total
		return result
	}
}