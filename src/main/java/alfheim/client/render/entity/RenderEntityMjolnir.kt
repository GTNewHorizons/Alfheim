package alfheim.client.render.entity

import alfheim.common.entity.EntityMjolnir
import alfheim.common.item.relic.ItemMjolnir
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.util.*
import org.lwjgl.opengl.*

// Basically a bit of an extension of RenderSnowball
class RenderEntityMjolnir: Render() {
	
	override fun doRender(entity: Entity, x: Double, y: Double, z: Double, p_76986_8_: Float, ticks: Float) {
		val c = entity as EntityMjolnir
		GL11.glPushMatrix()
		GL11.glTranslatef(x.toFloat(), y.toFloat(), z.toFloat())
		GL11.glEnable(GL12.GL_RESCALE_NORMAL)
		GL11.glScalef(0.5f, 0.5f, 0.5f)
		bindEntityTexture(entity)
		val tessellator = Tessellator.instance
		drawIcon(tessellator, ItemMjolnir.icons[0])
		drawIcon(tessellator, ItemMjolnir.icons[1])
		GL11.glDisable(GL12.GL_RESCALE_NORMAL)
		GL11.glPopMatrix()
	}
	
	override fun getEntityTexture(entity: Entity): ResourceLocation {
		return TextureMap.locationItemsTexture
	}
	
	private fun drawIcon(tes: Tessellator, icon: IIcon) {
		val f = icon.minU
		val f1 = icon.maxU
		val f2 = icon.minV
		val f3 = icon.maxV
		val f4 = 1.0f
		val f5 = 0.5f
		val f6 = 0.25f
		GL11.glRotatef(180.0f - renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
		GL11.glRotatef(-renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
		tes.startDrawingQuads()
		tes.setNormal(0.0f, 1.0f, 0.0f)
		tes.addVertexWithUV(0.0f - f5.toDouble(), 0.0f - f6.toDouble(), 0.0, f.toDouble(), f3.toDouble())
		tes.addVertexWithUV(f4 - f5.toDouble(), 0.0f - f6.toDouble(), 0.0, f1.toDouble(), f3.toDouble())
		tes.addVertexWithUV(f4 - f5.toDouble(), f4 - f6.toDouble(), 0.0, f1.toDouble(), f2.toDouble())
		tes.addVertexWithUV(0.0f - f5.toDouble(), f4 - f6.toDouble(), 0.0, f.toDouble(), f2.toDouble())
		tes.draw()
	}
}