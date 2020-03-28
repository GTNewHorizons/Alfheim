package alfheim.common.spell.darkness

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.network.MessageEffect
import alfheim.common.security.InteractionSecurity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect

object SpellDeathMark: SpellBase("deathmark", EnumRace.IMP, 24000, 3000, 10) {
	
	override var damage = Float.MAX_VALUE
	override var duration = 600
	
	override val usableParams
		get() = arrayOf(damage, duration)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		if (tg.target == null)
			return SpellCastResult.NOTARGET
		
		if (tg.target === caster || tg.isParty)
			return SpellCastResult.WRONGTGT
		
		if (ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		if (!InteractionSecurity.canHurtEntity(caster, tg.target)) return SpellCastResult.NOTALLOW
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDDeathMark, duration, 0, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, AlfheimConfigHandler.potionIDDeathMark, duration, 0))
			VisualEffectHandler.sendPacket(VisualEffects.DISPEL, tg.target)
		}
		
		return result
	}
}