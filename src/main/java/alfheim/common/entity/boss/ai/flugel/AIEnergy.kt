package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.math.Vector3
import alfheim.common.entity.EntityCharge
import alfheim.common.entity.boss.EntityFlugel

object AIEnergy: AIBase() {
	
	// TODO replace to AI.extraData
	var left = HashMap<Pair<Long, Long>, Int>()
	var max = HashMap<Pair<Long, Long>, Int>()
	
	override fun shouldStart(flugel: EntityFlugel) = true
	
	override fun startExecuting(flugel: EntityFlugel) {
		val count = if (flugel.isHardMode) 10 else 5
		max[bits(flugel)] = count
		left[bits(flugel)] = count
		flugel.AI.timer = count * 10
	}
	
	override fun shouldContinue(flugel: EntityFlugel) = --flugel.AI.timer > 0
	
	override fun continueExecuting(flugel: EntityFlugel) {
		if (flugel.AI.timer % 10 == 0) {
			var left = left[bits(flugel)] ?: return
			this.left[bits(flugel)] = --left
			val max = max[bits(flugel)] ?: return
			
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