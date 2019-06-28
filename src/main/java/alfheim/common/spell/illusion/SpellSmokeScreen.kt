package alfheim.common.spell.illusion

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.*

class SpellSmokeScreen: SpellBase("smokescreen", EnumRace.SPRIGGAN, 5000, 600, 20) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(16.0, 16.0, 16.0)) as List<EntityLivingBase>
		for (living in list) {
			if (PartySystem.mobsSameParty(caster, living) || Vector3.entityDistance(living, caster) > 16) continue
			living.addPotionEffect(PotionEffect(Potion.blindness.id, 200, -1, true))
			AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.blindness.id, 200, -1))
		}
		SpellEffectHandler.sendPacket(Spells.SMOKE, caster)
		return result
	}
}