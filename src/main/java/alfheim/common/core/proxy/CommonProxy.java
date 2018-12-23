package alfheim.common.core.proxy;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.core.handler.EventHandler;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRecipes;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.lexicon.AlfheimLexiconData;
import alfheim.common.world.dim.alfheim.WorldProviderAlfheim;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.common.core.handler.ConfigHandler;

public class CommonProxy {

	public void preInit() {
		AlfheimLexiconData.preInit();
		AlfheimBlocks.init();
		AlfheimItems.init();
		AlfheimRecipes.preInit();
		AlfheimRegistry.preInit();
		AlfheimAchievements.init();
		if (ConfigHandler.relicsEnabled) AlfheimLexiconData.preInit2();
	}

	public void registerRenderThings() {}

	public void registerKeyBinds() {}

	public void init() {
		AlfheimLexiconData.init();
		AlfheimRegistry.init();
		AlfheimRecipes.init();
		ASJUtilities.registerDimension(AlfheimConfig.dimensionIDAlfheim, WorldProviderAlfheim.class, false);
	}
	
	public void postInit() {
		AlfheimLexiconData.postInit();
		AlfheimRegistry.postInit();
	}
	
	public void initializeAndRegisterHandlers() {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		FMLCommonHandler.instance().bus().register(new EventHandler());
	}
}