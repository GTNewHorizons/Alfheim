package alfmod.client.core.proxy

import alfmod.client.core.handler.EventHandlerClient
import alfmod.client.gui.GUIBanner
import alfmod.client.render.entity.*
import alfmod.common.core.proxy.CommonProxy
import alfmod.common.entity.EntitySniceBall
import alfmod.common.entity.boss.EntityDedMoroz
import cpw.mods.fml.client.registry.RenderingRegistry

class ClientProxy: CommonProxy() {
	
	override fun preInit() {
		super.preInit()
		
		GUIBanner
		EventHandlerClient
	}
	
	override fun postInit() {
		super.postInit()
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDedMoroz::class.java, RenderEntityDedMoroz())
		RenderingRegistry.registerEntityRenderingHandler(EntitySniceBall::class.java, RenderEntitySniceBall())
	}
}