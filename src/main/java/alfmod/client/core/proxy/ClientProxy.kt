package alfmod.client.core.proxy

import alexsocol.asjlib.ASJReflectionHelper
import alfheim.client.model.entity.ModelEntityFlugel
import alfheim.client.render.entity.RenderEntityFlugel
import alfheim.common.core.handler.AlfheimConfigHandler
import alfmod.client.core.handler.EventHandlerClient
import alfmod.client.gui.GUIBanner
import alfmod.client.render.entity.*
import alfmod.client.render.item.RenderItemSnowSword
import alfmod.common.core.handler.HELLISH_VACATION
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
		
		RenderingRegistry.registerEntityRenderingHandler(EntityMuspellsun::class.java, RenderEntityMuspellsun)
		RenderingRegistry.registerEntityRenderingHandler(EntityRollingMelon::class.java, RenderEntityRollingMelon)
		
		if (!AlfheimConfigHandler.minimalGraphics)
			MinecraftForgeClient.registerItemRenderer(AlfheimModularItems.snowSword, RenderItemSnowSword)
		
		if (HELLISH_VACATION) {
			(RenderEntityFlugel.mainModel as ModelEntityFlugel).apply {
				rightglove.showModel = false
				leftglove.showModel = false
				rightboot.showModel = false
				leftboot.showModel = false
				chest.showModel = false
				
				ASJReflectionHelper.setFinalValue(this, null, "model1")
			}
		}
	}
}