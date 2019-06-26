package alfheim.common.spell.wind

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellFenrirStorm
import net.minecraft.entity.EntityLivingBase

class SpellFenrirStorm: SpellBase("fenrirstorm", EnumRace.SYLPH, 1000, 100, 5) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellBase.SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellFenrirStorm(caster.worldObj, caster))
		return result
	}
}