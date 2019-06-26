package alfheim.common.spell.tech

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellDriftingMine
import net.minecraft.entity.EntityLivingBase

class SpellDriftingMine: SpellBase("driftingmine", EnumRace.LEPRECHAUN, 6000, 1200, 15) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellBase.SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellDriftingMine(caster.worldObj, caster))
		return result
	}
}