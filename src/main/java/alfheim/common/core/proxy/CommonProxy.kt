package alfheim.common.core.proxy

import alexsocol.asjlib.ASJUtilities
import alfheim.api.ShadowFoxAPI
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.*
import alfheim.common.core.handler.*
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.crafting.recipe.*
import alfheim.common.integration.etfuturum.EtFuturumAlfheimConfig
import alfheim.common.integration.multipart.MultipartAlfheimConfig
import alfheim.common.integration.thaumcraft.TCHandlerShadowFoxAspects
import alfheim.common.item.*
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
		AlfheimBlocks.init()
		ShadowFoxBlocks
		AlfheimItems.init()
		ShadowFoxItems
		AlfheimRegistry.preInit()
		AlfheimAchievements.init()
		if (ConfigHandler.relicsEnabled) AlfheimLexiconData.preInit2()
		ShadowFoxLexiconData
		ShadowFoxThrowables
		HilarityHandler.register()
		BlockDispenser.dispenseBehaviorRegistry.putObject(ShadowFoxItems.resource, BifrostFlowerDispenserHandler())
		if (Botania.thaumcraftLoaded) TCHandlerShadowFoxAspects.initAspects()
		AlfheimMultiblocks.init()
	}
	
	open fun registerRenderThings() {}
	
	open fun registerKeyBinds() {}
	
	fun init() {
		AlfheimRecipes.init()
		ShadowFoxRecipes
		AlfheimRegistry.init()
		ASJUtilities.registerDimension(AlfheimConfig.dimensionIDAlfheim, WorldProviderAlfheim::class.java, false)
		ShadowFoxBlocks.registerBurnables()
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