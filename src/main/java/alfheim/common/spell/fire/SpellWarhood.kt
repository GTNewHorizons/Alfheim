package alfheim.common.spell.fire

import alexsocol.asjlib.ASJUtilities
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer

object SpellWarhood: SpellBase("warhood", EnumRace.SALAMANDER, 256000, 72000, 100, true) {
	
	override val usableParams
		get() = emptyArray<Any>()
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val pt = CardinalSystem.PartySystem.getMobParty(caster) ?: return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			for (i in 0..pt.count) {
				val mr = pt[i]
				if (mr === caster || mr !is EntityPlayer) continue
				
				ASJUtilities.sendToDimensionWithoutPortal(mr, caster.dimension, caster.posX, caster.posY, caster.posZ)
			}
		}
		
		return result
	}
}