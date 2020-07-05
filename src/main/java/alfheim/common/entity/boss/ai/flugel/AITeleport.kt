package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

class AITeleport(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	override fun startExecuting() {
		flugel.aiTaskTimer = flugel.worldObj.rand.nextInt(100) + 100
	}
	
	override fun continueExecuting(): Boolean {
		if (flugel.aiTaskTimer % (if (flugel.isHardMode) if (flugel.isDying) 60 else 100 else if (flugel.isDying) 80 else 120) == 0)
			tryToTP(flugel)

		return canContinue()
	}

	companion object {
		
		fun tryToTP(flugel: EntityFlugel) {
			var tries = 0
			while (!flugel.teleportRandomly() && tries < 50) tries++
			if (tries >= 50) {
				val src = flugel.source
				flugel.teleportTo(src.posX + 0.5, src.posY + 1.6, src.posZ + 0.5)
			}
		}
	}
}