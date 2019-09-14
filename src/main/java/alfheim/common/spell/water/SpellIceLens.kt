package alfheim.common.spell.water

import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.network.MessageVisualEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.potion.PotionEffect

class SpellIceLens: SpellBase("icelens", EnumRace.UNDINE, 6000, 1200, 30) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			caster.addPotionEffect(PotionEffect(AlfheimRegistry.icelens.id, 200, 0, true))
			
			if (caster is EntityPlayerMP) AlfheimCore.network.sendTo(MessageVisualEffect(VisualEffects.ICELENS.ordinal, 0.0, 0.0, 0.0), caster)
		}
		return result
	}
}