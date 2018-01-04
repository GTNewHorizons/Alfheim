package alfheim.common.proxy;

import alfheim.AlfheimCore;
import alfheim.common.event.CommonEventHandler;
import alfheim.common.registry.AlfheimAchievements;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.registry.AlfheimItems;
import alfheim.common.registry.AlfheimRecipes;
import alfheim.common.registry.AlfheimRegistry;
import alfheim.common.registry.lexicon.AlfheimCategory;
import alfheim.common.utils.ElvenStoryModModifiers;
import alfheim.common.world.dim.DimensionUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit() {
    	AlfheimBlocks.init();
    	AlfheimItems.init();
		AlfheimRecipes.init();
		AlfheimRegistry.preInit();
    	AlfheimCategory.init();
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
