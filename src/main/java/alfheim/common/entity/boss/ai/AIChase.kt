package alfheim.common.entity.boss.ai;

import alexsocol.asjlib.ASJUtilities
import alfheim.common.entity.boss.EntityFlugel
import vazkii.botania.common.core.helper.Vector3
import java.util.Collections

class AIChase(flugel: EntityFlugel, task: AITask) : AIBase(flugel, task) {
    
    override fun startExecuting() {
        val s = flugel.getStage()
        val i = if (s == 1) 300 else if (s == 2) 150 else 50
        flugel.setAITaskTimer(flugel.worldObj.rand.nextInt(i) + i)
    }

    override fun continueExecuting(): Boolean {
        flugel.checkCollision()
        if (flugel.getAITaskTimer() % 10 == 0) {
            var name: String
            try {
                name = ASJUtilities.mapGetKeyOrDefault(flugel.playersWhoAttacked, Collections.max(flugel.playersWhoAttacked.values), "")
            } catch (e: Throwable) {
                e.printStackTrace()
                return canContinue()
            }
            val target = flugel.worldObj.getPlayerEntityByName(name)
            if (target != null) {
                val mot = Vector3(target.posX - flugel.posX, target.posY - flugel.posY, target.posZ - flugel.posZ).normalize()
                flugel.motionX = mot.x
                flugel.motionY = mot.y
                flugel.motionZ = mot.z
            } else {
                flugel.playersWhoAttacked.remove(name)
            }
        }
        return canContinue()
    }
}