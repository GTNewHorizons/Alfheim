package alfheim.common.integration.travellersgear

import alfheim.AlfheimCore
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import travellersgear.api.ITravellersGear
import vazkii.botania.client.core.helper.RenderHelper
import vazkii.botania.common.item.equipment.bauble.ItemBauble

object TravellerBaubleTooltipHandler {
	
	fun addHiddenTooltip(bauble: ItemBauble, stack: ItemStack, tooltip: MutableList<Any?>) {
		if (AlfheimCore.TravellersGearLoaded) {
			bauble as ITravellersGear
			addStringToTooltip(StatCollector.translateToLocal("TG.desc.gearSlot.tg." + bauble.getSlot(stack)), tooltip)
			val key = RenderHelper.getKeyDisplayString("TG.keybind.openInv")
			if (key != null)
				addStringToTooltip(StatCollector.translateToLocal("alfheimmisc.tgtooltip").replace("%key%".toRegex(), key), tooltip)
		} else {
			val type = bauble.getBaubleType(stack)
			addStringToTooltip(StatCollector.translateToLocal("botania.baubletype." + type.name.toLowerCase()), tooltip)
			val key = RenderHelper.getKeyDisplayString("Baubles Inventory")
			if (key != null)
				addStringToTooltip(StatCollector.translateToLocal("botania.baubletooltip").replace("%key%".toRegex(), key), tooltip)
		}
		
		val cosmetic = bauble.getCosmeticItem(stack)
		if (cosmetic != null)
			addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.hasCosmetic"), cosmetic.displayName), tooltip)
		
		if (bauble.hasPhantomInk(stack))
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.hasPhantomInk"), tooltip)
	}
	
	// --------------------------------
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>) {
		tooltip.add(s.replace("&".toRegex(), "\u00a7"))
	}
}