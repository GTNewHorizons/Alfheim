package alfheim.client.gui

import org.lwjgl.opengl.GL11.*

import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.AlfheimConfig
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.Tessellator

class GUIDeathTimer: GuiScreen() {
	
	var timer: Int = 0
	
	override fun initGui() {
		super.initGui()
		timer = AlfheimConfig.deathScreenAddTime
	}
	
	override fun drawScreen(p_73863_1_: Int, p_73863_2_: Int, p_73863_3_: Float) {
		this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792)
		
		val font = mc.fontRenderer
		val resolution = ScaledResolution(mc, mc.displayWidth, mc.displayHeight)
		
		glScaled(2.0, 2.0, 1.0)
		run {
			glPushMatrix()
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glTranslated((resolution.scaledWidth / 4 - 32).toDouble(), (resolution.scaledHeight / 4 - 32).toDouble(), 0.0)
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
			glRotated((-(timer % 20) * 18).toDouble(), 0.0, 0.0, 1.0)
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
			val s = "" + Math.max(timer / 20, 0)
			font.drawString(s, (resolution.scaledWidth / (4 * sc) - font.getStringWidth(s) / 2).toInt(), (resolution.scaledHeight / (4 * sc) - 4).toInt(), 0xFFFFFF, true)
			glPopMatrix()
		}
		
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_)
	}
	
	override fun keyTyped(c: Char, i: Int) {}
	
	override fun doesGuiPauseGame(): Boolean {
		return false
	}
	
	override fun updateScreen() {
		super.updateScreen()
		--timer
		
		if (!mc.thePlayer.isPotionActive(AlfheimRegistry.leftFlame)) {
			mc.displayGuiScreen(null)
			mc.setIngameFocus()
		}
	}
}
