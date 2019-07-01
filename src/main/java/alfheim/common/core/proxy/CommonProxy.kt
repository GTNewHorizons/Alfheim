package alfheim.common.core.proxy

import alexsocol.asjlib.ASJUtilities
import alfheim.common.block.AlfheimMultiblocks
import alfheim.common.core.handler.EventHandler
import alfheim.common.core.registry.*
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.lexicon.AlfheimLexiconData
import alfheim.common.world.dim.alfheim.WorldProviderAlfheim
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.common.core.handler.ConfigHandler

open class CommonProxy {
	
	open fun preInit() {
		AlfheimLexiconData.preInit()
		AlfheimBlocks.init()
		AlfheimItems.init()
		AlfheimRegistry.preInit()
		AlfheimAchievements.init()
		if (ConfigHandler.relicsEnabled) AlfheimLexiconData.preInit2()
		AlfheimMultiblocks.init()
	}
	
	open fun registerRenderThings() {}
	
	open fun registerKeyBinds() {}
	
	fun init() {
		AlfheimRecipes.init()
		AlfheimRegistry.init()
		ASJUtilities.registerDimension(AlfheimConfig.dimensionIDAlfheim, WorldProviderAlfheim::class.java, false)
	}
	
	open fun postInit() {
		AlfheimRecipes.postInit()
		AlfheimLexiconData.init()
		AlfheimLexiconData.postInit()
		AlfheimRegistry.postInit()
	}
	
	open fun initializeAndRegisterHandlers() {
		MinecraftForge.EVENT_BUS.register(EventHandler())
		FMLCommonHandler.instance().bus().register(EventHandler())
	}
}