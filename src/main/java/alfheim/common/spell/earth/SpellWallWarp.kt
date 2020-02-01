package alfheim.common.spell.earth

import alexsocol.asjlib.ASJUtilities
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.util.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.util.MovingObjectPosition.MovingObjectType

object SpellWallWarp: SpellBase("wallwarp", EnumRace.GNOME, 4000, 600, 5) {
	
	override var radius = 2.0
	
	override val usableParams: Array<Any>
		get() = arrayOf(radius)
	
	override// This spell is slightly changed version of item from thKaguya's mod
	fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result: SpellCastResult
		
		val dist = (caster as? EntityPlayerMP)?.theItemInWorldManager?.blockReachDistance ?: 5.0
		val mop = ASJUtilities.getSelectedBlock(caster, dist, false)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return SpellCastResult.WRONGTGT
		
		var px = 0
		var py = 0
		var pz = 0
		when (mop.sideHit) {
			0    -> py = 1
			1    -> py = -1
			2    -> pz = 1
			3    -> pz = -1
			4    -> px = 1
			else -> px = -1
		}
		
		for (i in 0..radius.I) {
			if (caster.worldObj.isAirBlock(mop.blockX, mop.blockY, mop.blockZ)) {
				if (caster.worldObj.isAirBlock(mop.blockX, mop.blockY + 1, mop.blockZ)) {
					result = checkCast(caster)
					if (result != SpellCastResult.OK) return result
					
					caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5f, 1f)
					caster.posX = mop.blockX + 0.5
					caster.posY = (mop.blockY + caster.yOffset).D
					caster.posZ = mop.blockZ + 0.5
					caster.motionZ = 0.0
					caster.motionY = caster.motionZ
					caster.motionX = caster.motionY
					if (caster is EntityPlayerMP)
						caster.playerNetServerHandler.setPlayerLocation(mop.blockX + 0.5, (mop.blockY + caster.yOffset).D, mop.blockZ + 0.5, caster.rotationYaw, caster.rotationPitch)
					else
						caster.setPosition(mop.blockX + 0.5, (mop.blockY + caster.yOffset).D, mop.blockZ + 0.5)
					return result
				} else if (caster.worldObj.isAirBlock(mop.blockX, mop.blockY - 1, mop.blockZ)) {
					result = checkCast(caster)
					if (result != SpellCastResult.OK) return result
					
					caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5f, 1f)
					caster.posX = mop.blockX + 0.5
					caster.posY = mop.blockY + caster.yOffset - 1.0
					caster.posZ = mop.blockZ + 0.5
					caster.motionZ = 0.0
					caster.motionY = caster.motionZ
					caster.motionX = caster.motionY
					if (caster is EntityPlayerMP)
						caster.playerNetServerHandler.setPlayerLocation(mop.blockX + 0.5, (mop.blockY + caster.yOffset - 1).D, mop.blockZ + 0.5, caster.rotationYaw, caster.rotationPitch)
					else
						caster.setPosition(mop.blockX + 0.5, (mop.blockY + caster.yOffset - 1).D, mop.blockZ + 0.5)
					return result
				}
			}
			
			if (caster.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) === Blocks.bedrock) {
				return SpellCastResult.OBSTRUCT
			}
			mop.blockX += px
			mop.blockY += py
			mop.blockZ += pz
		}
		
		return SpellCastResult.OBSTRUCT
	}
}