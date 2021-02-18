package alfheim.client.render.entity

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper.discard
import alexsocol.asjlib.render.ASJRenderHelper.glColor1u
import alexsocol.asjlib.render.ASJRenderHelper.interpolateColor
import alexsocol.asjlib.render.ASJRenderHelper.setBlend
import alexsocol.asjlib.render.ASJRenderHelper.setGlow
import alexsocol.asjlib.render.ASJRenderHelper.setTwoside
import alfheim.api.lib.LibResourceLocations
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.client.core.helper.ShaderHelper
import java.awt.Color
import java.util.*
import kotlin.math.*

object RenderEntityGleipnir: Render() {
	
	private val rand = Random()
	private val oper = Vector3()
	private val vec2d = Vector3()
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, ticks: Float) {
		val tes = Tessellator.instance
		rand.setSeed(entity.uniqueID.mostSignificantBits)
		
		glPushMatrix()
		glTranslated(x, y + rand.nextDouble() * 0.0001, z)
		
		val life = entity.ticksExisted + ticks // change this for speed
		
		if (life > 10f) {
			setTwoside()
			setGlow()
			glColor1u(interpolateColor(Color(0xFFD400).rgb, Color(0xFFD400).brighter().brighter().rgb, sin(ClientTickHandler.ticksInGame / 20.0) * 0.5 + 0.5).rgb)
			
			val w = 0.1
			
			for (i in 1..64) {
				val f = min(10.0, (life - 10) * (rand.nextDouble() + 0.5)) * 0.1
				var c: Double
				
				// start pos, above
				val (xs, ys, zs) = oper.set(rand.nextDouble() * 12 - 6, if (i > 32) 10 else 0, rand.nextDouble() * 12 - 6)
				// end pos, below
				val (xe, ye, ze) = oper.set(rand.nextDouble() * 12 - 6, if (i > 32) 0 else 10, rand.nextDouble() * 12 - 6)
				// current pos
				val (xc, yc, zc) = oper.sub(xs, ys, zs).mul(f)
				
				c = (if (i > 32) cos(vec2d.set(0, ys, zs).sub(0, ye, ze).angle(Vector3.oZ)) else cos(vec2d.set(0, ye, ze).sub(0, ys, zs).angle(Vector3.oZ))) / 10
				
				mc.renderEngine.bindTexture(LibResourceLocations.gleipnir1)
				tes.startDrawingQuads()
				tes.addVertexWithUV(xs + xc, ys + yc + c, zs + zc - w, 0.0, 0.0)
				tes.addVertexWithUV(xs + xc, ys + yc - c, zs + zc + w, 0.0, 1.0)
				tes.addVertexWithUV(xs, ys - c, zs + w, 1.0 * f, 1.0)
				tes.addVertexWithUV(xs, ys + c, zs - w, 1.0 * f, 0.0)
				tes.draw()
				
				c = (if (i > 32) cos(vec2d.set(xs, ys, 0).sub(xe, ye, 0).angle(Vector3.oX)) else cos(vec2d.set(xe, ye, 0).sub(xs, ys, 0).angle(Vector3.oX))) / 10
				
				mc.renderEngine.bindTexture(LibResourceLocations.gleipnir2)
				tes.startDrawingQuads()
				tes.addVertexWithUV(xs + xc - w, ys + yc + c, zs + zc, 0.0, 0.0)
				tes.addVertexWithUV(xs + xc + w, ys + yc - c, zs + zc, 0.0, 1.0)
				tes.addVertexWithUV(xs + w, ys - c, zs, 1.0 * f, 1.0)
				tes.addVertexWithUV(xs - w, ys + c, zs, 1.0 * f, 0.0)
				tes.draw()
			}
			
			glColor4f(1f, 1f, 1f, 1f)
		}
		
		// circles
		setBlend()
		ShaderHelper.useShader(ShaderHelper.halo)
		
		mc.renderEngine.bindTexture(LibResourceLocations.babylon)
		
		val charge = min(10f, life)
		var s = charge / 10f
		s += min(1f, (life) * 0.2f)
		s /= 2f
		glScalef(s)
		
		for (i in 0..1) {
			glPushMatrix()
			glRotatef((charge * 9f + (entity.ticksExisted + ticks) * 0.5f + rand.nextFloat() * 360f) * if (i == 0) 1 else -1, 0f, 1f, 0f)
			tes.startDrawingQuads()
			tes.addVertexWithUV(-11.0, i * 9.9 + 0.1, -11.0, 0.0, 0.0)
			tes.addVertexWithUV(-11.0, i * 9.9 + 0.1, +11.0, 0.0, 1.0)
			tes.addVertexWithUV(+11.0, i * 9.9 + 0.1, +11.0, 1.0, 1.0)
			tes.addVertexWithUV(+11.0, i * 9.9 + 0.1, -11.0, 1.0, 0.0)
			tes.draw()
			glPopMatrix()
		}
		
		ShaderHelper.releaseShader()
		discard()
		glPopMatrix()
	}
	
	override fun getEntityTexture(entity: Entity?) = null // LibResourceLocations.gleipnir
}
