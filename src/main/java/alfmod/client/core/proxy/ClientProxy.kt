package alfmod.client.core.proxy

import alfmod.client.render.entity.RenderEntityDedMoroz
import alfmod.common.core.proxy.CommonProxy
import alfmod.common.entity.EntityDedMoroz
import cpw.mods.fml.client.registry.RenderingRegistry

class ClientProxy: CommonProxy() {
	
	override fun postInit() {
		super.postInit()
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDedMoroz::class.java, RenderEntityDedMoroz())
	}
}