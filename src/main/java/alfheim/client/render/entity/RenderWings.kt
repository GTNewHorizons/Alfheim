package alfheim.client.render.entity

import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.core.helper.ElvenFlightHelper
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.IBaubleRender.Helper
import vazkii.botania.common.Botania
import kotlin.math.*

object RenderWings {
	
	@SideOnly(Side.CLIENT)
	fun render(e: RenderPlayerEvent.Specials.Post, player: EntityPlayer) {
		if (!AlfheimConfig.enableWingsNonAlfheim && Minecraft.getMinecraft().theWorld.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) return
		if (EnumRace.getRace(player) == EnumRace.HUMAN) return
		if (player.isInvisible || player.isPotionActive(Potion.invisibility) || player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) return
		if (player.commandSenderName == "AlexSocol") return
		
		glPushMatrix()
		glDisable(GL_CULL_FACE)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glDisable(GL_LIGHTING)
		glDepthMask(false)
		glAlphaFunc(GL_GREATER, 0.003921569f)
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		val spd = 0.5
		EnumRace.getRace(player).glColorA(if (ElvenFlightHelper[player] / ElvenFlightHelper[player] < 0.05) min(0.75 + cos((player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks).toDouble() * spd * 0.3).toFloat() * 0.2, 1.0) else 1.0)
		
		Helper.rotateIfSneaking(player)
		glTranslated(0.0, -0.15, 0.0)
		
		// Icon
		glPushMatrix()
		glTranslated(-0.25, 0.25, 0.15)
		val si = 0.5
		glScaled(si, si, si)
		drawRect(getPlayerIconTexture(player), 0)
		glPopMatrix()
		
		glTranslated(0.0, 0.1, 0.0)
		
		player.sendPlayerAbilities()
		val flying = player.capabilities.isFlying
		val ry = 20f + ((sin((player.ticksExisted + Minecraft.getMinecraft().timer.renderPartialTicks).toDouble() * spd * (if (flying) 0.4f else 0.2f).toDouble()) + 0.5f) * if (flying) 30f else 5f).toFloat()
		
		// Wing left
		glPushMatrix()
		glTranslated(0.15, 0.1, 0.15)
		val swr = 1.5
		glScaled(swr, swr, swr)
		//glRotated(10, 0, 0, 1);
		glRotated((-ry).toDouble(), 0.0, 1.0, 0.0)
		getPlayerWingTexture(player)?.let { drawRect(it, -1) }
		glPopMatrix()
		
		// Wing right
		glPushMatrix()
		glTranslated(-0.15, 0.1, 0.15)
		val swl = 1.5
		glScaled(-swl, swl, swl)
		//glRotated(10, 0, 0, 1);
		glRotated((-ry).toDouble(), 0.0, 1.0, 0.0)
		getPlayerWingTexture(player)?.let { drawRect(it, -1) }
		glPopMatrix()
		
		//glColor4d(1, 1, 1, 1); for some reason it cleans color
		glAlphaFunc(GL_GREATER, 0.1f)
		glDepthMask(true)
		glEnable(GL_LIGHTING)
		glDisable(GL_BLEND)
		glEnable(GL_CULL_FACE)
		glPopMatrix()
		
		if (Minecraft.getMinecraft().thePlayer === player && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) return
		
		if (Minecraft.getMinecraft().theWorld.totalWorldTime % 10 == 0L && !Minecraft.getMinecraft().isGamePaused) {
			val v = Vector3(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().add(0.0, Math.random(), 0.0).mul(Math.random(), 1.0, Math.random()).mul(player.width.toDouble(), player.height.toDouble(), player.width.toDouble())
			Botania.proxy.sparkleFX(player.worldObj, player.posX + v.x, player.posY + v.y - if (Minecraft.getMinecraft().thePlayer === player) 1.62 else 0.0, player.posZ + v.z, 1f, 1f, 1f, 2f * Math.random().toFloat(), 20)
		}
	}
	
	private fun drawRect(texture: ResourceLocation, i: Int) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture)
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(0.0, i.toDouble(), 0.0, 1.0, 0.0)
		Tessellator.instance.addVertexWithUV(0.0, 1.0, 0.0, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(1.0, 1.0, 0.0, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(1.0, i.toDouble(), 0.0, 0.0, 0.0)
		Tessellator.instance.draw()
	}
	
	fun getPlayerWingTexture(player: EntityPlayer): ResourceLocation? {
		return LibResourceLocations.wings[EnumRace.getRaceID(player)]
	}
	
	fun getPlayerIconTexture(player: EntityPlayer): ResourceLocation {
		return LibResourceLocations.icons[EnumRace.getRaceID(player)]
	}
}