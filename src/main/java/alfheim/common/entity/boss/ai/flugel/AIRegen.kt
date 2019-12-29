package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

class AIRegen(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	var charge = 0
	
	override fun shouldExecute() = super.shouldExecute() && flugel.regens-- > 0
	
	override fun startExecuting() {
		charge = 20
		val div = if (flugel.isUltraMode) 6 else if (flugel.isHardMode) 8 else 10
		flugel.aiTaskTimer = (flugel.worldObj.rand.nextInt(EntityFlugel.SPAWN_TICKS / div) + EntityFlugel.SPAWN_TICKS / div) * 2
		AITeleport.tryToTP(flugel)
	}
	
	override fun continueExecuting(): Boolean {
		if (--charge > 0) return shouldContinue()
		
		if (flugel.health < flugel.maxHealth) flugel.health += 2
		flugel.motionX = 0.0
		flugel.motionY = 0.0
		flugel.motionZ = 0.0
		return shouldContinue()
	}
}