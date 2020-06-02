package alfheim.common.core.proxy

import alexsocol.asjlib.ASJUtilities
import alfheim.api.*
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.*
import alfheim.common.core.helper.ContributorsPrivacyHelper
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.crafting.recipe.*
import alfheim.common.integration.etfuturum.EtFuturumAlfheimConfig
import alfheim.common.integration.multipart.MultipartAlfheimConfig
import alfheim.common.integration.thaumcraft.TCHandlerShadowFoxAspects
import alfheim.common.item.AlfheimItems
import alfheim.common.lexicon.*
import alfheim.common.security.InteractionSecurity
import alfheim.common.world.dim.alfheim.WorldProviderAlfheim
import cpw.mods.fml.client.event.ConfigChangedEvent
import cpw.mods.fml.common.*
import cpw.mods.fml.common.eventhandler.*
import net.minecraft.block.BlockTrapDoor
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.ModItems

open class CommonProxy {
	
	open fun preInit() {
		ShadowFoxAPI.RUNEAXE.setRepairItem(ItemStack(ModItems.manaResource, 1, 7)) // Elementium
		
		AlfheimLexiconData.preInit()
		AlfheimBlocks
		AlfheimItems
		AlfheimRegistry.preInit()
		AlfheimAchievements
		if (ConfigHandler.relicsEnabled) AlfheimLexiconData.preInitRelics()
		ShadowFoxLexiconData
		ShadowFoxThrowables
		BifrostFlowerDispenserHandler
		ThrownPotionDispenserHandler
		ThrownItemDispenserHandler
		WaterBowlDispenserHandler
		if (Botania.thaumcraftLoaded) TCHandlerShadowFoxAspects.initAspects()
		AlfheimMultiblocks
		
		InteractionSecurity
	}
	
	open fun registerRenderThings() = Unit
	
	open fun registerKeyBinds() = Unit
	
	fun init() {
		AlfheimRecipes
		ShadowFoxRecipes
		AlfheimRegistry.init()
		ASJUtilities.registerDimension(AlfheimConfigHandler.dimensionIDAlfheim, WorldProviderAlfheim::class.java, false)
		AlfheimBlocks.registerBurnables()
		if (Loader.isModLoaded("ForgeMultipart")) MultipartAlfheimConfig.loadConfig()
		if (Loader.isModLoaded("etfuturum")) EtFuturumAlfheimConfig.loadConfig()
		BlockTrapDoor.disableValidation = AlfheimConfigHandler.floatingTrapDoors
	}
	
	open fun postInit() {
		AlfheimBlocks.regOreDict()
		AlfheimItems.regOreDict()
		AlfheimRecipes.postInit()
		ShadowFoxRecipes.postInit()
		AlfheimLexiconData.init()
		if (ConfigHandler.relicsEnabled) AlfheimLexiconData.initRelics()
		//AlfheimLexiconData.postInit()
		AlfheimRegistry.postInit()
	}
	
	open fun initializeAndRegisterHandlers() {
		EventHandler
		ESMHandler
		ChestGenHandler
		HilarityHandler
		SoulRestructurizationHandler
		
		FMLCommonHandler.instance().bus().register(object {
			@SubscribeEvent(priority = EventPriority.HIGHEST)
			fun onConfigChanged(e: ConfigChangedEvent.OnConfigChangedEvent) {
				if (e.modID == ModInfo.MODID) AlfheimConfigHandler.syncConfig()
			}
		})
		
		ContributorsPrivacyHelper
	}
	
	open fun featherFX(world: World, x: Double, y: Double, z: Double, color: Int, size: Float = 1f, lifetime: Float = 1f, distance: Float = 16f, must: Boolean = false) = Unit
	
	open fun bloodFX(world: World, x: Double, y: Double, z: Double, lifetime: Int = 100, size: Float = 1f, gravity: Float = 1f) = Unit
}