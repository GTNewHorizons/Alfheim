package alfheim.client.render.entity

import org.lwjgl.opengl.GL11.*

import alexsocol.asjlib.render.RenderPostShaders
import alexsocol.asjlib.render.ShadedObject
import alfheim.api.lib.LibResourceLocations
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import vazkii.botania.client.core.helper.ShaderHelper

class RenderEntityFlugel(model: ModelBase, shadowSize: Float): RenderLiving(model, shadowSize) {
	
	init {
		RenderPostShaders.registerShadedObject(so)
	}
	
	public override fun getEntityTexture(par1Entity: Entity): ResourceLocation {
		return LibResourceLocations.jibril
	}
	
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