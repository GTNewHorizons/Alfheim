package alfmod.common.core.proxy

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.util.AlfheimTab
import alfmod.AlfheimModularCore
import alfmod.common.entity.*
import alfmod.common.item.AlfheimModularItems

open class CommonProxy {
	
	open fun preInit() {
		AlfheimModularItems
	}
	
	open fun init() {
		ASJUtilities.registerEntity(EntityDedMoroz::class.java, "DedMoroz", AlfheimModularCore.instance)
		ASJUtilities.registerEntityEgg(EntitySnowSprite::class.java, "SnowSprite", 0xFFFFFF, 0x44FFFF, AlfheimModularCore.instance)
	}
	
	open fun registerHandlers() {
		SpriteKillhandler
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