package alfheim.common.spell.illusion

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*

object SpellHollowBody: SpellBase("hollowbody", EnumRace.SPRIGGAN, 10000, 1200, 20) {
	
	override var duration = 3600
	
	override val usableParams: Array<Any>
		get() = arrayOf(duration)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		if (tg.target == null) return SpellCastResult.NOTARGET
		
		if (!tg.isParty)
			return SpellCastResult.WRONGTGT
		
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(Potion.invisibility.id, duration, -1, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, Potion.invisibility.id, duration, -1))
			VisualEffectHandler.sendPacket(VisualEffects.PURE, tg.target)
		}
		
		return result
	}
}