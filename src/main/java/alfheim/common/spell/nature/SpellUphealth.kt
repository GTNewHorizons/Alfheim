package alfheim.common.spell.nature

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*

object SpellUphealth: SpellBase("uphealth", EnumRace.CAITSITH, 10000, 1200, 30) {
	
	override var duration = 36000
	override var efficiency = 1.0
	
	override val usableParams
		get() = arrayOf(duration, efficiency)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		if (tg.target == null) return SpellCastResult.NOTARGET
		
		if (!tg.isParty) return SpellCastResult.WRONGTGT
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(Potion.field_76434_w.id, duration, efficiency.I, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, Potion.field_76434_w.id, duration, efficiency.I))
			VisualEffectHandler.sendPacket(VisualEffects.UPHEAL, tg.target)
		}
		
		return result
	}
}