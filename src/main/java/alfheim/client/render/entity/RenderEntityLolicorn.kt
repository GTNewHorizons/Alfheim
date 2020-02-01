package alfheim.client.render.entity

import alfheim.api.lib.LibResourceLocations
import alfheim.common.entity.EntityLolicorn
import net.minecraft.client.model.ModelBaseimport net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.*
import org.lwjgl.opengl.GL11.*

object RenderEntityLolicorn: RenderLiving(ModelEntityLolicorn(), 0.5f) {
	
	override fun renderModel(entity: EntityLivingBase, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		
		val flag = (mc.thePlayer?.ridingEntity === entity && mc.gameSettings?.thirdPersonView == 0)
		
		if (flag) {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glColor4f(1f, 1f, 1f, 0.5f)
		}
		
		if (entity.isInvisible) {
			mainModel.setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		} else {
			bindEntityTexture(entity)
			mainModel.render(entity, f, f1, f2, f3, f4, f5)
		}
		if (flag) {
			glColor4f(1f, 1f, 1f, 1f)
			glDisable(GL_BLEND)
		}
	}
	
	override fun getEntityTexture(entity: Entity) = if ((entity as? EntityLolicorn)?.owner == "KAIIIAK") LibResourceLocations.roricorn else LibResourceLocations.lolicorn
}