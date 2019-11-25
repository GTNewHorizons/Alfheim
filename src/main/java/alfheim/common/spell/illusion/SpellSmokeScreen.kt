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
import net.minecraft.potion.*

object SpellSmokeScreen: SpellBase("smokescreen", EnumRace.SPRIGGAN, 5000, 600, 20) {
	
	override var duration = 200
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(radius, radius, radius)) as List<EntityLivingBase>
		for (living in list) {
			if (PartySystem.mobsSameParty(caster, living) || Vector3.entityDistance(living, caster) > radius) continue
			living.addPotionEffect(PotionEffect(Potion.blindness.id, duration, -1, true))
			AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.blindness.id, duration, -1))
		}
		VisualEffectHandler.sendPacket(VisualEffects.SMOKE, caster)
		return result
	}
}