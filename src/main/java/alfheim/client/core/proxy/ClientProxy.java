package alfheim.client.core.proxy;

import org.lwjgl.input.Keyboard;

import alfheim.AlfheimCore;
import alfheim.client.event.ClientEventHandler;
import alfheim.client.gui.*;
import alfheim.client.model.entity.ModelEntityElf;
import alfheim.client.render.block.*;
import alfheim.client.render.entity.*;
import alfheim.common.block.tile.*;
import alfheim.common.core.proxy.CommonProxy;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.entity.*;
import cpw.mods.fml.client.registry.*;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.client.lib.LibRenderIDs;

public class ClientProxy extends CommonProxy {
	
	public static final KeyBinding keyFlight = new KeyBinding("key.flight.desc", Keyboard.KEY_F, "key.categories.movement");
	public static final int idPylon = RenderingRegistry.getNextAvailableRenderId();
	
	@Override
	public void registerRenderThings() {
		RenderingRegistry.registerBlockHandler(idPylon, new RenderBlockElvenPylon());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfheimPortal.class, new RenderTileAlfheimPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileElvenPylon.class, new RenderTileElvenPylon());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTradePortal.class, new RenderTileTradePortal());
		
		if (AlfheimCore.enableElvenStory) ClientRegistry.registerKeyBinding(keyFlight);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityElf.class, new RenderEntityElf(new ModelEntityElf(), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityAlfheimPixie.class, new RenderEntityAlfheimPixie());
	}

	@Override
	public void registerKeyBinds() {}

	@Override
	public void initializeAndRegisterHandlers() {
		super.initializeAndRegisterHandlers();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		FMLCommonHandler.instance().bus().register(new ClientEventHandler());
		if (AlfheimCore.enableElvenStory) {
			MinecraftForge.EVENT_BUS.register(new GUIRace(Minecraft.getMinecraft()));
			if (AlfheimConfig.prolongDeathScreen) MinecraftForge.EVENT_BUS.register(new GUIDeathTimer(Minecraft.getMinecraft()));
		}
	}
}