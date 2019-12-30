package alfheim.common.entity.boss.ai.flugel.constant

import alexsocol.asjlib.ASJUtilities
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.entity.boss.ai.flugel.AIConstantExecutable

class AIWatchAgroHolder(flugel: EntityFlugel): AIConstantExecutable(flugel) {
	
	override fun execute() {
		val target = flugel.worldObj.getPlayerEntityByName(flugel.playerDamage.maxBy { it.value }?.key ?: "Notch") ?: return
		ASJUtilities.faceEntity(flugel, target, 360f, 360f)
	}
}