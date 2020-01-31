package alfheim.common.spell.darkness

import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellVisualizations
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect

object SpellSacrifice: SpellBase("sacrifice", EnumRace.IMP, 256000, 75000, 100, true) {
	
	override var damage = Float.MAX_VALUE
	override var radius = 32.0
	
	override val usableParams: Array<Any>
		get() = arrayOf(damage, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			val pe = PotionEffect(AlfheimConfigHandler.potionIDSacrifice, 32, 0, false)
			pe.curativeItems.clear()
			caster.addPotionEffect(pe)
			// AlfheimCore.network.sendToAll(MessageEffect(caster.entityId, AlfheimRegistry.sacrifice.id, 32, 0))
		}
		
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		SpellVisualizations.negateSphere(radius / 32)
	}
}