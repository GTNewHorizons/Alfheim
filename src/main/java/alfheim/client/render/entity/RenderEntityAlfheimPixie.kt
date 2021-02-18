package alfheim.client.render.entity

import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.lib.LibResourceLocations
import alfheim.common.entity.EntityAlfheimPixie
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.*
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.model.ModelPixie

object RenderEntityAlfheimPixie: RenderLiving(ModelPixie(), 0.25f) {
	
	init {
		setRenderPassModel(ModelPixie())
		shadowSize = 0f
	}
	
	override fun getEntityTexture(entity: Entity?) = LibResourceLocations.pixie
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
		if (entity.isInvisible) return
		
		glPushMatrix()
		glTranslatef(0f, -0.25f, 0f)
		super.doRender(entity, x, y, z, yaw, pitch)
		glPopMatrix()
		
		ASJRenderHelper.discard()
	}
	
	private fun setPixieBrightness(pixie: EntityAlfheimPixie, pass: Int): Int {
		if (pass != 0) return -1
		
		bindTexture(getEntityTexture(pixie))
		glEnable(GL_BLEND)
		glDisable(GL_ALPHA_TEST)
		glBlendFunc(GL_ONE, GL_ONE)
		
		if (pixie.isInvisible)
			glDepthMask(false)
		else
			glDepthMask(true)
		
		ASJRenderHelper.setGlow()
		glColor4f(1f, 1f, 1f, 1f)
		return 1
	}
	
	override fun shouldRenderPass(entity: EntityLivingBase, pass: Int, ticks: Float) = setPixieBrightness(entity as EntityAlfheimPixie, pass)
}
