package alfheim.common.spell.fire

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*
import java.util.*

class SpellDispel: SpellBase("dispel", EnumRace.SALAMANDER, 1000, 600, 25) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		if (caster !is EntityPlayer) return SpellBase.SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		if (tg == null || tg.target == null)
			return SpellBase.SpellCastResult.NOTARGET
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellBase.SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellBase.SpellCastResult.OK) {
			val l = ArrayList<PotionEffect>()
			for (o in tg.target.activePotionEffects) if (Potion.potionTypes[(o as PotionEffect).getPotionID()].isBadEffect == tg.isParty) l.add(o)
			
			if (l.isEmpty()) {
				tg.target.addPotionEffect(PotionEffect(Potion.confusion.id, 300, 0, true))
				AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, Potion.confusion.id, 300, 0))
			} else {
				for (pe in l) {
					tg.target.removePotionEffect(pe.potionID)
					AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, pe.getPotionID(), 0, 0))
				}
			}
			SpellEffectHandler.sendPacket(Spells.DISPEL, tg.target)
		}
		
		return result
	}
}