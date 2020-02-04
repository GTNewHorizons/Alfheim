package alfheim.common.spell.darkness

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.security.InteractionSecurity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer

object SpellJoin: SpellBase("join", EnumRace.IMP, 10000, 1800, 30) {
	
	override val usableParams
		get() = emptyArray<Any>()
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (caster !is EntityPlayer) return SpellCastResult.NOTARGET // TODO add targets for mobs
		
		val pt = CardinalSystem.PartySystem.getMobParty(caster) ?: return SpellCastResult.NOTARGET
		val tg = CardinalSystem.TargetingSystem.getTarget(caster)
		
		if (!tg.isParty) return SpellCastResult.WRONGTGT
		
		val tgt = pt[tg.partyIndex] ?: return SpellCastResult.WRONGTGT
		
		if (tgt === caster) return SpellCastResult.WRONGTGT
		
		val (tx, ty, tz) = Vector3.fromEntity(tgt)
		if (!InteractionSecurity.canDoSomethingHere(caster, tx, ty, tz, tgt.worldObj)) return SpellCastResult.NOTALLOW
		if (!InteractionSecurity.canDoSomethingHere(tgt)) return SpellCastResult.NOTALLOW
		
		val result = checkCast(caster)
		if (result == SpellCastResult.OK)
			ASJUtilities.sendToDimensionWithoutPortal(caster, tgt.dimension, tx, ty, tz)
		
		return result
	}
}