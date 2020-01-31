package alfheim.client.render.entity

import alfheim.api.lib.LibResourceLocations
import alfheim.common.entity.EntityAlfheimPixie
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.*
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.model.ModelPixie

class RenderEntityAlfheimPixie: RenderLiving(ModelPixie(), 0.25f) {
	init {
		setRenderPassModel(ModelPixie())
		shadowSize = 0f
	}
	
	override fun getEntityTexture(entity: Entity): ResourceLocation {
		return LibResourceLocations.pixie
	}
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
		if (entity.isInvisible) return
		glPushMatrix()
		glTranslated(0.0, -0.25, 0.0)
		super.doRender(entity, x, y, z, yaw, pitch)
		glPopMatrix()
	}
	
	private fun setPixieBrightness(pixie: EntityAlfheimPixie, par2: Int, par3: Float): Int {
		if (par2 != 0)
			return -1
		else {
			bindTexture(getEntityTexture(pixie))
			glEnable(GL_BLEND)
			glDisable(GL_ALPHA_TEST)
			glBlendFunc(GL_ONE, GL_ONE)
			
			if (pixie.isInvisible)
				glDepthMask(false)
			else
				glDepthMask(true)
			
			val c0: Char = 61680.toChar()
			val j = (c0.toInt() % 65536).toFloat()
			val k = (c0.toInt() / 65536).toFloat()
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k)
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glColor4d(1.0, 1.0, 1.0, 1.0)
			return 1
		}
	}
	
	override fun shouldRenderPass(entity: EntityLivingBase, par2: Int, par3: Float): Int {
		return setPixieBrightness(entity as EntityAlfheimPixie, par2, par3)
	}
}
