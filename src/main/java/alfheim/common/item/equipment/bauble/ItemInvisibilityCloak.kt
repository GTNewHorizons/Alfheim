package alfheim.common.item.equipment.bauble

import alfheim.AlfheimCore
import alfheim.common.integration.travellersgear.handler.TGHandlerBotaniaAdapter
import baubles.api.BaubleType
import cpw.mods.fml.common.Optional
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.StatCollector
import travellersgear.api.ITravellersGear
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.client.core.helper.RenderHelper
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.bauble.ItemBauble

@Optional.Interface(modid = "TravellersGear", iface = "travellersgear.api.ITravellersGear", striprefs = true)
class ItemInvisibilityCloak: ItemBauble("InvisibilityCloak"), IManaUsingItem, ITravellersGear {
	
	init {
		creativeTab = AlfheimCore.alfheimTab
	}
	
	override fun getBaubleType(arg0: ItemStack): BaubleType? {
		return if (AlfheimCore.TravellersGearLoaded) null else BaubleType.BELT
	}
	
	override fun onUnequipped(stack: ItemStack?, player: EntityLivingBase) {
		val effect = player.getActivePotionEffect(Potion.invisibility)
		if (effect != null && player is EntityPlayer && effect.amplifier == -42)
			player.removePotionEffect(Potion.invisibility.id)
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player is EntityPlayer && !player.worldObj.isRemote) {
			val manaCost = 2
			val hasMana = ManaItemHandler.requestManaExact(stack, player, manaCost, false)
			if (!hasMana)
				onUnequipped(stack, player)
			else {
				if (player.getActivePotionEffect(Potion.invisibility) != null)
					player.removePotionEffect(Potion.invisibility.id)
				
				player.addPotionEffect(PotionEffect(Potion.invisibility.id, Integer.MAX_VALUE, -42, true))
			}
		}
	}
	
	override fun usesMana(stack: ItemStack): Boolean {
		return true
	}
	
	override fun getSlot(stack: ItemStack): Int {
		return 0
	}
	
	override fun onTravelGearTick(player: EntityPlayer, stack: ItemStack) { // because for some reason it gots called AFTER unequip :/
		if (ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false)) onWornTick(stack, player)
	}
	
	override fun onTravelGearEquip(player: EntityPlayer, stack: ItemStack) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, true)
	}
	
	override fun onTravelGearUnequip(player: EntityPlayer, stack: ItemStack) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, false)
		onUnequipped(stack, player)
	}
	
	override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<*>, adv: Boolean) {
		if (AlfheimCore.TravellersGearLoaded) {
			TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("TG.desc.gearSlot.tg.0"), tooltip)
			val key = RenderHelper.getKeyDisplayString("TG.keybind.openInv")
			if (key != null)
				TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("alfheimmisc.tgtooltip").replace("%key%".toRegex(), key), tooltip)
		} else {
			val type = getBaubleType(stack)
			TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("botania.baubletype." + type!!.name.toLowerCase()), tooltip)
			val key = RenderHelper.getKeyDisplayString("Baubles Inventory")
			if (key != null)
				TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("botania.baubletooltip").replace("%key%".toRegex(), key), tooltip)
		}
		
		val cosmetic = getCosmeticItem(stack)
		if (cosmetic != null)
			TGHandlerBotaniaAdapter.addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.hasCosmetic"), cosmetic.displayName), tooltip)
		
		if (hasPhantomInk(stack))
			TGHandlerBotaniaAdapter.addStringToTooltip(StatCollector.translateToLocal("botaniamisc.hasPhantomInk"), tooltip)
	}
	
	companion object {
		
		val TAG_EQUIPPED = "equipped" // damn it, next time check your sync!
	}
}