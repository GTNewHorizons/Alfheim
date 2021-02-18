package alfheim.client.render.item

import alexsocol.asjlib.*
import alexsocol.asjlib.render.ASJRenderHelper.discard
import alexsocol.asjlib.render.ASJRenderHelper.setBlend
import alexsocol.asjlib.render.ASJRenderHelper.setGlow
import alexsocol.asjlib.render.ASJRenderHelper.setTwoside
import alfheim.api.lib.LibResourceLocations
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType.*
import org.lwjgl.opengl.GL11.*

object RenderItemFenrirClaws: IItemRenderer {
	
	override fun renderItem(type: IItemRenderer.ItemRenderType, item: ItemStack?, vararg data: Any?) {
		glPushMatrix()
		setTwoside()
		setBlend()
		
		@Suppress("NON_EXHAUSTIVE_WHEN")
		when (type) {
			EQUIPPED_FIRST_PERSON -> {
				glTranslatef(0f, 0.5f, -0.75f)
				glRotatef(-45f, 0f, 0f, 1f)
				
				data.firstOrNull { it is EntityPlayer }?.let {
					if ((it as EntityPlayer).isBlocking) {
						glRotatef(45f, 0f, 0f, 1f)
						glTranslated(0.25, -0.35, -0.25)
					}
				}
			}
			
			EQUIPPED              -> {
				// FUCK YOU MOJANG FOR YOUR NON-ORTHO PLACEMENT RELATIVE TO PLAYERS' HAND! FUCK! FUCK! FUCK! JUST DIE THE ONE WHO DID IT! YOU DESERVE TO BURN IN HELL!
				glRotatef(100f, 1f, 0f, 0f)
				glRotatef(-30f, 0f, 0f, 1f)
				glRotatef(15f, 0f, 1f, 0f)
				glTranslatef(-4 / 16f, 5 / 16f, -5.5f / 16f)
				glRotatef(-30f, 0f, 0f, 1f)
				glTranslatef(-1 / 16f, 2 / 16f, 0f)
			}
			
			INVENTORY             -> {
				glScaled(1.5)
				glRotatef(180f, 0f, 0f, 1f)
				glTranslatef(-9.25f, -10.25f, 0f)
			}
			
			ENTITY                -> {
				glTranslated(-0.25, -0.25, -0.5)
			}
		}
		
		if (type != INVENTORY) glScaled(1.0 / 16)
		
		val tes = Tessellator.instance
		
		for (i in 0..1) {
			mc.renderEngine.bindTexture(if (i == 0) LibResourceLocations.fenrirClaw else LibResourceLocations.fenrirClawOverlay)
			
			if (i != 0) setGlow() else if (type == INVENTORY) glColor4f(0.75f, 0.75f, 0.75f, 1f)
			tes.startDrawingQuads()
			
			for ((z0, z1) in arrayOf<Pair<Number, Number>>(10.8 to 10.801, 9 to 9.001, 7.15 to 7.151, 5.1 to 5.601)) {
				tes.addVertexWithUV(0, 0, z0, 0, 6)
				tes.addVertexWithUV(0, 10, z0, 0, 16)
				tes.addVertexWithUV(10, 10, z1, 10, 16)
				tes.addVertexWithUV(10, 0, z1, 10, 6)
			}
			
			tes.draw()
			if (i != 0) discard() else if (type == INVENTORY) glColor4f(1f, 1f, 1f, 1f)
		}

//		discard() -- above in for-loop
		glPopMatrix()
	}
	
	override fun handleRenderType(item: ItemStack, type: IItemRenderer.ItemRenderType) = true
	
	override fun shouldUseRenderHelper(type: IItemRenderer.ItemRenderType, item: ItemStack, helper: IItemRenderer.ItemRendererHelper) =
		helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION || helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING
	
	private fun Tessellator.addVertexWithUV(x: Number, y: Number, z: Number, u: Number, v: Number) {
		this.setTextureUV(u.D / 16, v.D / 16)
		this.addVertex(x.D, y.D, z.D)
	}
}
