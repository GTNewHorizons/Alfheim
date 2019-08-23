package alfheim.client.render.entity

import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.common.entity.EntityThrownPotion
import alfheim.common.item.AlfheimItems
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.IIcon
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL

class RenderThrownPotion: Render() {
	
	override fun doRender(p_76986_1_: Entity, p_76986_2_: Double, p_76986_4_: Double, p_76986_6_: Double, p_76986_8_: Float, p_76986_9_: Float) {
		val e = p_76986_1_ as EntityThrownPotion
        val iicon = e.stack.item.getIcon(e.stack, 0)
        if (iicon != null) {
            glPushMatrix()
            glTranslatef(p_76986_2_.toFloat(), p_76986_4_.toFloat(), p_76986_6_.toFloat())
            glEnable(GL_RESCALE_NORMAL)
            glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
            glScalef(0.5f, 0.5f, 0.5f)
            bindEntityTexture(p_76986_1_)
            val tessellator = Tessellator.instance
			func_77026_a(tessellator, iicon, -1)
			ASJRenderHelper.glColor1u(ASJRenderHelper.addAlpha(e.color, 255))
			func_77026_a(tessellator, AlfheimItems.splashPotion.getIcon(e.stack, 1), 240)
			glColor4f(1f, 1f, 1f, 1f)
			
			glDisable(GL_BLEND)
			glDisable(GL_RESCALE_NORMAL)
			glPopMatrix()
        }
	}
	
	override fun getEntityTexture(p_110775_1_: Entity) = TextureMap.locationItemsTexture!!
	
	private fun func_77026_a(p_77026_1_: Tessellator, p_77026_2_: IIcon, light: Int) {
		val f = p_77026_2_.minU
		val f1 = p_77026_2_.maxU
		val f2 = p_77026_2_.minV
		val f3 = p_77026_2_.maxV
		val f4 = 1.0f
		val f5 = 0.5f
		val f6 = 0.25f
		glPushMatrix()
		glRotatef(180.0f - renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
		glRotatef(-renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
		p_77026_1_.startDrawingQuads()
		p_77026_1_.setNormal(0.0f, 1.0f, 0.0f)
		if (light != -1) {
			p_77026_1_.setBrightness(light)
		}
		
		p_77026_1_.addVertexWithUV((0.0f - f5).toDouble(), (0.0f - f6).toDouble(), 0.0, f.toDouble(), f3.toDouble())
		p_77026_1_.addVertexWithUV((f4 - f5).toDouble(), (0.0f - f6).toDouble(), 0.0, f1.toDouble(), f3.toDouble())
		p_77026_1_.addVertexWithUV((f4 - f5).toDouble(), (f4 - f6).toDouble(), 0.0, f1.toDouble(), f2.toDouble())
		p_77026_1_.addVertexWithUV((0.0f - f5).toDouble(), (f4 - f6).toDouble(), 0.0, f.toDouble(), f2.toDouble())
		p_77026_1_.draw()
		glPopMatrix()
	}
}
