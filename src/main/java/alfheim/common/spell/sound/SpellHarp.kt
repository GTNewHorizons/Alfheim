package alfheim.common.spell.sound

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellHarp
import codechicken.lib.math.MathHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MovingObjectPosition.MovingObjectType

class SpellHarp: SpellBase("harp", EnumRace.POOKA, 15000, 3600, 30) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result: SpellBase.SpellCastResult
		val hit: Vector3
		val mop = ASJUtilities.getSelectedBlock(caster, 32.0, true)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1)
			hit = Vector3(caster.lookVec).normalize().mul(32.0).add(caster.posX, caster.posY + caster.eyeHeight, caster.posZ)
		else
			hit = Vector3(mop.blockX.toDouble(), mop.blockY.toDouble(), mop.blockZ.toDouble())
		
		var x = MathHelper.floor_double(hit.x) + 0.5
		var y = MathHelper.floor_double(hit.y) + 0.5
		var z = MathHelper.floor_double(hit.z) + 0.5
		
		if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK && mop.sideHit != -1) {
			when (mop.sideHit) {
				0 -> --y
				1 -> ++y
				2 -> --z
				3 -> ++z
				4 -> --x
				5 -> ++x
			}
		}
		
		result = checkCast(caster)
		if (result == SpellBase.SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellHarp(caster.worldObj, caster, x, y, z))
		return result
	}
}