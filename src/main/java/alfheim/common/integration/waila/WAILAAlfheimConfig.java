package alfheim.common.integration.waila;

import alfheim.common.block.tile.TileAnyavil;
import alfheim.common.block.tile.TileTradePortal;
import alfheim.common.integration.waila.handler.WAILAHandlerAnyavil;
import alfheim.common.integration.waila.handler.WAILAHandlerTradePortal;
import mcp.mobius.waila.api.impl.ModuleRegistrar;

public class WAILAAlfheimConfig {
	
	public static void loadConfig() {
		ModuleRegistrar.instance().registerBodyProvider(new WAILAHandlerAnyavil(), TileAnyavil.class);
		ModuleRegistrar.instance().registerNBTProvider(new WAILAHandlerAnyavil(), TileAnyavil.class);
		
		ModuleRegistrar.instance().registerBodyProvider(new WAILAHandlerTradePortal(), TileTradePortal.class);
		ModuleRegistrar.instance().registerNBTProvider(new WAILAHandlerTradePortal(), TileTradePortal.class);
	}
}
