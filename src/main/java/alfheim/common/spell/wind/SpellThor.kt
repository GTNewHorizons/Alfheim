package alfheim.common.spell.wind

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.security.InteractionSecurity
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.util.MovingObjectPosition.MovingObjectType

object SpellThor: SpellBase("thor", EnumRace.SYLPH, 6000, 1200, 30) {
	
	override var radius = 32.0
	
	override val usableParams: Array<Any>
		get() = arrayOf(radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result: SpellCastResult
		val mop = ASJUtilities.getSelectedBlock(caster, radius, true)
		
		val hit = if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1)
			Vector3(caster.lookVec).normalize().mul(radius).add(caster.posX, caster.posY + caster.eyeHeight, caster.posZ)
		else
			Vector3(mop.blockX.D, mop.blockY.D, mop.blockZ.D)
		
		var x = hit.x.mfloor()
		var y = hit.y.mfloor()
		var z = hit.z.mfloor()
		
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
		
		if (!InteractionSecurity.canDoSomethingHere(caster, x, y, z)) return SpellCastResult.NOTALLOW
		
		if (caster.worldObj.canBlockSeeTheSky(x, y, z) && caster.worldObj.getPrecipitationHeight(x, z) <= y) {
			result = checkCast(caster)
			if (result != SpellCastResult.OK) return result
			
			caster.worldObj.addWeatherEffect(EntityLightningBolt(caster.worldObj, x.D, y.D, z.D))
			return result
		}
		
		return SpellCastResult.WRONGTGT
	}
}