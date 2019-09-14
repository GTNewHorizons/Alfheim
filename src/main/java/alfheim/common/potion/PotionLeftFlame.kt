package alfheim.common.potion

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.common.eventhandler.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.BaseAttributeMap
import net.minecraft.entity.player.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.living.LivingHealEvent
import net.minecraftforge.event.entity.player.PlayerEvent.*
import net.minecraftforge.event.world.BlockEvent.*

class PotionLeftFlame: PotionAlfheim(AlfheimConfigHandler.potionIDLeftFlame, "leftFlame", false, 0x0) {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase?, attributes: BaseAttributeMap, ampl: Int) {
		super.applyAttributesModifiersToEntity(target, attributes, ampl)
		if (AlfheimCore.enableMMO && target is EntityPlayer) {
			val player = target as EntityPlayer?
			player!!.capabilities.allowEdit = false
			player.capabilities.allowFlying = true
			player.capabilities.disableDamage = true
			player.capabilities.isFlying = true
			player.sendPlayerAbilities()
			if (player is EntityPlayerMP) player.theItemInWorldManager.blockReachDistance = 0.1
			if (!ASJUtilities.isServer) VisualEffectHandlerClient.onDeath(target)
		}
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase?, attributes: BaseAttributeMap, ampl: Int) {
		super.removeAttributesModifiersFromEntity(target, attributes, ampl)
		if (AlfheimCore.enableMMO && target is EntityPlayer) {
			val player = target as EntityPlayer?
			player!!.capabilities.allowEdit = true
			player.capabilities.allowFlying = false
			player.capabilities.disableDamage = false
			player.capabilities.isFlying = false
			player.sendPlayerAbilities()
			if (player is EntityPlayerMP) player.theItemInWorldManager.blockReachDistance = 5.0
			player.dataWatcher.updateObject(6, ampl.toFloat())
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onBreakSpeed(e: BreakSpeed) {
		if (check(e.entityLiving)) e.newSpeed = 0f
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onHarvestCheck(e: HarvestCheck) {
		if (check(e.entityLiving)) e.success = false
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onBlockBreak(e: BreakEvent) {
		if (check(e.player)) e.isCanceled = true
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onBlockPlace(e: PlaceEvent) {
		if (check(e.player)) e.isCanceled = true
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onBlockMultiPlace(e: MultiPlaceEvent) {
		if (check(e.player)) e.isCanceled = true
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onPlayerSaid(e: ServerChatEvent) {
		if (check(e.player)) e.isCanceled = true
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onPlayerDrop(e: ItemTossEvent) {
		if (check(e.player)) {
			e.isCanceled = true
			e.player.inventory.addItemStackToInventory(e.entityItem.entityItem.copy())
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onHeal(e: LivingHealEvent) {
		if (check(e.entityLiving)) e.isCanceled = true
	}
	
	fun check(e: EntityLivingBase?) = AlfheimCore.enableMMO && e?.isPotionActive(this) == true
}
