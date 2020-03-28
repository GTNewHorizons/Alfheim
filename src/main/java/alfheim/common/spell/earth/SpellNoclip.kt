package alfheim.common.spell.earth

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.network.MessageEffect
import alfheim.common.security.InteractionSecurity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect

object SpellNoclip: SpellBase("noclip", EnumRace.GNOME, 24000, 2400, 20) {
	
	override var duration = 200
	
	override val usableParams: Array<Any>
		get() = arrayOf(duration)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		val tgt = tg.target ?: return SpellCastResult.NOTARGET
		if (!tg.isParty || tgt !is EntityPlayer || !tgt.capabilities.allowFlying) return SpellCastResult.WRONGTGT
		if (tgt !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		if (!InteractionSecurity.canDoSomethingHere(tgt)) return SpellCastResult.NOTALLOW
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDNoclip, duration, -1, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, AlfheimConfigHandler.potionIDNoclip, duration, -1))
			VisualEffectHandler.sendPacket(VisualEffects.UPHEAL, tg.target)
		}
		
		return result
	}
}