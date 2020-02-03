package alfheim.common.spell.sound

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.util.*
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.*

object SpellDragonGrowl: SpellBase("dragongrowl", EnumRace.POOKA, 12000, 2400, 20) {
	
	override var duration = 100
	override var efficiency = 2.0
	override var radius = 8.0
	
	override val usableParams
		get() = arrayOf(duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(radius)) as List<EntityLivingBase>
		
		if (list.isEmpty()) return SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		for (living in list) {
			if (PartySystem.mobsSameParty(caster, living) || Vector3.entityDistance(living, caster) > radius*2) continue
			if (!WorldGuardCommons.canDoSomethingWithEntity(caster, living)) continue
			
			living.addPotionEffect(PotionEffect(Potion.blindness.id, duration, 0, true))
			AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.blindness.id, duration, 0))
			living.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, duration, (efficiency * 2.5).I, true))
			AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.moveSlowdown.id, duration, (efficiency * 2.5).I))
			living.addPotionEffect(PotionEffect(Potion.weakness.id, duration, efficiency.I, true))
			AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.moveSlowdown.id, duration, efficiency.I))
		}
		caster.worldObj.playSoundEffect(caster.posX, caster.posY, caster.posZ, "mob.enderdragon.growl", 100f, 0.8f + caster.worldObj.rand.nextFloat() * 0.2f)
		return result
	}
}