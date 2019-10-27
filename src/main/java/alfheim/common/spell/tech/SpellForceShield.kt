package alfheim.common.spell.tech

import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect

class SpellForceShield: SpellBase("shield", EnumRace.LEPRECHAUN, 8000, 1200, 30) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		caster.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDShield, 36000, 0, true))
		AlfheimCore.network.sendToAll(MessageEffect(caster.entityId, AlfheimConfigHandler.potionIDShield, 36000, 0))
		
		return result
	}
}