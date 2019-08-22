package alfheim.client.gui

import alfheim.AlfheimCore
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.registry.AlfheimRegistry
import cpw.mods.fml.common.eventhandler.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.Tessellator
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import org.lwjgl.opengl.GL11.*

class GUIIceLens(private val mc: Minecraft): Gui() {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	fun onOverlayRendering(event: RenderGameOverlayEvent.Post) {
		if (!AlfheimCore.enableMMO || event.type != ElementType.HELMET || Minecraft.getMinecraft().thePlayer.getActivePotionEffect(AlfheimRegistry.icelens) == null) return
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glDisable(GL_DEPTH_TEST)
		glDepthMask(false)
		//glDisable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.003921569f)
		
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.iceLens)
		val res = ScaledResolution(mc, mc.displayWidth, mc.displayHeight)
		val u = res.scaledWidth
		val v = res.scaledHeight
		val tes = Tessellator.instance
		tes.startDrawingQuads()
		tes.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0)
		tes.addVertexWithUV(0.0, v.toDouble(), 0.0, 0.0, 1.0)
		tes.addVertexWithUV(u.toDouble(), v.toDouble(), 0.0, 1.0, 1.0)
		tes.addVertexWithUV(u.toDouble(), 0.0, 0.0, 1.0, 0.0)
		tes.draw()
		
		glAlphaFunc(GL_GREATER, 0.1f)
		//glEnable(GL_ALPHA_TEST);
		glDepthMask(true)
		glEnable(GL_DEPTH_TEST)
		glDisable(GL_BLEND)
		glPopMatrix()
	}
}