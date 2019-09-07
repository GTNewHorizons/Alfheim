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

class SpellDeathMark: SpellBase("deathmark", EnumRace.IMP, 24000, 3000, 10) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		if (tg.target == null)
			return SpellCastResult.NOTARGET
		
		if (tg.target === caster || tg.isParty)
			return SpellCastResult.WRONGTGT
		
		if (ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(AlfheimRegistry.deathMark.id, 600, 0, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, AlfheimRegistry.deathMark.id, 600, 0))
			SpellEffectHandler.sendPacket(Spells.DISPEL, tg.target)
		}
		
		return result
	}
}