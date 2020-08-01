package alfmod.client.model.entity

import alexsocol.asjlib.glScaled
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11.*

class ModelBipedGlowing: ModelBiped() {
	
	override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		glDisable(GL_LIGHTING)
		
		val lastX = OpenGlHelper.lastBrightnessX
		val lastY = OpenGlHelper.lastBrightnessY
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		
		if (isChild) {
			glPushMatrix()
			glScaled(0.75)
			glTranslatef(0f, 16f * f5, 0f)
		}
		
		super.render(entity, f, f1, f2, f3, f4, f5)
		
		if (isChild) glPopMatrix()
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
		glEnable(GL_LIGHTING)
	}
}