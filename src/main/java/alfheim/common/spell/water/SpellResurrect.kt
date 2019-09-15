package alfheim.common.spell.water

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*

class SpellResurrect: SpellBase("resurrect", EnumRace.UNDINE, 256000, 72000, 100, true) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		ASJUtilities.chatLog("${tg.target?.commandSenderName}", caster)
		
		if (tg.target == null) return SpellCastResult.NOTARGET
		
		ASJUtilities.chatLog("Target is${if (tg.isParty) " " else " NOT"} in party", caster)
		ASJUtilities.chatLog("Target has${if (tg.target.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) " " else " no"} left flame effect (id ${AlfheimConfigHandler.potionIDLeftFlame})", caster)
		
		if (!tg.isParty || !tg.target.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame))
			return SpellCastResult.WRONGTGT
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDLeftFlame, 0, 10, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, Potion.field_76434_w.id, 0, 10))
			VisualEffectHandler.sendPacket(VisualEffects.UPHEAL, tg.target)
			PartySystem.getMobParty(caster)?.setDead(tg.target, false)
			
			ASJUtilities.chatLog("Success!", caster)
		}
		
		return result.also { ASJUtilities.chatLog("Returning $it", caster) }
	}
}