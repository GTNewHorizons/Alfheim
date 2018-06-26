package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel

class AIInvul(flugel: EntityFlugel, task: AITask) : AIBase(flugel, task) {

	override fun isInterruptible(): Boolean = false

	override fun startExecuting() = flugel.setAITaskTimer(EntityFlugel.SPAWN_TICKS)

	override fun continueExecuting(): Boolean {
		flugel.setHealth(flugel.getHealth() + (flugel.getMaxHealth() - 1f) / EntityFlugel.SPAWN_TICKS)
		flugel.motionX = 0.0
		flugel.motionY = 0.0
		flugel.motionZ = 0.0
		return canContinue()
	}

	override fun resetTask() = flugel.dropState()
}