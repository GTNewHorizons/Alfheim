package alfheim.common.entity.boss.ai.flugel

import alfheim.common.core.util.D
import alfheim.common.entity.boss.EntityFlugel

object AIRegen: AIBase() {
	
	private const val REGENS = "regens"
	
	override fun shouldStart(flugel: EntityFlugel): Boolean {
		initRegens(flugel)
		var regens = flugel.AI.extraData[REGENS] as? Int ?: return false
		flugel.AI.extraData[REGENS] = --regens
		return regens > 0
	}
	
	override fun startExecuting(flugel: EntityFlugel) {
		val src = flugel.source
		AITeleport.teleportTo(flugel, src.posX.D, src.posY.D, src.posZ.D)
		
		// TODO spawn regen pylons
	}
	
	override fun shouldContinue(flugel: EntityFlugel): Boolean {
		return true
		
		// TODO add check for regen pylons
	}
	
	override fun continueExecuting(flugel: EntityFlugel) {
		// TODO move regen to pylons
		if (flugel.health < flugel.maxHealth) flugel.health += 2
		
		flugel.motionX = 0.0
		flugel.motionY = 0.0
		flugel.motionZ = 0.0
	}
	
	fun initRegens(flugel: EntityFlugel) {
		if (!flugel.AI.extraData.containsKey(REGENS)) {
			flugel.AI.extraData[REGENS] = when (flugel.AI.stage) {
				EntityFlugel.Companion.STAGE.AGGRO        -> 5
				EntityFlugel.Companion.STAGE.MAGIC        -> 10
				EntityFlugel.Companion.STAGE.POSTDEATHRAY -> 8
				else -> 0
			}
		}
	}
}