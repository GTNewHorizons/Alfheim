package alfheim.common.spell.illusion

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.util.D
import alfheim.common.network.MessageVisualEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.*

object SpellTrueSigh: SpellBase("truesigh", EnumRace.SPRIGGAN, 2000, 2500, 40) {
	
	override val usableParams
		get() = emptyArray<Any>()
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayerMP) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster as EntityPlayer)
		if (tg.target == null) return SpellCastResult.NOTARGET
		
		if (tg.isParty || tg.target !is EntityPlayer) return SpellCastResult.WRONGTGT
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			val mana = CardinalSystem.ManaSystem.getMana(tg.target)
			AlfheimCore.network.sendTo(MessageVisualEffect(VisualEffects.MANA.ordinal, tg.target.entityId.D, mana.D, 0.0), caster)
		}
		
		return result
	}
}