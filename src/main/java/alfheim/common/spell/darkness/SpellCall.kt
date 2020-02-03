package alfheim.common.spell.darkness

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.core.util.WorldGuardCommons
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer

object SpellCall: SpellBase("call", EnumRace.IMP, 10000, 1800, 30) {
	
	override val usableParams
		get() = emptyArray<Any>()
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val tg = CardinalSystem.TargetingSystem.getTarget(caster)
		val pt = CardinalSystem.PartySystem.getMobParty(caster) ?: return SpellCastResult.NOTARGET
		
		if (!tg.isParty) return SpellCastResult.WRONGTGT
		
		val tgt = pt[tg.partyIndex] ?: return SpellCastResult.WRONGTGT
		
		if (tgt === caster) return SpellCastResult.WRONGTGT
		
		if (tgt !is EntityPlayer && tgt.dimension != caster.dimension) return SpellCastResult.WRONGTGT
		
		val (cx, cy, cz) = Vector3.fromEntity(caster)
		if (!WorldGuardCommons.canDoSomethingHere(caster)) return SpellCastResult.NOTALLOW
		if (!WorldGuardCommons.canDoSomethingHere(tgt, cx, cy, cz, caster.worldObj)) return SpellCastResult.NOTALLOW
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK) {
			if (tgt is EntityPlayer) {
				ASJUtilities.sendToDimensionWithoutPortal(tgt, caster.dimension, cx, cy, cz)
			} else {
				val cry = caster.rotationYaw
				val crp = caster.rotationPitch
				
				tgt.setLocationAndAngles(cx, cy, cz, cry, crp)
				tgt.setPositionAndRotation(cx, cy, cz, cry, crp)
				tgt.setPositionAndUpdate(cx, cy, cz)
			}
		}
		
		return result
	}
}