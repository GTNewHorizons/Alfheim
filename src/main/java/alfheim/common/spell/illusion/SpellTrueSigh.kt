package alfheim.common.spell.illusion

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.network.MessageParticles
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.*

class SpellTrueSigh: SpellBase("truesigh", EnumRace.SPRIGGAN, 2000, 2500, 40) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayerMP) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster as EntityPlayer)
		if (tg.target == null) return SpellCastResult.NOTARGET
		
		if (tg.isParty || tg.target !is EntityPlayer) return SpellCastResult.WRONGTGT
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			val mana = CardinalSystem.ManaSystem.getMana(tg.target)
			AlfheimCore.network.sendTo(MessageParticles(Spells.MANA.ordinal, tg.target.entityId.toDouble(), mana.toDouble(), 0.0), caster)
		}
		
		return result
	}
}