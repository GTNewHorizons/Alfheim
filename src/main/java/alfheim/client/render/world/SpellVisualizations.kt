package alfheim.client.render.world

import alexsocol.asjlib.render.*
import alfheim.api.lib.LibShaderIDs
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient
import alfheim.client.core.util.mc
import alfheim.common.core.util.*
import alfheim.common.spell.tech.SpellTimeStop
import net.minecraft.client.renderer.Tessellator
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL
import kotlin.math.*

object SpellVisualizations {
	
	val so: ShadedObject
	
	init {
		so = object: ShadedObject(LibShaderIDs.idNoise, RenderPostShaders.nextAvailableRenderObjectMaterialID, null) {
			
			override fun preRender() {
				glEnable(GL_RESCALE_NORMAL)
				
				glDisable(GL_LIGHTING)
				glDisable(GL_TEXTURE_2D)
				glColor4d(0.0, 0.0, 0.0, 1.0)
				
				glEnable(GL_CULL_FACE)
				glCullFace(GL_BACK)
			}
			
			override fun drawMesh() {
				val size = (240 / 3.6 / 16 * SpellTimeStop.radius).F
				renderSphere(Tessellator.instance, size)
			}
			
			override fun postRender() {
				glColor4d(1.0, 1.0, 1.0, 1.0)
				glEnable(GL_TEXTURE_2D)
				glEnable(GL_LIGHTING)
				
				glDisable(GL_RESCALE_NORMAL)
			}
		}
		
		RenderPostShaders.registerShadedObject(so)
	}
	
	fun redSphere(x: Double, y: Double, z: Double) {
		glPushMatrix()
		ASJRenderHelper.interpolatedTranslationReverse(mc.thePlayer)
		val inside = TimeStopSystemClient.inside(mc.thePlayer)
		glTranslated(x, y, z)
		glEnable(GL_RESCALE_NORMAL)
		
		glDisable(GL_LIGHTING)
		glDisable(GL_TEXTURE_2D)
		if (RenderPostShaders.allowShaders)
			glColor4d(0.0, 0.0, 0.0, 1.0)
		else
			glColor4d(0.25, 0.0, 0.0, 1.0)
		val size = (240 / 3.6 / 16 * SpellTimeStop.radius).F
		
		glScaled(0.5, 0.5, 0.5)
		if (RenderPostShaders.allowShaders)
			so.addTranslation()
		else
			renderSphere(Tessellator.instance, size)
		glColorMask(false, true, true, false)
		glCullFace(GL_FRONT)
		if (inside) glDisable(GL_DEPTH_TEST)
		renderSphere(Tessellator.instance, size)
		if (inside) glEnable(GL_DEPTH_TEST)
		glColorMask(true, true, true, true)
		glCullFace(GL_BACK)
		
		glColor4d(1.0, 1.0, 1.0, 1.0)
		glEnable(GL_TEXTURE_2D)
		glEnable(GL_LIGHTING)
		
		glDisable(GL_RESCALE_NORMAL)
		glPopMatrix()
	}
	
	fun negateSphere(s: Double) {
		glPushMatrix()
		ASJRenderHelper.interpolatedTranslation(mc.thePlayer)
		val tes = Tessellator.instance
		
		glEnable(GL_RESCALE_NORMAL)
		glDisable(GL_CULL_FACE)
		glDepthMask(false)
		glDepthFunc(GL_LEQUAL)
		glEnable(GL_BLEND)
		glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR)
		glDisable(GL_TEXTURE_2D)
		glDisable(GL_LIGHTING)
		glColor4d(1.0, 1.0, 1.0, 1.0)
		val size = 240 / 3.6f
		
		glPushMatrix()
		glLoadIdentity()
		val z = -0.1
		tes.startDrawingQuads()
		tes.addVertex(-10.0, -10.0, z)
		tes.addVertex(-10.0, 10.0, z)
		tes.addVertex(10.0, 10.0, z)
		tes.addVertex(10.0, -10.0, z)
		tes.draw()
		glPopMatrix()
		
		glScaled(s, s, s)
		renderSphere(tes, size)
		glScaled(1 / s, 1 / s, 1 / s)
		
		glEnable(GL_LIGHTING)
		glEnable(GL_TEXTURE_2D)
		glDisable(GL_BLEND)
		glDepthMask(true)
		glEnable(GL_CULL_FACE)
		glDisable(GL_RESCALE_NORMAL)
		
		glPopMatrix()
	}
	
	/**
	 * @author thKaguya
	 */
	internal fun renderSphere(tessellator: Tessellator, width: Float) {
		var width = width
		val maxWidth = width / 2f
		val zAngleDivNum = 18
		var angleZ: Double
		val angleSpanZ = PI * 2.0 / zAngleDivNum.D
		val zDivNum = 9
		var zPos = sin(-PI / 2.0) * maxWidth
		var zPosOld = zPos
		var xPos: Float
		var yPos: Float
		var xPos2: Float
		var yPos2: Float
		var xPosOld: Float
		var yPosOld: Float
		var xPos2Old: Float
		var yPos2Old: Float
		var angle = -PI.F / 2f
		val angleSpan = PI.F / zDivNum.F
		angle += angleSpan
		var widthOld = 0f
		for (j in 0 until zDivNum) {
			zPos = sin(angle.D) * maxWidth
			width = cos(angle.D).F * maxWidth
			angleZ = 0.0
			xPosOld = cos(angleZ).F * width
			yPosOld = sin(angleZ).F * width
			xPos2Old = cos(angleZ).F * widthOld
			yPos2Old = sin(angleZ).F * widthOld
			angleZ = angleSpanZ
			for (i in 1..zAngleDivNum) {
				xPos = cos(angleZ).F * width
				yPos = sin(angleZ).F * width
				xPos2 = cos(angleZ).F * widthOld
				yPos2 = sin(angleZ).F * widthOld
				tessellator.startDrawingQuads()
				//tessellator.setColorRGBA_F(1f, 1f, 1f , alpha);
				tessellator.setNormal(0f, 1f, 0f)
				tessellator.addVertexWithUV(xPos.D, yPos.D, zPos, 1.0, 0.0)
				tessellator.addVertexWithUV(xPosOld.D, yPosOld.D, zPos, 0.0, 0.0)
				tessellator.addVertexWithUV(xPos2Old.D, yPos2Old.D, zPosOld, 0.0, 1.0)
				tessellator.addVertexWithUV(xPos2.D, yPos2.D, zPosOld, 1.0, 1.0)
				tessellator.draw()
				xPosOld = xPos
				yPosOld = yPos
				xPos2Old = xPos2
				yPos2Old = yPos2
				angleZ += angleSpanZ
			}
			zPosOld = zPos
			angle += angleSpan
			widthOld = width
		}
	}
	
}
