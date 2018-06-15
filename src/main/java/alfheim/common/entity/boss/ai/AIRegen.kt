package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel

class AIRegen(flugel: EntityFlugel, task: AITask) : AIBase(flugel, task) {
    
    override fun startExecuting() {
        val div = if (flugel.isHardMode()) 8 else 10
        flugel.setAITaskTimer(flugel.worldObj.rand.nextInt(EntityFlugel.SPAWN_TICKS / div) + EntityFlugel.SPAWN_TICKS / div)
    }

    override fun continueExecuting(): Boolean {
        flugel.setHealth(flugel.getHealth() + (flugel.getMaxHealth() - 1f) / EntityFlugel.SPAWN_TICKS)
        flugel.motionX = 0.0
        flugel.motionY = 0.0
        flugel.motionZ = 0.0
        return canContinue()
    }
}