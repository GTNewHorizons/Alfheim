package advtitles

import advtitles.items.ItemAdvancedTitle
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraft.item.Item

@Mod(modid = "advtitles", name = "AdvancedTitles", version = "1")
class AdvTitlesCore {
	
	lateinit var advTitle: Item
	
	@Mod.EventHandler
	fun preInit(e: FMLPreInitializationEvent) {
		advTitle = ItemAdvancedTitle()
	}
}