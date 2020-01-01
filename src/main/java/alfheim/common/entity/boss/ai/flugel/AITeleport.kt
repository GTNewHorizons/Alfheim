package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.math.Vector3
import alfheim.common.core.util.*
import alfheim.common.entity.boss.EntityFlugel

object AITeleport: AIBase() {
	
	override fun startExecuting(flugel: EntityFlugel) {
		flugel.aiTaskTimer = flugel.worldObj.rand.nextInt(100) + 100
	}
	
	override fun continueExecuting(flugel: EntityFlugel) {
		if (flugel.aiTaskTimer % (if (flugel.isHardMode) if (flugel.isDying) 60 else 100 else if (flugel.isDying) 80 else 120) == 0)
			tryToTP(flugel)

	}

	fun tryToTP(flugel: EntityFlugel) {
		var tries = 0
		while (!teleportRandomly(flugel) && tries < 50) tries++
		
		if (tries >= 50) {
			val src = flugel.source
			teleportTo(flugel, src.posX + 0.5, src.posY + 1.6, src.posZ + 0.5)
		}
	}
		
	// EntityEnderman code below ============================================================================
		
	fun teleportRandomly(flugel: EntityFlugel): Boolean {
		val x = flugel.posX + (flugel.rnd.nextDouble() - 0.5) * EntityFlugel.RANGE / 2.0
		val y = flugel.posY + (flugel.rnd.nextInt(64) - 32)
		val z = flugel.posZ + (flugel.rnd.nextDouble() - 0.5) * EntityFlugel.RANGE / 2.0
		return teleportTo(flugel, x, y, z)
	}
	
	fun teleportTo(flugel: EntityFlugel, x: Double, y: Double, z: Double): Boolean {
		val d3 = flugel.posX
		val d4 = flugel.posY
		val d5 = flugel.posZ
		flugel.posX = x
		flugel.posY = y
		flugel.posZ = z
		var flag = false
		val i = flugel.posX.mfloor()
		val j = flugel.posY.mfloor()
		val k = flugel.posZ.mfloor()
		
		if (flugel.worldObj.blockExists(i, j, k)) {
			flugel.setPosition(flugel.posX, flugel.posY, flugel.posZ)
			flugel.motionZ = 0.0
			flugel.motionY = flugel.motionZ
			flugel.motionX = flugel.motionY
			
			if (flugel.worldObj.getCollidingBoundingBoxes(flugel, flugel.boundingBox).isEmpty() && !flugel.worldObj.isAnyLiquid(flugel.boundingBox)) flag = true
			
			// Prevent out of bounds teleporting
			val source = flugel.source
			if (Vector3.pointDistanceSpace(flugel.posX, flugel.posY, flugel.posZ, source.posX, source.posY, source.posZ) > EntityFlugel.RANGE) flag = false
		}
		
		if (!flag) {
			flugel.setPosition(d3, d4, d5)
			return false
		}
		
		val short1: Short = 128
		
		for (l in 0 until short1) {
			val d6 = l / (short1 - 1.0)
			val f = (flugel.rnd.nextFloat() - 0.5f) * 0.2f
			val f1 = (flugel.rnd.nextFloat() - 0.5f) * 0.2f
			val f2 = (flugel.rnd.nextFloat() - 0.5f) * 0.2f
			val d7 = d3 + (flugel.posX - d3) * d6 + (flugel.rnd.nextDouble() - 0.5) * flugel.width.D * 2.0
			val d8 = d4 + (flugel.posY - d4) * d6 + flugel.rnd.nextDouble() * flugel.height
			val d9 = d5 + (flugel.posZ - d5) * d6 + (flugel.rnd.nextDouble() - 0.5) * flugel.width.D * 2.0
			flugel.worldObj.spawnParticle("portal", d7, d8, d9, f.D, f1.D, f2.D)
		}
		
		flugel.worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0f, 1.0f)
		flugel.playSound("mob.endermen.portal", 1.0f, 1.0f)
		return true
	}
}