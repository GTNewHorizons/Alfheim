package alfheim.client.render.entity

import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelButterfly
import alfheim.common.entity.EntityButterfly
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.*
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import java.awt.Color

object RenderEntityButterfly: RenderLiving(ModelButterfly(), 0.25f) {
	
	init {
		setRenderPassModel(ModelButterfly())
		shadowSize = 0f
	}
	
	override fun getEntityTexture(entity: Entity) = LibResourceLocations.butterfly
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
		if (entity.isInvisible) return
		
		val lastX = OpenGlHelper.lastBrightnessX
		val lastY = OpenGlHelper.lastBrightnessY
		
		glEnable(GL_BLEND)
		glAlphaFunc(GL_GREATER, 0f)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		ASJRenderHelper.glColor1u(Color.HSBtoRGB((ClientTickHandler.ticksInGame * entity.entityId) % 360 / 360f, 1f, 1f))
		
		glPushMatrix()
		glTranslatef(0f, -0.25f, 0f)
		super.doRender(entity, x, y, z, yaw, pitch)
		glPopMatrix()
		
		glDisable(GL_BLEND)
		glColor4f(1f, 1f, 1f, 1f)
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
	}
	
	private fun setPixieBrightness(pixie: EntityButterfly, pass: Int, ticks: Float): Int {
		if (pass != 0) return -1
		
		bindTexture(getEntityTexture(pixie))
		
		if (pixie.isInvisible)
			glDepthMask(false)
		else
			glDepthMask(true)
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		return 1
	}
	
	override fun shouldRenderPass(entity: EntityLivingBase, pass: Int, ticks: Float) = setPixieBrightness(entity as EntityButterfly, pass, ticks)
}
