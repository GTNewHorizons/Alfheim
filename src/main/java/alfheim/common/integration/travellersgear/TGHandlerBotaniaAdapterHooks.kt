package alfheim.common.integration.travellersgear

import alfheim.AlfheimCore
import alfheim.common.item.equipment.bauble.ItemBalanceCloak
import baubles.api.BaubleType
import cpw.mods.fml.relauncher.*
import gloomyfolken.hooklib.asm.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraftforge.event.entity.living.LivingHurtEvent
import travellersgear.api.TravellersGearAPI
import vazkii.botania.common.item.equipment.bauble.*

object TGHandlerBotaniaAdapterHooks {
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getBaubleType(item: ItemHolyCloak, stack: ItemStack): BaubleType? {
		return if (AlfheimCore.TravellersGearLoaded) null else BaubleType.BELT
	}
	
	@JvmStatic
	@Hook(createMethod = true)
	fun onTravelGearEquip(item: ItemHolyCloak, player: EntityPlayer, stack: ItemStack) {
		item.onEquipped(stack, player)
	}
	
	@JvmStatic
	@Hook(createMethod = true)
	fun onTravelGearTick(item: ItemHolyCloak, player: EntityPlayer, stack: ItemStack) {
		if (AlfheimCore.TravellersGearLoaded) item.onWornTick(stack, player)
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	fun onPlayerDamage(item: ItemHolyCloak, event: LivingHurtEvent): Boolean {
		if (!AlfheimCore.TravellersGearLoaded) return false
		
		if (event.entityLiving is EntityPlayer) {
			val player = event.entityLiving as EntityPlayer
			
			val tg = TravellersGearAPI.getExtendedInventory(player)
			
			if (tg[0] != null && tg[0].item is ItemHolyCloak && !ItemHolyCloak.isInEffect(tg[0])) {
				val cloak = tg[0].item as ItemHolyCloak
				val cooldown = ItemHolyCloak.getCooldown(tg[0])
				
				// Used to prevent StackOverflows with mobs that deal damage when damaged
				ItemHolyCloak.setInEffect(tg[0], true)
				if (cooldown == 0 && cloak.effectOnDamage(event, player, tg[0]))
					ItemHolyCloak.setCooldown(tg[0], cloak.getCooldownTime(tg[0]))
				ItemHolyCloak.setInEffect(tg[0], false)
			}
			
			TravellersGearAPI.setExtendedInventory(player, tg)
		}
		return true
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, createMethod = true, isMandatory = true)
	fun addHiddenTooltip(cloak: ItemHolyCloak, stack: ItemStack, player: EntityPlayer, tooltip: MutableList<Any?>, adv: Boolean) {
		TravellerBaubleTooltipHandler.addHiddenTooltip(cloak, stack, tooltip)
	}
	
	@JvmStatic
	@SideOnly(Side.CLIENT)
	@Hook(returnCondition = ReturnCondition.ALWAYS, createMethod = true)
	fun getIconFromDamage(cloak: ItemHolyCloak, meta: Int): IIcon {
		return ItemBalanceCloak.iconHoly
	}
	
	@JvmStatic
	@SideOnly(Side.CLIENT)
	@Hook(returnCondition = ReturnCondition.ALWAYS, createMethod = true)
	fun getIconFromDamage(cloak: ItemUnholyCloak, meta: Int): IIcon {
		return ItemBalanceCloak.iconUnholy
	}
}