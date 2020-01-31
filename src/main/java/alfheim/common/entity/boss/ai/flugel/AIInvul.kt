package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

class AIInvul(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	override fun isInterruptible(): Boolean {
		return false
	}
	
	override fun startExecuting() {
		flugel.aiTaskTimer = EntityFlugel.SPAWN_TICKS
	}
	
	override fun continueExecuting(): Boolean {
		flugel.health = flugel.health + (flugel.maxHealth - 1f) / EntityFlugel.SPAWN_TICKS
		flugel.motionZ = 0.0
		flugel.motionY = flugel.motionZ
		flugel.motionX = flugel.motionY
		return canContinue()
	}
	
	override fun resetTask() {
		flugel.dropState()
	}
}