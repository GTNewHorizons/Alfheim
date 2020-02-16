package alfmod.client.render.entity

import alfheim.client.core.util.glScaled
import alfmod.AlfheimModularCore
import alfmod.client.render.model.ModelSnowSprite
import alfmod.common.entity.EntitySnowSprite
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.*
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*

object RenderEntitySnowSprite: RenderLiving(ModelSnowSprite(), 0.25f) {
	
	val texture = ResourceLocation(AlfheimModularCore.MODID, "textures/model/entity/SnowSprite.png")
	
	init {
		setRenderPassModel(ModelSnowSprite())
		shadowSize = 0f
	}
	
	override fun getEntityTexture(entity: Entity) = texture
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
		if (entity.isInvisible) return
		
		glPushMatrix()
		glTranslatef(0f, -0.25f, 0f)
		super.doRender(entity, x, y, z, yaw, pitch)
		glPopMatrix()
	}
	
	private fun setPixieBrightness(pixie: EntitySnowSprite, pass: Int, ticks: Float): Int {
		if (pass != 0) return -1
		
		bindTexture(getEntityTexture(pixie))
		glEnable(GL_BLEND)
		glDisable(GL_ALPHA_TEST)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		if (pixie.isInvisible)
			glDepthMask(false)
		else
			glDepthMask(true)
		
		glTranslatef(0f, -0.105f, 0f)
		glScaled(1.1)
		glColor4f(1f, 1f, 1f, 0.5f)
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		
		return 1
	}
	
	override fun shouldRenderPass(entity: EntityLivingBase, pass: Int, ticks: Float) =
		setPixieBrightness(entity as EntitySnowSprite, pass, ticks)
}
