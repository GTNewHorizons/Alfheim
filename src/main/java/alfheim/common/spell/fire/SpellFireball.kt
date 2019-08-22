package alfheim.common.spell.fire

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellFireball
import net.minecraft.entity.EntityLivingBase

class SpellFireball: SpellBase("fireball", EnumRace.SALAMANDER, 1000, 50, 5) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellBase.SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellFireball(caster.worldObj, caster))
		return result
	}
}