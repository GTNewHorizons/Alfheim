package alfheim.client.core.proxy;

import org.lwjgl.input.Keyboard;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.client.core.utils.AlfheimBotaniaModifiers;
import alfheim.client.event.ClientEventHandler;
import alfheim.client.gui.GUIDeathTimer;
import alfheim.client.gui.GUIRace;
import alfheim.client.lib.LibRenderIDs;
import alfheim.client.model.entity.ModelEntityElf;
import alfheim.client.model.entity.ModelEntityFlugel;
import alfheim.client.render.block.RenderBlockAlfheimPylons;
import alfheim.client.render.block.RenderBlockAnyavil;
import alfheim.client.render.block.RenderBlockTransferer;
import alfheim.client.render.block.RenderTileAlfheimPortal;
import alfheim.client.render.block.RenderTileAlfheimPylons;
import alfheim.client.render.block.RenderTileAnyavil;
import alfheim.client.render.block.RenderTileTradePortal;
import alfheim.client.render.block.RenderTileTransferer;
import alfheim.client.render.entity.RenderEntityAlfheimPixie;
import alfheim.client.render.entity.RenderEntityElf;
import alfheim.client.render.entity.RenderEntityFlugel;
import alfheim.client.render.entity.RenderEntityLightningMark;
import alfheim.common.block.tile.TileAlfheimPortal;
import alfheim.common.block.tile.TileAlfheimPylons;
import alfheim.common.block.tile.TileAnyavil;
import alfheim.common.block.tile.TileTradePortal;
import alfheim.common.block.tile.TileTransferer;
import alfheim.common.core.proxy.CommonProxy;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EntityElf;
import alfheim.common.entity.EntityLightningMark;
import alfheim.common.entity.boss.EntityFlugel;
import alfheim.common.network.AttributeMessage;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

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
		
		RenderingRegistry.registerEntityRenderingHandler(EntityAlfheimPixie.class, new RenderEntityAlfheimPixie());
		RenderingRegistry.registerEntityRenderingHandler(EntityElf.class, new RenderEntityElf(new ModelEntityElf(), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityFlugel.class, new RenderEntityFlugel(new ModelEntityFlugel(), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningMark.class, new RenderEntityLightningMark());
	}

	@Override
	public void registerKeyBinds() {}

	@Override
	public void initializeAndRegisterHandlers() {
		super.initializeAndRegisterHandlers();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		FMLCommonHandler.instance().bus().register(new ClientEventHandler());
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
	
	@Override
	public void registerPackets() {
		super.registerPackets();
		AlfheimCore.network.registerMessage(AttributeMessage.Handler.class, AttributeMessage.class, 1, Side.CLIENT);
	}
}