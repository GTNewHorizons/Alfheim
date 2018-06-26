package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.entity.ai.EntityAIBase

abstract class AIBase(val flugel: EntityFlugel, val task: AITask) : EntityAIBase() {
	
	init {
		setMutexBits(5)
	}

	override fun shouldExecute(): Boolean = flugel.getHealth() > 0 && flugel.getAITask().equals(task) && flugel.getAITaskTimer() == 0

	override abstract fun startExecuting()
	
	fun canContinue(): Boolean {
		if (flugel.getHealth() <= 0 || !flugel.getAITask().equals(task)) return false
		flugel.setAITaskTimer(flugel.getAITaskTimer() - 1)
		return flugel.getAITaskTimer() > 0
	}

	override abstract fun continueExecuting(): Boolean
	
	override fun resetTask() {
		flugel.setAITaskTimer(0)
		flugel.setAITask(flugel.nextTask())
	}
}