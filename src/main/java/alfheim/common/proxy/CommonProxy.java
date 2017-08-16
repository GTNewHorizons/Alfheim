package alfheim.common.proxy;

import alfheim.common.event.CommonEventHandler;
import alfheim.common.event.CommonTickHandler;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.registry.AlfheimItems;
import alfheim.common.registry.AlfheimRecipes;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit() {
    	AlfheimBlocks.init();
    	AlfheimItems.init();
		AlfheimRecipes.init();
	}

	public void registerRenderThings() {}

	public void registerKeyBinds() {}

	public void init() {}
	
	public void postInit() {}
	
	public void initializeAndRegisterHandlers() {
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
		FMLCommonHandler.instance().bus().register(new CommonEventHandler());
		FMLCommonHandler.instance().bus().register(new CommonTickHandler());		
	}
}
