package alfheim.common.potion

import alexsocol.asjlib.getActivePotionEffect
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import java.util.*

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class PotionEternity: PotionAlfheim(AlfheimConfigHandler.potionIDEternity, "eternity", false, 0xDAA520) {
	
	val uuid = UUID.fromString("0B02BC22-17AE-484C-8FD8-BA9BF3472D5C")!!
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase, map: BaseAttributeMap, mod: Int) {
		super.applyAttributesModifiersToEntity(target, map, mod)
		if (mod == 0) return
		
		val m = AttributeModifier(uuid, name, -1.0, 2)
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m)
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(m)
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase, map: BaseAttributeMap, mod: Int) {
		super.removeAttributesModifiersFromEntity(target, map, mod)
		if (mod == 0) return
		
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid) ?: return)
	}
	
	@SubscribeEvent
	fun onUpdate(event: LivingUpdateEvent) {
		val target = event.entityLiving
		val eff = target.activePotionsMap[AlfheimConfigHandler.potionIDEternity] as? PotionEffect ?: return
		
		if (eff.amplifier == 0) {
			if (target.isSneaking) target.removePotionEffect(id)
			
			if (eff.duration >= 115) return
		}
		
		if (target is EntityPlayer && target.capabilities.isFlying)
			target.capabilities.isFlying = false
		
		if (!target.onGround) {
			target.motionX = 0.0
			target.motionY = 0.0
			target.motionZ = 0.0
			target.fallDistance = 0f
		}
	}
	
	@SubscribeEvent
	fun onDamageTaken(event: LivingHurtEvent) {
		val player = event.entityLiving as? EntityPlayer ?: return
		val eff = player.getActivePotionEffect(this.id) ?: return
		if (eff.amplifier == 0) event.ammount = 0f
	}
}
