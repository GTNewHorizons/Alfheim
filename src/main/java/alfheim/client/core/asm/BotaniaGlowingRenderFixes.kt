package alfheim.client.core.asm

import gloomyfolken.hooklib.asm.Hook
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderPlayerEvent
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.client.core.handler.BaubleRenderHandler
import vazkii.botania.client.render.entity.RenderPixie
import vazkii.botania.common.item.equipment.bauble.*

object BotaniaGlowingRenderFixes {
	
	var lastX = 0f
	var lastY = 0f
	
	@JvmStatic
	@Hook(targetMethod = "renderManaTablet")
	fun renderManaTabletPre(renderer: BaubleRenderHandler, event: RenderPlayerEvent) {
		lastX = OpenGlHelper.lastBrightnessX
		lastY = OpenGlHelper.lastBrightnessY
	}
	
	@JvmStatic
	@Hook(targetMethod = "renderManaTablet", injectOnExit = true)
	fun renderManaTabletPost(renderer: BaubleRenderHandler, event: RenderPlayerEvent) {
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
	}
	
	@JvmStatic
	@Hook(targetMethod = "onPlayerBaubleRender")
	fun onPlayerBaubleRenderPre(item: ItemBloodPendant, stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		lastX = OpenGlHelper.lastBrightnessX
		lastY = OpenGlHelper.lastBrightnessY
	}
	
	@JvmStatic
	@Hook(targetMethod = "onPlayerBaubleRender", injectOnExit = true)
	fun onPlayerBaubleRenderPost(item: ItemBloodPendant, stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
	}
	
	@JvmStatic
	@Hook(targetMethod = "doRender")
	fun doRenderPre(render: RenderPixie, entity: Entity, x: Double, y: Double, z: Double, f1: Float, f2: Float) {
		lastX = OpenGlHelper.lastBrightnessX
		lastY = OpenGlHelper.lastBrightnessY
	}
	
	@JvmStatic
	@Hook(targetMethod = "doRender", injectOnExit = true)
	fun doRenderPost(render: RenderPixie, entity: Entity, x: Double, y: Double, z: Double, f1: Float, f2: Float) {
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
	}
	
	@JvmStatic
	@Hook(targetMethod = "onPlayerBaubleRender")
	fun onPlayerBaubleRenderPre(item: ItemFlightTiara, stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		lastX = OpenGlHelper.lastBrightnessX
		lastY = OpenGlHelper.lastBrightnessY
	}
	
	@JvmStatic
	@Hook(targetMethod = "onPlayerBaubleRender", injectOnExit = true)
	fun onPlayerBaubleRenderPost(item: ItemFlightTiara, stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
	}
}
