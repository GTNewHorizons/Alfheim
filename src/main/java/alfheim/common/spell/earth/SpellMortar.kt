package alfheim.common.spell.earth

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellMortar
import net.minecraft.entity.EntityLivingBase

class SpellMortar: SpellBase("mortar", EnumRace.GNOME, 7500, 200, 5) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellMortar(caster.worldObj, caster))
		return result
	}
}