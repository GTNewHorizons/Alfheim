package alfheim.common.item

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.util.*

/**
 * @author WireSegal
 * Created at 7:14 PM on 2/9/16.
 */
class EventHandlerTooltip {
	
	companion object {
		val instance = EventHandlerTooltip()
		
		fun register() {
			MinecraftForge.EVENT_BUS.register(instance)
		}
		
		val wip = ArrayList<ItemStack>()
		
		fun registerStack(itemStack: ItemStack) {
			if (!wip.contains(itemStack))
				wip.add(itemStack)
		}
	}
	
	@SubscribeEvent
	fun onItemTooltip(e: ItemTooltipEvent) {
		for (stack in wip) {
			if (e.itemStack.itemDamage != stack.itemDamage && stack.itemDamage != OreDictionary.WILDCARD_VALUE) continue
			if (e.itemStack.item != stack.item) continue
			if (e.itemStack.hasTagCompound() && stack.hasTagCompound() && ItemNBTHelper.getString(e.itemStack, "type", "no") != ItemNBTHelper.getString(stack, "type", "definitely not")) continue
			e.toolTip.add("\u00a74\u00a7lWIP")
			return
		}
	}
}