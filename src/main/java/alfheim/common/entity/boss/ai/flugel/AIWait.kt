package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.setMotion
import alfheim.common.entity.boss.EntityFlugel

class AIWait(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	override fun startExecuting() {
		flugel.aiTaskTimer = Integer.MAX_VALUE
	}
	
	override fun continueExecuting(): Boolean {
		flugel.setMotion(0.0)
		val src = flugel.source
		flugel.setPosition(src.posX + 0.5, src.posY + 1.6, src.posZ + 0.5)
		return true
	}
}