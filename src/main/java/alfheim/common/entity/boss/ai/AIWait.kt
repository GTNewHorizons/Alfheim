package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel

class AIWait(flugel: EntityFlugel, task: AITask) : AIBase(flugel, task) {
	
	override fun startExecuting()  = flugel.setAITaskTimer(Integer.MAX_VALUE)
	override fun continueExecuting(): Boolean = true
}