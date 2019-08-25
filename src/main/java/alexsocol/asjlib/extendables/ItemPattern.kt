package alexsocol.asjlib.extendables

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

class ItemPattern(modid: String, name: String, tab: CreativeTabs, stackSize: Int): Item() {
	
	init {
		creativeTab = tab
		maxStackSize = stackSize
		setTextureName("$modid:$name")
		unlocalizedName = name
	}
}
