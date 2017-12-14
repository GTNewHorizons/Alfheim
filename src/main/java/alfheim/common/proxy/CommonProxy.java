package alfheim.common.proxy;

import alfheim.common.event.CommonEventHandler;
import alfheim.common.registry.AlfheimAchievements;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.registry.AlfheimItems;
import alfheim.common.registry.AlfheimRecipes;
import alfheim.common.registry.AlfheimRegistry;
import alfheim.common.utils.DimensionUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit() {
    	AlfheimBlocks.init();
    	AlfheimItems.init();
		AlfheimRecipes.init();
		AlfheimRegistry.preInit();
	}

	public void registerRenderThings() {}

	public void registerKeyBinds() {}

	public void init() {
		AlfheimRegistry.init();
		DimensionUtil.init();
	}
	
	public void postInit() {
		AlfheimAchievements.init();
	}
	
	public void initializeAndRegisterHandlers() {
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
		FMLCommonHandler.instance().bus().register(new CommonEventHandler());
	}

	/**
	 * Returns a side-appropriate EntityPlayer for use during message handling
	 * @author coolAlias
	 */
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}
}
