package alfheim.common.spell.water

import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.core.handler.SpellEffectHandler
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer

class SpellHealing: SpellBase("healing", EnumRace.UNDINE, 2000, 200, 10) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster))
				 ?: return SpellBase.SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellBase.SpellCastResult.OK) return result
		
		for (i in 0 until pt.count) {
			val living = pt.get(i)
			if (living != null && Vector3.entityDistance(living!!, caster) < 32) {
				living!!.heal(5.0f)
				SpellEffectHandler.sendPacket(Spells.HEAL, living!!)
			}
		}
		
		return result
	}
}