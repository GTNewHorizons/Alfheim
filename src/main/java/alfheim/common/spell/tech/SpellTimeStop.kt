package alfheim.common.spell.tech

import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellVisualizations
import alfheim.common.core.handler.CardinalSystem.TimeStopSystem
import alexsocol.asjlib.security.InteractionSecurity
import net.minecraft.entity.EntityLivingBase

object SpellTimeStop: SpellBase("timestop", EnumRace.LEPRECHAUN, 256000, 75000, 100, true) {
	
	override var duration = 100
	
	override val usableParams: Array<Any>
		get() = arrayOf(duration, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (!InteractionSecurity.canDoSomethingHere(caster)) return SpellCastResult.NOTALLOW
		
		val (x, y, z) = Vector3.fromEntity(caster)
		val rads = arrayOf(-radius, radius)
		for (rx in rads)
			for (rz in rads)
				if (!InteractionSecurity.canDoSomethingHere(caster, x + rx, y, z + rz))
					return SpellCastResult.NOTALLOW
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) TimeStopSystem.stop(caster)
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		SpellVisualizations.negateSphere(radius / 32)
	}
}