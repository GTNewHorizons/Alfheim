package alexsocol.asjlib.extendables

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

class ItemPattern(modid: String, name: String, tab: CreativeTabs, stackSize: Int): Item() {
	
	init {
		this.creativeTab = tab
		this.setMaxStackSize(stackSize)
		this.setTextureName("$modid:$name")
		this.unlocalizedName = name
	}
}
