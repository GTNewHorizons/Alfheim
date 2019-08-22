package alfheim.common.spell.darkness

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect

class SpellDecay: SpellBase("decay", EnumRace.IMP, 12000, 2400, 25) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		if (caster !is EntityPlayer) return SpellBase.SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		if (tg == null || tg.target == null)
			return SpellBase.SpellCastResult.NOTARGET
		
		if (tg.target === caster || tg.isParty)
			return SpellBase.SpellCastResult.WRONGTGT
		
		if (ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellBase.SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellBase.SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(AlfheimRegistry.decay.id, 600, 0, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, AlfheimRegistry.decay.id, 600, 0))
			SpellEffectHandler.sendPacket(Spells.DISPEL, tg.target)
		}
		
		return result
	}
}