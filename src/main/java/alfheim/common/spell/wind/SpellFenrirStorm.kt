package alfheim.common.spell.wind

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellFenrirStorm
import net.minecraft.entity.EntityLivingBase

object SpellFenrirStorm: SpellBase("fenrirstorm", EnumRace.SYLPH, 1000, 100, 5) {
	
	override var damage = 10f
	override var radius = 8.0
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellFenrirStorm(caster.worldObj, caster))
		return result
	}
}