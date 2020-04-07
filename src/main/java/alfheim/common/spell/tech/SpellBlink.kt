package alfheim.common.spell.tech

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.security.InteractionSecurity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import vazkii.botania.common.Botania

object SpellBlink: SpellBase("blink", EnumRace.LEPRECHAUN, 10000, 1200, 5) {
	
	override var radius = 8.0
	
	override val usableParams: Array<Any>
		get() = arrayOf(radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
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
		
		if (caster.worldObj.isAirBlock(x, y, z)) {
			if (caster.worldObj.isAirBlock(x, y + 1, z)) {
				val result = checkCast(caster)
				if (result != SpellCastResult.OK) return result
				
				caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5f, 1f)
				caster.posX = x + 0.5
				caster.posY = (y + caster.yOffset).D
				caster.posZ = z + 0.5
				caster.motionZ = 0.0
				caster.motionY = caster.motionZ
				caster.motionX = caster.motionY
				if (caster is EntityPlayerMP)
					caster.playerNetServerHandler.setPlayerLocation(x + 0.5, (y + caster.yOffset).D, z + 0.5, caster.rotationYaw, caster.rotationPitch)
				else
					caster.setPosition(x + 0.5, (y + caster.yOffset).D, z + 0.5)
				return result
			} else if (caster.worldObj.isAirBlock(x, y - 1, z)) {
				val result = checkCast(caster)
				if (result != SpellCastResult.OK) return result
				
				caster.worldObj.playSoundAtEntity(caster, "random.fizz", 0.5f, 1f)
				caster.posX = x + 0.5
				caster.posY = y + caster.yOffset - 1.0
				caster.posZ = z + 0.5
				caster.motionZ = 0.0
				caster.motionY = caster.motionZ
				caster.motionX = caster.motionY
				if (caster is EntityPlayerMP)
					caster.playerNetServerHandler.setPlayerLocation(x + 0.5, (y + caster.yOffset - 1).D, z + 0.5, caster.rotationYaw, caster.rotationPitch)
				else
					caster.setPosition(x + 0.5, (y + caster.yOffset - 1).D, z + 0.5)
				return result
			}
		}
		
		return SpellCastResult.OBSTRUCT
	}
	
	override fun render(caster: EntityLivingBase) {
		val mop = ASJUtilities.getSelectedBlock(caster, radius, true)
		val hit = if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1)
			Vector3(caster.lookVec).normalize().mul(radius).add(caster.posX, caster.posY, caster.posZ)
		else
			Vector3(mop.hitVec)
		
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