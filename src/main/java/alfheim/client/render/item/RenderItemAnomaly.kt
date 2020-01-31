package alfheim.client.render.item

import alfheim.api.AlfheimAPI
import alfheim.api.block.tile.SubTileEntity
import alfheim.api.lib.LibResourceLocations
import alfheim.common.block.AlfheimBlocks
import alfheim.common.item.block.ItemBlockAnomaly
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import org.lwjgl.opengl.GL11.*

//Render from Thaumcraft nodes by Azanor
object RenderItemAnomaly: IItemRenderer {
	
	override fun handleRenderType(item: ItemStack?, type: ItemRenderType) =
		item != null && item.item === Item.getItemFromBlock(AlfheimBlocks.anomaly) && ItemBlockAnomaly.getType(item) != ItemBlockAnomaly.TYPE_UNDEFINED
	
	override fun shouldUseRenderHelper(type: ItemRenderType, item: ItemStack, helper: IItemRenderer.ItemRendererHelper) =
		helper != IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK
	
	override fun renderItem(type: ItemRenderType, item: ItemStack, vararg data: Any) {
		if (type == ItemRenderType.ENTITY) {
			glScaled(2.0, 2.0, 2.0)
			glTranslated(-0.5, -0.25, -0.5)
		} else if (type == ItemRenderType.EQUIPPED && data[1] is EntityPlayer) {
			glTranslated(0.0, 0.0, -0.5)
		} else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			glRotated(93.2, 1.0, 0.0, 0.0)
			glTranslated(0.0, -0.5, -1.0)
		}
		
		renderItemAnomaly(AlfheimAPI.getAnomalyInstance(ItemBlockAnomaly.getType(item)))
	}
	
	fun renderItemAnomaly(subtile: SubTileEntity) {
		glPushMatrix()
		glAlphaFunc(GL_GREATER, 0.003921569f)
		glDepthMask(false)
		glDisable(GL_CULL_FACE)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glColor4d(1.0, 1.0, 1.0, 1.0)
		glPushMatrix()
		
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.anomalies)
		val frames = subtile.frames
		val frame = ((System.nanoTime() / 40000000L + 1L) % frames.toLong()).toInt()
		val strip = subtile.strip
		val color = subtile.color
		
		glTranslated(0.5, 0.5, 0.5)
		renderAnimatedQuadStrip(1.5f, 1f, frames, strip, frame, color)
		glRotated(90.0, 0.0, 1.0, 0.0)
		renderAnimatedQuadStrip(1.5f, 1f, frames, strip, frame, color)
		glRotatef(90f, 1f, 0f, 0f)
		renderAnimatedQuadStrip(1.5f, 1f, frames, strip, frame, color)
		
		glPopMatrix()
		glDisable(GL_BLEND)
		glEnable(GL_CULL_FACE)
		glDepthMask(true)
		glAlphaFunc(GL_GREATER, 0.1f)
		glPopMatrix()
	}
	
	fun renderAnimatedQuadStrip(scale: Float, alpha: Float, frames: Int, strip: Int, cframe: Int, color: Int) {
		if (Minecraft.getMinecraft().renderViewEntity is EntityPlayer) {
			val tessellator = Tessellator.instance
			tessellator.startDrawingQuads()
			tessellator.setBrightness(220)
			tessellator.setColorRGBA_I(color, (alpha * 255.0f).toInt())
			val f2 = cframe.toFloat() / frames.toFloat()
			val f3 = (cframe + 1).toFloat() / frames.toFloat()
			val f4 = strip.toFloat() / frames.toFloat()
			val f5 = (strip + 1).toFloat() / frames.toFloat()
			tessellator.setNormal(0.0f, 0.0f, -1.0f)
			tessellator.addVertexWithUV(-0.5 * scale.toDouble(), 0.5 * scale.toDouble(), 0.0, f2.toDouble(), f5.toDouble())
			tessellator.addVertexWithUV(0.5 * scale.toDouble(), 0.5 * scale.toDouble(), 0.0, f3.toDouble(), f5.toDouble())
			tessellator.addVertexWithUV(0.5 * scale.toDouble(), -0.5 * scale.toDouble(), 0.0, f3.toDouble(), f4.toDouble())
			tessellator.addVertexWithUV(-0.5 * scale.toDouble(), -0.5 * scale.toDouble(), 0.0, f2.toDouble(), f4.toDouble())
			tessellator.draw()
		}
	}
}
