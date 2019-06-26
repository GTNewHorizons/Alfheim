package alfheim.common.spell.sound

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

class SpellBattleHorn: SpellBase("battlehorn", EnumRace.POOKA, 5000, 600, 15) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val pt = (if (caster is EntityPlayer) PartySystem.getParty(caster) else PartySystem.getMobParty(caster))
				 ?: return SpellBase.SpellCastResult.NOTARGET
		
		val result = checkCast(caster)
		if (result != SpellBase.SpellCastResult.OK) return result
		
		for (i in 0 until pt.count) {
			val living = pt.get(i)
			if (living != null && Vector3.entityDistance(living!!, caster) < 32) {
				living!!.addPotionEffect(PotionEffect(Potion.damageBoost.id, 36000, 0, true))
				AlfheimCore.network.sendToAll(MessageEffect(living!!.getEntityId(), Potion.damageBoost.id, 36000, 0))
				living!!.addPotionEffect(PotionEffect(Potion.moveSpeed.id, 36000, 0, true))
				AlfheimCore.network.sendToAll(MessageEffect(living!!.getEntityId(), Potion.moveSpeed.id, 36000, 0))
				living!!.addPotionEffect(PotionEffect(Potion.resistance.id, 36000, 0, true))
				AlfheimCore.network.sendToAll(MessageEffect(living!!.getEntityId(), Potion.resistance.id, 36000, 0))
			}
		}
		
		caster.worldObj.playSound(caster.posX, caster.posY, caster.posZ, ModInfo.MODID + ":horn.bhorn", 100.0f, 0.8f + caster.worldObj.rand.nextFloat() * 0.2f, false)
		return result
	}
}