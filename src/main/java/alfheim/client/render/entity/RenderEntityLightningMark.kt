package alfheim.client.render.entity

import alexsocol.asjlib.render.*
import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.util.glScalef
import alfheim.common.entity.EntityLightningMark
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.helper.ShaderHelper
import java.util.*
import kotlin.math.min

object RenderEntityLightningMark: Render() {
	
	internal val rand = Random()
	
	init {
		shadowSize = 0f
	}
	
	override fun getEntityTexture(entity: Entity) = null
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTick: Float) {
		val mark = entity as EntityLightningMark
		glPushMatrix()
		glTranslated(x, y + 0.01, z)
		
		val live = mark.timer / 2f
		val charge = min(10f, live + partialTick)
		val chargeMul = charge / 10f
		
		
		rand.setSeed(mark.uniqueID.mostSignificantBits)
		
		var s = chargeMul
		s += min(1f, (live + partialTick) * 0.2f)
		s /= 2f
		glScalef(s)
		
		glRotatef(charge * 9f + (mark.ticksExisted + partialTick) * 0.5f + rand.nextFloat() * 360f, 0f, 1f, 0f)
		
		so.addTranslation()
		
		glPopMatrix()
	}
	
	val so: ShadedObject = object: ShadedObject(ShaderHelper.halo, RenderPostShaders.nextAvailableRenderObjectMaterialID, LibResourceLocations.mark) {
		
		override fun preRender() {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
			glDisable(GL_CULL_FACE)
			glShadeModel(GL_SMOOTH)
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
			glEnable(GL_TEXTURE_2D)
			glShadeModel(GL_FLAT)
			glDisable(GL_BLEND)
		}
		
	}.also { RenderPostShaders.registerShadedObject(it) }
}