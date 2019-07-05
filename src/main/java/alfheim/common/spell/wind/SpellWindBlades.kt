package alfheim.common.spell.wind

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellWindBlade
import net.minecraft.entity.EntityLivingBase

class SpellWindBlades: SpellBase("windblades", EnumRace.SYLPH, 8000, 120, 10) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellCastResult.OK) {
			caster.worldObj.spawnEntityInWorld(EntitySpellWindBlade(caster.worldObj, caster,-1.0))
			caster.worldObj.spawnEntityInWorld(EntitySpellWindBlade(caster.worldObj, caster))
			caster.worldObj.spawnEntityInWorld(EntitySpellWindBlade(caster.worldObj, caster, 1.0))
		}
		return result
	}
}