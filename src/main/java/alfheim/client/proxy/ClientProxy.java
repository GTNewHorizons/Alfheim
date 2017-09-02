package alfheim.client.proxy;

import alfheim.common.entity.ElfEntity;
import alfheim.common.entity.model.ElfModel;
import alfheim.common.entity.render.ElfRender;
import alfheim.common.proxy.CommonProxy;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderThings() {
		RenderingRegistry.registerEntityRenderingHandler(ElfEntity.class, new ElfRender(new ElfModel(), 0.25F));
	}

	@Override
	public void registerKeyBinds() {}
}