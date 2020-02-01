package alfheim.common.spell.sound

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.util.*
import alfheim.common.entity.spell.EntitySpellHarp
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MovingObjectPosition.MovingObjectType

object SpellHarp: SpellBase("harp", EnumRace.POOKA, 15000, 3600, 30) {
	
	override var damage = 0.5f
	override var duration = 600
	override var efficiency = 20.0
	
	override val usableParams
		get() = arrayOf(damage, duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val mop = ASJUtilities.getSelectedBlock(caster, 32.0, true)
		val hit = if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1)
			Vector3(caster.lookVec).normalize().mul(32.0).add(caster.posX, caster.posY + caster.eyeHeight, caster.posZ)
		else
			Vector3(mop.blockX.D, mop.blockY.D, mop.blockZ.D)
		
		var x = hit.x.mfloor() + 0.5
		var y = hit.y.mfloor() + 0.5
		var z = hit.z.mfloor() + 0.5
		
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
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellHarp(caster.worldObj, caster, x, y, z))
		return result
	}
}