package alfheim.common.core.proxy

import alexsocol.asjlib.ASJUtilities
import alfheim.api.ShadowFoxAPI
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
import cpw.mods.fml.common.*
import net.minecraft.block.BlockDispenser
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.ModItems

open class CommonProxy {
	
	open fun preInit() {
		ShadowFoxAPI.RUNEAXE.setRepairItem(ItemStack(ModItems.manaResource, 1, 7)) // Elementium
		
		AlfheimLexiconData.preInit()
		AlfheimBlocks
		AlfheimBlocks
		AlfheimItems
		AlfheimRegistry.preInit()
		AlfheimAchievements
		if (ConfigHandler.relicsEnabled) AlfheimLexiconData.preInit2()
		ShadowFoxLexiconData
		ShadowFoxThrowables
		ChestGenHandler
		HilarityHandler.register()
		BlockDispenser.dispenseBehaviorRegistry.putObject(AlfheimItems.elvenResource, BifrostFlowerDispenserHandler())
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
		AlfheimLexiconData.postInit()
		AlfheimRegistry.postInit()
	}
	
	open fun initializeAndRegisterHandlers() {
		MinecraftForge.EVENT_BUS.register(EventHandler())
		FMLCommonHandler.instance().bus().register(EventHandler())
	}
}