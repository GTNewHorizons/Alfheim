package alfheim.common.spell.water

import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.VisualEffectHandler
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer

object SpellHealing: SpellBase("healing", EnumRace.UNDINE, 2000, 200, 10) {
	
	override var damage = 5f
	
	override val usableParams: Array<Any>
		get() = arrayOf(damage)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster)) ?: return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (i in 0 until pt.count) {
			val living = pt[i]
			if (living != null && Vector3.entityDistance(living, caster) < 32) {
				living.heal(damage)
				if (living is EntityPlayer) living.foodStats.addStats(2, 1f)
				VisualEffectHandler.sendPacket(VisualEffects.HEAL, living)
			}
		}
		
		return result
	}
}