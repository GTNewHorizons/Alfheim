package alfheim.client.render.entity

import org.lwjgl.opengl.GL11.*

import alfheim.api.ModInfo
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.item.material.ItemElvenResource
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom

class RenderEntityWindBlade: Render() {
	init {
		shadowSize = 0.0f
	}
	
	override fun getEntityTexture(entity: Entity): ResourceLocation {
		return LibResourceLocations.wind
	}
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTick: Float) {
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glAlphaFunc(GL_GREATER, 0.003921569f)
		glTranslated(x, y + 0.05, z)
		glRotated(((Minecraft.getMinecraft().theWorld.totalWorldTime + entity.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) * 5).toDouble(), 0.0, 1.0, 0.0)
		if (AlfheimConfig.minimalGraphics) {
			glScaled(3.0, 0.5, 3.0)
			glRotated(90.0, 1.0, 0.0, 0.0)
			glTranslated(-0.5, -0.5, 0.03125)
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.wind!!.maxU, ItemElvenResource.wind!!.minV, ItemElvenResource.wind!!.minU, ItemElvenResource.wind!!.maxV, ItemElvenResource.wind!!.iconWidth, ItemElvenResource.wind!!.iconHeight, 1f / 16f)
		} else {
			glScaled(1.0, 0.1, 1.0)
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.wind)
			model.renderAll()
		}
		glDisable(GL_BLEND)
		glPopMatrix()
	}
	
	companion object {
		
		val model = AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/tor.obj"))
	}
}