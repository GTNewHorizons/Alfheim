package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

abstract class AIBase {
	
	open fun shouldStart(flugel: EntityFlugel) = flugel.health > 0
	
	abstract fun startExecuting(flugel: EntityFlugel)
	
	open fun shouldContinue(flugel: EntityFlugel): Boolean {
		if (flugel.health <= 0 || flugel.aiTask != task) return false
		return --flugel.aiTaskTimer > 0
	}
	
	abstract fun continueExecuting(flugel: EntityFlugel)
	
	open fun isInterruptible(flugel: EntityFlugel) = true
	
	open fun endTask(flugel: EntityFlugel) {
		flugel.aiTaskTimer = 0
	}
}

abstract class AIConstantExecutable(val flugel: EntityFlugel) {
	abstract fun execute()
}