package alfmod.client.render.entity

import alfheim.api.ModInfo
import alfheim.client.core.util.mc
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11.*

object RenderEntitySniceBall: Render() {
	
	val model = AdvancedModelLoader.loadModel(ResourceLocation(ModInfo.MODID, "model/mine.obj"))
	val textureIce = ResourceLocation("textures/blocks/ice.png")
	val textureSnow = ResourceLocation("textures/blocks/snow.png")
	
	init {
		shadowSize = 0.25f
	}
	
	override fun getEntityTexture(entity: Entity) = textureIce
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTick: Float) {
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		glTranslated(x, y + 0.25, z)
		glRotated((mc.theWorld.totalWorldTime + entity.ticksExisted + mc.timer.renderPartialTicks) * 0.5, 1.0, 1.0, 1.0)
		
		glScaled(0.5, 0.5, 0.5)
		
		/*if (AlfheimConfigHandler.minimalGraphics) {
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			glTranslated(-0.5, -0.5, 0.03125)
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.mine.maxU, ItemElvenResource.mine.minV, ItemElvenResource.mine.minU, ItemElvenResource.mine.maxV, ItemElvenResource.mine.iconWidth, ItemElvenResource.mine.iconHeight, 1f / 16f)
			
			glRotated(90.0, 0.0, 1.0, 0.0)
			glTranslated(-0.5, 0.0, 0.5 + 0.0625)
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.mine.maxU, ItemElvenResource.mine.minV, ItemElvenResource.mine.minU, ItemElvenResource.mine.maxV, ItemElvenResource.mine.iconWidth, ItemElvenResource.mine.iconHeight, 1f / 16f)
			
			glRotated(90.0, 1.0, 0.0, 0.0)
			glTranslated(0.0, -0.5, -0.5 + 0.0625)
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.mine.maxU, ItemElvenResource.mine.minV, ItemElvenResource.mine.minU, ItemElvenResource.mine.maxV, ItemElvenResource.mine.iconWidth, ItemElvenResource.mine.iconHeight, 1f / 16f)
		} else {*/
			mc.renderEngine.bindTexture(textureSnow)
			model.renderPart("insphere")
			mc.renderEngine.bindTexture(textureIce)
			model.renderPart("outsphere")
			model.renderPart("spikes")
		//}
		
		glEnable(GL_BLEND)
		glPopMatrix()
	}
}