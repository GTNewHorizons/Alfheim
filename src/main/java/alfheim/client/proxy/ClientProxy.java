package alfheim.client.proxy;

import org.lwjgl.input.Keyboard;

import alfheim.AlfheimCore;
import alfheim.client.blocks.render.AlfheimPortalRender;
import alfheim.client.blocks.render.TestBlockRender;
import alfheim.client.entity.model.ModelEntityElf;
import alfheim.client.entity.render.RenderAlfheimPixie;
import alfheim.client.entity.render.RenderEntityElf;
import alfheim.client.event.ClientEventHandler;
import alfheim.client.gui.RaceGUI;
import alfheim.client.render.ShaderHelperAlfheim;
import alfheim.common.blocks.TestBlock.TileEntityTestBlock;
import alfheim.common.blocks.tileentity.AlfheimPortalTileEntity;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EntityElf;
import alfheim.common.proxy.CommonProxy;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	
	public final static KeyBinding keyFlight = new KeyBinding("key.flight.desc", Keyboard.KEY_F, "key.categories.movement");


	@Override
	public void registerRenderThings() {
		ClientRegistry.bindTileEntitySpecialRenderer(AlfheimPortalTileEntity.class, new AlfheimPortalRender());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTestBlock.class, new TestBlockRender());
		
		if (AlfheimCore.enableElvenStory) ClientRegistry.registerKeyBinding(keyFlight);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityElf.class, new RenderEntityElf(new ModelEntityElf(), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityAlfheimPixie.class, new RenderAlfheimPixie());
		
		ShaderHelperAlfheim.initShaders();
	}

	@Override
	public void registerKeyBinds() {}

	@Override
	public void initializeAndRegisterHandlers() {
		super.initializeAndRegisterHandlers();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		FMLCommonHandler.instance().bus().register(new ClientEventHandler());
		if (AlfheimCore.enableElvenStory) MinecraftForge.EVENT_BUS.register(new RaceGUI(Minecraft.getMinecraft()));
	}
	
	/**
	 * Returns a side-appropriate EntityPlayer for use during message handling
	 * @author coolAlias
	 */
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
}