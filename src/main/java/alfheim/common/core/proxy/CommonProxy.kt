package alfheim.common.core.proxy

import alexsocol.asjlib.ASJUtilities
import alfheim.api.*
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.*
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.crafting.recipe.*
import alfheim.common.integration.etfuturum.EtFuturumAlfheimConfig
import alfheim.common.integration.multipart.MultipartAlfheimConfig
import alfheim.common.integration.thaumcraft.TCHandlerShadowFoxAspects
import alfheim.common.item.AlfheimItems
import alfheim.common.lexicon.*
import alfheim.common.world.dim.alfheim.WorldProviderAlfheim
import cpw.mods.fml.client.event.ConfigChangedEvent
import cpw.mods.fml.common.*
import cpw.mods.fml.common.eventhandler.*
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
		if (Botania.thaumcraftLoaded) TCHandlerShadowFoxAspects.initAspects()
		AlfheimMultiblocks
	}
	
	open fun registerRenderThings() {}
	
	open fun registerKeyBinds() {}
	
	fun init() {
		AlfheimRecipes
		ShadowFoxRecipes
		AlfheimRegistry.init()
		ASJUtilities.registerDimension(AlfheimConfigHandler.dimensionIDAlfheim, WorldProviderAlfheim::class.java, false)
		AlfheimBlocks.registerBurnables()
		if (Loader.isModLoaded("ForgeMultipart")) MultipartAlfheimConfig.loadConfig()
		if (Loader.isModLoaded("etfuturem")) EtFuturumAlfheimConfig.loadConfig()
	}
	
	open fun postInit() {
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
	}
	
	open fun featherFX(world: World, x: Double, y: Double, z: Double, color: Int) = featherFX(world, x, y, z, color, 1f)
	open fun featherFX(world: World, x: Double, y: Double, z: Double, color: Int, scale: Float) = featherFX(world, x, y, z, color, scale, 1f)
	open fun featherFX(world: World, x: Double, y: Double, z: Double, color: Int, scale: Float, lifetime: Float) = featherFX(world, x, y, z, color, scale, lifetime, 16f)
	open fun featherFX(world: World, x: Double, y: Double, z: Double, color: Int, scale: Float, lifetime: Float, distance: Float) = featherFX(world, x, y, z, color, scale, lifetime, distance, false)
	open fun featherFX(world: World, x: Double, y: Double, z: Double, color: Int, scale: Float, lifetime: Float, distance: Float, must: Boolean) = Unit
}