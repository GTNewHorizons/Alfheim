package alfheim.common.spell.wind

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import codechicken.lib.math.MathHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType

class SpellThor: SpellBase("thor", EnumRace.SYLPH, 6000, 1200, 30) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result: SpellBase.SpellCastResult
		val hit: Vector3
		val mop = ASJUtilities.getSelectedBlock(caster, 32.0, true)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1)
			hit = Vector3(caster.lookVec).normalize().mul(32.0).add(caster.posX, caster.posY + caster.eyeHeight, caster.posZ)
		else
			hit = Vector3(mop.blockX.toDouble(), mop.blockY.toDouble(), mop.blockZ.toDouble())
		
		var x = MathHelper.floor_double(hit.x)
		var y = MathHelper.floor_double(hit.y)
		var z = MathHelper.floor_double(hit.z)
		
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
		
		if (caster.worldObj.canBlockSeeTheSky(x, y, z) && caster.worldObj.getPrecipitationHeight(x, z) <= y) {
			result = checkCast(caster)
			if (result != SpellBase.SpellCastResult.OK) return result
			caster.worldObj.addWeatherEffect(EntityLightningBolt(caster.worldObj, x.toDouble(), y.toDouble(), z.toDouble()))
			return result
		}
		
		return SpellBase.SpellCastResult.WRONGTGT
	}
}