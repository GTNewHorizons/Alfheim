package alfheim.common.spell.fire

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
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
		if (tg.target == null) return SpellCastResult.NOTARGET
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			val l = ArrayList<PotionEffect>()
			for (o in tg.target.activePotionEffects) if (Potion.potionTypes[(o as PotionEffect).potionID].isBadEffect == tg.isParty) if (o.potionID != AlfheimConfigHandler.potionIDLeftFlame) l.add(o)
			
			if (l.isEmpty()) {
				tg.target.addPotionEffect(PotionEffect(Potion.confusion.id, duration, 0, true))
				AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, Potion.confusion.id, duration, 0))
			} else {
				for (pe in l) {
					tg.target.removePotionEffect(pe.potionID)
					AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, pe.getPotionID(), 0, 0))
				}
			}
			VisualEffectHandler.sendPacket(VisualEffects.DISPEL, tg.target)
		}
		
		return result
	}
}