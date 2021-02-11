package alfheim.client.render.entity

import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.*
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelEntityFlugel
import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import vazkii.botania.client.core.handler.BossBarHandler
import vazkii.botania.client.core.helper.ShaderHelper

object RenderEntityFlugel: RenderLiving(ModelEntityFlugel(), 0.25f) {
	
	override fun getEntityTexture(entity: Entity?) =
		if (entity is EntityFlugel) getEntityTexture(entity) else LibResourceLocations.jibril
	
	fun getEntityTexture(flugel: EntityFlugel): ResourceLocation {
		if (Vector3.fromEntity(flugel) != Vector3.zero) BossBarHandler.setCurrentBoss(flugel)
		
		return if (flugel.isUltraMode) LibResourceLocations.jibrilDark else LibResourceLocations.jibril
	}
	
	val so: ShadedObject = object: ShadedObject(ShaderHelper.halo, RenderPostShaders.nextAvailableRenderObjectMaterialID, LibResourceLocations.halo) {
		
		var lastX = 0f
		var lastY = 0f
		
		override fun preRender() {
			GL11.glEnable(GL11.GL_BLEND)
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
			GL11.glShadeModel(GL11.GL_SMOOTH)
			
			lastX = OpenGlHelper.lastBrightnessX
			lastY = OpenGlHelper.lastBrightnessY
			
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
			GL11.glDisable(GL11.GL_LIGHTING)
			GL11.glDisable(GL11.GL_CULL_FACE)
			GL11.glColor4f(1f, 1f, 1f, 1f)
		}
		
		override fun drawMesh() {
			val tes = Tessellator.instance
			tes.startDrawingQuads()
			tes.addVertexWithUV(-0.75, 0.0, -0.75, 0.0, 0.0)
			tes.addVertexWithUV(-0.75, 0.0, 0.75, 0.0, 1.0)
			tes.addVertexWithUV(0.75, 0.0, 0.75, 1.0, 1.0)
			tes.addVertexWithUV(0.75, 0.0, -0.75, 1.0, 0.0)
			tes.draw()
		}
		
		override fun postRender() {
			GL11.glEnable(GL11.GL_CULL_FACE)
			//glEnable(GL_LIGHTING); breaks some other stuff, urgh -_-
			GL11.glShadeModel(GL11.GL_FLAT)
			GL11.glDisable(GL11.GL_BLEND)
			
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
		}
	}.also { RenderPostShaders.registerShadedObject(it) }
}