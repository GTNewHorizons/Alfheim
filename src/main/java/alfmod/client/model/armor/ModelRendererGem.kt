package alfmod.client.model.armor

import alexsocol.asjlib.render.ASJRenderHelper
import alfmod.common.item.equipment.armor.ItemVolcanoArmor
import net.minecraft.client.model.*
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.GL11
import java.awt.Color

class ModelRendererGem(model: ModelBase?, u: Int, v: Int): ModelRenderer(model, u, v) {
	
	var charge = 0f
	
	override fun render(f5: Float) {
		val prevX = OpenGlHelper.lastBrightnessX
		val prevY = OpenGlHelper.lastBrightnessY
		
		if (charge > 0f)
			ASJRenderHelper.glColor1u(Color(1f, 2f / 3f / ItemVolcanoArmor.MAX_CHARGE * charge, 0f).rgb)
		else
			GL11.glColor4f(0f, 0f, 0.5f, 1f)
		
		if (charge == ItemVolcanoArmor.MAX_CHARGE) {
			GL11.glDisable(GL11.GL_LIGHTING)
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		}
		
		super.render(f5)
		
		if (charge == ItemVolcanoArmor.MAX_CHARGE) {
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY)
			GL11.glEnable(GL11.GL_LIGHTING)
		}
		
		GL11.glColor4f(1f, 1f, 1f, 1f)
	}
}
