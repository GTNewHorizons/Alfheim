package alfheim.client.core.proxy;

import org.lwjgl.input.Keyboard;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.client.core.utils.AlfheimBotaniaModifiers;
import alfheim.client.event.ClientEventHandler;
import alfheim.client.gui.*;
import alfheim.client.lib.LibRenderIDs;
import alfheim.client.model.entity.ModelEntityElf;
import alfheim.client.render.block.*;
import alfheim.client.render.entity.*;
import alfheim.common.block.tile.*;
import alfheim.common.core.proxy.CommonProxy;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.entity.*;
import cpw.mods.fml.client.registry.*;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.common.item.ModItems;

public class ClientProxy extends CommonProxy {
	
	public static final KeyBinding keyFlight = new KeyBinding("key.flight.desc", Keyboard.KEY_F, "key.categories.movement");
	
	@Override
	public void registerRenderThings() {
		LibRenderIDs.idAnyavil = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idPylon = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idTransferer = RenderingRegistry.getNextAvailableRenderId();

		RenderingRegistry.registerBlockHandler(LibRenderIDs.idAnyavil, new RenderBlockAnyavil());
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idPylon, new RenderBlockAlfheimPylons());
		RenderingRegistry.registerBlockHandler(LibRenderIDs.idTransferer, new RenderBlockTransferer());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfheimPortal.class, new RenderTileAlfheimPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfheimPylons.class, new RenderTileAlfheimPylons());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnyavil.class, new RenderTileAnyavil());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTradePortal.class, new RenderTileTradePortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTransferer.class, new RenderTileTransferer());
		
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
		if (AlfheimConfig.numericalMana) MinecraftForge.EVENT_BUS.register(new GUIManaTester(Minecraft.getMinecraft()));
		if (AlfheimCore.enableElvenStory) {
			ASJUtilities.log("Registering ES GUIs");
			MinecraftForge.EVENT_BUS.register(new GUIRace(Minecraft.getMinecraft()));
			if (AlfheimConfig.prolongDeathScreen) MinecraftForge.EVENT_BUS.register(new GUIDeathTimer(Minecraft.getMinecraft()));
		}
	}
	
	@Override
	public void postInit() {
		super.postInit();
		AlfheimBotaniaModifiers.postInit();
	}
}