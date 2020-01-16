package alfmod

import alfmod.common.core.proxy.CommonProxy
import cpw.mods.fml.common.*
import cpw.mods.fml.common.Mod.*
import cpw.mods.fml.common.event.*

@Mod(modid = AlfheimModularCore.MODID, version = "5", useMetadata = true)
class AlfheimModularCore {

	companion object {
		const val MODID = "alfmod"
		
		@Instance(MODID)
		lateinit var instance: AlfheimModularCore
		
		@field:SidedProxy(clientSide = "$MODID.client.core.proxy.ClientProxy", serverSide = "$MODID.common.core.proxy.CommonProxy")
		lateinit var proxy: CommonProxy
		
		@Metadata(MODID)
		lateinit var meta: ModMetadata
	}
	
	@EventHandler
	fun preInit(e: FMLPreInitializationEvent) {
		proxy.preInit()
	}
	
	@EventHandler
	fun init(e: FMLInitializationEvent) {
		proxy.init()
		proxy.registerHandlers()
	}
	
	@EventHandler
	fun postInit(e: FMLPostInitializationEvent) {
		proxy.postInit()
	}
	
}