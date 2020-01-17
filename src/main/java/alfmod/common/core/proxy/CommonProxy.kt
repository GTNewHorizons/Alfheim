package alfmod.common.core.proxy

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.*
import alfheim.common.world.dim.alfheim.biome.BiomeField
import alfmod.AlfheimModularCore
import alfmod.common.entity.*
import alfmod.common.entity.boss.EntityDedMoroz
import alfmod.common.item.AlfheimModularItems

open class CommonProxy {
	
	open fun preInit() {
		AlfheimModularItems
	}
	
	open fun init() {
		ASJUtilities.registerEntityEgg(EntityDedMoroz::class.java, "DedMoroz", 0xBBBBBB, 0x44FFFF, AlfheimModularCore.instance)
		ASJUtilities.registerEntityEgg(EntitySnowSprite::class.java, "SnowSprite", 0xFFFFFF, 0x88FFFF, AlfheimModularCore.instance)
		
		ASJUtilities.registerEntity(EntitySniceBall::class.java, "SniceBall", AlfheimModularCore.instance)
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
		
		BiomeField.addEntry(EntitySnowSprite::class.java, AlfheimConfigHandler.pixieSpawn.mapInPlace { it * 4 })
	}
}