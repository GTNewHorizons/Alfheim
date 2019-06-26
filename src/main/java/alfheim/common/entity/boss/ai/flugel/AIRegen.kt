package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

class AIRegen(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	override fun startExecuting() {
		val div = if (flugel.isHardMode) 8 else 10
		flugel.aiTaskTimer = flugel.worldObj.rand.nextInt(EntityFlugel.SPAWN_TICKS / div) + EntityFlugel.SPAWN_TICKS / div
	}
	
	override fun continueExecuting(): Boolean {
		flugel.health = flugel.health + (flugel.maxHealth - 1f) / EntityFlugel.SPAWN_TICKS
		flugel.motionZ = 0.0
		flugel.motionY = flugel.motionZ
		flugel.motionX = flugel.motionY
		return canContinue()
	}
}