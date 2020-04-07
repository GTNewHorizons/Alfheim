package alfheim.client.gui

import alexsocol.asjlib.*
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.Tessellator
import org.lwjgl.opengl.GL11.*
import kotlin.math.max

class GUIDeathTimer: GuiScreen() {
	
	var timer: Int = 0
	
	override fun initGui() {
		super.initGui()
		timer = AlfheimConfigHandler.deathScreenAddTime
	}
	
	override fun drawScreen(p_73863_1_: Int, p_73863_2_: Int, p_73863_3_: Float) {
		drawGradientRect(0, 0, width, height, 1615855616, -1602211792)
		
		val font = mc.fontRenderer
		val resolution = ScaledResolution(mc, mc.displayWidth, mc.displayHeight)
		
		glScaled(2.0, 2.0, 1.0)
		run {
			glPushMatrix()
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glTranslated((resolution.scaledWidth / 4 - 32).D, (resolution.scaledHeight / 4 - 32).D, 0.0)
			val tes = Tessellator.instance
			mc.renderEngine.bindTexture(LibResourceLocations.deathTimerBG)
			tes.startDrawingQuads()
			tes.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0)
			tes.addVertexWithUV(0.0, 64.0, 0.0, 0.0, 1.0)
			tes.addVertexWithUV(64.0, 64.0, 0.0, 1.0, 1.0)
			tes.addVertexWithUV(64.0, 0.0, 0.0, 1.0, 0.0)
			tes.draw()
			mc.renderEngine.bindTexture(LibResourceLocations.deathTimer)
			glTranslated(32.0, 32.0, 0.0)
			glRotated((-(timer % 20) * 18).D, 0.0, 0.0, 1.0)
			glTranslated(-32.0, -32.0, 0.0)
			tes.startDrawingQuads()
			tes.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0)
			tes.addVertexWithUV(0.0, 64.0, 0.0, 0.0, 1.0)
			tes.addVertexWithUV(64.0, 64.0, 0.0, 1.0, 1.0)
			tes.addVertexWithUV(64.0, 0.0, 0.0, 1.0, 0.0)
			tes.draw()
			glPopMatrix()
		}
		
		run {
			glPushMatrix()
			val sc = 1.5
			glScaled(sc, sc, 1.0)
			val s = "${max(timer / 20, 0)}"
			font.drawString(s, (resolution.scaledWidth / (4 * sc) - font.getStringWidth(s) / 2).I, (resolution.scaledHeight / (4 * sc) - 4).I, 0xFFFFFF, true)
			glPopMatrix()
		}
		
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_)
	}
	
	override fun keyTyped(c: Char, i: Int) = Unit
	
	override fun doesGuiPauseGame() = false
	
	override fun updateScreen() {
		super.updateScreen()
		--timer
		
		if (!mc.thePlayer.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame) || timer < 0) {
			mc.displayGuiScreen(null)
			mc.setIngameFocus()
		}
	}
}
