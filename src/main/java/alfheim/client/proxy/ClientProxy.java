package alfheim.client.proxy;

import alfheim.common.blocks.render.AlfheimPortalRender;
import alfheim.common.blocks.tileentity.AlfheimPortalTileEntity;
import alfheim.common.entity.ElfEntity;
import alfheim.common.entity.model.ElfModel;
import alfheim.common.entity.render.ElfRender;
import alfheim.common.proxy.CommonProxy;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderThings() {
		RenderingRegistry.registerEntityRenderingHandler(ElfEntity.class, new ElfRender(new ElfModel(), 0.25F));
		ClientRegistry.bindTileEntitySpecialRenderer(AlfheimPortalTileEntity.class, new AlfheimPortalRender());
	}

	@Override
	public void registerKeyBinds() {}

	@Override
	public void initializeAndRegisterHandlers() {
		super.initializeAndRegisterHandlers();
	}
}