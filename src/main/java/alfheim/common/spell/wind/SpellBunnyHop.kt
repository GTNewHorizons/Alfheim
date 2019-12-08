package alfheim.common.spell.wind

import alexsocol.asjlib.ASJUtilities
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

object SpellBunnyHop: SpellBase("bunnyhop", EnumRace.SYLPH, 6000, 2400, 30) {
	
	override var duration = 3600
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
			tg.target.addPotionEffect(PotionEffect(Potion.jump.id, duration, efficiency.toInt(), true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, Potion.jump.id, duration, efficiency.toInt()))
			VisualEffectHandler.sendPacket(VisualEffects.HEAL, tg.target)
		}
		
		return result
	}
}