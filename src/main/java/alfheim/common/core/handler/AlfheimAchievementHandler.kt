package alfheim.common.core.handler

import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.item.AlfheimItems
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.passive.EntityHorse
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.*
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import vazkii.botania.common.item.ModItems

object AlfheimAchievementHandler {
	
	init {
		FMLCommonHandler.instance().bus().register(this)
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun wingedHussars(e: LivingUpdateEvent) {
		val player = e.entityLiving as? EntityPlayer ?: return
		
		val armorFlag = (0..4).all {
			player.getEquipmentInSlot(it)?.item == when (it) {
				0    -> AlfheimItems.realitySword
				1    -> AlfheimItems.elvoriumBoots
				2    -> AlfheimItems.elvoriumLeggings
				3    -> AlfheimItems.elvoriumChestplate
				4    -> AlfheimItems.elvoriumHelmet
				else -> Item.getItemFromBlock(Blocks.air)
			}
		}
		
		val tiaraStack = PlayerHandler.getPlayerBaubles(player).getStackInSlot(0)
		val baublesFlag = tiaraStack?.item == ModItems.flightTiara && tiaraStack?.itemDamage == 4
		
		val horse = player.ridingEntity
		val horseFlag = horse is EntityHorse &&
						horse.horseType == 0 &&
						horse.horseVariant and 255 == 1 &&
						horse.horseChest.getStackInSlot(0)?.item == Items.saddle &&
						horse.horseChest.getStackInSlot(1)?.item == Items.golden_horse_armor
		
		if (armorFlag && baublesFlag && horseFlag) player.triggerAchievement(AlfheimAchievements.wingedHussar)
	}
}