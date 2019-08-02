package alfheim.common.potion

import alfheim.common.core.util.AlfheimConfig
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class PotionEternity: PotionAlfheim(AlfheimConfig.potionIDEternity, "eternity", false, 0xDAA520) {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun onUpdate(event: LivingUpdateEvent) {
		val target = event.entityLiving
		if (target.isPotionActive(this) && target.getActivePotionEffect(this).getDuration() < 115) {
			if (target.isSneaking) target.removePotionEffect(id)
			else {
				target.motionX = 0.0
				target.motionY = 0.0
				target.motionZ = 0.0
			}
		}
	}
	
	@SubscribeEvent
	fun onDamageTaken(event: LivingHurtEvent) {
		val player = event.entityLiving as? EntityPlayer ?: return
		if (player.isPotionActive(this)) event.ammount = 0f
	}
}
