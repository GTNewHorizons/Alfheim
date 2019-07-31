package alfheim.client.model.item

import alfheim.common.item.material.ItemElvenResource
import net.minecraft.client.Minecraft
import net.minecraft.client.model.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.*
import org.lwjgl.opengl.GL11.*
import java.awt.Color

/**
 * @author Azanor
 */
class ModelCreatorStaff: ModelBase() {
	
	companion object {
		val instance = ModelCreatorStaff()
	}
	
	var rodT = ResourceLocation("thaumcraft:textures/models/creator/wand_rod_primal.png")
	var capT = ResourceLocation("thaumcraft:textures/models/wand_cap_alfheimMauftrium.png")
	var wandT = ResourceLocation("thaumcraft:textures/models/creator/wand.png")
	var runeT = ResourceLocation("thaumcraft:textures/models/creator/script.png")
	
	var Rod: ModelRenderer
	var Focus: ModelRenderer
	var Cap: ModelRenderer
	var CapBottom: ModelRenderer
	val renderBlocks = RenderBlocks()
	
	init {
		textureWidth = 32
		textureHeight = 32
		Cap = ModelRenderer(this, 0, 0)
		Cap.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2)
		Cap.setRotationPoint(0.0f, 0.0f, 0.0f)
		Cap.setTextureSize(64, 32)
		Cap.mirror = true
		CapBottom = ModelRenderer(this, 0, 0)
		CapBottom.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2)
		CapBottom.setRotationPoint(0.0f, 20.0f, 0.0f)
		CapBottom.setTextureSize(64, 32)
		CapBottom.mirror = true
		Rod = ModelRenderer(this, 0, 8)
		Rod.addBox(-1.0f, -1.0f, -1.0f, 2, 18, 2)
		Rod.setRotationPoint(0.0f, 2.0f, 0.0f)
		Rod.setTextureSize(64, 32)
		Rod.mirror = true
		Focus = ModelRenderer(this, 0, 0)
		Focus.addBox(-3.0f, -6.0f, -3.0f, 6, 6, 6)
		Focus.setRotationPoint(0.0f, 0.0f, 0.0f)
		Focus.setTextureSize(64, 32)
		Focus.mirror = true
	}
	
	fun render() {
		if (RenderManager.instance.renderEngine != null) {
			val player = Minecraft.getMinecraft().thePlayer
			Minecraft.getMinecraft().renderEngine.bindTexture(rodT)
			glPushMatrix()
			
			glTranslated(0.0, 0.2, 0.0)
			
			glPushMatrix()
			var i = (200.0f + MathHelper.sin(player.ticksExisted.toFloat()) * 5.0f + 5.0f).toInt()
			var j = i % 65536
			var k = i / 65536
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j.toFloat() / 1.0f, k.toFloat() / 1.0f)
			
			glTranslated(0.0, -0.1, 0.0)
			glScaled(1.2, 2.0, 1.2)
			
			Rod.render(0.0625f)
			i = player.getBrightnessForRender(0.0f)
			j = i % 65536
			k = i / 65536
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j.toFloat() / 1.0f, k.toFloat() / 1.0f)
			
			glPopMatrix()
			Minecraft.getMinecraft().renderEngine.bindTexture(capT)
			glPushMatrix()
			glScaled(1.3, 1.1, 1.3)
			
			glPushMatrix()
			glScaled(1.3, 1.3, 1.3)
			Cap.render(0.0625f)
			glPopMatrix()
			glPushMatrix()
			glTranslated(0.0, 0.3, 0.0)
			glScaled(1.0, 0.66, 1.0)
			Cap.render(0.0625f)
			glPopMatrix()
			
			glTranslated(0.0, 0.225, 0.0)
			glPushMatrix()
			glScaled(1.0, 0.66, 1.0)
			Cap.render(0.0625f)
			glPopMatrix()
			glTranslated(0.0, 0.65, 0.0)
			
			CapBottom.render(0.0625f)
			glPopMatrix()
			glPushMatrix()
			glRotatef(180.0f, 1.0f, 0.0f, 0.0f)
			val tessellator = Tessellator.instance
			val icon = ItemElvenResource.orn
			val f1 = icon.maxU
			val f2 = icon.minV
			val f3 = icon.minU
			val f4 = icon.maxV
			RenderManager.instance.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			glPushMatrix()
			glTranslatef(-0.25f, -0.1f, 0.0275f)
			glScaled(0.5, 0.5, 0.5)
			ItemRenderer.renderItemIn2D(tessellator, f1, f2, f3, f4, icon.iconWidth, icon.iconHeight, 0.1f)
			glPopMatrix()
			glPushMatrix()
			glRotatef(90.0f, 0.0f, 1.0f, 0.0f)
			glTranslatef(-0.25f, -0.1f, 0.0275f)
			glScaled(0.5, 0.5, 0.5)
			ItemRenderer.renderItemIn2D(tessellator, f1, f2, f3, f4, icon.iconWidth, icon.iconHeight, 0.1f)
			glPopMatrix()
			glPopMatrix()
			
			val alpha = 0.6f
			glPushMatrix()
			glTranslatef(0.0f, -0.15f, 0.0f)
			glScaled(0.165, 0.1765, 0.165)
			
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
			renderBlocks.setRenderBoundsFromBlock(Blocks.stone)
			drawFaces(renderBlocks, ItemElvenResource.dep)
			glPopMatrix()
			
			Minecraft.getMinecraft().renderEngine.bindTexture(wandT)
			
			glPushMatrix()
			glTranslatef(0.0f, -0.0475f, 0.0f)
			glScaled(0.525, 0.5525, 0.525)
			
			val c = Color(0xFFE9CF)
			glColor4f(c.red.toFloat() / 255.0f, c.green.toFloat() / 255.0f, c.blue.toFloat() / 255.0f, alpha)
			i = (195.0f + MathHelper.sin(player.ticksExisted.toFloat() / 3.0f) * 10.0f + 10.0f).toInt()
			j = i % 65536
			k = i / 65536
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j.toFloat() / 1.0f, k.toFloat() / 1.0f)
			Focus.render(0.0625f)
			glPopMatrix()
			
			glPushMatrix()
			i = 200
			j = i % 65536
			k = i / 65536
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j.toFloat() / 1.0f, k.toFloat() / 1.0f)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE)
			
			for (rot in 0..9) {
				glPushMatrix()
				glRotated((36 * rot + player.ticksExisted).toDouble(), 0.0, 1.0, 0.0)
				drawRune(0.16, -0.009999999776482582, -0.125, rot, player)
				glPopMatrix()
			}
			
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glPopMatrix()
			
			glPushMatrix()
			i = 200
			j = i % 65536
			k = i / 65536
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j.toFloat() / 1.0f, k.toFloat() / 1.0f)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE)
			
			for (rot in 0..3) {
				glRotated(90.0, 0.0, 1.0, 0.0)
				
				for (a in 0..13) {
					val rune = (a + rot * 3) % 16
					drawRune(0.36 + a.toDouble() * 0.14, -0.009999999776482582, -0.08, rune, player)
				}
			}
			
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glPopMatrix()
			
			glPopMatrix()
		}
	}
	
	fun drawFaces(renderblocks: RenderBlocks, i: IIcon) {
		val block = null
		val tessellator = Tessellator.instance
		glTranslatef(-0.5f, -0.5f, -0.5f)
		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0f, -1.0f, 0.0f)
		renderblocks.renderFaceYNeg(block, 0.0, 0.0, 0.0, i)
		tessellator.draw()
		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0f, 1.0f, 0.0f)
		renderblocks.renderFaceYPos(block, 0.0, 0.0, 0.0, i)
		tessellator.draw()
		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0f, 0.0f, 1.0f)
		renderblocks.renderFaceXNeg(block, 0.0, 0.0, 0.0, i)
		tessellator.draw()
		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0f, 0.0f, -1.0f)
		renderblocks.renderFaceXPos(block, 0.0, 0.0, 0.0, i)
		tessellator.draw()
		tessellator.startDrawingQuads()
		tessellator.setNormal(1.0f, 0.0f, 0.0f)
		renderblocks.renderFaceZNeg(block, 0.0, 0.0, 0.0, i)
		tessellator.draw()
		tessellator.startDrawingQuads()
		tessellator.setNormal(-1.0f, 0.0f, 0.0f)
		renderblocks.renderFaceZPos(block, 0.0, 0.0, 0.0, i)
		tessellator.draw()
		glTranslatef(0.5f, 0.5f, 0.5f)
	}
	
	private fun drawRune(x: Double, y: Double, z: Double, rune: Int, player: EntityPlayer) {
		glPushMatrix()
		Minecraft.getMinecraft().renderEngine.bindTexture(runeT)
		val r = MathHelper.sin((player.ticksExisted + rune * 5).toFloat() / 5.0f) * 0.1f + 0.88f
		val g = MathHelper.sin((player.ticksExisted + rune * 5).toFloat() / 7.0f) * 0.1f + 0.63f
		val alpha = MathHelper.sin((player.ticksExisted + rune * 5).toFloat() / 10.0f) * 0.2f
		glColor4f(r, g, 0.2f, alpha + 0.6f)
		glRotated(90.0, 0.0, 0.0, 1.0)
		glTranslated(x, y, z)
		val tessellator = Tessellator.instance
		val var8 = 0.0625f * rune.toFloat()
		val var9 = var8 + 0.0625f
		val var10 = 0.0f
		val var11 = 1.0f
		tessellator.startDrawingQuads()
		tessellator.setColorRGBA_F(r, g, 0.2f, alpha + 0.6f)
		tessellator.addVertexWithUV(-0.06 - (alpha / 40.0f).toDouble(), 0.06 + (alpha / 40.0f).toDouble(), 0.0, var9.toDouble(), var11.toDouble())
		tessellator.addVertexWithUV(0.06 + (alpha / 40.0f).toDouble(), 0.06 + (alpha / 40.0f).toDouble(), 0.0, var9.toDouble(), var10.toDouble())
		tessellator.addVertexWithUV(0.06 + (alpha / 40.0f).toDouble(), -0.06 - (alpha / 40.0f).toDouble(), 0.0, var8.toDouble(), var10.toDouble())
		tessellator.addVertexWithUV(-0.06 - (alpha / 40.0f).toDouble(), -0.06 - (alpha / 40.0f).toDouble(), 0.0, var8.toDouble(), var11.toDouble())
		tessellator.draw()
		glPopMatrix()
	}
	
	fun setRotationAngles(f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, null)
	}
}
