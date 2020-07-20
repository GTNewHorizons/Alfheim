package alfmod.common.item.equipment.tool

import alfmod.common.core.util.AlfheimModularTab
import net.minecraftforge.common.util.EnumHelper
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword

class ItemVolcanoMace: ItemManasteelSword(volcano, "VolcanoMace") {
	
	companion object {
		val volcano = EnumHelper.addToolMaterial("Volcano", 0, 1200, 6f, 6f, 6)!!
	}
	
	init {
		creativeTab = AlfheimModularTab
	}
	
}