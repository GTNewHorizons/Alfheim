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

class RenderEntityHarp: Render() {
	init {
		shadowSize = 0.0f
	}
	
	override fun getEntityTexture(entity: Entity): ResourceLocation {
		return LibResourceLocations.harp
	}
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTick: Float) {
		glPushMatrix()
		glTranslated(x, y + 0.2 + Math.sin((Minecraft.getMinecraft().theWorld.totalWorldTime.toFloat() + entity.ticksExisted.toFloat() + Minecraft.getMinecraft().timer.renderPartialTicks) / 50.0) / 10.0, z)
		glRotated((Minecraft.getMinecraft().theWorld.totalWorldTime + entity.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks) * 0.5, 0.0, 1.0, 0.0)
		
		if (AlfheimConfig.minimalGraphics) {
			glTranslated(-0.5, 0.0, 0.0)
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.harp!!.maxU, ItemElvenResource.harp!!.minV, ItemElvenResource.harp!!.minU, ItemElvenResource.harp!!.maxV, ItemElvenResource.harp!!.iconWidth, ItemElvenResource.harp!!.iconHeight, 1f / 16f)
			glTranslated(0.5, 0.0, 0.0)
		} else {
			Minecraft.getMinecraft().renderEngine.bindTexture(getEntityTexture(entity))
			model.renderAll()
		}
		glPopMatrix()
	}
	
	companion object {
		
		val model = AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/Arfa.obj"))
	}
}