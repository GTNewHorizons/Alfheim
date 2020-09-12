package alfheim.client.render.item

import alexsocol.asjlib.*
import alexsocol.asjlib.render.ASJRenderHelper.discard
import alexsocol.asjlib.render.ASJRenderHelper.setGlow
import alexsocol.asjlib.render.ASJRenderHelper.setTwoside
import alfheim.api.ModInfo
import net.minecraft.client.renderer.Tessellator
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType.*
import org.lwjgl.opengl.GL11.*

object RenderItemFenrirClaws: IItemRenderer {
	
	val texture = ResourceLocation("${ModInfo.MODID}:textures/items/FenrirClaws0.png")
	val overlay = ResourceLocation("${ModInfo.MODID}:textures/items/FenrirClaws1.png")
	
	override fun renderItem(type: IItemRenderer.ItemRenderType, item: ItemStack?, vararg data: Any?) {
		glPushMatrix()
		setTwoside()
		
		glScaled(1.0/16)
		
		@Suppress("NON_EXHAUSTIVE_WHEN")
		when (type) {
			EQUIPPED              -> {
				glRotatef(-10f, 0f, 0f, 1f)
				glRotatef(90f, 1f, 0f, 0f)
				glTranslatef(6f, -2f, 0f)
			}
			EQUIPPED_FIRST_PERSON -> {
				glRotatef(90f, 0f, 1f, 0f)
				glRotatef(90f, 1f, 0f, 0f)
				glTranslatef(0f, 4f, 0f)
			}
			INVENTORY             -> {
				glRotatef(180f, 0f, 1f, 0f)
				glTranslatef(-3f, 3f, 0f)
			}
		}
		
//		glTranslatef(2f, 2f, 0f)
		
		val tes = Tessellator.instance
		
		val x0: Number
		val y0: Number
//		var z0: Number
		val u0: Number
		val v0: Number
		
		val x1: Number
		val y1: Number
//		var z1: Number
		val u1: Number
		val v1: Number
		
		x0 = 0
		y0 = 0
//		z0 = 9.5
		u0 = 0
		v0 = 6
		x1 = 10
		y1 = 10
//		z1 = 9.501
		u1 = 10
		v1 = 16
		
		for (i in 0..1) {
			mc.renderEngine.bindTexture(if (i == 0) texture else overlay)
			
			if (i != 0) setGlow()
			tes.startDrawingQuads()
			
			for ((z0, z1) in arrayOf<Pair<Number, Number>>(9.5 to 9.501, 8 to 8.001, 6 to 6.501)) {
				tes.addVertexWithUV(x0, y0, z0, u0, v0)
				tes.addVertexWithUV(x0, y1, z0, u0, v1)
				tes.addVertexWithUV(x1, y1, z1, u1, v1)
				tes.addVertexWithUV(x1, y0, z1, u1, v0)
			}
			
			tes.draw()
			if (i != 0) discard()
		}
		
//		discard() -- above in for-loop
		glPopMatrix()
	}
	
	override fun handleRenderType(item: ItemStack, type: IItemRenderer.ItemRenderType) = type != INVENTORY && type != ENTITY
	
	override fun shouldUseRenderHelper(type: IItemRenderer.ItemRenderType, item: ItemStack, helper: IItemRenderer.ItemRendererHelper) =
		helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION || helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING
	
	private fun Tessellator.addVertexWithUV(x: Number, y: Number, z: Number, u: Number, v: Number) {
		this.setTextureUV(u.D, v.D)
		this.addVertex(x.D, y.D, z.D)
	}
}
