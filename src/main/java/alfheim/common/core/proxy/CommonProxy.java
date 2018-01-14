package alfheim.common.core.proxy;

import alfheim.AlfheimCore;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRecipes;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.utils.AlfheimBotaniaModifiers;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.event.CommonEventHandler;
import alfheim.common.lexicon.AlfheimLexiconData;
import alfheim.common.world.dim.DimensionUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		AlfheimConfig.loadConfig(e.getSuggestedConfigurationFile());
    	AlfheimLexiconData.preInit();
    	AlfheimBlocks.init();
    	AlfheimItems.init();
		AlfheimRecipes.preInit();
		AlfheimRegistry.preInit();
    	AlfheimLexiconData.init();
	}

	public void registerRenderThings() {}

	public void registerKeyBinds() {}

	public void init() {
		AlfheimRegistry.init();
		DimensionUtil.init();
		AlfheimRecipes.init();
	}
	
	public void postInit() {
		AlfheimAchievements.init();
		AlfheimBotaniaModifiers.postInit();
	}
	
	public void initializeAndRegisterHandlers() {
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
		FMLCommonHandler.instance().bus().register(new CommonEventHandler());
	}
}
