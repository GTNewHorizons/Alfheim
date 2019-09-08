package alfheim.common.spell.tech

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellVisualizations
import alfheim.common.core.handler.CardinalSystem.TimeStopSystem
import net.minecraft.entity.EntityLivingBase

class SpellTimeStop: SpellBase("timestop", EnumRace.LEPRECHAUN, 256000, 75000, 100, true) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) TimeStopSystem.stop(caster)
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		SpellVisualizations.negateSphere(0.5)
	}
}