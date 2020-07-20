package alfmod.common.core.proxy

import alexsocol.asjlib.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.world.dim.alfheim.biome.BiomeField
import alfmod.AlfheimModularCore
import alfmod.common.block.AlfheimModularBlocks
import alfmod.common.core.handler.*
import alfmod.common.crafting.recipe.AlfheimModularRecipes
import alfmod.common.entity.*
import alfmod.common.entity.boss.EntityDedMoroz
import alfmod.common.item.AlfheimModularItems
import alfmod.common.lexicon.AlfheimModularLexiconData

open class CommonProxy {
	
	open fun preInit() {
		AlfheimModularBlocks
		AlfheimModularItems
		
		AlfheimModularLexiconData
		
		EventHandlerWinter.eventFML()
		EventHandlerSummer.eventForge()
	}
	
	open fun init() {
		AlfheimModularRecipes
		
		ASJUtilities.registerEntity(EntityDedMoroz::class.java, "DedMoroz", AlfheimModularCore.instance, AlfheimRegistry.id)
		ASJUtilities.registerEntity(EntitySnowSprite::class.java, "SnowSprite", AlfheimModularCore.instance, AlfheimRegistry.id)
		ASJUtilities.registerEntity(EntitySniceBall::class.java, "SniceBall", AlfheimModularCore.instance, AlfheimRegistry.id)
		
		ASJUtilities.registerEntity(EntityFirespirit::class.java, "FireSpirit", AlfheimModularCore.instance, AlfheimRegistry.id)
		ASJUtilities.registerEntity(EntityMuspellsun::class.java, "Muspellsun", AlfheimModularCore.instance, AlfheimRegistry.id)
		ASJUtilities.registerEntity(EntityRollingMelon::class.java, "RollingMelon", AlfheimModularCore.instance, AlfheimRegistry.id)
	}
	
	open fun registerHandlers() {
		if (WRATH_OF_THE_WINTER) SpriteKillhandler
	}
	
	open fun postInit() {

		if (WRATH_OF_THE_WINTER)
			BiomeField.addEntry(EntitySnowSprite::class.java, AlfheimConfigHandler.pixieSpawn.map { it * 2 }.toIntArray())
	}
}