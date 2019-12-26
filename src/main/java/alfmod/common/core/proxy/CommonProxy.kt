package alfmod.common.core.proxy

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.util.AlfheimTab
import alfmod.AlfheimModularCore
import alfmod.common.entity.EntityDedMoroz
import alfmod.common.item.AlfheimModularItems

open class CommonProxy {
	
	open fun preInit() {
		AlfheimModularItems
	}
	
	open fun init() {
		ASJUtilities.registerEntity(EntityDedMoroz::class.java, "DedMoroz", AlfheimModularCore.instance)
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