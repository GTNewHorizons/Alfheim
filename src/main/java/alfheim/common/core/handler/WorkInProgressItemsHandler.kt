package alfheim.common.core.handler

import alexsocol.asjlib.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.event.entity.player.ItemTooltipEvent

object WorkInProgressItemsHandler {
	
	val wipList = ArrayList<Item>()
	
	init {
		this.eventForge()
	}
	
	fun Block.WIP(): Block {
		this.toItem()?.let { wipList.add(it) }
		return this
	}
	
	fun Item.WIP(): Item {
		wipList.add(this)
		return this
	}
	
	@SubscribeEvent
	fun onTooltip(e: ItemTooltipEvent) {
		if (e.itemStack.item inn wipList)
			e.toolTip.add("${EnumChatFormatting.DARK_RED}[WIP]")
	}
}