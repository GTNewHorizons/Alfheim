package alfheim.client.render.entity

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.entity.*
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.helper.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.IBaubleRender.Helper
import vazkii.botania.common.Botania
import java.awt.Color
import kotlin.math.*

object RenderWings {
	
	val customWings =		arrayOf("MonoShiki",							"lie4me",								"DemnaGvasalia",						"KAIIIAK"							)
	val customTextures =	arrayOf(LibResourceLocations.wingsButterfly,	LibResourceLocations.wingsHeavenBird,	LibResourceLocations.wingsDarkPhoenix,	LibResourceLocations.wingsSprite	)
	val wingMap = customWings.zip(customTextures).toMap()
	
	@SideOnly(Side.CLIENT)
	fun render(e: RenderPlayerEvent.Specials.Post, player: EntityPlayer) {
		// player.sendPlayerAbilities()
		
		val match = customWings.indexOfFirst { ContributorsPrivacyHelper.isCorrect(player, it) }
		
		if (match == -1) {
			if (!AlfheimConfigHandler.enableElvenStory) return
			if (AlfheimConfigHandler.wingsBlackList.contains(mc.theWorld?.provider?.dimensionId ?: Int.MAX_VALUE)) return
			if (player.race == EnumRace.HUMAN) return
			if (ContributorsPrivacyHelper.isCorrect(player.commandSenderName, "AlexSocol")) return
		}
		
		if (player.isInvisible || player.isPotionActive(Potion.invisibility) || player.isInvisibleToPlayer(mc.thePlayer)) return
		
		glPushMatrix()
		glDisable(GL_CULL_FACE)
		
		//if (match == -1) {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glDepthMask(false)
			glAlphaFunc(GL_GREATER, 1/255f)
		//}
		glDisable(GL_LIGHTING)
		
		val lastX = OpenGlHelper.lastBrightnessX
		val lastY = OpenGlHelper.lastBrightnessY
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		val spd = 0.5
		
		if (match != -1) {
			if (match == 0)
				ASJRenderHelper.glColor1u(ASJRenderHelper.addAlpha(Color.HSBtoRGB(Botania.proxy.worldElapsedTicks % 360 / 360f, 1f, 1f), 255))
			else
				glColor4f(1f, 1f, 1f, 1f)
		} else
			player.race.glColorA(if (player.flight / ElvenFlightHelper.max < 0.05) min(0.75 + cos((player.ticksExisted + mc.timer.renderPartialTicks).D * spd * 0.3).F * 0.2, 1.0) else 1.0)
		
		Helper.rotateIfSneaking(player)
		glTranslated(0.0, -0.15, 0.0)
		
		// Icon
		if (match == -1 && player.race != EnumRace.HUMAN) {
			glPushMatrix()
			glTranslated(-0.25, 0.25, 0.15)
			val si = 0.5
			glScaled(si)
			drawRect(getPlayerIconTexture(player), 0)
			glPopMatrix()
		}
		
		glTranslated(0.0, 0.1, 0.0)
		
		val flying = player.capabilities.isFlying
		val ry = 20f + ((sin((player.ticksExisted + mc.timer.renderPartialTicks).D * spd * (if (flying) 0.4f else 0.2f).D) + 0.5f) * if (flying) 30f else 5f).F
		
		// Wing left
		glPushMatrix()
		glTranslated(0.15, 0.1, 0.15)
		val swr = 1.5
		glScaled(swr)
		//glRotated(10, 0, 0, 1);
		glRotated((-ry).D, 0.0, 1.0, 0.0)
		getPlayerWingTexture(player)?.let { drawRect(it, -1) }
		glPopMatrix()
		
		// Wing right
		glPushMatrix()
		glTranslated(-0.15, 0.1, 0.15)
		val swl = 1.5
		glScaled(-swl, swl, swl)
		//glRotated(10, 0, 0, 1);
		glRotated((-ry).D, 0.0, 1.0, 0.0)
		getPlayerWingTexture(player)?.let { drawRect(it, -1) }
		glPopMatrix()
		
		//glColor4d(1, 1, 1, 1); for some reason it cleans color
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
		glEnable(GL_LIGHTING)
		//if (match == -1) {
			glAlphaFunc(GL_GREATER, 0.1f)
			glDepthMask(true)
			glDisable(GL_BLEND)
		//}
		glEnable(GL_CULL_FACE)
		glPopMatrix()
		
		if (mc.thePlayer === player && mc.gameSettings.thirdPersonView == 0) return
		
		if (mc.theWorld.totalWorldTime % 10 == 0L && !mc.isGamePaused) {
			val v = Vector3(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().add(0.0, Math.random(), 0.0).mul(Math.random(), 1.0, Math.random()).mul(player.width.D, player.height.D, player.width.D)
			Botania.proxy.sparkleFX(player.worldObj, player.posX + v.x, player.posY + v.y - if (mc.thePlayer === player) 1.62 else 0.0, player.posZ + v.z, 1f, 1f, 1f, 2f * Math.random().F, 20)
		}
	}
	
	fun drawRect(texture: ResourceLocation, i: Int) {
		mc.renderEngine.bindTexture(texture)
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(0.0, i.D, 0.0, 1.0, 0.0)
		Tessellator.instance.addVertexWithUV(0.0, 1.0, 0.0, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(1.0, 1.0, 0.0, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(1.0, i.D, 0.0, 0.0, 0.0)
		Tessellator.instance.draw()
	}
	
	fun getPlayerWingTexture(player: EntityPlayer) = wingMap[customWings.firstOrNull { ContributorsPrivacyHelper.isCorrect(player, it) } ?: ""] ?: LibResourceLocations.wings[player.raceID]
	
	fun getPlayerIconTexture(player: EntityPlayer) = LibResourceLocations.icons[player.raceID]
}