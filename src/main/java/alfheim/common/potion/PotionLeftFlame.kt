package alfheim.common.potion

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.client.render.world.SpellEffectHandlerClient
import alfheim.common.core.registry.*
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.item.AlfheimItems
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.BaseAttributeMap
import net.minecraft.entity.player.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.living.LivingHealEvent
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed
import net.minecraftforge.event.world.BlockEvent

class PotionLeftFlame: PotionAlfheim(AlfheimConfig.potionIDLeftFlame, "leftFlame", false, 0x0) {
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
			if (player is EntityPlayerMP) player.theItemInWorldManager.blockReachDistance = -1.0
			if (!ASJUtilities.isServer) SpellEffectHandlerClient.onDeath(target)
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
			if (player is EntityPlayerMP) player.theItemInWorldManager.blockReachDistance = 5.0
			player.dataWatcher.updateObject(6, ampl.toFloat())
		}
	}
	
	@SubscribeEvent
	fun onBreakSpeed(e: BreakSpeed) {
		if (AlfheimCore.enableMMO && e.entityLiving.isPotionActive(AlfheimRegistry.leftFlame)) e.newSpeed = 0f
	}
	
	@SubscribeEvent
	fun onBlockBreak(e: BlockEvent.BreakEvent) {
		if (AlfheimCore.enableMMO && e.player.isPotionActive(AlfheimRegistry.leftFlame)) e.isCanceled = true
		if (e.player.currentEquippedItem != null && (e.player.currentEquippedItem.item === AlfheimItems.flugelSoul || e.player.currentEquippedItem.item === AlfheimItems.holoProjector)) e.isCanceled = true
	}
	
	@SubscribeEvent
	fun onBlockPlace(e: BlockEvent.PlaceEvent) {
		if (AlfheimCore.enableMMO && e.player.isPotionActive(AlfheimRegistry.leftFlame)) e.isCanceled = true
	}
	
	@SubscribeEvent
	fun onBlockMultiPlace(e: BlockEvent.MultiPlaceEvent) {
		if (AlfheimCore.enableMMO && e.player.isPotionActive(AlfheimRegistry.leftFlame)) e.isCanceled = true
	}
	
	@SubscribeEvent
	fun onPlayerDrop(e: ItemTossEvent) {
		if (AlfheimCore.enableMMO && e.player.isPotionActive(AlfheimRegistry.leftFlame)) {
			e.isCanceled = true
			e.player.inventory.addItemStackToInventory(e.entityItem.entityItem.copy())
		}
	}
	
	@SubscribeEvent
	fun onHeal(e: LivingHealEvent) {
		if (AlfheimCore.enableMMO && e.entityLiving.isPotionActive(AlfheimRegistry.leftFlame)) e.isCanceled = true
	}
}
