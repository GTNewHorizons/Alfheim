package alfheim.common.spell.earth

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellMortar
import net.minecraft.entity.EntityLivingBase

object SpellMortar: SpellBase("mortar", EnumRace.GNOME, 7500, 200, 5) {
	
	override var damage = 8f
	override var duration = 100 // lifetime
	override var efficiency = 2.0 // speed
	override var radius = 2.0 // AoE
	
	override val usableParams
		get() = arrayOf(damage, duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellMortar(caster.worldObj, caster))
		return result
	}
}