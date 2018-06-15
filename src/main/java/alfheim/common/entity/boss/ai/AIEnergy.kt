package alfheim.common.entity.boss.ai;

import alfheim.common.entity.EntityCharge
import alfheim.common.entity.boss.EntityFlugel
import vazkii.botania.common.core.helper.Vector3

class AIEnergy(flugel: EntityFlugel, task: AITask) : AIBase(flugel, task) {
    internal var left: Int = 0
    internal var max: Int = 0
    internal var oY = Vector3(0.0, 1.0, 0.0)
    
    override fun startExecuting() {
        max = if (flugel.isHardMode()) 10 else 5
        left = max
        flugel.setAITaskTimer(100)
    }

    override fun continueExecuting(): Boolean {
        if (flugel.getAITaskTimer() % 20 == 0) {
            --left
            val look = Vector3(flugel.getLookVec()).multiply(1.5).rotate(Math.toRadians((-45f + left * (90f / max)).toDouble()), oY)
            val list = flugel.getPlayersAround()
            if (list.isEmpty()) return false
            val target = list.get(flugel.worldObj.rand.nextInt(list.size))
            val targetVector = Vector3.fromEntityCenter(target)
            val pos = Vector3.fromEntityCenter(flugel).add(look)
            val motion = targetVector.copy().sub(pos).copy().normalize()
            val charge = EntityCharge(flugel.worldObj, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z)
            flugel.worldObj.spawnEntityInWorld(charge)
        }
        return canContinue()
    }
}