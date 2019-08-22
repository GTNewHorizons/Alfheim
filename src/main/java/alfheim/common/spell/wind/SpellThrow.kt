package alfheim.common.spell.wind

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect

class SpellThrow: SpellBase("throw", EnumRace.SYLPH, 8000, 600, 10) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCast(caster)
		if (result == SpellBase.SpellCastResult.OK) {
			caster.addPotionEffect(PotionEffect(AlfheimRegistry.tHrOw.id, 10, 0, true))
			AlfheimCore.network.sendToAll(MessageEffect(caster.entityId, AlfheimRegistry.tHrOw.id, 10, 0))
			val v = Vector3(caster.lookVec).negate().mul(0.5)
			SpellEffectHandler.sendPacket(Spells.THROW, caster.dimension, caster.posX, caster.posY, caster.posZ, v.x, v.y, v.z)
		}
		return result
	}
}