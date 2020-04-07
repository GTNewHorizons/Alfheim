package alfheim.client.render.entity

import alexsocol.asjlib.glScaled
import alexsocol.asjlib.render.*
import alfheim.api.lib.*
import alfheim.common.spell.tech.SpellGravityTrap
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11.*
import java.util.*
import kotlin.math.min

object RenderEntityGravityTrap: Render() {
	
	internal val rand = Random()
	
	init {
		shadowSize = 0f
	}
	
	override fun getEntityTexture(p_110775_1_: Entity) = null
	
	override fun doRender(e: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTick: Float) {
		glPushMatrix()
		glTranslated(x, y + 0.01, z)
		
		val charge = min(20f, e.ticksExisted + partialTick)
		
		rand.setSeed(e.uniqueID.mostSignificantBits)
		
		val s = (charge / 20 + min(1f, (e.ticksExisted + partialTick) * 0.2f)) / 2 * SpellGravityTrap.radius
		glScaled(s)
		
		glRotatef(charge * 9f + (e.ticksExisted + partialTick) * 0.5f + rand.nextFloat() * 360f, 0f, 1f, 0f)
		
		so.addTranslation()
		
		glPopMatrix()
	}
	
	val so: ShadedObject = object: ShadedObject(LibShaderIDs.idGravity, RenderPostShaders.nextAvailableRenderObjectMaterialID, LibResourceLocations.gravity) {
		
		override fun preRender() {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
			glDisable(GL_CULL_FACE)
			glShadeModel(GL_SMOOTH)
			
			// glColor4d(1, 1, 1, chargeMul);
		}
		
		override fun drawMesh() {
			val tes = Tessellator.instance
			tes.startDrawingQuads()
			tes.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 0.0)
			tes.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 1.0)
			tes.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0)
			tes.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 0.0)
			tes.draw()
		}
		
		override fun postRender() {
			glShadeModel(GL_FLAT)
		}
	}.also { RenderPostShaders.registerShadedObject(it) }
}