package alfheim.common.spell.darkness

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellVisualizations
import alfheim.common.core.registry.AlfheimRegistry
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect

class SpellSacrifice: SpellBase("sacrifice", EnumRace.IMP, 256000, 75000, 100, true) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			val pe = PotionEffect(AlfheimRegistry.sacrifice.id, 32, 0, false)
			pe.curativeItems.clear()
			caster.addPotionEffect(pe)
			// AlfheimCore.network.sendToAll(MessageEffect(caster.entityId, AlfheimRegistry.sacrifice.id, 32, 0))
		}
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		SpellVisualizations.negateSphere(1.0)
	}
}