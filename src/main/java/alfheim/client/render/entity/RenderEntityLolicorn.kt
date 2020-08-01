package alfheim.client.render.entity

import alexsocol.asjlib.mc
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.*
import alfheim.common.entity.EntityLolicorn
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.*
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*

object RenderEntityLolicorn: RenderLiving(ModelEntityLolicorn(), 0.5f) {
	
	val lolicorn = mainModel
	val sleipnir = ModelEntitySleipnir()
	
	override fun renderModel(entity: EntityLivingBase, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		val isSleipnir = (entity as? EntityLolicorn)?.type == 1
		val firstPerson = !isSleipnir && (mc.thePlayer === entity.riddenByEntity && mc.gameSettings?.thirdPersonView == 0)
		
		mainModel = if (isSleipnir) sleipnir else lolicorn
		
		if (firstPerson) {
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
		
		if (firstPerson) {
			glColor4f(1f, 1f, 1f, 1f)
			glDisable(GL_BLEND)
		}
	}
	
	override fun getEntityTexture(entity: Entity): ResourceLocation {
		if (entity is EntityLolicorn) {
			if (entity.type == 1) return LibResourceLocations.sleipnir
			if (entity.owner == "KAIIIAK") return LibResourceLocations.roricorn
		}
		return LibResourceLocations.lolicorn
	}
}