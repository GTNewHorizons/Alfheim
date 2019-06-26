package alfheim.common.spell.tech

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import codechicken.lib.math.MathHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import vazkii.botania.common.Botania

class SpellBlink: SpellBase("blink", EnumRace.LEPRECHAUN, 10000, 1200, 5) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result: SpellBase.SpellCastResult
		val hit: Vector3
		val mop = ASJUtilities.getSelectedBlock(caster, 8.0, true)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1)
			hit = Vector3(caster.lookVec).normalize().mul(8.0).add(caster.posX, caster.posY + caster.eyeHeight, caster.posZ)
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
		
		if (caster.worldObj.isAirBlock(x, y, z)) {
			if (caster.worldObj.isAirBlock(x, y + 1, z)) {
				result = checkCast(caster)
				if (result != SpellBase.SpellCastResult.OK) return result
				
				caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5f, 1.0f)
				caster.posX = x + 0.5
				caster.posY = (y + caster.yOffset).toDouble()
				caster.posZ = z + 0.5
				caster.motionZ = 0.0
				caster.motionY = caster.motionZ
				caster.motionX = caster.motionY
				if (caster is EntityPlayerMP)
					caster.playerNetServerHandler.setPlayerLocation(x + 0.5, (y + caster.yOffset).toDouble(), z + 0.5, caster.rotationYaw, caster.rotationPitch)
				else
					caster.setPosition(x + 0.5, (y + caster.yOffset).toDouble(), z + 0.5)
				return result
			} else if (caster.worldObj.isAirBlock(x, y - 1, z)) {
				result = checkCast(caster)
				if (result != SpellBase.SpellCastResult.OK) return result
				
				caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5f, 1.0f)
				caster.posX = x + 0.5
				caster.posY = y + caster.yOffset - 1.0
				caster.posZ = z + 0.5
				caster.motionZ = 0.0
				caster.motionY = caster.motionZ
				caster.motionX = caster.motionY
				if (caster is EntityPlayerMP)
					caster.playerNetServerHandler.setPlayerLocation(x + 0.5, (y + caster.yOffset - 1).toDouble(), z + 0.5, caster.rotationYaw, caster.rotationPitch)
				else
					caster.setPosition(x + 0.5, (y + caster.yOffset - 1).toDouble(), z + 0.5)
				return result
			}
		}
		
		return SpellBase.SpellCastResult.OBSTRUCT
	}
	
	override fun render(caster: EntityLivingBase) {
		val hit: Vector3
		val mop = ASJUtilities.getSelectedBlock(caster, 8.0, true)
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1)
			hit = Vector3(caster.lookVec).normalize().mul(8.0).add(caster.posX, caster.posY, caster.posZ)
		else
			hit = Vector3(mop.hitVec)
		
		val x = hit.x
		val y = hit.y
		val z = hit.z
		
		/*if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK && mop.sideHit != -1) {
	    	switch(mop.sideHit) {
	    		case 0: --y; break;
	    		case 1: ++y; break;
	    		case 2: --z; break;
	    		case 3: ++z; break;
	    		case 4: --x; break;
	    		case 5: ++x; break;
	    	}
    	}*/
		
		Botania.proxy.wispFX(caster.worldObj, x, y, z, 0.5f, 0f, 1f, 0.15f, 0f, 0.075f)
	}
}