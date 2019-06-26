package alfheim.common.spell.sound

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

class SpellDragonGrowl: SpellBase("dragongrowl", EnumRace.POOKA, 12000, 2400, 20) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellBase.SpellCastResult.OK) return result
		
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(8.0, 8.0, 8.0))
		for (living in list) {
			if (PartySystem.mobsSameParty(caster, living) || Vector3.entityDistance(living, caster) > 16) continue
			living.addPotionEffect(PotionEffect(Potion.blindness.id, 100, 0, true))
			AlfheimCore.network.sendToAll(MessageEffect(living.getEntityId(), Potion.blindness.id, 100, 0))
			living.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 100, 5, true))
			AlfheimCore.network.sendToAll(MessageEffect(living.getEntityId(), Potion.moveSlowdown.id, 100, 5))
			living.addPotionEffect(PotionEffect(Potion.weakness.id, 100, 2, true))
			AlfheimCore.network.sendToAll(MessageEffect(living.getEntityId(), Potion.moveSlowdown.id, 100, 2))
		}
		caster.worldObj.playSoundEffect(caster.posX, caster.posY, caster.posZ, "mob.enderdragon.growl", 100.0f, 0.8f + caster.worldObj.rand.nextFloat() * 0.2f)
		return result
	}
}