package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.getActivePotionEffect
import alfheim.AlfheimCore
import alfheim.common.core.util.AlfheimTab
import alfheim.common.integration.travellersgear.*
import baubles.api.BaubleType
import cpw.mods.fml.common.Optional
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.StatCollector
import vazkii.botania.api.mana.*
import vazkii.botania.client.core.helper.*
import vazkii.botania.common.item.equipment.bauble.ItemBauble

@Optional.Interface(modid = "TravellersGear", iface = "alfheim.common.integration.travellersgear.ITravellersGearSynced", striprefs = true)
class ItemInvisibilityCloak: ItemBauble("InvisibilityCloak"), IManaUsingItem, ITravellersGearSynced {
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun registerIcons(reg: IIconRegister?) {
		itemIcon = IconHelper.forName(reg, "cloak_invisibility")
	}
	
	override fun getBaubleType(arg0: ItemStack) =
		if (AlfheimCore.TravellersGearLoaded) null else BaubleType.BELT
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player is EntityPlayer && !player.worldObj.isRemote) {
			val manaCost = 2
			val hasMana = ManaItemHandler.requestManaExact(stack, player, manaCost, false)
			if (!hasMana)
				onUnequipped(stack, player)
			else {
				if (player.getActivePotionEffect(Potion.invisibility.id) != null)
					player.removePotionEffect(Potion.invisibility.id)
				ManaItemHandler.requestManaExact(stack, player, manaCost, true)
				player.addPotionEffect(PotionEffect(Potion.invisibility.id, 10, -42, true))
			}
		}
	}
	
	override fun onUnequipped(stack: ItemStack?, player: EntityLivingBase) {
		val effect = player.getActivePotionEffect(Potion.invisibility.id)
		if (effect != null && player is EntityPlayer && effect.amplifier == -42)
			player.removePotionEffect(Potion.invisibility.id)
	}
	
	override fun usesMana(stack: ItemStack) = true
	
	override fun getSlot(stack: ItemStack) = 0
	
	override fun onTravelGearTickSynced(player: EntityPlayer, stack: ItemStack) {
		onWornTick(stack, player)
	}
	
	override fun onTravelGearUnequip(player: EntityPlayer, stack: ItemStack) {
		super.onTravelGearUnequip(player, stack)
		onUnequipped(stack, player)
	}
	
	override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<Any?>, adv: Boolean) {
		tooltip as MutableList<String>
		if (AlfheimCore.TravellersGearLoaded) {
			TGHandlerBotaniaAdapterHooks.addStringToTooltip(StatCollector.translateToLocal("TG.desc.gearSlot.tg.0"), tooltip)
			val key = RenderHelper.getKeyDisplayString("TG.keybind.openInv")
			if (key != null)
				TGHandlerBotaniaAdapterHooks.addStringToTooltip(StatCollector.translateToLocal("alfheimmisc.tgtooltip").replace("%key%".toRegex(), key), tooltip)
		} else {
			val type = getBaubleType(stack)
			TGHandlerBotaniaAdapterHooks.addStringToTooltip(StatCollector.translateToLocal("botania.baubletype." + type!!.name.toLowerCase()), tooltip)
			val key = RenderHelper.getKeyDisplayString("Baubles Inventory")
			if (key != null)
				TGHandlerBotaniaAdapterHooks.addStringToTooltip(StatCollector.translateToLocal("botania.baubletooltip").replace("%key%".toRegex(), key), tooltip)
		}
		
		val cosmetic = getCosmeticItem(stack)
		if (cosmetic != null)
			TGHandlerBotaniaAdapterHooks.addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.hasCosmetic"), cosmetic.displayName), tooltip)
		
		if (hasPhantomInk(stack))
			TGHandlerBotaniaAdapterHooks.addStringToTooltip(StatCollector.translateToLocal("botaniamisc.hasPhantomInk"), tooltip)
	}
	
	companion object {
		
		const val TAG_EQUIPPED = "equipped" // damn it, next time check your sync!
	}
}