package alfheim.client.render.tile

import alexsocol.asjlib.*
import alfheim.common.block.BlockTradePortal
import alfheim.common.block.tile.TileTradePortal
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import kotlin.math.*

object RenderTileTradePortal: TileEntitySpecialRenderer() {
	
	override fun renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, ticks: Float) {
		if (tile !is TileTradePortal) return
		
		val meta = tile.getBlockMetadata()
		if (meta == 0)
			return
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glDisable(GL_ALPHA_TEST)
		glDisable(GL_LIGHTING)
		glDisable(GL_CULL_FACE)
		glColor4d(1.0, 1.0, 1.0, min(1.0, (sin(((ClientTickHandler.ticksInGame + ticks) / 8).D) + 1) / 7 + 0.6) * (min(60, tile.ticksOpen) / 60.0) * 0.85)
		
		glTranslated(x - 1, y + 1, z + 0.25)
		if (meta == 2) {
			glTranslated(1.25, 0.0, 1.75)
			glRotated(90.0, 0.0, 1.0, 0.0)
		}
		
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture)
		val icon = BlockTradePortal.textures[1]
		if (icon != null) renderIcon(0.0, 0.0, icon, 3.0, 3.0, 240)
		glTranslated(0.0, 0.0, 0.5)
		if (icon != null) renderIcon(0.0, 0.0, icon, 3.0, 3.0, 240)
		
		glColor4d(1.0, 1.0, 1.0, 1.0)
		if (tile.isTradeOn) {
			if (meta == 2) glTranslated(0.05, 0.0, 0.0)
			if (meta == 1) glTranslated(0.046875, 0.0, 0.0)
			glTranslated(1.453125, -0.6640625, 0.251)
			val out = tile.output
			if (out.item is ItemBlock)
				glTranslated(0.0, 0.0, -0.140625)
			else
				glTranslated(0.0, -0.04, 0.0)
			renderItem(tile, out)
			glRotated(180.0, 0.0, 1.0, 0.0)
			if (out.item is ItemBlock)
				glTranslated(0.0, 0.0, 0.72075)
			else
				glTranslated(0.01, 0.0, 1.002)
			renderItem(tile, out)
		}
		
		glEnable(GL_CULL_FACE)
		glEnable(GL_LIGHTING)
		glEnable(GL_ALPHA_TEST)
		glDisable(GL_BLEND)
		glPopMatrix()
	}
	
	fun renderIcon(par1: Double, par2: Double, par3Icon: IIcon, par4: Double, par5: Double, brightness: Int) {
		val tessellator = Tessellator.instance
		tessellator.startDrawingQuads()
		tessellator.setBrightness(brightness)
		tessellator.addVertexWithUV(par1 + 0, par2 + par5, 0.0, par3Icon.minU.D, par3Icon.maxV.D)
		tessellator.addVertexWithUV(par1 + par4, par2 + par5, 0.0, par3Icon.maxU.D, par3Icon.maxV.D)
		tessellator.addVertexWithUV(par1 + par4, par2 + 0, 0.0, par3Icon.maxU.D, par3Icon.minV.D)
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0.0, par3Icon.minU.D, par3Icon.minV.D)
		tessellator.draw()
	}
	
	fun renderItem(tile: TileEntity, stack: ItemStack?) {
		if (stack == null) return
		val entityitem = EntityItem(tile.worldObj, 0.0, 0.0, 0.0, stack)
		entityitem.entityItem.stackSize = 1
		entityitem.hoverStart = 0f
		RenderItem.renderInFrame = true
		RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, 0.0, 0.0, 0f, 0f)
		RenderItem.renderInFrame = false
	}
}
