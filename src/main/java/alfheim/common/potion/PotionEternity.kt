package alfheim.common.potion

import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.getActivePotionEffect
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class PotionEternity: PotionAlfheim(AlfheimConfigHandler.potionIDEternity, "eternity", false, 0xDAA520) {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun onUpdate(event: LivingUpdateEvent) {
		val target = event.entityLiving
		val eff = target.activePotionsMap[AlfheimConfigHandler.potionIDEternity] as? PotionEffect ?: return
		
		if (eff.amplifier == 0) {
			if (target.isSneaking) target.removePotionEffect(id)
			
			if (eff.duration >= 115) return
		}
		
		target.motionX = 0.0
		target.motionY = 0.0
		target.motionZ = 0.0
	}
	
	@SubscribeEvent
	fun onDamageTaken(event: LivingHurtEvent) {
		val player = event.entityLiving as? EntityPlayer ?: return
		val eff = player.getActivePotionEffect(this.id) ?: return
		if (eff.amplifier == 0) event.ammount = 0f
	}
}
