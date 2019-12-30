package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

object AIRegen: AIBase() {
	
	var charge = 0
	
	override fun shouldStart(flugel: EntityFlugel) = super.shouldStart(flugel) && flugel.regens-- > 0
	
	override fun startExecuting(flugel: EntityFlugel) {
		charge = 20
		val div = if (flugel.isUltraMode) 6 else if (flugel.isHardMode) 8 else 10
		flugel.aiTaskTimer = (flugel.worldObj.rand.nextInt(EntityFlugel.SPAWN_TICKS / div) + EntityFlugel.SPAWN_TICKS / div) * 2
		AITeleport.tryToTP(flugel)
	}
	
	override fun continueExecuting(flugel: EntityFlugel) {
		if (--charge > 0) return
		
		if (flugel.health < flugel.maxHealth) flugel.health += 2
		flugel.motionX = 0.0
		flugel.motionY = 0.0
		flugel.motionZ = 0.0
	}
}