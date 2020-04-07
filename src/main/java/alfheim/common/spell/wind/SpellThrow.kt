package alfheim.common.spell.wind

import alexsocol.asjlib.I
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect

object SpellThrow: SpellBase("throw", EnumRace.SYLPH, 8000, 600, 10) {
	
	override var damage = 5f
	override var duration = 10
	override var radius = 2.0
	
	override val usableParams
		get() = arrayOf(damage, duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			caster.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDThrow, duration, efficiency.I, true))
			AlfheimCore.network.sendToAll(MessageEffect(caster.entityId, AlfheimConfigHandler.potionIDThrow, duration, efficiency.I))
			val v = Vector3(caster.lookVec).negate().mul(0.5)
			VisualEffectHandler.sendPacket(VisualEffects.THROW, caster.dimension, caster.posX, caster.posY, caster.posZ, v.x, v.y, v.z)
		}
		return result
	}
}