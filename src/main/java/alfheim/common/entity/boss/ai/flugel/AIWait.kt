package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

class AIWait(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	override fun startExecuting() {
		flugel.aiTaskTimer = Integer.MAX_VALUE
	}
	
	override fun continueExecuting(): Boolean {
		return true
	}
}