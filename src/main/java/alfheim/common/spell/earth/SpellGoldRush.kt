package alfheim.common.spell.earth

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect

object SpellGoldRush: SpellBase("goldrush", EnumRace.GNOME, 7000, 3000, 30) {
	
	override var duration = 1200
	override var efficiency = 2.0
	
	override val usableParams
		get() = arrayOf(duration, efficiency)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster)) ?: return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (i in 0 until pt.count) {
			val living = pt[i] ?: continue
			if (Vector3.entityDistance(living, caster) < 32) {
				living.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDGoldRush, duration, -1, true))
				AlfheimCore.network.sendToAll(MessageEffect(living.entityId, AlfheimConfigHandler.potionIDGoldRush, duration, -1))
				VisualEffectHandler.sendPacket(VisualEffects.UPHEAL, living)
			}
		}
		
		return result
	}
}