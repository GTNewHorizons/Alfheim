package alfheim.common.integration.travellersgear

import alexsocol.asjlib.addStringToTooltip
import alfheim.AlfheimCore
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import travellersgear.api.ITravellersGear
import vazkii.botania.client.core.helper.RenderHelper
import vazkii.botania.common.item.equipment.bauble.ItemBauble

object TravellerBaubleTooltipHandler {
	
	fun addHiddenTooltip(bauble: ItemBauble, stack: ItemStack, tooltip: MutableList<Any?>) {
		if (AlfheimCore.TravellersGearLoaded) {
			val slot = (bauble as? ITravellersGear)?.getSlot(stack) ?: 0
			addStringToTooltip(tooltip, "TG.desc.gearSlot.tg.$slot")
			val key = RenderHelper.getKeyDisplayString("TG.keybind.openInv")
			if (key != null)
				addStringToTooltip(tooltip, "alfheimmisc.tgtooltip", key)
		} else {
			val type = bauble.getBaubleType(stack)
			addStringToTooltip(tooltip, "botania.baubletype.${type.name.toLowerCase()}")
			val key = RenderHelper.getKeyDisplayString("Baubles Inventory")
			if (key != null)
				addStringToTooltip(tooltip, StatCollector.translateToLocal("botania.baubletooltip").replace("%key%".toRegex(), key))
		}
		
		val cosmetic = bauble.getCosmeticItem(stack)
		if (cosmetic != null)
			addStringToTooltip(tooltip, String.format(StatCollector.translateToLocal("botaniamisc.hasCosmetic"), cosmetic.displayName))
		
		if (bauble.hasPhantomInk(stack))
			addStringToTooltip(tooltip, StatCollector.translateToLocal("botaniamisc.hasPhantomInk"))
	}
}