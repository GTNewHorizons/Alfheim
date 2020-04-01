package alfheim.common.spell.illusion

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.network.MessageVisualEffect
import alfheim.common.security.InteractionSecurity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.*

object SpellTrueSight: SpellBase("truesight", EnumRace.SPRIGGAN, 2000, 2500, 40) {
	
	override val usableParams
		get() = emptyArray<Any>()
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayerMP) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster as EntityPlayer)
		val tgt = tg.target ?: return SpellCastResult.NOTARGET
		
		if (tgt !is EntityPlayer) return SpellCastResult.WRONGTGT
		
		if (tgt !== caster && ASJUtilities.isNotInFieldOfVision(tgt, caster)) return SpellCastResult.NOTSEEING
		
		if (!tg.isParty && !InteractionSecurity.canDoSomethingWithEntity(caster, tgt)) return SpellCastResult.NOTALLOW
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			val mana = CardinalSystem.ManaSystem.getMana(tgt)
			AlfheimCore.network.sendTo(MessageVisualEffect(VisualEffects.MANA.ordinal, tgt.entityId.D, mana.D, 0.0), caster)
		}
		
		return result
	}
}