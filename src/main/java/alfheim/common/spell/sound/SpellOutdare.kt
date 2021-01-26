package alfheim.common.spell.sound

import alexsocol.asjlib.boundingBox
import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.boss.EntityFlugel
import alexsocol.asjlib.security.InteractionSecurity
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import kotlin.collections.set

object SpellOutdare: SpellBase("outdare", EnumRace.POOKA, 6000, 2400, 20) {
	
	override val usableParams
		get() = arrayOf<Any>(radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val l = caster.worldObj.getEntitiesWithinAABB(EntityLiving::class.java, caster.boundingBox(radius)) as MutableList<EntityLiving>
		l.remove(caster)
		if (l.isEmpty()) return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (e in l)
			if (Vector3.entityDistance(caster, e) < radius) {
				if (!InteractionSecurity.canDoSomethingWithEntity(caster, e))
					continue
				
				if (e is EntityFlugel) {
					if (caster is EntityPlayer)
						e.playersDamage[caster.commandSenderName] = e.playersDamage.getOrDefault(caster.commandSenderName, 0f) + 400f
					continue
				}
				
				e.attackTarget = caster
				e.setLastAttacker(caster)
				e.setRevengeTarget(caster)
			}
		
		return result
	}
}