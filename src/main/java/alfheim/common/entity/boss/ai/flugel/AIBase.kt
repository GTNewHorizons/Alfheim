package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.entity.ai.EntityAIBase

abstract class AIBase(internal val flugel: EntityFlugel, internal val task: AITask): EntityAIBase() {
	
	init {
		mutexBits = 5
	}
	
	override fun shouldExecute(): Boolean {
		return flugel.health > 0 && flugel.aiTask == task && flugel.aiTaskTimer == 0
	}
	
	abstract override fun startExecuting()
	
	fun canContinue(): Boolean {
		if (flugel.health <= 0 || flugel.aiTask != task) return false
		flugel.aiTaskTimer = flugel.aiTaskTimer - 1
		return flugel.aiTaskTimer > 0
	}
	
	abstract override fun continueExecuting(): Boolean
	
	override fun resetTask() {
		flugel.aiTaskTimer = 0
		flugel.aiTask = flugel.nextTask()
	}
}