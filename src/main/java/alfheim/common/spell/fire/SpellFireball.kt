package alfheim.common.spell.fire

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.entity.spell.EntitySpellFireball
import alfheim.common.network.Message2d
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer

object SpellFireball: SpellBase("fireball", EnumRace.SALAMANDER, 1000, 50, 5) {
	
	override var damage = 6f
	override var duration = 100 // lifetime
	override var efficiency = 0.1 // speed
	override var radius = 2.0 // AoE
	
	override val usableParams
		get() = arrayOf(damage, duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val target = (caster as? EntityPlayer)?.let { CardinalSystem.TargetingSystem.getTarget(it) }?.let { if (it.isParty) null else it.target }
		
		if (target !== caster && target != null && ASJUtilities.isNotInFieldOfVision(target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCastOver(caster)
		if (result == SpellCastResult.OK) {
			val fireball = EntitySpellFireball(caster.worldObj, caster)
			fireball.target = target
			caster.worldObj.spawnEntityInWorld(fireball)
			
			if (fireball.target != null)
				AlfheimCore.network.sendToDimension(Message2d(Message2d.m2d.FIREBALLSYNC, fireball.entityId.D, fireball.target!!.entityId.D), caster.dimension)
		}
		
		return result
	}
}