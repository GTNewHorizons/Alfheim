package alfmod.common.core.proxy

import alexsocol.asjlib.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.world.dim.alfheim.biome.*
import alfheim.common.world.dim.alfheim.biome.BiomeAlfheim.Companion.addEntry
import alfmod.AlfheimModularCore
import alfmod.common.block.AlfheimModularBlocks
import alfmod.common.core.handler.*
import alfmod.common.crafting.recipe.AlfheimModularRecipes
import alfmod.common.entity.*
import alfmod.common.entity.boss.EntityDedMoroz
import alfmod.common.item.AlfheimModularItems
import alfmod.common.lexicon.AlfheimModularLexiconData
import net.minecraft.entity.EnumCreatureType
import net.minecraft.world.biome.BiomeGenBase

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
		
		ASJUtilities.registerEntity(EntityFireSpirit::class.java, "FireSpirit", AlfheimModularCore.instance, AlfheimRegistry.id)
		ASJUtilities.registerEntity(EntityMuspellsun::class.java, "Muspellsun", AlfheimModularCore.instance, AlfheimRegistry.id)
		ASJUtilities.registerEntity(EntityRollingMelon::class.java, "RollingMelon", AlfheimModularCore.instance, AlfheimRegistry.id)
	}
	
	open fun registerHandlers() {
		if (WRATH_OF_THE_WINTER) SpriteKillhandler
	}
	
	open fun postInit() {
		if (WRATH_OF_THE_WINTER)
			BiomeField.addEntry(EntitySnowSprite::class.java, AlfheimConfigHandler.pixieSpawn.map { it * 2 }.toIntArray())
		
		if (HELLISH_VACATION) {
			arrayOf(BiomeBeach, BiomeSandbank, BiomeGenBase.jungle, BiomeGenBase.jungleEdge, BiomeGenBase.jungleHills, BiomeGenBase.beach).forEach {
				it.addEntry(EntityRollingMelon::class.java, AlfheimConfigHandler.pixieSpawn.map { v -> v * 4 }.toIntArray())
			}
			
			BiomeGenBase.hell.getSpawnableList(EnumCreatureType.monster).add(BiomeGenBase.SpawnListEntry(EntityMuspellsun::class.java, 20, 4, 4))
		}
	}
}