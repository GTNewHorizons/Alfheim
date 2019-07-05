package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.common.entity.boss.EntityFlugel
import java.util.*

class AIChase(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	override fun startExecuting() {
		val s = flugel.stage
		val i = if (s == 1) 200 else if (s == 2) 100 else 50
		flugel.aiTaskTimer = flugel.worldObj.rand.nextInt(i) + i
	}
	
	override fun continueExecuting(): Boolean {
		flugel.checkCollision()
		if (flugel.aiTaskTimer % 10 == 0) {
			val name: String
			try {
				name = flugel.playersWhoAttacked.maxBy { it.value }?.key ?: "Notch"
			} catch (e: Throwable) {
				e.printStackTrace()
				return canContinue()
			}
			
			val target = flugel.worldObj.getPlayerEntityByName(name)
			
			if (target != null) {
				val mot = Vector3(target.posX - flugel.posX, target.posY - flugel.posY, target.posZ - flugel.posZ).normalize()
				flugel.motionX = mot.x
				flugel.motionY = mot.y
				flugel.motionZ = mot.z
			
			} else {
				flugel.playersWhoAttacked.remove(name)
			}
		}
		return canContinue()
	}
}