package alfmod.client.core.proxy

import alfheim.common.core.handler.AlfheimConfigHandler
import alfmod.client.core.handler.EventHandlerClient
import alfmod.client.gui.GUIBanner
import alfmod.client.render.entity.*
import alfmod.client.render.item.RenderItemSnowSword
import alfmod.common.core.proxy.CommonProxy
import alfmod.common.entity.*
import alfmod.common.entity.boss.EntityDedMoroz
import alfmod.common.item.AlfheimModularItems
import cpw.mods.fml.client.registry.RenderingRegistry
import net.minecraftforge.client.MinecraftForgeClient

class ClientProxy: CommonProxy() {
	
	override fun preInit() {
		super.preInit()
		
		GUIBanner
		EventHandlerClient
	}
	
	override fun postInit() {
		super.postInit()
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDedMoroz::class.java, RenderEntityDedMoroz)
		RenderingRegistry.registerEntityRenderingHandler(EntitySniceBall::class.java, RenderEntitySniceBall)
		RenderingRegistry.registerEntityRenderingHandler(EntitySnowSprite::class.java, RenderEntitySnowSprite)
		
		if (!AlfheimConfigHandler.minimalGraphics)
			MinecraftForgeClient.registerItemRenderer(AlfheimModularItems.snowSword, RenderItemSnowSword)
	}
}