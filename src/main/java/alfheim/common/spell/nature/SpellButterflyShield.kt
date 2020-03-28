package alfheim.common.spell.nature

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect

object SpellButterflyShield: SpellBase("butterflyshield", EnumRace.CAITSITH, 8000, 12000, 30) {
	
	override var duration = 6000
	override var efficiency = 3.0
	
	override val usableParams
		get() = arrayOf(duration, efficiency)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		if (tg.target == null) return SpellCastResult.NOTARGET
		
		if (!tg.isParty) return SpellCastResult.WRONGTGT
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDButterShield, duration, efficiency.I, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, AlfheimConfigHandler.potionIDButterShield, duration, efficiency.I))
		}
		
		return result
	}
}