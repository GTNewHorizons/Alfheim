package alfheim.common.potion

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.security.InteractionSecurity
import alfheim.common.spell.wind.SpellThrow
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource

class PotionThrow: PotionAlfheim(AlfheimConfigHandler.potionIDThrow, "throw", false, 0xAAFFFF) {
	
	override fun isReady(time: Int, mod: Int) = AlfheimConfigHandler.enableMMO
	
	override fun performEffect(target: EntityLivingBase, mod: Int) {
		if (!AlfheimConfigHandler.enableMMO) return
		
		val v = Vector3(target.lookVec).mul((mod + 1).D)
		target.motionX = v.x
		target.motionY = v.y
		target.motionZ = v.z
		
		var pt = PartySystem.getMobParty(target)
		if (pt == null) pt = Party()
		
		val l = target.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, target.boundingBox.copy().expand(SpellThrow.radius)) as MutableList<EntityLivingBase>
		l.remove(target)
		for (e in l) if (!pt.isMember(e) && InteractionSecurity.canHurtEntity(target, e)) e.attackEntityFrom(DamageSource.causeMobDamage(target), SpellThrow.damage)
	}
}
