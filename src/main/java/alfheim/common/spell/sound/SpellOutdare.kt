package alfheim.common.spell.sound

import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB

object SpellOutdare: SpellBase("outdare", EnumRace.POOKA, 6000, 2400, 20) {
	
	override val usableParams
		get() = emptyArray<Any>()
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val l = caster.worldObj.getEntitiesWithinAABB(EntityLiving::class.java, AxisAlignedBB.getBoundingBox(caster.posX, caster.posY, caster.posZ, caster.posX, caster.posY, caster.posZ).expand(32.0, 32.0, 32.0)) as MutableList<EntityLiving>
		l.remove(caster)
		if (l.isEmpty()) return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (e in l)
			if (Vector3.entityDistance(caster, e) < 32) {
				if (e is EntityFlugel) {
					if (caster is EntityPlayer)
						e.playerDamage[caster.commandSenderName] = e.playerDamage.getOrDefault(caster.commandSenderName, 0f) + 400f
					else
						continue
				}
				
				e.attackTarget = caster
				e.setLastAttacker(caster)
				e.setRevengeTarget(caster)
			}
		
		return result
	}
}