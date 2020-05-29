package alfheim.client.render.entity

import alexsocol.asjlib.mc
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

object RenderEntityDriftingMine: Render() {
	
	val model = if (AlfheimConfigHandler.minimalGraphics) null else AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/mine.obj"))
	
	init {
		shadowSize = 0f
	}
	
	override fun getEntityTexture(entity: Entity) = LibResourceLocations.mine2
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTick: Float) {
		glPushMatrix()
		glTranslated(x, y + 0.5, z)
		glRotated((mc.theWorld.totalWorldTime + entity.ticksExisted + mc.timer.renderPartialTicks) * 0.5, 1.0, 1.0, 1.0)
		if (model == null) {
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			glTranslated(-0.5, -0.5, 0.03125)
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.mine.maxU, ItemElvenResource.mine.minV, ItemElvenResource.mine.minU, ItemElvenResource.mine.maxV, ItemElvenResource.mine.iconWidth, ItemElvenResource.mine.iconHeight, 1f / 16f)
			
			glRotated(90.0, 0.0, 1.0, 0.0)
			glTranslated(-0.5, 0.0, 0.5 + 0.0625)
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.mine.maxU, ItemElvenResource.mine.minV, ItemElvenResource.mine.minU, ItemElvenResource.mine.maxV, ItemElvenResource.mine.iconWidth, ItemElvenResource.mine.iconHeight, 1f / 16f)
			
			glRotated(90.0, 1.0, 0.0, 0.0)
			glTranslated(0.0, -0.5, -0.5 + 0.0625)
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.mine.maxU, ItemElvenResource.mine.minV, ItemElvenResource.mine.minU, ItemElvenResource.mine.maxV, ItemElvenResource.mine.iconWidth, ItemElvenResource.mine.iconHeight, 1f / 16f)
		} else {
			mc.renderEngine.bindTexture(LibResourceLocations.mine1)
			model.renderPart("insphere")
			mc.renderEngine.bindTexture(LibResourceLocations.mine2)
			model.renderPart("outsphere")
			mc.renderEngine.bindTexture(LibResourceLocations.mine3)
			model.renderPart("spikes")
		}
		glPopMatrix()
	}
}