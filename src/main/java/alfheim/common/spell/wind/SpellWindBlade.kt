package alfheim.common.spell.wind

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellWindBlade
import net.minecraft.entity.EntityLivingBase

class SpellWindBlade: SpellBase("windbalde", EnumRace.SYLPH, 8000, 120, 10) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellBase.SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellWindBlade(caster.worldObj, caster))
		return result
	}
}