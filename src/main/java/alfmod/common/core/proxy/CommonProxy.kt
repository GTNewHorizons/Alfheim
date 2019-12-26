package alfmod.common.core.proxy

import alfheim.common.core.util.AlfheimTab
import alfmod.common.item.AlfheimModularItems

open class CommonProxy {
	
	open fun preInit() {
		AlfheimModularItems
		
	}
	
	open fun init() {
	
	}
	
	open fun postInit() {
		AlfheimTab.additionalDisplays.add {
			AlfheimTab.addItem(AlfheimModularItems.snowSword)
			AlfheimTab.addItem(AlfheimModularItems.snowHelmet)
			AlfheimTab.addItem(AlfheimModularItems.snowChest)
			AlfheimTab.addItem(AlfheimModularItems.snowLeggings)
			AlfheimTab.addItem(AlfheimModularItems.snowBoots)
		}
	}
}