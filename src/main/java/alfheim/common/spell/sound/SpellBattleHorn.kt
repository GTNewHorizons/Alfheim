package alfheim.common.spell.sound

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*

class SpellBattleHorn: SpellBase("battlehorn", EnumRace.POOKA, 5000, 600, 15) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster)) ?: return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (i in 0 until pt.count) {
			val living = pt[i] ?: continue
			if (Vector3.entityDistance(living, caster) < 32) {
				living.addPotionEffect(PotionEffect(Potion.damageBoost.id, 36000, 0, true))
				AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.damageBoost.id, 36000, 0))
				living.addPotionEffect(PotionEffect(Potion.moveSpeed.id, 36000, 0, true))
				AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.moveSpeed.id, 36000, 0))
				living.addPotionEffect(PotionEffect(Potion.resistance.id, 36000, 0, true))
				AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.resistance.id, 36000, 0))
			}
		}
		
		VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.HORN, caster)
		
		return result
	}
}