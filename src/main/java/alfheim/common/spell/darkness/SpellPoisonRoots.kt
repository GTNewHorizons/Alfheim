package alfheim.common.spell.darkness

import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.util.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*
import java.util.*

object SpellPoisonRoots: SpellBase("poisonroots", EnumRace.IMP, 60000, 6000, 30) {
	
	override var duration = 300
	override var efficiency = 4.0
	
	override val usableParams
		get() = arrayOf(duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster)) ?: return SpellCastResult.NOTARGET
		var flagBadEffs = false
		var member: EntityLivingBase?
		
		scanpt@ for (i in 0 until pt.count) {
			member = pt[i]
			if (member == null || Vector3.entityDistance(caster, member) > 32) continue
			for (o in member.activePotionEffects) {
				if (Potion.potionTypes[(o as PotionEffect).getPotionID()].isBadEffect) {
					flagBadEffs = true
					break@scanpt
				}
			}
		}
		
		if (!flagBadEffs) return SpellCastResult.WRONGTGT
		
		val l = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(radius)) as List<EntityLivingBase>
		val flagNotParty = l.any { pt.isMember(it) }
		
		if (!flagNotParty) return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		val remove = ArrayList<PotionEffect>()
		var mobs = l.iterator()
		var target = mobs.next()
		var pe: PotionEffect
		
		for (i in 0 until pt.count) {
			member = pt[i] ?: continue
			for (o in member.activePotionEffects) {
				pe = o as PotionEffect
				
				while (pt.isMember(target) && mobs.hasNext()) target = mobs.next()
				if (pt.isMember(target)) return SpellCastResult.NOTARGET            // Some desync, sorry for your mana :(
				
				if (Potion.potionTypes[pe.getPotionID()].isBadEffect) {
					target.addPotionEffect(PotionEffect(pe.potionID, pe.duration, pe.amplifier, pe.isAmbient))
					remove.add(pe)
					if (mobs.hasNext())
						target = mobs.next()
					else
						mobs = l.iterator()
				}
			}
			
			for (r in remove) member.removePotionEffect(r.potionID)
			remove.clear()
		}
		
		for (e in l) if (!pt.isMember(e)) e.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, duration, efficiency.I, true))
		
		return result
	}
}