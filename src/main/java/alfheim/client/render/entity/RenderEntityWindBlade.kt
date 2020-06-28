package alfheim.client.render.entity

import alexsocol.asjlib.mc
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.material.ItemElvenResource
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*

object RenderEntityWindBlade: Render() {
	
	val model = if (AlfheimConfigHandler.minimalGraphics) null else AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/tor.obj"))
	
	init {
		shadowSize = 0f
	}
	
	override fun getEntityTexture(entity: Entity) = LibResourceLocations.wind
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTick: Float) {
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glAlphaFunc(GL_GREATER, 1/255f)
		glTranslated(x, y + 0.05, z)
		val time = mc.theWorld.totalWorldTime + entity.ticksExisted - 1.0
		glRotated(ASJRenderHelper.interpolate(time, time + 1), 0.0, 1.0, 0.0)
		if (model == null) {
			glScaled(3.0, 0.5, 3.0)
			glRotated(90.0, 1.0, 0.0, 0.0)
			glTranslated(-0.5, -0.5, 0.03125)
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.wind.maxU, ItemElvenResource.wind.minV, ItemElvenResource.wind.minU, ItemElvenResource.wind.maxV, ItemElvenResource.wind.iconWidth, ItemElvenResource.wind.iconHeight, 1f / 16f)
		} else {
			glScaled(1.0, 0.1, 1.0)
			mc.renderEngine.bindTexture(LibResourceLocations.wind)
			model.renderAll()
		}
		glDisable(GL_BLEND)
		glPopMatrix()
	}
}