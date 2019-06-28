package alfheim.common.integration.waila

import alfheim.common.block.tile.*
import alfheim.common.integration.waila.handler.*
import mcp.mobius.waila.api.impl.ModuleRegistrar

object WAILAAlfheimConfig {
	
	fun loadConfig() {
		ModuleRegistrar.instance().registerBodyProvider(WAILAHandlerAnyavil(), TileAnyavil::class.java!!)
		ModuleRegistrar.instance().registerNBTProvider(WAILAHandlerAnyavil(), TileAnyavil::class.java!!)
		
		ModuleRegistrar.instance().registerBodyProvider(WAILAHandlerItemHolder(), TileItemHolder::class.java!!)
		ModuleRegistrar.instance().registerNBTProvider(WAILAHandlerItemHolder(), TileItemHolder::class.java!!)
		
		ModuleRegistrar.instance().registerBodyProvider(WAILAHandlerTradePortal(), TileTradePortal::class.java!!)
		ModuleRegistrar.instance().registerNBTProvider(WAILAHandlerTradePortal(), TileTradePortal::class.java!!)
	}
}
