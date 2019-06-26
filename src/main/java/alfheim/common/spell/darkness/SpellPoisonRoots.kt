package alfheim.common.spell.darkness

import java.util.ArrayList

import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

class SpellPoisonRoots: SpellBase("poisonroots", EnumRace.IMP, 60000, 6000, 30) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster))
				 ?: return SpellBase.SpellCastResult.NOTARGET
		var flagBadEffs = false
		var flagNotParty = false
		var member: EntityLivingBase?
		
		scanpt@ for (i in 0 until pt.count) {
			member = pt.get(i)
			if (member == null || Vector3.entityDistance(caster, member) > 32) continue
			for (o in member.activePotionEffects) {
				if (Potion.potionTypes[(o as PotionEffect).getPotionID()].isBadEffect) {
					flagBadEffs = true
					break@scanpt
				}
			}
		}
		
		if (!flagBadEffs) return SpellBase.SpellCastResult.WRONGTGT
		
		val l = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(16.0, 16.0, 16.0))
		for (e in l) {
			if (pt.isMember(e)) {
				flagNotParty = true
				break
			}
		}
		
		if (!flagNotParty) return SpellBase.SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellBase.SpellCastResult.OK) return result
		
		val remove = ArrayList<PotionEffect>()
		var mobs = l.iterator()
		var target = mobs.next()
		var pe: PotionEffect
		
		for (i in 0 until pt.count) {
			member = pt.get(i)
			for (o in member!!.activePotionEffects) {
				pe = o as PotionEffect
				
				while (pt.isMember(target) && mobs.hasNext()) target = mobs.next()
				if (pt.isMember(target)) return SpellBase.SpellCastResult.NOTARGET            // Some desync, sorry for your mana :(
				
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
		
		for (e in l) if (!pt.isMember(e)) e.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 300, 4, true))
		
		return result
	}
}