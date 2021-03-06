package alfheim.common.spell.water

import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.network.MessageVisualEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.potion.PotionEffect

object SpellIceLens: SpellBase("icelens", EnumRace.UNDINE, 6000, 1200, 30) {
	
	override var duration = 200
	
	override val usableParams: Array<Any>
		get() = arrayOf(duration)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			caster.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDIceLens, duration, 0, true))
			
			if (caster is EntityPlayerMP) AlfheimCore.network.sendTo(MessageVisualEffect(VisualEffects.ICELENS.ordinal), caster)
		}
		return result
	}
}