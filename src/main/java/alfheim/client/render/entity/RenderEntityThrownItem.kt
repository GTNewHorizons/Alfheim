package alfheim.client.render.entity

import alfheim.common.core.util.*
import alfheim.common.item.AlfheimItems
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.IIcon
import org.lwjgl.opengl.*

/**
 * Created by l0nekitsune on 1/20/16.
 */
class RenderEntityThrownItem: Render() {
	
	override fun doRender(p_76986_1_: Entity, p_76986_2_: Double, p_76986_4_: Double, p_76986_6_: Double, p_76986_8_: Float, p_76986_9_: Float) {
        //val c = p_76986_1_ as EntityThrowableItem
        val iicon = AlfheimItems.fireGrenade.getIconFromDamage(0) // c.event.itemStack.item.getIconFromDamage(c.event.itemStack.meta)
        if (iicon != null) {
            GL11.glPushMatrix()
            GL11.glTranslatef(p_76986_2_.F, p_76986_4_.F, p_76986_6_.F)
            GL11.glEnable(GL12.GL_RESCALE_NORMAL)
            GL11.glScalef(0.5f, 0.5f, 0.5f)
            bindEntityTexture(p_76986_1_)
            val tessellator = Tessellator.instance
            func_77026_a(tessellator, iicon, -1)
            GL11.glDisable(GL12.GL_RESCALE_NORMAL)
            GL11.glPopMatrix()
        }
	}
	
	override fun getEntityTexture(p_110775_1_: Entity) = TextureMap.locationItemsTexture!!
	
	private fun func_77026_a(p_77026_1_: Tessellator, p_77026_2_: IIcon, light: Int) {
		val f = p_77026_2_.minU
		val f1 = p_77026_2_.maxU
		val f2 = p_77026_2_.minV
		val f3 = p_77026_2_.maxV
		val f4 = 1f
		val f5 = 0.5f
		val f6 = 0.25f
		GL11.glRotatef(180f - renderManager.playerViewY, 0f, 1f, 0f)
		GL11.glRotatef(-renderManager.playerViewX, 1f, 0f, 0f)
		p_77026_1_.startDrawingQuads()
		p_77026_1_.setNormal(0f, 1f, 0f)
		if (light != -1) {
			p_77026_1_.setBrightness(light)
		}
		
		p_77026_1_.addVertexWithUV((0f - f5).D, (0f - f6).D, 0.0, f.D, f3.D)
		p_77026_1_.addVertexWithUV((f4 - f5).D, (0f - f6).D, 0.0, f1.D, f3.D)
		p_77026_1_.addVertexWithUV((f4 - f5).D, (f4 - f6).D, 0.0, f1.D, f2.D)
		p_77026_1_.addVertexWithUV((0f - f5).D, (f4 - f6).D, 0.0, f.D, f2.D)
		p_77026_1_.draw()
	}
}
