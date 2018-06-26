package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel

class AITeleport(flugel: EntityFlugel, task: AITask) : AIBase(flugel, task) {
	
	override fun startExecuting() = flugel.setAITaskTimer(flugel.worldObj.rand.nextInt(100) + 100)

	override fun continueExecuting(): Boolean {
		if (flugel.getAITaskTimer() % (if (flugel.isHardMode()) (if (flugel.isDying()) 60 else 100) else (if (flugel.isDying()) 80 else 120)) == 0) {
			var tries = 0
			while (!flugel.teleportRandomly() && tries < 50) tries++
			val src = flugel.getSource()
			if (tries >= 50) flugel.teleportTo(src.posX + 0.5, src.posY + 1.6, src.posZ + 0.5)
		}
		return canContinue()
	}
}