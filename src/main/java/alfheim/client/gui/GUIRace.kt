package alfheim.client.gui

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.api.entity.*
import alfheim.client.render.entity.RenderWings
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.helper.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.Tessellator
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import org.lwjgl.opengl.GL11.*
import kotlin.math.*

class GUIRace: Gui() {
	
	@SubscribeEvent
	fun onOverlayRendering(e: RenderGameOverlayEvent.Post) {
		if (!AlfheimConfigHandler.enableElvenStory || (AlfheimConfigHandler.enableMMO && AlfheimConfigHandler.selfHealthUI)) return
		if (e.type != ElementType.HOTBAR || mc.thePlayer.race == EnumRace.HUMAN) return
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glDisable(GL_DEPTH_TEST)
		glDepthMask(false)
		glDisable(GL_ALPHA_TEST)
		
		glTranslated(e.resolution.scaledWidth_double / 2 + 91, e.resolution.scaledHeight_double - 32, 0.0)
		
		mc.textureManager.bindTexture(RenderWings.getPlayerIconTexture(mc.thePlayer))
		//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		
		mc.thePlayer.race.glColorA(0.5)
		
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV(0.0, 32.0, 0.0, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(32.0, 32.0, 0.0, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(32.0, 0.0, 0.0, 1.0, 0.0)
		Tessellator.instance.draw()
		
		//		ASJShaderHelper.useShader(LibShaderIDs.idShadow);
		
		val mod = min(1.0, mc.thePlayer.flight / ElvenFlightHelper.max)
		val time = sin((mc.theWorld.totalWorldTime / 2).D) * 0.5
		mc.thePlayer.race.glColorA(if (mc.thePlayer.capabilities.isFlying) if (mod > 0.1) time + 0.5 else time else 1.0)
		
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(0.0, 32 - mod * 32, 0.0, 0.0, 1 - mod)
		Tessellator.instance.addVertexWithUV(0.0, 32.0, 0.0, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(32.0, 32.0, 0.0, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(32.0, 32 - mod * 32, 0.0, 1.0, 1 - mod)
		Tessellator.instance.draw()
		
		glEnable(GL_ALPHA_TEST)
		glDepthMask(true)
		glEnable(GL_DEPTH_TEST)
		glDisable(GL_BLEND)
		glPopMatrix()
	}
}
