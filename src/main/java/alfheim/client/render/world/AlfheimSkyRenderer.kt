package alfheim.client.render.world

import alexsocol.asjlib.*
import cpw.mods.fml.relauncher.ReflectionHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.*
import net.minecraft.util.*
import net.minecraftforge.client.IRenderHandler
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.lib.LibObfuscation
import java.util.*
import kotlin.math.*

object AlfheimSkyRenderer: IRenderHandler() {
	
	private val textureSkybox = ResourceLocation(LibResources.MISC_SKYBOX)
	private val textureRainbow = ResourceLocation(LibResources.MISC_RAINBOW)
	private val textureMoonPhases = ResourceLocation("textures/environment/moon_phases.png")
	private val textureSun = ResourceLocation("textures/environment/sun.png")
	
	private val planetTextures = arrayOf(
		ResourceLocation(LibResources.MISC_PLANET + "0.png"),
		ResourceLocation(LibResources.MISC_PLANET + "1.png"),
		ResourceLocation(LibResources.MISC_PLANET + "2.png"),
		ResourceLocation(LibResources.MISC_PLANET + "3.png"),
		ResourceLocation(LibResources.MISC_PLANET + "4.png"),
		ResourceLocation(LibResources.MISC_PLANET + "5.png")
	)
	
	override fun render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
		val glSkyList = ReflectionHelper.getPrivateValue<Int, RenderGlobal>(RenderGlobal::class.java, mc.renderGlobal, *LibObfuscation.GL_SKY_LIST)
		val starGLCallList = ReflectionHelper.getPrivateValue<Int, RenderGlobal>(RenderGlobal::class.java, mc.renderGlobal, *LibObfuscation.STAR_GL_CALL_LIST)
		glDisable(GL_TEXTURE_2D)
		val vec3 = world.getSkyColor(mc.renderViewEntity, partialTicks)
		var f1 = vec3.xCoord.F
		var f2 = vec3.yCoord.F
		var f3 = vec3.zCoord.F
		var f6: Float
		var insideVoid = 0f
		if (mc.thePlayer.posY <= -2) insideVoid = min(1.0, -(mc.thePlayer.posY + 2) / 30f).F
		f1 = max(0f, f1 - insideVoid)
		f2 = max(0f, f2 - insideVoid)
		f3 = max(0f, f3 - insideVoid)
		val tessellator1 = Tessellator.instance
		glDepthMask(false)
		glEnable(GL_FOG)
		glColor3f(f1, f2, f3)
		glCallList(glSkyList)
		glDisable(GL_FOG)
		glDisable(GL_ALPHA_TEST)
		glEnable(GL_BLEND)
		OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0)
		RenderHelper.disableStandardItemLighting()
		val afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks)
		var f7: Float
		var f8: Float
		var f10: Float
		// === Sunset
		if (afloat != null) {
			glDisable(GL_TEXTURE_2D)
			glShadeModel(GL_SMOOTH)
			glPushMatrix()
			glRotatef(90f, 1f, 0f, 0f)
			glRotatef(if (MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0f) 180f else 0f, 0f, 0f, 1f)
			glRotatef(90f, 0f, 0f, 1f)
			f6 = afloat[0]
			f7 = afloat[1]
			f8 = afloat[2]
			var f11: Float
			tessellator1.startDrawing(6)
			tessellator1.setColorRGBA_F(f6, f7, f8, afloat[3] * (1f - insideVoid))
			tessellator1.addVertex(0.0, 100.0, 0.0)
			val b0: Byte = 16
			tessellator1.setColorRGBA_F(afloat[0], afloat[1], afloat[2], 0f)
			for (j in 0..b0) {
				f11 = j.F * Math.PI.F * 2f / b0.F
				val f12 = MathHelper.sin(f11)
				val f13 = MathHelper.cos(f11)
				tessellator1.addVertex(f12 * 120f.D, f13 * 120f.D, -f13 * 40f * afloat[3].D)
			}
			tessellator1.draw()
			glPopMatrix()
			glShadeModel(GL_FLAT)
		}
		glEnable(GL_TEXTURE_2D)
		glPushMatrix()
		f6 = max(0.2f, 1f - world.getRainStrength(partialTicks)) * (1f - insideVoid)
		glRotatef(-90f, 0f, 1f, 0f)
		val celAng = world.getCelestialAngle(partialTicks)
		var effCelAng = celAng
		if (celAng > 0.5) effCelAng = 0.5f - (celAng - 0.5f)
		// === Planets
		f10 = 20f
		val lowA = max(0f, effCelAng - 0.3f) * f6
		var a = max(0.1f, lowA)
		OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0)
		glPushMatrix()
		glColor4f(1f, 1f, 1f, a * 4 * (1f - insideVoid) * (1f - mc.theWorld.rainingStrength))
		glRotatef(90f, 0.5f, 0.5f, 0f)
		for (p in planetTextures.indices) {
			mc.renderEngine.bindTexture(planetTextures[p])
			drawObject(tessellator1, f10)
			when (p) {
				0 -> {
					glRotatef(70f, 1f, 0f, 0f)
					f10 = 12f
				}
				
				1 -> {
					glRotatef(120f, 0f, 0f, 1f)
					f10 = 15f
				}
				
				2 -> {
					glRotatef(80f, 1f, 0f, 1f)
					f10 = 25f
				}
				
				3 -> {
					glRotatef(100f, 0f, 0f, 1f)
					f10 = 10f
				}
				
				4 -> {
					glRotatef(-60f, 1f, 0f, 0.5f)
					f10 = 40f
				}
			}
		}
		glColor4f(1f, 1f, 1f, 1f)
		glPopMatrix()
		// === Rays
		mc.renderEngine.bindTexture(textureSkybox)
		f10 = 20f
		a = lowA
		glPushMatrix()
		OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE, 1, 0)
		glTranslatef(0f, -1f, 0f)
		glRotatef(220f, 1f, 0f, 0f)
		glColor4f(1f, 1f, 1f, a * (1f - mc.theWorld.rainingStrength))
		val angles = 90
		val y = 2f
		val y0 = 0f
		val uPer = 1f / 360f
		val anglePer = 360f / angles
		var fuzzPer = Math.PI * 10 / angles
		var rotSpeed = 1f
		val rotSpeedMod = 0.4f
		for (p in 0..2) {
			val baseAngle = rotSpeed * rotSpeedMod * (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks)
			glRotatef((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.25f * rotSpeed * rotSpeedMod, 0f, 1f, 0f)
			tessellator1.startDrawingQuads()
			for (i in 0 until angles) {
				var j = i
				if (i % 2 == 0) j--
				val ang = j * anglePer + baseAngle
				val xp = cos(ang * Math.PI / 180f) * f10
				val zp = sin(ang * Math.PI / 180f) * f10
				val yo = sin(fuzzPer * j) * 1
				val ut = ang * uPer
				if (i % 2 == 0) {
					tessellator1.addVertexWithUV(xp, yo + y0 + y, zp, ut.D, 1.0)
					tessellator1.addVertexWithUV(xp, yo + y0, zp, ut.D, 0.0)
				} else {
					tessellator1.addVertexWithUV(xp, yo + y0, zp, ut.D, 0.0)
					tessellator1.addVertexWithUV(xp, yo + y0 + y, zp, ut.D, 1.0)
				}
			}
			tessellator1.draw()
			when (p) {
				0 -> {
					glRotatef(20f, 1f, 0f, 0f)
					glColor4f(1f, 0.4f, 0.4f, a)
					fuzzPer = Math.PI * 14 / angles
					rotSpeed = 0.2f
				}
				
				1 -> {
					glRotatef(50f, 1f, 0f, 0f)
					glColor4f(0.4f, 1f, 0.7f, a)
					fuzzPer = Math.PI * 6 / angles
					rotSpeed = 2f
				}
			}
		}
		glPopMatrix()
		// === Rainbow
		glPushMatrix()
		OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0)
		mc.renderEngine.bindTexture(textureRainbow)
		f10 = 10f
		var effCelAng1 = celAng
		if (effCelAng1 > 0.25f) effCelAng1 = 1f - effCelAng1
		effCelAng1 = 0.25f - min(0.25f, effCelAng1)
		val time = world.worldTime + 1000
		val day = (time / 24000L).I
		val rand = Random((day * 0xFF).toLong())
		val angle1 = rand.nextFloat() * 360f
		val angle2 = rand.nextFloat() * 360f
		glColor4f(1f, 1f, 1f, effCelAng1 * (1f - insideVoid) * (1f - mc.theWorld.rainingStrength))
		glRotatef(angle1, 0f, 1f, 0f)
		glRotatef(angle2, 0f, 0f, 1f)
		tessellator1.startDrawingQuads()
		for (i in 0 until angles) {
			var j = i
			if (i % 2 == 0) j--
			val ang = j * anglePer
			val xp = cos(ang * Math.PI / 180f) * f10
			val zp = sin(ang * Math.PI / 180f) * f10
			val yo = 0.0
			val ut = ang * uPer
			if (i % 2 == 0) {
				tessellator1.addVertexWithUV(xp, yo + y0 + y, zp, ut.D, 1.0)
				tessellator1.addVertexWithUV(xp, yo + y0, zp, ut.D, 0.0)
			} else {
				tessellator1.addVertexWithUV(xp, yo + y0, zp, ut.D, 0.0)
				tessellator1.addVertexWithUV(xp, yo + y0 + y, zp, ut.D, 1.0)
			}
		}
		tessellator1.draw()
		glPopMatrix()
		glColor4f(1f, 1f, 1f, (1f - insideVoid) * (1f - mc.theWorld.rainingStrength))
		OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE, 1, 0)
		// === Sun
		glRotatef(world.getCelestialAngle(partialTicks) * 360f, 1f, 0f, 0f)
		f10 = 60f
		mc.renderEngine.bindTexture(textureSun)
		drawObject(tessellator1, f10)
		// === Moon
		f10 = 60f
		mc.renderEngine.bindTexture(textureMoonPhases)
		val k = world.moonPhase
		val l = k % 4
		val i1 = k / 4 % 2
		val f14 = l.F / 4f
		val f15 = i1.F / 2f
		val f16 = (l + 1) / 4f
		val f17 = (i1 + 1) / 2f
		tessellator1.startDrawingQuads()
		tessellator1.addVertexWithUV(-f10.D, -100.0, f10.D, f16.D, f17.D)
		tessellator1.addVertexWithUV(f10.D, -100.0, f10.D, f14.D, f17.D)
		tessellator1.addVertexWithUV(f10.D, -100.0, -f10.D, f14.D, f15.D)
		tessellator1.addVertexWithUV(-f10.D, -100.0, -f10.D, f16.D, f15.D)
		tessellator1.draw()
		// === Stars
		f6 *= max(0.1f, effCelAng * 2)
		val t = (ClientTickHandler.ticksInGame + partialTicks + 2000) * 0.005f
		glPushMatrix()
		glDisable(GL_TEXTURE_2D)
		glPushMatrix()
		glRotatef(t * 3, 0f, 1f, 0f)
		glColor4f(1f, 1f, 1f, f6)
		glCallList(starGLCallList)
		glPopMatrix()
		glPushMatrix()
		glRotatef(t, 0f, 1f, 0f)
		glColor4f(0.5f, 1f, 1f, f6)
		glCallList(starGLCallList)
		glPopMatrix()
		glPushMatrix()
		glRotatef(t * 2, 0f, 1f, 0f)
		glColor4f(1f, 0.75f, 0.75f, f6)
		glCallList(starGLCallList)
		glPopMatrix()
		glPushMatrix()
		glRotatef(t * 3, 0f, 0f, 1f)
		glColor4f(1f, 1f, 1f, 0.25f * f6)
		glCallList(starGLCallList)
		glPopMatrix()
		glPushMatrix()
		glRotatef(t, 0f, 0f, 1f)
		glColor4f(0.5f, 1f, 1f, 0.25f * f6)
		glCallList(starGLCallList)
		glPopMatrix()
		glPushMatrix()
		glRotatef(t * 2, 0f, 0f, 1f)
		glColor4f(1f, 0.75f, 0.75f, 0.25f * f6)
		glCallList(starGLCallList)
		glPopMatrix()
		glEnable(GL_TEXTURE_2D)
		glPopMatrix()
		glColor4f(1f, 1f, 1f, 1f)
		glDisable(GL_BLEND)
		glEnable(GL_ALPHA_TEST)
		glEnable(GL_FOG)
		glPopMatrix()
		glDepthMask(true)
	}
	
	private fun drawObject(tess: Tessellator, f10: Float) {
		tess.startDrawingQuads()
		tess.addVertexWithUV(-f10.D, 100.0, -f10.D, 0.0, 0.0)
		tess.addVertexWithUV(f10.D, 100.0, -f10.D, 1.0, 0.0)
		tess.addVertexWithUV(f10.D, 100.0, f10.D, 1.0, 1.0)
		tess.addVertexWithUV(-f10.D, 100.0, f10.D, 0.0, 1.0)
		tess.draw()
	}
}