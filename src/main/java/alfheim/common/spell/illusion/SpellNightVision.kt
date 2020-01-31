package alfheim.common.spell.illusion

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*

object SpellNightVision: SpellBase("nightvision", EnumRace.SPRIGGAN, 6000, 1200, 30) {
	
	override var duration = 36000
	
	override val usableParams: Array<Any>
		get() = arrayOf(duration)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster))
				 ?: return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (i in 0 until pt.count) {
			val living = pt[i] ?: continue
			if (Vector3.entityDistance(living, caster) < 32) {
				living.addPotionEffect(PotionEffect(Potion.nightVision.id, duration, -1, true))
				AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.nightVision.id, duration, -1))
				VisualEffectHandler.sendPacket(VisualEffects.NVISION, living)
			}
		}
		
		return result
	}
}