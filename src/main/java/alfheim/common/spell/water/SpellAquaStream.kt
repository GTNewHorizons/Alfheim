package alfheim.common.spell.water

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellAquaStream
import net.minecraft.entity.EntityLivingBase

class SpellAquaStream: SpellBase("aquastream", EnumRace.UNDINE, 2000, 100, 5) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellBase.SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellAquaStream(caster.worldObj, caster))
		return result
	}
}