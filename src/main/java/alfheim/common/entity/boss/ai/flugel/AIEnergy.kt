package alfheim.common.entity.boss.ai.flugel

import alfheim.common.core.util.D
import alfheim.common.entity.EntityCharge
import alfheim.common.entity.boss.EntityFlugel
import vazkii.botania.common.core.helper.Vector3

class AIEnergy(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	internal var left = 0
	internal var max = 0
	internal val oY = Vector3(0.0, 1.0, 0.0)
	
	override fun startExecuting() {
		max = if (flugel.isHardMode) 10 else 5
		left = max
		flugel.aiTaskTimer = max * 20
	}
	
	override fun continueExecuting(): Boolean {
		if (flugel.aiTaskTimer % 20 == 0) {
			--left
			val look = Vector3(flugel.lookVec).multiply(1.5).rotate(Math.toRadians((-45f + left * (90f / max)).D), oY)
			val list = flugel.playersAround
			if (list.isEmpty()) return false
			val target = list[flugel.worldObj.rand.nextInt(list.size)]
			
			val pos = Vector3.fromEntityCenter(flugel).add(look)
			val motion = Vector3.fromEntityCenter(target).sub(pos).normalize()
			
			val charge = EntityCharge(flugel, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z)
			flugel.worldObj.spawnEntityInWorld(charge)
		}
		return canContinue()
	}
}