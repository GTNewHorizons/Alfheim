package alfheim.client.render.tile

import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.util.mc
import alfheim.client.model.entity.ModelBipedNew
import alfheim.client.render.entity.RenderWings
import alfheim.common.block.tile.TileRaceSelector
import alfheim.common.core.util.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.StatCollector
import org.lwjgl.opengl.GL11.*
import kotlin.math.sin

object RenderTileRaceSelector: TileEntitySpecialRenderer() {
	
	override fun renderTileEntityAt(tile: TileEntity?, cx: Double, cy: Double, cz: Double, ticks: Float) {
		if (tile !is TileRaceSelector) return
		
		val font = mc.fontRenderer
		val meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord)
		
		if (meta != 0 && meta != 1) return
		
		run {
			glPushMatrix()
			glTranslated(cx + 0.5, cy + 2.0 / 16, cz + 13.1 / 16)
			glRotatef(180f, 1f, 0f, 0f)
			
			val s = 1f / 8 / 16
			glScalef(s, s, s)
			val text = StatCollector.translateToLocal("elvenstory.select${if (meta == 0) "gender" else if (meta == 1) "race" else ""}")
			font.drawString(text, font.getStringWidth(text) / -2, 0, 0)
			glPopMatrix()
		}
		
		if (meta == 0) {
			glPushMatrix()
			glTranslated(cx + 0.5, cy + 0.5, cz + 0.5)
			glRotatef(180f, 1f, 0f, 0f)
			val s = 1f/16
			glScalef(s, s, s)
			
			font.drawString("\u2642", -5, -3, 0x0000FF)
			font.drawString("\u2640", 2, -3, 0xFF00FF)
			
			glPopMatrix()
		}
		
		if (meta == 1) {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
			run { // ######## render models ########
				glPushMatrix()
				glDisable(GL_CULL_FACE)
				var adRot = 0f
				if (tile.activeRotation != 0) adRot += tile.activeRotation * 2 + ticks * if (tile.activeRotation > 0) -2 else 2
				
				glTranslated(cx + 0.5, cy + 1.5, cz - 3)
				glRotatef(-40f * tile.rotation - adRot, 0f, 1f, 0f)
				
				val angles = 360
				val segAngles = angles / 9
				val shift = (360 - segAngles / 2).F - 90
				
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
			}
			
			if (tile.activeRotation == 0) { // ######## render wings ########
				glPushMatrix()
				glDisable(GL_CULL_FACE)
				glDisable(GL_LIGHTING)
				glDepthMask(false)
				glAlphaFunc(GL_GREATER, 0.003921569f)
				glTranslated(cx + 0.5, cy + 1.4	, cz - 3)
				glRotatef(180f, 1f, 0f, 0f)
				glTranslatef(0f, 0f, -2.4f)
				
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
				val spd = 0.5
				EnumRace[tile.rotation + 1].glColorA(1.0)
				
				glTranslated(0.0, -0.15, 0.0)
				
				// Icon
				glPushMatrix()
				glTranslated(-0.25, 0.25, 0.15)
				val si = 0.5
				glScaled(si, si, si)
				RenderWings.drawRect(LibResourceLocations.icons[tile.rotation + 1], 0)
				glPopMatrix()
				
				glTranslated(0.0, 0.1, 0.0)
				
				val ry = ((sin((mc.theWorld.totalWorldTime + mc.timer.renderPartialTicks).D * spd * 0.2) + 0.5) * 5f).F
				
				// Wing left
				glPushMatrix()
				glTranslated(0.15, 0.1, 0.15)
				val swr = 1.5
				glScaled(swr, swr, swr)
				//glRotated(10, 0, 0, 1);
				glRotated((-ry).D, 0.0, 1.0, 0.0)
				LibResourceLocations.wings[tile.rotation + 1]?.let { RenderWings.drawRect(it, -1) }
				glPopMatrix()
				
				// Wing right
				glPushMatrix()
				glTranslated(-0.15, 0.1, 0.15)
				val swl = 1.5
				glScaled(-swl, swl, swl)
				//glRotated(10, 0, 0, 1);
				glRotated((-ry).D, 0.0, 1.0, 0.0)
				LibResourceLocations.wings[tile.rotation + 1]?.let { RenderWings.drawRect(it, -1) }
				glPopMatrix()
				
				//glColor4d(1, 1, 1, 1); for some reason it cleans color
				glAlphaFunc(GL_GREATER, 0.1f)
				glDepthMask(true)
				glEnable(GL_LIGHTING)
				glEnable(GL_CULL_FACE)
				glPopMatrix()
			}
			
			run { // ######## render texts ########
				glPushMatrix()
				glTranslated(cx + 0.5, cy + 0.35, cz + 2.9 / 16)
				glRotatef(180f, 1f, 0f, 0f)
				val s = 1f / 64
				glScalef(s, s, s)
				
				val race = EnumRace[tile.rotation + 1]
				val text = StatCollector.translateToLocal("race.$race.name")
				val width = font.getStringWidth(text) / -2
				val w = -width.D
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
				
				font.drawString(text, width, 0, 0xFFFFFF)
				
				glPopMatrix()
			}
			
			glDisable(GL_BLEND)
		}
	}
}
