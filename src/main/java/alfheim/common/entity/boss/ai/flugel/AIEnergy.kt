package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.EntityCharge
import alfheim.common.entity.boss.EntityFlugel
import vazkii.botania.common.core.helper.Vector3

object AIEnergy: AIBase() {
	
	// TODO replace to hashmaps
	var left = 0
	var max = 0
	val oY = Vector3(0.0, 1.0, 0.0)
	
	override fun shouldStart(flugel: EntityFlugel) = true
	
	override fun startExecuting(flugel: EntityFlugel) {
		max = if (flugel.isHardMode) 10 else 5
		left = max
		flugel.AI.timer = max * 20
	}
	
	override fun shouldContinue(flugel: EntityFlugel) = --flugel.AI.timer > 0
	
	override fun continueExecuting(flugel: EntityFlugel) {
		if (flugel.AI.timer % 20 == 0) {
			--left
			val look = Vector3(flugel.lookVec).multiply(1.5).rotate(Math.toRadians((-45f + left * (90f / max)).toDouble()), oY)
			val list = flugel.playersAround
			if (list.isEmpty()) return
			
			val target = list[flugel.worldObj.rand.nextInt(list.size)]
			
			val pos = Vector3.fromEntityCenter(flugel).add(look)
			val motion = Vector3.fromEntityCenter(target).sub(pos).normalize()
			
			val charge = EntityCharge(flugel, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z)
			flugel.worldObj.spawnEntityInWorld(charge)
		}
	}
}