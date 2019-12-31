package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.math.Vector3
import alfheim.common.entity.EntityCharge
import alfheim.common.entity.boss.EntityFlugel

object AIEnergy: AIBase() {
	
	const val MAX_CHARGES = "AIEnergy_max"
	const val LEFT_CHARGES = "AIEnergy_left"
	
	override fun shouldStart(flugel: EntityFlugel) = true
	
	override fun startExecuting(flugel: EntityFlugel) {
		val count = if (flugel.isHardMode) 10 else 5
		flugel.AI.extraData[MAX_CHARGES] = count
		flugel.AI.extraData[LEFT_CHARGES] = count
		flugel.AI.timer = count * 10
	}
	
	override fun shouldContinue(flugel: EntityFlugel) = --flugel.AI.timer > 0
	
	override fun continueExecuting(flugel: EntityFlugel) {
		if (flugel.AI.timer % 10 == 0) {
			var left = flugel.AI.extraData[LEFT_CHARGES] as? Int ?: return
			flugel.AI.extraData[LEFT_CHARGES] = --left
			val max = flugel.AI.extraData[MAX_CHARGES] as? Int ?: return
			
			val look = Vector3(flugel.lookVec).mul(1.5).rotate(Math.toRadians((-45f + left * (90f / max)).toDouble()), Vector3.oY)
			val list = flugel.playersAround
			if (list.isEmpty()) return
			
			val target = list[flugel.worldObj.rand.nextInt(list.size)]
			
			val pos = Vector3.fromEntityCenter(flugel).add(look)
			val motion = Vector3.fromEntityCenter(target).sub(pos).normalize()
			
			val charge = EntityCharge(flugel, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z)
			flugel.worldObj.spawnEntityInWorld(charge)
		}
	}
}