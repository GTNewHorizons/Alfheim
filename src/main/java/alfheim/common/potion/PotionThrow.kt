package alfheim.common.potion

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource

class PotionThrow: PotionAlfheim(AlfheimConfigHandler.potionIDThrow, "throw", false, 0xAAFFFF) {
	
	override fun isReady(time: Int, mod: Int): Boolean {
		return AlfheimCore.enableMMO
	}
	
	override fun performEffect(target: EntityLivingBase, mod: Int) {
		if (!AlfheimCore.enableMMO) return
		val v = Vector3(target.lookVec).mul((mod + 1).toDouble())
		target.motionX = v.x
		target.motionY = v.y
		target.motionZ = v.z
		
		var pt = PartySystem.getMobParty(target)
		if (pt == null) pt = Party()
		
		val l = target.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, target.boundingBox.copy().expand(1.0, 1.0, 1.0)) as MutableList<EntityLivingBase>
		l.remove(target)
		for (e in l) if (!pt.isMember(e)) e.attackEntityFrom(DamageSource.causeMobDamage(target), 5f)
	}
}
