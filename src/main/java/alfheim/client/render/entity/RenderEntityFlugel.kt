package alfheim.client.render.entity

import alexsocol.asjlib.render.*
import alfheim.api.lib.LibResourceLocations
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.helper.ShaderHelper

class RenderEntityFlugel(model: ModelBase, shadowSize: Float): RenderLiving(model, shadowSize) {
	
	init {
		RenderPostShaders.registerShadedObject(so)
	}
	
	override fun getEntityTexture(par1Entity: Entity) = LibResourceLocations.jibril
	
	companion object {
		
		val so: ShadedObject = object: ShadedObject(ShaderHelper.halo, RenderPostShaders.nextAvailableRenderObjectMaterialID, LibResourceLocations.halo) {
			
			override fun preRender() {
				glEnable(GL_BLEND)
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
				glShadeModel(GL_SMOOTH)
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
				glDisable(GL_LIGHTING)
				glDisable(GL_CULL_FACE)
				glColor4f(1f, 1f, 1f, 1f)
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
				glEnable(GL_CULL_FACE)
				//glEnable(GL_LIGHTING); breaks some other stuuf, urgh -_-
				glShadeModel(GL_FLAT)
				glDisable(GL_BLEND)
			}
		}
	}
}