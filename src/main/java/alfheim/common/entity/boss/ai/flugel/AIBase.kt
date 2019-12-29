package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

abstract class AIBase(val flugel: EntityFlugel, val task: AITask) {
	
	open fun shouldExecute(): Boolean {
		return flugel.health > 0
	}
	
	abstract fun startExecuting()
	
	open fun shouldContinue(): Boolean {
		if (flugel.health <= 0 || flugel.aiTask != task) return false
		return --flugel.aiTaskTimer > 0
	}
	
	abstract fun continueExecuting(): Boolean
	
	open fun isInterruptible() = true
	
	open fun endTask() {
		flugel.aiTaskTimer = 0
	}
}

abstract class AIConstantExecutable(val flugel: EntityFlugel) {
	abstract fun execute()
}