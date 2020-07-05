package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.*
import alfheim.common.entity.boss.EntityFlugel

class AIWait(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	override fun startExecuting() {
		flugel.aiTaskTimer = Integer.MAX_VALUE
	}
	
	override fun continueExecuting(): Boolean {
		flugel.setMotion(0.0)
		flugel.setPosition(flugel.source)
		return true
	}
}