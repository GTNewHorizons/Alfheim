package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

abstract class AIBase {
	
	protected fun bits(flugel: EntityFlugel) = flugel.uniqueID.leastSignificantBits to flugel.uniqueID.mostSignificantBits
	
	abstract fun shouldStart(flugel: EntityFlugel): Boolean
	
	abstract fun startExecuting(flugel: EntityFlugel)
	
	abstract fun shouldContinue(flugel: EntityFlugel): Boolean
	
	abstract fun continueExecuting(flugel: EntityFlugel)
	
	open fun isInterruptible(flugel: EntityFlugel) = true
	
	open fun endTask(flugel: EntityFlugel) = Unit
}

abstract class AIConstantExecutable(val flugel: EntityFlugel) {
	abstract fun execute()
}