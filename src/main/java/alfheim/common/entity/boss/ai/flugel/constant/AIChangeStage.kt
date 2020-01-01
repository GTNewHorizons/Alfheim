package alfheim.common.entity.boss.ai.flugel.constant

import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.entity.boss.ai.flugel.AIConstantExecutable

class AIChangeStage(flugel: EntityFlugel): AIConstantExecutable(flugel) {
	
	override fun execute() {
		if (flugel.AI.stage > EntityFlugel.Companion.STAGE.INIT) {
			if (flugel.AI.stage < EntityFlugel.Companion.STAGE.MAGIC && flugel.health <= flugel.maxHealth * 0.6) {
				flugel.AI.stage = EntityFlugel.Companion.STAGE.MAGIC
			}
		}
	}
}