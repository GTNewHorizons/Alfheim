package alfheim.common.spell.earth

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.handler.CardinalSystem.TargetingSystem.Target
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect

class SpellNoclip: SpellBase("noclip", EnumRace.GNOME, 24000, 2400, 20) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		if (caster !is EntityPlayer) return SpellBase.SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = TargetingSystem.getTarget(caster)
		if (tg == null || tg.target == null) return SpellBase.SpellCastResult.NOTARGET
		if (!tg.isParty || tg.target !is EntityPlayer || !tg.target.capabilities.allowFlying) return SpellBase.SpellCastResult.WRONGTGT
		if (tg.target !== caster && ASJUtilities.isNotInFieldOfVision(tg.target, caster)) return SpellBase.SpellCastResult.NOTSEEING
		
		val result = checkCast(caster)
		if (result == SpellBase.SpellCastResult.OK) {
			tg.target.addPotionEffect(PotionEffect(AlfheimRegistry.noclip.id, 200, 0, true))
			AlfheimCore.network.sendToAll(MessageEffect(tg.target.entityId, AlfheimRegistry.noclip.id, 200, 0))
			SpellEffectHandler.sendPacket(Spells.UPHEAL, tg.target)
		}
		
		return result
	}
}