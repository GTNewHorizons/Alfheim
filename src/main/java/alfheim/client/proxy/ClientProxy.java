package alfheim.client.proxy;

import org.lwjgl.input.Keyboard;

import alfheim.AlfheimCore;
import alfheim.client.blocks.render.AlfheimPortalRender;
import alfheim.client.entity.model.ModelEntityElf;
import alfheim.client.entity.render.RenderAlfheimPixie;
import alfheim.client.entity.render.RenderEntityElf;
import alfheim.client.event.ClientEventHandler;
import alfheim.client.gui.DeathTimerGUI;
import alfheim.client.gui.RaceGUI;
import alfheim.common.blocks.tileentity.AlfheimPortalTileEntity;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EntityElf;
import alfheim.common.proxy.CommonProxy;
import alfheim.common.utils.AlfheimConfig;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	
	public final static KeyBinding keyFlight = new KeyBinding("key.flight.desc", Keyboard.KEY_F, "key.categories.movement");


	@Override
	public void registerRenderThings() {
		ClientRegistry.bindTileEntitySpecialRenderer(AlfheimPortalTileEntity.class, new AlfheimPortalRender());
		
		if (AlfheimCore.enableElvenStory) ClientRegistry.registerKeyBinding(keyFlight);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityElf.class, new RenderEntityElf(new ModelEntityElf(), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityAlfheimPixie.class, new RenderAlfheimPixie());
	}

	@Override
	public void registerKeyBinds() {}

	@Override
	public void initializeAndRegisterHandlers() {
		super.initializeAndRegisterHandlers();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		FMLCommonHandler.instance().bus().register(new ClientEventHandler());
		if (AlfheimCore.enableElvenStory) {
			MinecraftForge.EVENT_BUS.register(new RaceGUI(Minecraft.getMinecraft()));
			if (AlfheimConfig.prolongDeathScreen) MinecraftForge.EVENT_BUS.register(new DeathTimerGUI(Minecraft.getMinecraft()));
		}
	}
}