package alfheim.client.render.tile

import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelBipedNew
import alfheim.common.block.tile.TileRaceSelector
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.StatCollector
import org.lwjgl.opengl.GL11.*

class RenderTileRaceSelector: TileEntitySpecialRenderer() {
	
	override fun renderTileEntityAt(tile: TileEntity?, cx: Double, cy: Double, cz: Double, ticks: Float) {
		if (tile !is TileRaceSelector) return
		
		val mc = Minecraft.getMinecraft()
		val font = mc.fontRenderer
		val meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord)
		
		if (meta != 0 && meta != 1) return
		
		glPushMatrix()
		glTranslated(cx + 0.5, cy + 2.0/16, cz + 13.1/16)
		glRotatef(180f, 1f, 0f, 0f)
		
		var s = 1f/8/16
		glScalef(s, s, s)
		var text = StatCollector.translateToLocal("alfheimmisc.select${ if (meta == 0) "gender" else if (meta == 1) "race" else "" }")
		font.drawString(text, font.getStringWidth(text) / -2, 0, 0)
		glPopMatrix()
		
		
		if (meta == 1) {
			// ######## render models ########
			glPushMatrix()
			glDisable(GL_CULL_FACE)
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			var adRot = 0f
			if (tile.activeRotation != 0) adRot += tile.activeRotation * 2 + ticks * if (tile.activeRotation > 0) -2 else 2
			
			glTranslated(cx + 0.5, cy + 1.5, cz - 3)
			glRotatef(-40f * tile.rotation - adRot, 0f, 1f, 0f)
			
			val angles = 360
			val segAngles = angles / 9
			val shift = (360 - segAngles / 2).toFloat() - 90
			
			for (seg in 0 until 9) {
				val rotationAngle = (seg + 0.5f) * segAngles + shift
				
				glPushMatrix()
				glRotatef(rotationAngle, 0f, 1f, 0f)
				glTranslated(2.4, -0.75, 0.0)
				glColor4f(1f, 1f, 1f, if (seg == tile.rotation) 1f else 0.5f)
				
				mc.renderEngine.bindTexture(if (tile.female) LibResourceLocations.female[seg] else LibResourceLocations.male[seg])
				glScaled(0.75, 0.75, 0.75)
				glRotatef(180f, 1f, 0f, 0f)
				glRotatef((seg - tile.rotation) * 40 - 90f - adRot, 0f, 1f, 0f)
				glTranslatef(0f, -0.75f, 0f)
				ModelBipedNew.model.render(0.0625f)
				glPopMatrix()
			}
			
			glEnable(GL_CULL_FACE)
			glPopMatrix()
			
			
			
			// ######## render texts ########
			glPushMatrix()
			glTranslated(cx + 0.5, cy + 1, cz + 2.9/16)
			glRotatef(180f, 1f, 0f, 0f)
			s *= 2
			glScalef(s, s, s)
			
			val race = EnumRace.values()[tile.rotation + 1]
			text = StatCollector.translateToLocal("race.$race.name")
			val width = font.getStringWidth(text) / -2
			val w = -width.toDouble()
			glTranslatef(0f, 0f, 1f)
			glColor4f(0f, 0f, 0f, 0.5f)
			glDisable(GL_TEXTURE_2D)
			val tes = Tessellator.instance
			tes.startDrawingQuads()
			tes.addVertex(-w - 1, -1.0, 0.0)
			tes.addVertex(-w - 1, 8.0, 0.0)
			tes.addVertex(w + 1, 8.0, 0.0)
			tes.addVertex(w + 1, -1.0, 0.0)
			tes.draw()
			glEnable(GL_TEXTURE_2D)
			glTranslatef(0f, 0f, -1f)
			
			font.drawString(text, width, 0, race.rgbColor)
			
			glPopMatrix()
		}
	}
}
