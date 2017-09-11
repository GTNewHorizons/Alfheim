package alfheim.client.proxy;

import alfheim.client.blocks.render.AlfheimPortalRender;
import alfheim.client.entity.model.ModelEntityElf;
import alfheim.client.entity.render.RenderAlfheimPixie;
import alfheim.client.entity.render.RenderEntityElf;
import alfheim.client.event.ClientEventHandler;
import alfheim.common.blocks.tileentity.AlfheimPortalTileEntity;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EntityElf;
import alfheim.common.proxy.CommonProxy;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.client.render.tile.RenderTilePrism;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderThings() {
		RenderingRegistry.registerEntityRenderingHandler(EntityElf.class, new RenderEntityElf(new ModelEntityElf(), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityAlfheimPixie.class, new RenderAlfheimPixie());
		
		ClientRegistry.bindTileEntitySpecialRenderer(AlfheimPortalTileEntity.class, new AlfheimPortalRender());
	}

	@Override
	public void registerKeyBinds() {}

	@Override
	public void initializeAndRegisterHandlers() {
		super.initializeAndRegisterHandlers();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		FMLCommonHandler.instance().bus().register(new ClientEventHandler());
	}
}