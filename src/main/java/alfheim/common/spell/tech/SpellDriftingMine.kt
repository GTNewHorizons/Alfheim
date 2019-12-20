package alfheim.common.spell.tech

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellDriftingMine
import net.minecraft.entity.EntityLivingBase

object SpellDriftingMine: SpellBase("driftingmine", EnumRace.LEPRECHAUN, 6000, 1200, 15) {
	
	override var damage = 6f
	override var duration = 2400
	override var efficiency = 0.05
	override var radius = 5.0
	
	override val usableParams
		get() = arrayOf(damage, duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellDriftingMine(caster.worldObj, caster))
		return result
	}
}