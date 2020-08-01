package alfmod.client.model.entity

import alexsocol.asjlib.mc
import net.minecraft.client.model.*
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*

class ModelBipedEyes(val textureEyes: ResourceLocation): ModelBiped() {
	
	val bipedEyes = ModelRenderer(this, 0, 0).apply {
		addBox(-4f, -8f, -4f, 8, 8, 8, 0f)
		setRotationPoint(0f, 0f, 0f)
	}
	
	override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		super.render(entity, f, f1, f2, f3, f4, f5)
		
		mc.renderEngine.bindTexture(textureEyes)
		
		glDisable(GL_LIGHTING)
		
		val lastX = OpenGlHelper.lastBrightnessX
		val lastY = OpenGlHelper.lastBrightnessY
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		
		if (isChild) {
			glPushMatrix()
			alexsocol.asjlib.glScaled(0.75)
			glTranslatef(0f, 16f * f5, 0f)
			bipedEyes.render(f5)
			glPopMatrix()
		} else {
			bipedEyes.render(f5)
		}
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
		glEnable(GL_LIGHTING)
	}
	
	override fun setRotationAngles(f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, entity: Entity?) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		
		bipedEyes.rotateAngleX = bipedHead.rotateAngleX
		bipedEyes.rotateAngleY = bipedHead.rotateAngleY
		bipedEyes.rotateAngleZ = bipedHead.rotateAngleZ
	}
}