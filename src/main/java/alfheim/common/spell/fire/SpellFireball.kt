package alfheim.common.spell.fire

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellFireball
import net.minecraft.entity.EntityLivingBase

object SpellFireball: SpellBase("fireball", EnumRace.SALAMANDER, 1000, 50, 5) {
	
	override var damage = 6f
	override var duration = 100 // lifetime
	override var efficiency = 0.1 // speed
	override var radius = 2.0 // AoE
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellFireball(caster.worldObj, caster))
		return result
	}
}