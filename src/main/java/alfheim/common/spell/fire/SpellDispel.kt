package alfheim.common.spell.fire

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.security.InteractionSecurity
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*
import java.util.*

object SpellDispel: SpellBase("dispel", EnumRace.SALAMANDER, 1000, 600, 25) {
	
	override var duration = 300
	
	override val usableParams: Array<Any>
		get() = arrayOf(duration)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		val tgt = tg.target ?: return SpellCastResult.NOTARGET
		
		if (tgt !== caster && ASJUtilities.isNotInFieldOfVision(tgt, caster)) return SpellCastResult.NOTSEEING
		
		if (!InteractionSecurity.canDoSomethingWithEntity(caster, tgt)) return SpellCastResult.NOTALLOW
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			val l = ArrayList<PotionEffect>()
			for (o in tgt.activePotionEffects) if (Potion.potionTypes[(o as PotionEffect).potionID].isBadEffect == tg.isParty) if (o.potionID != AlfheimConfigHandler.potionIDLeftFlame) l.add(o)
			
			if (l.isEmpty()) {
				tgt.addPotionEffect(PotionEffect(Potion.confusion.id, duration, 0, true))
				AlfheimCore.network.sendToAll(MessageEffect(tgt.entityId, Potion.confusion.id, duration, 0))
			} else {
				for (pe in l) {
					tgt.removePotionEffect(pe.potionID)
					AlfheimCore.network.sendToAll(MessageEffect(tgt.entityId, pe.getPotionID(), 0, 0))
				}
			}
			VisualEffectHandler.sendPacket(VisualEffects.DISPEL, tgt)
		}
		
		return result
	}
}