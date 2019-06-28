package alfheim.common.spell.water

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*

class SpellResurrect: SpellBase("resurrect", EnumRace.UNDINE, 256000, 72000, 100, true) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		if (caster !is EntityPlayer) return SpellBase.SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		if (tg == null || tg.target == null)
			return SpellBase.SpellCastResult.NOTARGET
		
		if (!tg.isParty || !tg.target.isPotionActive(AlfheimRegistry.leftFlame))
			return SpellBase.SpellCastResult.WRONGTGT
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellBase.SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellBase.SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(AlfheimRegistry.leftFlame.id, 0, 10, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, Potion.field_76434_w.id, 0, 10))
			SpellEffectHandler.sendPacket(Spells.UPHEAL, tg.target)
			val pt = PartySystem.getMobParty(caster)
			pt?.setDead(tg.target, false)
		}
		
		return result
	}
}