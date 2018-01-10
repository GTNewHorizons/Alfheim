package alfheim.common.core.proxy;

import alfheim.AlfheimCore;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRecipes;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.utils.ElvenStoryModModifiers;
import alfheim.common.event.CommonEventHandler;
import alfheim.common.lexicon.AlfheimLexiconCategory;
import alfheim.common.world.dim.DimensionUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit() {
    	AlfheimLexiconCategory.preInit();
    	AlfheimBlocks.init();
    	AlfheimItems.init();
		AlfheimRecipes.init();
		AlfheimRegistry.preInit();
    	AlfheimLexiconCategory.init();
	}

	public void registerRenderThings() {}

	public void registerKeyBinds() {}

	public void init() {
		AlfheimRegistry.init();
		DimensionUtil.init();
	}
	
	public void postInit() {
		AlfheimAchievements.init();
		if (AlfheimCore.enableElvenStory) ElvenStoryModModifiers.postInit();
	}
	
	public void initializeAndRegisterHandlers() {
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
		FMLCommonHandler.instance().bus().register(new CommonEventHandler());
	}
}
