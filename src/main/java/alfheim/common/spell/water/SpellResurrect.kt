package alfheim.common.spell.water

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.util.getActivePotionEffect
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer

object SpellResurrect: SpellBase("resurrect", EnumRace.UNDINE, 256000, 72000, 100, true) {
	
	override val usableParams
		get() = emptyArray<Any>()
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		
		if (tg.target == null) return SpellCastResult.NOTARGET
		
		if (!tg.isParty || !tg.target.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame))
			return SpellCastResult.WRONGTGT
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			val pe = tg.target.getActivePotionEffect(AlfheimConfigHandler.potionIDLeftFlame)!!
			pe.duration = 1
			pe.amplifier = 1
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, AlfheimConfigHandler.potionIDLeftFlame, 1, 1))
			VisualEffectHandler.sendPacket(VisualEffects.UPHEAL, tg.target)
			PartySystem.getMobParty(caster)?.setDead(tg.target, false)
			tg.target.dataWatcher.updateObject(6, 10f)
		}
		
		return result
	}
}