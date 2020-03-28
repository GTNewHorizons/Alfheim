package alfheim.client.render.tile

import alexsocol.asjlib.*
import alfheim.common.block.BlockAlfheimPortal
import alfheim.common.block.tile.TileAlfheimPortal
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import kotlin.math.*

object RenderTileAlfheimPortal: TileEntitySpecialRenderer() {
	
	override fun renderTileEntityAt(tileentity: TileEntity, x: Double, y: Double, z: Double, ticks: Float) {
		val portal = tileentity as TileAlfheimPortal
		val meta = portal.getBlockMetadata()
		if (meta == 0)
			return
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glDisable(GL_ALPHA_TEST)
		glDisable(GL_LIGHTING)
		glDisable(GL_CULL_FACE)
		glColor4d(1.0, 1.0, 1.0, min(1.0, (sin(((ClientTickHandler.ticksInGame + ticks) / 8).D) + 1) / 7 + 0.6) * (min(60, portal.ticksOpen) / 60.0) * 0.5)
		
		glTranslated(x - 1, y + 1, z + 0.25)
		if (meta == 2) {
			glTranslated(1.25, 0.0, 1.75)
			glRotated(90.0, 0.0, 1.0, 0.0)
		}
		
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture)
		val icon = BlockAlfheimPortal.textures[1]
		
		if (icon != null) renderIcon(0, 0, icon, 3, 3, 240)
		glTranslated(0.0, 0.0, 0.5)
		if (icon != null) renderIcon(0, 0, icon, 3, 3, 240)
		
		glColor4d(1.0, 1.0, 1.0, 1.0)
		glEnable(GL_CULL_FACE)
		glEnable(GL_LIGHTING)
		glEnable(GL_ALPHA_TEST)
		glDisable(GL_BLEND)
		glPopMatrix()
	}
	
	fun renderIcon(par1: Int, par2: Int, par3Icon: IIcon, par4: Int, par5: Int, brightness: Int) {
		val tessellator = Tessellator.instance
		tessellator.startDrawingQuads()
		tessellator.setBrightness(brightness)
		tessellator.addVertexWithUV(par1.D, (par2 + par5).D, 0.0, par3Icon.minU.D, par3Icon.maxV.D)
		tessellator.addVertexWithUV((par1 + par4).D, (par2 + par5).D, 0.0, par3Icon.maxU.D, par3Icon.maxV.D)
		tessellator.addVertexWithUV((par1 + par4).D, par2.D, 0.0, par3Icon.maxU.D, par3Icon.minV.D)
		tessellator.addVertexWithUV(par1.D, par2.D, 0.0, par3Icon.minU.D, par3Icon.minV.D)
		tessellator.draw()
	}
}
